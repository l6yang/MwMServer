package com.mwm.loyal.dao;

import com.mwm.loyal.beans.*;
import com.mwm.loyal.imp.Contact;
import com.mwm.loyal.utils.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataUtil implements Contact {
    private static final String className = "oracle.jdbc.driver.OracleDriver";
    private static final String user = "loyal";
    private static final String password = "111111";

    private static String returnQuerySql(LoginBean loginBean) {
        return "select * from MWM_account where M_ID='" + loginBean.getAccount() + "'";
    }

    private static String returnFeedBackSql(FeedBackBean feedBackBean) {
        //String类型插入Oracle Date类型数据  需要通过to_date转换
        return "insert into MWM_FEED_BACK values (to_date('" + feedBackBean.getTime() + "','yyyy-mm-dd hh24:mi:ss'),'" + feedBackBean.getAccount() + "','" + feedBackBean.getContent() + "')";
    }

    public static ResultBean doRegister(LoginBean loginBean) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("INSERT INTO MWM_ACCOUNT VALUES (?,?,?,?,?,?)");
            pre.setString(1, loginBean.getAccount());
            pre.setString(2, loginBean.getPassword());
            pre.setString(3, loginBean.getNickname());
            File copyFile = new File("E:\\Oracle\\images\\mwm.jpg");
            File file = new File("E:\\IntelliJ Space\\MwMServer\\src\\main\\webapp\\images\\mwm.jpg");
            if (!copyFile.exists()) {
                new CopyFileThread(file, copyFile).run();
            }
            FileInputStream in = new FileInputStream(copyFile.exists() ? copyFile : file);
            pre.setBinaryStream(4, in);
            pre.setString(5, loginBean.getSignature());
            pre.setString(6, "0");
            int result = pre.executeUpdate();
            bean.setResultCode(result);
            if (result == -1)
                bean.setResultMsg("用户已存在");
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(null, conn, pre);
        }
    }

    public static ResultBean doLogin(LoginBean loginBean) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement(returnQuerySql(loginBean));
            rs = pre.executeQuery();
            if (rs.next()) {
                String password = StringUtil.replaceNull(rs.getString("m_password"));
                String lock = StringUtil.replaceNull(rs.getString("m_lock"));
                if (loginBean.getPassword().equals(password)) {
                    if (lock.equals("1")) {
                        if (doLoginSameDevice(loginBean)) {
                            int code = accountSetStates(loginBean);
                            bean.setResultCode(code);
                            if (code == 1) {
                                bean.setResultMsg(rs.getString("m_nickname"));
                                bean.setExceptMsg(rs.getString("m_signature"));
                            }
                        } else {
                            bean.setResultCode(-1);
                            bean.setResultMsg("当前账号已被别的设备绑定");
                        }
                    } else {
                        int code = accountSetStates(loginBean);
                        bean.setResultCode(code);
                        if (code == 1) {
                            bean.setResultMsg(rs.getString("m_nickname"));
                            bean.setExceptMsg(rs.getString("m_signature"));
                        }
                    }
                } else {
                    bean.setResultCode(-1);
                    bean.setResultMsg("用户名和密码不匹配");
                }
            } else {
                bean.setResultCode(-1);
                bean.setResultMsg("用户不存在");
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(rs, conn, pre);
        }
    }

    public static ResultBean doQueryAccount(String account) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("SELECT *FROM MWM_account where M_ID=?");
            pre.setString(1, account);
            rs = pre.executeQuery();
            if (rs.next()) {
                bean.setResultCode(1);
                bean.setResultMsg(rs.getString("m_nickname"));
                bean.setExceptMsg(rs.getString("m_signature"));
            } else
                bean.setResultCode(-1);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(rs, conn, pre);
        }
    }

    /**
     * 判断绑定的设备是否与当前登录设备一致
     */
    private static boolean doLoginSameDevice(LoginBean loginBean) {
        List<String> lockList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("SELECT M_DEVICE , M_MAC from MWM_ACCOUNT_SECURITY  where M_ACCOUNT=? ");
            pre.setString(1, loginBean.getAccount());
            rs = pre.executeQuery();
            while (rs.next()) {
                lockList.add(rs.getString("M_DEVICE") + "," + rs.getString("M_MAC"));
            }
            return lockList.contains(loginBean.getDevice() + "," + loginBean.getMac());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            StreamUtil.close(rs, conn, pre);
        }
    }

    public static void doShowIconByIO(HttpServletResponse response, LoginBean loginBean) {
        response.setContentType("image/jpeg");
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        OutputStream outputStream = null;
        InputStream in = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement(returnQuerySql(loginBean));
            rs = pre.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob("m_icon");
                if (blob != null) {
                    in = blob.getBinaryStream();
                    byte[] data = new byte[(int) blob.length()];
                    in.read(data);
                    outputStream = response.getOutputStream();
                    outputStream.write(data, 0, data.length);
                    outputStream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(outputStream);
            StreamUtil.close(in);
            StreamUtil.close(rs, conn, pre);
        }
    }

    public static void doDownLoadApk(HttpServletResponse response) {
        OutputStream outputStream = null;
        FileInputStream fis = null;
        try {
            File copyFile = new File("E:\\Oracle\\apks\\mwm.apk");
            File file = new File("E:\\Oracle\\apks\\mwm.apk");
            if (!copyFile.exists()) {
                new CopyFileThread(file, copyFile).run();
            }
            fis = new FileInputStream(copyFile.exists() ? copyFile : file);
            byte[] data = new byte[(int) fis.getChannel().size()];
            fis.read(data);
            outputStream = response.getOutputStream();
            outputStream.write(data, 0, data.length);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(outputStream);
            StreamUtil.close(fis);
        }
    }

    public static ResultBean doFeedBack(FeedBackBean feedBackBean) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            // String sql = "INSERT into MWM_FEED_BACK values (to_date('" + feedBackBean.getTime() + "','yyyy-mm-dd hh24:mi:ss'),'" + feedBackBean.getAccount() + "','" + feedBackBean.getContent() + ")";
            pre = conn.prepareStatement(returnFeedBackSql(feedBackBean));
            int result = pre.executeUpdate();
            bean.setResultCode(result);
            if (result == -1)
                bean.setResultMsg("用户已存在");
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(null, conn, pre);
        }
    }

    public static ResultBean doInsertContact(ContactBean contactBean) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("INSERT INTO MWM_CONTACT VALUES (?,?,?)");
            pre.setTimestamp(1, TimeUtil.date2Timestamp(contactBean.getTime()));
            pre.setString(2, contactBean.getAccount());
            String contact = contactBean.getContact();
            if (contact.contains("&k="))
                contact = contact.substring(contact.indexOf("&k=")).replace("&k=", "");
            pre.setString(3, contact.contains("&k=") ? CipherUtil.decodeStr(contact) : contact);
            int result = pre.executeUpdate();
            bean.setResultCode(result);
            if (result == -1)
                bean.setResultMsg("用户已存在");
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(null, conn, pre);
        }
    }

    public static ResultBean doUpdate(LoginBean loginBean, String state, String oldPassWord) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rst = null;
        try {
            conn = getConnection();
            switch (state) {
                case "personal":
                    pre = conn.prepareStatement("UPDATE  MWM_ACCOUNT SET  M_NICKNAME= ? , M_SIGNATURE= ? WHERE M_ID= ?");
                    pre.setString(1, loginBean.getNickname());
                    pre.setString(2, loginBean.getSignature());
                    pre.setString(3, loginBean.getAccount());
                    int result = pre.executeUpdate();
                    bean.setResultCode(result);
                    if (result == 0)
                        bean.setResultMsg("用户不存在，请注册");
                    else if (result == -1)
                        bean.setResultMsg("更新资料失败");
                    else if (result == 1) {
                        bean.setResultMsg(loginBean.getNickname());
                        bean.setExceptMsg(loginBean.getSignature());
                    }
                    break;
                case "password":
                    pre = conn.prepareStatement("SELECT *FROM MWM_ACCOUNT WHERE M_PASSWORD=? AND M_ID=?");
                    pre.setString(1, oldPassWord);
                    pre.setString(2, loginBean.getAccount());
                    rst = pre.executeQuery();
                    if (rst.next()) {
                        pre = conn.prepareStatement("UPDATE MWM_ACCOUNT SET M_PASSWORD= ? WHERE M_ID= ?");
                        pre.setString(1, loginBean.getPassword());
                        pre.setString(2, loginBean.getAccount());
                        result = pre.executeUpdate();
                        bean.setResultCode(result);
                        if (result == 0)
                            bean.setResultMsg("用户不存在，请注册");
                    } else {
                        bean.setResultCode(-1);
                        bean.setResultMsg("账号与密码不匹配，请检查原始密码是否输入正确");
                    }
                    break;
                case "lock":
                    pre = conn.prepareStatement("UPDATE MWM_ACCOUNT SET M_LOCK=? WHERE M_ID=?");
                    String lock = StringUtil.replaceNull(loginBean.getLock());
                    pre.setString(1, loginBean.getLock());
                    pre.setString(2, loginBean.getAccount());
                    result = pre.executeUpdate();
                    if (result == 1) {
                        pre = conn.prepareStatement("INSERT INTO MWM_ACCOUNT_SECURITY VALUES (?,?,?,?)");
                        pre.setString(1, loginBean.getAccount());
                        pre.setTimestamp(2, TimeUtil.getTimestamp());
                        pre.setString(3, loginBean.getDevice());
                        pre.setString(4, loginBean.getMac());
                        int res = pre.executeUpdate();
                        bean.setResultCode(res);
                        bean.setResultMsg(lock.equals("1") ? "lock" : "unlock");
                        if (res == 0)
                            bean.setResultMsg("修改失败，请重试");
                    } else {
                        bean.setResultCode(-1);
                        bean.setResultMsg("账号不存在，请注册");
                    }
                    break;
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(rst, conn, pre);
        }
    }

    public static ResultBean doLocked(String account) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rst = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("SELECT M_LOCK FROM MWM_ACCOUNT WHERE M_ID=?");
            pre.setString(1, account);
            rst = pre.executeQuery();
            if (rst.next()) {
                bean.setResultCode(1);
                bean.setResultMsg(rst.getString("m_lock"));
            } else {
                bean.setResultCode(-1);
                bean.setResultMsg("未找到该账号记录");
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(rst, conn, pre);
        }
    }

    public static ResultBean doUpdateIcon(String account, InputStream inputStream) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("UPDATE  MWM_ACCOUNT SET M_ICON= ? WHERE M_ID= ?");
            pre.setBlob(1, inputStream);
            pre.setString(2, account);
            int result = pre.executeUpdate();
            bean.setResultCode(result);
            if (result == 0)
                bean.setResultMsg("用户不存在，请注册");
            else if (result == -1)
                bean.setResultMsg("更新失败");
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(null, conn, pre);
        }
    }

    public static ResultBean doUpdateApk(String apkName, String version, InputStream inputStream) {
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet resultSet = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("SELECT * FROM MWM_VER");
            resultSet = pre.executeQuery();
            while (resultSet.next()) {
                int result = deleteApk(resultSet.getString("apk_version"));
                if (result != 1)
                    break;
            }
            pre = conn.prepareStatement("INSERT INTO MWM_VER VALUES (?,?,?,?,?)");
            pre.setString(1, apkName);
            pre.setString(2, version);
            pre.setBlob(3, inputStream);
            pre.setTimestamp(4, TimeUtil.getTimestamp());
            pre.setString(5, "1");
            int result = pre.executeUpdate();
            bean.setResultCode(result);
            bean.setResultMsg(result == 1 ? "上传成功" : "上传失败");
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(resultSet, conn, pre);
        }
    }

    private static int deleteApk(String appVer) {
        PreparedStatement pre = null;
        Connection conn = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("DELETE FROM MWM_VER WHERE apk_version =?");
            pre.setString(1, appVer);
            return pre.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            StreamUtil.close(null, conn, pre);
        }
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(className);
        String url = "jdbc:oracle:thin:@" + Str.localhost + ":1521/orcl";
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 账号最近登录历史
     */
    private static List<String> accountNearStates(String account) {
        List<String> nearList = new ArrayList<>();
        nearList.clear();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet resultSet = null;
        try {
            conn = getConnection();
            String nowTime = TimeUtil.getDateTime();
            String before = TimeUtil.beforeMonth(nowTime, Contact.Str.TIME_ALL, 1);
            pre = conn.prepareStatement("SELECT * FROM MWM_ACCOUNT_SECURITY WHERE M_ACCOUNT= ? AND M_TIME BETWEEN ? AND ?");
            pre.setString(1, account);
            pre.setTimestamp(2, TimeUtil.date2Timestamp(before));
            pre.setTimestamp(3, TimeUtil.date2Timestamp(nowTime));
            resultSet = pre.executeQuery();
            while (resultSet.next()) {
                nearList.add(resultSet.getString("m_DEVICE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(resultSet, conn, pre);
        }
        return nearList;
    }

    //从数据库中查找是否有新版本
    public static ResultBean queryApkVersion(String port,String apkVer) {
        List<ResultBean> beanList = new ArrayList<>();
        ResultBean bean = new ResultBean();
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet resultSet = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("SELECT * FROM MWM_VER WHERE APK_ZT= ? AND APK_VERSION>? order by APK_TIME desc");
            pre.setString(1, "1");
            pre.setString(2, apkVer);
            resultSet = pre.executeQuery();
            while (resultSet.next()) {
                String apkUrl = "http://192.168.0.66:" + port + "/mwm/apk/mwm_" + resultSet.getString("apk_version") + ".apk";
                beanList.add(new ResultBean(1, "检查到新版本", apkUrl));
            }
            return beanList.isEmpty() ? new ResultBean(-1, "当前已是最新版本", null) : beanList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return errorBean(bean, e);
        } finally {
            StreamUtil.close(resultSet, conn, pre);
        }
    }

    //需要删除
    private static int accountSetStates(LoginBean loginBean) {
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            conn = getConnection();
            pre = conn.prepareStatement("SELECT * FROM MWM_ACCOUNT_SECURITY WHERE M_ACCOUNT=?");
            pre.setString(1, loginBean.getAccount());
            int result = pre.executeUpdate();
            if (result == 1) {
                pre = conn.prepareStatement("UPDATE MWM_ACCOUNT_SECURITY SET M_TIME=?,M_DEVICE=?,M_MAC=? WHERE M_ACCOUNT=?");
                pre.setTimestamp(1, TimeUtil.date2Timestamp(TimeUtil.getDateTime()));
                pre.setString(2, loginBean.getDevice());
                pre.setString(3, loginBean.getMac());
                pre.setString(4, loginBean.getAccount());
                return pre.executeUpdate();
            } else {
                pre = conn.prepareStatement("INSERT INTO MWM_ACCOUNT_SECURITY VALUES (?,?,?,?)");
                pre.setString(1, loginBean.getAccount());
                pre.setTimestamp(2, TimeUtil.date2Timestamp(TimeUtil.getDateTime()));
                pre.setString(3, loginBean.getDevice());
                pre.setString(4, loginBean.getMac());
                return pre.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            StreamUtil.close(null, conn, pre);
        }
    }

    private static void copyFile(File file, File copyFile) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(copyFile);
        byte[] bytes = new byte[1024];
        int length;
        try {
            while ((length = fis.read(bytes)) != -1) {
                fos.write(bytes, 0, length);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fos);
            StreamUtil.close(fis);
        }
    }

    private static ResultBean errorBean(ResultBean bean, Exception e) {
        bean.setResultCode(-1);
        bean.setResultMsg(e.getMessage());
        return bean;
    }

    private static class CopyFileThread implements Runnable {
        private File file, copyFile;

        CopyFileThread(File file, File copyFile) {
            this.file = file;
            this.copyFile = copyFile;
        }

        @Override
        public void run() {
            try {
                copyFile(file, copyFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}