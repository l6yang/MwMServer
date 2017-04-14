package com.mwm.loyal.action;

import com.google.gson.Gson;
import com.mwm.loyal.beans.ContactBean;
import com.mwm.loyal.beans.LoginBean;
import com.mwm.loyal.beans.ResultBean;
import com.mwm.loyal.imp.Contact;
import com.mwm.loyal.model.AccountBean;
import com.mwm.loyal.service.BaseAndroidService;
import com.mwm.loyal.utils.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RequestMapping("/mwm/action.do")
public class AndroidAction extends MultiActionController implements Contact {

    @RequestMapping(params = "method=doRegister")
    public void doRegister(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String json = getReqParams(request, "json_register");
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            LoginBean loginBean = GsonUtil.getBeanFromJson(json, LoginBean.class);
            resultBean = DataUtil.doRegister(loginBean);
            writer.print(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doLogin")
    public void doLogin(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String json = getReqParams(request, "json_login");
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            AccountBean accountBean = service.loginByAccount(GsonUtil.getBeanFromJson(json, AccountBean.class));
            if (null == accountBean) {
                resultBean = new ResultBean(-1, "用户名或密码不正确");
            } else {
                resultBean = new ResultBean(1, accountBean.getNickname(), accountBean.getSign());
            }
            writer.print(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doLoginWithJson")
    public void doLoginWithJson(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String account = getReqParams(request, "account");
            String password = getReqParams(request, "password");
            ResultBean resultBean;
            if (emptyJson(account) || emptyJson(password)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            AccountBean accountBean = service.loginByAccount(new AccountBean(account, password));
            if (null == accountBean) {
                resultBean = new ResultBean(-1, "用户名或密码不正确");
            } else resultBean = new ResultBean(1, accountBean.getNickname(), accountBean.getSign());
            writer.print(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doQueryAccount")
    public void doQueryAccount(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String account = getReqParams(request, "account");
            ResultBean resultBean;
            if (emptyJson(account)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            AccountBean accountBean = service.getUserByAccount(new AccountBean(account));
            if (null == accountBean) {
                resultBean = new ResultBean(-1, "用户名或密码不正确");
            } else resultBean = new ResultBean(1, accountBean.getNickname(), accountBean.getSign());
            writer.print(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doShowIconByIO")
    public void doShowIconByIO(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("utf-8");
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            String account = getReqParams(request, "account");
            if (emptyJson(account)) {
                outputStream.write(0);
                return;
            }
            AccountBean accountBean = service.getUserByAccount(new AccountBean(account));
            if (null == accountBean) {
                outputStream.write(0);
            } else
                outputStream.write(accountBean.getIcon(), 0, accountBean.getIcon().length);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(outputStream);
        }
    }

    @RequestMapping(params = "method=doDownLoadApk")
    public void doDownLoadApk(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"mwm.apk\"");
            DataUtil.doDownLoadApk(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(params = "method=doUpdateAccount")
    public void doUpdateAccount(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            LoginBean loginBean = GsonUtil.getBeanFromJson(request, "json_update", LoginBean.class);
            ResultBean bean = DataUtil.doUpdate(loginBean, getReqParams(request, "update_state"), getReqParams(request, "old_data"));
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doAccountLocked")
    public void doAccountLocked(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        try {
            writer = response.getWriter();
            String account = getReqParams(request, "account");
            ResultBean resultBean;
            if (emptyJson(account)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            AccountBean accountBean = service.getUserByAccount(new AccountBean(account));
            if (null == accountBean) {
                resultBean = new ResultBean(-1, "用户名或密码不正确");
            } else resultBean = new ResultBean(1, String.valueOf(accountBean.getLocked()));
            writer.print(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doUpdateIcon")
    public void doUpdateIcon(HttpServletRequest request, HttpServletResponse response) {
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
                        ResultBean bean = DataUtil.doUpdateIcon(name, item.getInputStream());
                        writer.print(bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doUpdateApk")
    public void doUpdateApk(HttpServletRequest request, HttpServletResponse response) {
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
                    String buffer = "<script>" + "parent.showProcessMessage('" + bean.getResultMsg() + "')" + "</script>";
                    writer.println(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doFeedBack")
    public void doFeedBack(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String json = getReqParams(request, "json_feed");
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            int code = service.insertFeedBack(GsonUtil.getBeanFromJson(json, com.mwm.loyal.model.FeedBackBean.class));
            resultBean = new ResultBean(code, 1 == code ? "反馈成功" : "反馈失败");
            writer.print(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doUCropTest")
    public void doUCropTest(HttpServletRequest request, HttpServletResponse response) {
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

    @RequestMapping(params = "method=doScan")
    public void doScan(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String param = getReqParams(request, "param");
            String json = getReqParams(request, "json_scan");
            if (param.isEmpty() || !param.equals("com.mwm.loyal")) {
                doScanJsp(request, response);
            } else {
                ContactBean contactBean = GsonUtil.getBeanFromJson(json, ContactBean.class);
                ResultBean bean = DataUtil.doInsertContact(contactBean);
                writer.print(bean);
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

    @RequestMapping(params = "method=doCheckApkVer")
    public void doCheckApkVer(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            ResultBean bean = DataUtil.queryApkVersion(String.valueOf(request.getLocalPort()), getReqParams(request, "apkVer"));
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=doGetSelfFeed")
    public void doGetSelfFeed(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        try {
            String account = getReqParams(request, "account");
            writer = response.getWriter();
            ResultBean resultBean;
            if (emptyJson(account)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            List<com.mwm.loyal.model.FeedBackBean> beanList = service.queryByAccount(account);
            if (null == beanList || beanList.isEmpty()) {
                resultBean = new ResultBean(1, "{}");
            } else
                resultBean = new ResultBean(1, new Gson().toJson(beanList));
            writer.print(resultBean);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    private void release(PrintWriter writer) {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    private String getReqParams(HttpServletRequest request, String param) {
        try {
            return StringUtil.replaceNull(request.getParameter(param));
        } catch (Exception e) {
            return "";
        }
    }

    private BaseAndroidService service;

    @RequestMapping(params = "method=androidTest1")
    public void androidTest1(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        try {
            writer = response.getWriter();
            String json = getReqParams(request, "json_register");
            System.out.println("androidTest1::" + json);
            writer = response.getWriter();
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            int code = service.registerAccount(GsonUtil.getBeanFromJson(json, AccountBean.class));
            writer.print(code);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=androidUpdate")
    public void androidUpdate(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        try {
            writer = response.getWriter();
            String json = getReqParams(request, "json_update");
            writer = response.getWriter();
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            int code = service.updateAccount(GsonUtil.getBeanFromJson(json, AccountBean.class));
            writer.print(code);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
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
            String json = getReqParams(request, "json_destroy");
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean(-1, "参数不能为空");
                writer.print(resultBean);
                return;
            }
            AccountBean accountBean = service.getUserByAccount(GsonUtil.getBeanFromJson(json, AccountBean.class));
            if (null == accountBean)
                resultBean = new ResultBean(-1, "用户名或密码不正确");
            else {
                int code = service.destroyAccount(accountBean);
                resultBean = new ResultBean(code, 1 == code ? "删除成功" : "删除失败");
            }
            writer.print(resultBean);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(writer);
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