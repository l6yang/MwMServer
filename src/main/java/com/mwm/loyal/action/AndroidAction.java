package com.mwm.loyal.action;

import com.mwm.loyal.beans.ContactBean;
import com.mwm.loyal.beans.ResultBean;
import com.mwm.loyal.imp.Contact;
import com.mwm.loyal.model.AccountBean;
import com.mwm.loyal.model.FeedBackBean;
import com.mwm.loyal.service.BaseAndroidService;
import com.mwm.loyal.utils.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import sun.net.www.protocol.ftp.FtpURLConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.List;

@RequestMapping("/mwm/action.do")
public class AndroidAction extends MultiActionController implements Contact {
    private BaseAndroidService service;

    @RequestMapping(params = "method=mwmRegister")
    public void mwmRegister(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = getReqParams(request, "beanJson");
            if (initParams(writer, response, json)) return;
            AccountBean registerBean = GsonUtil.getBeanFromJson(json, AccountBean.class);
            AccountBean accountBean = service.getUserByAccount(registerBean);
            ResultBean resultBean;
            if (null == accountBean) {
                String path = "E:\\IntelliJ Space\\MwMServer\\src\\main\\webapp\\images\\mwm.jpg";
                registerBean.setIcon(StreamUtil.file2ByteArray(path));
                int code = service.registerAccount(registerBean);
                resultBean = new ResultBean(String.valueOf(code), code == 1 ? "" : "注册失败");
            } else resultBean = new ResultBean("-1", "账号已存在");
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=readFtp")
    public void readFtp(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("utf-8");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
		/*	String url = request.getParameter("url");
//			FTPUtil ftp = new FTPUtil("192.168.0.155", 21, "admin", "admin");
//			List<HashMap<String, String>> list = ftp.getFileFullPathList(url);

			//ftp://hisense:hisense@192.168.232.15:21/TSJK/_20181012_152133(3452660).png
			String[] userpass = url.split("@");
			String[] namepass = userpass[0].replace("ftp://", "").split(":");
			String filename = userpass[1].substring(userpass[1].lastIndexOf("/"),userpass[1].length()).replace("/", "");
			String[] ip = userpass[1].replace(filename, "").split(":");
			String path = ip[1].substring(ip[1].indexOf("/"), ip[1].length());
			ReadFTPFile rftp = new  ReadFTPFile();
			String a = rftp.readConfigFileForFTP(namepass[0], namepass[1], path, ip[0], 21, filename);
			System.out.println(a);*/
            String path = request.getParameter("url");
            FtpURLConnection connection;
            URL url;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                url = new URL(/*URLDecoder.decode(*/path/*, "utf-8")*/);
                connection = (FtpURLConnection) url.openConnection();
                connection.getInputStream(); //读取图片文件流
                int length;
                byte[] images = new byte[1024];
                while ((length = connection.getInputStream().read(images)) != -1) {
                    baos.write(images, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.write(baos.toByteArray(), 0, baos.toByteArray().length);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(out);
        }
    }

    @RequestMapping(params = "method=mwmLogin")
    public void mwmLogin(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = getReqParams(request, "beanJson");
            if (initParams(writer, response, json)) return;
            ResultBean resultBean;
            try {
                AccountBean accountBean = service.loginByAccount(GsonUtil.getBeanFromJson(json, AccountBean.class));
                if (null == accountBean) {
                    resultBean = new ResultBean("-1", "用户名或密码不正确");
                } else {
                    resultBean = new ResultBean<>(accountBean);
                }
            } catch (Exception e) {
                resultBean = new ResultBean("-1", e.getMessage());
            }
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmLoginWithJson")
    public void mwmLoginWithJson(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        System.out.println("mwmLoginWithJson");
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            writer = response.getWriter();
            String account = getReqParams(request, "account");
            String password = getReqParams(request, "password");
            System.out.println("account--" + account);
            System.out.println("password--" + password);
            ResultBean<AccountBean> resultBean;
            if (emptyJson(account) || emptyJson(password)) {
                resultBean = new ResultBean<>("-1", "参数不能为空");
                writer.print(GsonUtil.beanToJson(resultBean));
                return;
            }
            AccountBean accountBean = service.loginByAccount(new AccountBean(account, password));
            if (null == accountBean) {
                resultBean = new ResultBean<>("-1", "用户名或密码不正确");
            } else resultBean = new ResultBean<>(accountBean);
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmQueryAccount")
    public void mwmQueryAccount(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String account = getReqParams(request, "account");
            ResultBean<AccountBean> resultBean;
            if (initParams(writer, response, account)) {
                return;
            }
            AccountBean accountBean = service.getUserByAccount(new AccountBean(account));
            if (null == accountBean) {
                resultBean = new ResultBean<>("-1", "用户名或密码不正确");
            } else resultBean = new ResultBean<>(accountBean);
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmShowIconByIO")
    public void mwmShowIconByIO(HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            String account = getReqParams(request, "account");
            response.setContentType("image/jpeg");
            System.out.println("mwmShowIconByIO::" + account);
            if (emptyJson(account)) {
                outputStream.write(0);
                return;
            }
            AccountBean accountBean = service.getUserByAccount(new AccountBean(account));
            if (null == accountBean) {
                outputStream.write(0);
            } else {
                byte[] bytes = accountBean.getIcon();
                if (null == bytes) {
                    String path = "E:\\IntelliJ Space\\MwMServer\\src\\main\\webapp\\images\\mwm.jpg";
                    bytes = StreamUtil.file2ByteArray(path);
                }
                if (bytes == null)
                    outputStream.write(0);
                else
                    outputStream.write(bytes, 0, bytes.length);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(outputStream);
        }
    }

    @RequestMapping(params = "method=mwmDownLoadApk")
    public void mwmDownLoadApk(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"mwm.apk\"");
            DataUtil.doDownLoadApk(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(params = "method=mwmUpdateAccount")
    public void mwmUpdateAccount(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = getReqParams(request, "beanJson");
            System.out.println("mwmUpdateAccount::" + json);
            ResultBean resultBean;
            if (initParams(writer, response, json)) {
                return;
            }
            AccountBean updateBean = GsonUtil.getBeanFromJson(json, AccountBean.class);
            //修改密码时用
            String oldPassword = updateBean.getOldPassword();
            AccountBean accountBean;
            if (null == oldPassword || oldPassword.isEmpty()) {
                //不用验证原来的密码
                accountBean = service.getUserByAccount(updateBean);
            } else {
                //需要原來的密碼
                accountBean = service.loginByAccount(new AccountBean(updateBean.getAccount(), updateBean.getOldPassword()));
            }
            if (null == accountBean) {
                resultBean = new ResultBean("-1", "用户名或密码不正确");
            } else {
                int code = service.updateAccount(updateBean);
                resultBean = new ResultBean(String.valueOf(code), code == 1 ? "资料更新成功" : "更新资料失败");
            }
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmAccountLocked")
    public void mwmAccountLocked(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String account = getReqParams(request, "account");
            ResultBean<String> resultBean;
            if (initParams(writer, response, account)) {
                return;
            }
            AccountBean accountBean = service.getUserByAccount(new AccountBean(account));
            if (null == accountBean) {
                resultBean = new ResultBean<>("-1", "用户名或密码不正确");
            } else resultBean = new ResultBean<>(String.valueOf(accountBean.getLocked()));
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmUpdateIcon")
    public void mwmUpdateIcon(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);
                for (FileItem item : items) {
                    String name = item.getFieldName();
                    if (item.isFormField()) {
                        String value = item.getString();
                        request.setAttribute(name, value);
                    } else {
                        String value = item.getName();
                        int start = value.lastIndexOf("\\");
                        String filename = value.substring(start + 1);
                        request.setAttribute(name, filename);
                        ResultBean resultBean = DataUtil.doUpdateIcon(name, item.getInputStream());
                        writer.print(GsonUtil.beanToJson(resultBean));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmUpdateApk")
    public void mwmUpdateApk(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);
                FileItem item = items.get(0);
                String name = item.getFieldName();
                if (item.getSize() == 0) {
                    request.setAttribute("message", "请至少选择一个文件上传！！");
                    request.getRequestDispatcher("/loyal/apk_upload.jsp").forward(request, response);
                    return;
                }
                if (item.isFormField()) {
                    String value = item.getString();
                    request.setAttribute(name, value);
                } else {
                    String value = item.getName();
                    int start = value.lastIndexOf("\\");
                    String filename = value.substring(start + 1);
                    request.setAttribute(name, filename);
                    String apkVer;
                    if (filename.contains("_")) {
                        apkVer = filename.substring(filename.lastIndexOf("_") + 1).replace(".apk", "");
                    } else apkVer = filename;
                    String path = request.getSession().getServletContext().getRealPath("/apk");
                    File file = new File(path, "mwm_" + apkVer + ".apk");
                    FileUtil.deleteFile(file);
                    OutputStream outs = new FileOutputStream(file);
                    int length;
                    byte[] buf = new byte[1024];
                    InputStream in = new ByteArrayInputStream(item.get());
                    while ((length = in.read(buf)) != -1) {
                        outs.write(buf, 0, length);
                    }
                    outs.flush();
                    outs.close();
                    ResultBean bean = DataUtil.doUpdateApk(filename, apkVer, item.getInputStream());
                    String buffer = "<script>" + "parent.showProcessMessage('" + bean.getMessage() + "')" + "</script>";
                    writer.println(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmFeedBack")
    public void mwmFeedBack(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = getReqParams(request, "beanJson");
            ResultBean resultBean;
            if (initParams(writer, response, json)) {
                return;
            }
            int code = service.insertFeedBack(GsonUtil.getBeanFromJson(json, FeedBackBean.class));
            resultBean = new ResultBean(String.valueOf(code), 1 == code ? "反馈成功" : "反馈失败");
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmUCropTest")
    public void mwmUCropTest(HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        response.setContentType("image/jpeg");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            RequestDispatcher dis = request.getRequestDispatcher("/images/ucrop_test.jpg");
            dis.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmScan")
    public void mwmScan(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String param = getReqParams(request, "param");
            String json = getReqParams(request, "beanJson");
            if (param.isEmpty() || !param.equals("com.mwm.loyal")) {
                doScanJsp(request, response);
            } else {
                ContactBean contactBean = GsonUtil.getBeanFromJson(json, ContactBean.class);
                ResultBean resultBean = DataUtil.doInsertContact(contactBean);
                writer.print(GsonUtil.beanToJson(resultBean));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doScanJsp")
    private void doScanJsp(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("message", "信息错误");
            request.getRequestDispatcher("/loyal/scan.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(params = "method=mwmCheckApkVer")
    public void mwmCheckApkVer(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            ResultBean resultBean = DataUtil.queryApkVersion(String.valueOf(request.getLocalPort()), getReqParams(request, "apkVer"));
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=mwmGetSelfFeed")
    public void mwmGetSelfFeed(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String account = getReqParams(request, "account");
            ResultBean<List<FeedBackBean>> resultBean;
            if (initParams(writer, response, account)) {
                return;
            }
            List<FeedBackBean> beanList = service.queryByAccount(account);
            resultBean = new ResultBean<>(beanList);
            System.out.println(GsonUtil.beanToJson(resultBean));
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=deleteSelfFeed")
    public void deleteSelfFeed(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        try {
            String json = getReqParams(request, "beanJson");
            writer = response.getWriter();
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean("-1", "参数不能为空");
                writer.print(GsonUtil.beanToJson(resultBean));
                return;
            }
            int code = service.deleteFeedBack(GsonUtil.getBeanFromJson(json, FeedBackBean.class));
            resultBean = new ResultBean(String.valueOf(code), 1 == code ? "已删除" : "删除失败");
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=destroyAccount")
    public void destroyAccount(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        try {
            writer = response.getWriter();
            String json = getReqParams(request, "beanJson");
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean("-1", "参数不能为空");
                writer.print(GsonUtil.beanToJson(resultBean));
                return;
            }
            AccountBean accountBean = service.getUserByAccount(GsonUtil.getBeanFromJson(json, AccountBean.class));
            if (null == accountBean)
                resultBean = new ResultBean("-1", "用户名或密码不正确");
            else {
                int code = service.destroyAccount(accountBean);
                resultBean = new ResultBean(String.valueOf(code), 1 == code ? "删除成功" : "删除失败");
            }
            writer.print(GsonUtil.beanToJson(resultBean));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    private void release(Closeable stream) {
        IOUtil.closeStream(stream);
    }

    private boolean initParams(PrintWriter writer, HttpServletResponse response, String json) {
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        if (emptyJson(json)) {
            ResultBean resultBean = new ResultBean("-1", "参数不能为空");
            writer.print(GsonUtil.beanToJson(resultBean));
            return true;
        }
        return false;
    }

    private String getReqParams(HttpServletRequest request, String param) {
        try {
            return StringUtil.replaceNull(request.getParameter(param));
        } catch (Exception e) {
            return "";
        }
    }

    public BaseAndroidService getService() {
        return service;
    }

    private boolean emptyJson(String json) {
        return null == json || json.isEmpty();
    }

    public void setService(BaseAndroidService service) {
        this.service = service;
    }
}