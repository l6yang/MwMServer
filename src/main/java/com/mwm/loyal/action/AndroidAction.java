package com.mwm.loyal.action;

import com.mwm.loyal.beans.ContactBean;
import com.mwm.loyal.beans.FeedBackBean;
import com.mwm.loyal.beans.LoginBean;
import com.mwm.loyal.beans.ResultBean;
import com.mwm.loyal.dao.DataUtil;
import com.mwm.loyal.imp.ResListener;
import com.mwm.loyal.utils.GsonUtil;
import com.mwm.loyal.utils.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class AndroidAction extends MultiActionController implements ResListener{

    public void doRegister(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            LoginBean loginBean = GsonUtil.getBeanFromJson(request, "json_register", LoginBean.class);
            bean = DataUtil.doRegister(loginBean);
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    public void doLoginTest(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String account = request.getParameter("account");
            String password = request.getParameter("password");
            bean = DataUtil.doLogin(new LoginBean(account, password));
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    public void doLogin(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            LoginBean loginBean = GsonUtil.getBeanFromJson(request, "json_login", LoginBean.class);
            bean = DataUtil.doLogin(loginBean);
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    public void doShowIconByIO(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        try {
            response.setCharacterEncoding("utf-8");
            LoginBean loginBean = new LoginBean();
            System.out.println("account--" + getReqParams(request, "account"));
            loginBean.setAccount(getReqParams(request, "account"));
            DataUtil.doShowIconByIO(response, loginBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doDownLoadApk(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("android Action：：doDownLoadApk");
        try {
            response.setCharacterEncoding("utf-8");
            DataUtil.doDownLoadApk(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doUpdateAccount(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            LoginBean loginBean = GsonUtil.getBeanFromJson(request, "json_update", LoginBean.class);
            bean = DataUtil.doUpdate(loginBean, getReqParams(request, "update_state"), getReqParams(request, "old_data"));
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    public void doAccountLocked(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String account = getReqParams(request, "account");
            bean = DataUtil.doLocked(account);
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    public void doUpdateIcon(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
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
                        //System.out.println(name + "::" + value + "::" + filename);
                        //System.out.println("获取文件总量的容量:" + item.getSize());
                        bean = DataUtil.doUpdateIcon(name, item.getInputStream());
                        writer.print(bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    public void doFeedBack(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            FeedBackBean feedBackBean = GsonUtil.getBeanFromJson(request, "json_feed", FeedBackBean.class);
            bean = DataUtil.doFeedBack(feedBackBean);
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    public void doUCropTest(HttpServletRequest request, HttpServletResponse response) {
        response.reset();
        response.setContentType("image/jpeg");
        PrintWriter writer = null;
        ResultBean bean;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            RequestDispatcher dis = request.getRequestDispatcher("/images/ucrop_test.jpg");
            dis.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    public void doScan(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
        try {
            File root = new File(System.getProperty("user.dir") + "\\src");
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String param = getReqParams(request, "param");
            ContactBean contactBean = GsonUtil.getBeanFromCipherJson(request, "json_scan", ContactBean.class);
            if (param.isEmpty() || !param.equals("com.mwm.loyal")) {
                doScanJsp(request, response);
            } else {
                bean = DataUtil.doInsertContact(contactBean);
                writer.print(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(e.getMessage());
        } finally {
            release(writer);
        }
    }

    private void doScanJsp(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("message", "信息错误");
            request.getRequestDispatcher("/loyal/scan.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doCheckApkVer(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        ResultBean bean;
        try {
            request.setAttribute("message", "信息错误");
            writer = response.getWriter();
            bean = new ResultBean();
            bean.setResultCode(1);
            bean.setResultMsg(getReqParams(request, "apkVer"));
            bean.setExceptMsg("http://"+ Str.localhost+":8080/mwm/apk/mwm.apk");
            writer.print(bean);
        } catch (Exception e) {
            e.printStackTrace();
            bean = new ResultBean();
            bean.setResultCode(-99);
            bean.setResultMsg(getReqParams(request, "apkVer"));
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
}