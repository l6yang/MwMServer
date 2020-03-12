package com.mwm.loyal.action;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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

    @RequestMapping(params = "method=register")
    public void register(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = getRequestParams(request, "beanJson");
            if (initParams(writer, response, json)) return;
            AccountBean registerBean = GsonUtil.json2Bean(json, AccountBean.class);
            AccountBean accountBean = service.getUserByAccount(registerBean);
            ResultBean resultBean;
            if (null == accountBean) {
                String path = "E:\\IntelliJ Space\\MwMServer\\src\\main\\webapp\\images\\mwm.jpg";
                registerBean.setIcon(StreamUtil.file2ByteArray(path));
                int code = service.registerAccount(registerBean);
                resultBean = new ResultBean(String.valueOf(code), code == 1 ? "" : "注册失败");
            } else resultBean = new ResultBean("-1", "账号已存在");
            writer.print(GsonUtil.bean2Json(resultBean));
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

    @RequestMapping(params = "method=loginByJson")
    public void loginByJson(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = getRequestParams(request, "beanJson");
            if (initParams(writer, response, json)) return;
            ResultBean resultBean;
            try {
                AccountBean accountBean = service.loginByAccount(GsonUtil.json2Bean(json, AccountBean.class));
                if (null == accountBean) {
                    resultBean = new ResultBean("-1", "用户名或密码不正确");
                } else {
                    resultBean = new ResultBean<>(accountBean);
                }
            } catch (Exception e) {
                resultBean = new ResultBean("-1", e.getMessage());
            }
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=loginByParams")
    public void loginByParams(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        System.out.println("loginByParams");
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            writer = response.getWriter();
            String account = getRequestParams(request, "account");
            String password = getRequestParams(request, "password");
            System.out.println("account--" + account);
            System.out.println("password--" + password);
            ResultBean<AccountBean> resultBean;
            if (emptyJson(account) || emptyJson(password)) {
                resultBean = new ResultBean<>("-1", "参数不能为空");
                writer.print(GsonUtil.bean2Json(resultBean));
                return;
            }
            AccountBean accountBean = service.loginByAccount(new AccountBean(account, password));
            if (null == accountBean) {
                resultBean = new ResultBean<>("-1", "用户名或密码不正确");
            } else resultBean = new ResultBean<>(accountBean);
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=queryAccount")
    public void queryAccount(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String account = getRequestParams(request, "account");
            ResultBean<AccountBean> resultBean;
            if (initParams(writer, response, account)) {
                return;
            }
            AccountBean accountBean = service.getUserByAccount(new AccountBean(account));
            if (null == accountBean) {
                resultBean = new ResultBean<>("-1", "用户名或密码不正确");
            } else resultBean = new ResultBean<>(accountBean);
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=wsdlTest")
    public void wsdlTest(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("dmtb");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String result = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                    "<root>\n" +
                    "    <head>\n" +
                    "        <code>1</code>\n" +
                    "        <message>成功</message>\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <vehisinfo>\n" +
                    "            <hpzl>\n" +
                    "                {\"22\":\"临时行驶车\",\"01\":\"大型汽车\",\"23\":\"警用汽车\",\"02\":\"小型汽车\",\"24\":\"警用摩托\",\"03\":\"使馆汽车\",\"25\":\"原农机号牌\",\"26\":\"香港入出境车\",\"04\":\"领馆汽车\",\"27\":\"澳门入出境车\",\"05\":\"境外汽车\",\"06\":\"外籍汽车\",\"07\":\"普通摩托车\",\"08\":\"轻便摩托车\",\"09\":\"使馆摩托车\",\"51\":\"大型新能源汽车\",\"52\":\"小型新能源汽车\",\"31\":\"武警号牌\",\"32\":\"军队号牌\",\"10\":\"领馆摩托车\",\"99\":\"其他号牌\",\"11\":\"境外摩托车\",\"12\":\"外籍摩托车\",\"13\":\"低速车\",\"14\":\"拖拉机\",\"15\":\"挂车\",\"16\":\"教练汽车\",\"17\":\"教练摩托车\",\"18\":\"试验汽车\",\"19\":\"试验摩托车\",\"41\":\"无号牌\",\"42\":\"假号牌\",\"20\":\"临时入境汽车\",\"43\":\"挪用号牌\",\"21\":\"临时入境摩托车\"}\n" +
                    "            </hpzl>\n" +
                    "            <csys>\n" +
                    "                {\"A\":\"白\",\"B\":\"灰\",\"C\":\"黄\",\"D\":\"粉\",\"E\":\"红\",\"F\":\"紫\",\"G\":\"绿\",\"H\":\"蓝\",\"I\":\"棕\",\"J\":\"黑\"}\n" +
                    "            </csys>\n" +
                    "            <syxz>\n" +
                    "                {\"A\":\"非营运\",\"B\":\"公路客运\",\"C\":\"公交客运\",\"D\":\"出租客运\",\"E\":\"旅游客运\",\"F\":\"货运\",\"G\":\"租赁\",\"H\":\"警用\",\"I\":\"消防\",\"J\":\"救护\",\"K\":\"工程救险\",\"L\":\"营转非\",\"M\":\"出租转非\",\"N\":\"教练\",\"O\":\"幼儿校车\",\"P\":\"小学生校车\",\"Q\":\"初中生校车\",\"R\":\"危化品运输\",\"S\":\"中小学生校车\",\"T\":\"预约出租客运\",\"U\":\"预约出租转非\"}\n" +
                    "            </syxz>\n" +
                    "            <cllx>\n" +
                    "                {\"B21\":\"中型普通半挂车\",\"B22\":\"中型厢式半挂车\",\"B23\":\"中型罐式半挂车\",\"B24\":\"中型平板半挂车\",\"B25\":\"中型集装箱半挂车\",\"B1U\":\"重型中置轴旅居挂车\",\"B26\":\"中型自卸半挂车\",\"B1V\":\"重型中置轴车辆运输车\",\"B27\":\"中型特殊结构半挂车\",\"K41\":\"微型普通客车\",\"B1W\":\"重型中置轴普通挂车\",\"B28\":\"中型仓栅式半挂车\",\"K42\":\"微型越野客车\",\"B29\":\"中型旅居半挂车\",\"K43\":\"微型轿车\",\"K49\":\"微型面包车\",\"B2A\":\"中型专项作业半挂车\",\"B2B\":\"中型低平板半挂车\",\"B2C\":\"中型车辆运输半挂车\",\"B2D\":\"中型罐式自卸半挂车\",\"B2E\":\"中型平板自卸半挂车\",\"B2F\":\"中型集装箱自卸半挂车\",\"B2G\":\"中型特殊结构自卸半挂车\",\"B2H\":\"中型仓栅式自卸半挂车\",\"Z21\":\"中型非载货专项作业车\",\"Z22\":\"中型载货专项作业车\",\"B2J\":\"中型专项作业自卸半挂车\",\"B2K\":\"中型低平板自卸半挂车\",\"B31\":\"轻型普通半挂车\",\"B32\":\"轻型厢式半挂车\",\"B33\":\"轻型罐式半挂车\",\"B34\":\"轻型平板半挂车\",\"B35\":\"轻型自卸半挂车\",\"B36\":\"轻型仓栅式半挂车\",\"B2U\":\"中型中置轴旅居挂车\",\"B37\":\"轻型旅居半挂车\",\"B2V\":\"中型中置轴车辆运输车\",\"B38\":\"轻型专项作业半挂车\",\"B2W\":\"中型中置轴普通挂车\",\"B39\":\"轻型低平板半挂车\",\"Q11\":\"重型半挂牵引车\",\"Q12\":\"重型全挂牵引车\",\"B3C\":\"轻型车辆运输半挂车\",\"B3D\":\"轻型罐式自卸半挂车\",\"B3E\":\"轻型平板自卸半挂车\",\"B3F\":\"轻型集装箱自卸半挂车\",\"B3G\":\"轻型特殊结构自卸半挂车\",\"B3H\":\"轻型仓栅式自卸半挂车\",\"Z31\":\"小型非载货专项作业车\",\"Z32\":\"小型载货专项作业车\",\"B3J\":\"轻型专项作业自卸半挂车\",\"B3K\":\"轻型低平板自卸半挂车\",\"B3U\":\"轻型中置轴旅居挂车\",\"B3V\":\"轻型中置轴车辆运输车\",\"B3W\":\"轻型中置轴普通挂车\",\"Q21\":\"中型半挂牵引车\",\"Q22\":\"中型全挂牵引车\",\"Z41\":\"微型非载货专项作业车\",\"Z42\":\"微型载货专项作业车\",\"H11\":\"重型普通货车\",\"H12\":\"重型厢式货车\",\"H13\":\"重型封闭货车\",\"H14\":\"重型罐式货车\",\"H15\":\"重型平板货车\",\"H16\":\"重型集装厢车\",\"H17\":\"重型自卸货车\",\"Q31\":\"轻型半挂牵引车\",\"H18\":\"重型特殊结构货车\",\"Q32\":\"轻型全挂牵引车\",\"H19\":\"重型仓栅式货车\",\"Z51\":\"重型非载货专项作业车\",\"Z52\":\"重型载货专项作业车\",\"H1A\":\"重型车辆运输车\",\"H1B\":\"重型厢式自卸货车\",\"H1C\":\"重型罐式自卸货车\",\"H1D\":\"重型平板自卸货车\",\"H1E\":\"重型集装厢自卸货车\",\"H1F\":\"重型特殊结构自卸货车\",\"H1G\":\"重型仓栅式自卸货车\",\"H21\":\"中型普通货车\",\"H22\":\"中型厢式货车\",\"H23\":\"中型封闭货车\",\"H24\":\"中型罐式货车\",\"H25\":\"中型平板货车\",\"H26\":\"中型集装厢车\",\"H27\":\"中型自卸货车\",\"H28\":\"中型特殊结构货车\",\"H29\":\"中型仓栅式货车\",\"H2A\":\"中型车辆运输车\",\"H2B\":\"中型厢式自卸货车\",\"H2C\":\"中型罐式自卸货车\",\"H2D\":\"中型平板自卸货车\",\"H2E\":\"中型集装厢自卸货车\",\"H2F\":\"中型特殊结构自卸货车\",\"H2G\":\"中型仓栅式自卸货车\",\"H31\":\"轻型普通货车\",\"G11\":\"重型普通全挂车\",\"H32\":\"轻型厢式货车\",\"G12\":\"重型厢式全挂车\",\"H33\":\"轻型封闭货车\",\"G13\":\"重型罐式全挂车\",\"H34\":\"轻型罐式货车\",\"G14\":\"重型平板全挂车\",\"H35\":\"轻型平板货车\",\"G15\":\"重型集装箱全挂车\",\"G16\":\"重型自卸全挂车\",\"H37\":\"轻型自卸货车\",\"G17\":\"重型仓栅式全挂车\",\"H38\":\"轻型特殊结构货车\",\"G18\":\"重型旅居全挂车\",\"H39\":\"轻型仓栅式货车\",\"G19\":\"重型专项作业全挂车\",\"Z71\":\"轻型非载货专项作业车\",\"Z72\":\"轻型载货专项作业车\",\"H3A\":\"轻型车辆运输车\",\"G1A\":\"重型厢式自卸全挂车\",\"H3B\":\"轻型厢式自卸货车\",\"G1B\":\"重型罐式自卸全挂车\",\"H3C\":\"轻型罐式自卸货车\",\"G1C\":\"重型平板自卸全挂车\",\"H3D\":\"轻型平板自卸货车\",\"G1D\":\"重型集装箱自卸全挂车\",\"G1E\":\"重型仓栅式自卸全挂车\",\"H3F\":\"轻型特殊结构自卸货车\",\"G1F\":\"重型专项作业自卸全挂车\",\"H3G\":\"轻型仓栅式自卸货车\",\"H41\":\"微型普通货车\",\"G21\":\"中型普通全挂车\",\"H42\":\"微型厢式货车\",\"G22\":\"中型厢式全挂车\",\"H43\":\"微型封闭货车\",\"G23\":\"中型罐式全挂车\",\"H44\":\"微型罐式货车\",\"G24\":\"中型平板全挂车\",\"H45\":\"微型自卸货车\",\"G25\":\"中型集装箱全挂车\",\"H46\":\"微型特殊结构货车\",\"G26\":\"中型自卸全挂车\",\"H47\":\"微型仓栅式货车\",\"G27\":\"中型仓栅式全挂车\",\"G28\":\"中型旅居全挂车\",\"G29\":\"中型专项作业全挂车\",\"H4A\":\"微型车辆运输车\",\"G2A\":\"中型厢式自卸全挂车\",\"H4B\":\"微型厢式自卸货车\",\"G2B\":\"中型罐式自卸全挂车\",\"H4C\":\"微型罐式自卸货车\",\"G2C\":\"中型平板自卸全挂车\",\"G2D\":\"中型集装箱自卸全挂车\",\"G2E\":\"中型仓栅式自卸全挂车\",\"H4F\":\"微型特殊结构自卸货车\",\"G2F\":\"中型专项作业自卸全挂车\",\"H4G\":\"微型仓栅式自卸货车\",\"H51\":\"普通低速货车\",\"G31\":\"轻型普通全挂车\",\"H52\":\"厢式低速货车\",\"G32\":\"轻型厢式全挂车\",\"H53\":\"罐式低速货车\",\"G33\":\"轻型罐式全挂车\",\"H54\":\"自卸低速货车\",\"G34\":\"轻型平板全挂车\",\"H55\":\"仓栅式低速货车\",\"G35\":\"轻型自卸全挂车\",\"G36\":\"轻型仓栅式全挂车\",\"G37\":\"轻型旅居全挂车\",\"G38\":\"轻型专项作业全挂车\",\"N11\":\"三轮汽车\",\"G3A\":\"轻型厢式自卸全挂车\",\"H5B\":\"厢式自卸低速货车\",\"G3B\":\"轻型罐式自卸全挂车\",\"H5C\":\"罐式自卸低速货车\",\"G3C\":\"轻型平板自卸全挂车\",\"G3D\":\"轻型集装箱自卸全挂车\",\"G3E\":\"轻型仓栅式自卸全挂车\",\"G3F\":\"轻型专项作业自卸全挂车\",\"M11\":\"普通正三轮摩托车\",\"M12\":\"轻便正三轮摩托车\",\"M13\":\"正三轮载客摩托车\",\"M14\":\"正三轮载货摩托车\",\"M15\":\"侧三轮摩托车\",\"M21\":\"普通二轮摩托车\",\"M22\":\"轻便二轮摩托车\",\"D11\":\"无轨电车\",\"D12\":\"有轨电车\",\"T11\":\"大型轮式拖拉机\",\"X99\":\"其它\",\"T21\":\"小型轮式拖拉机\",\"T22\":\"手扶拖拉机\",\"T23\":\"手扶变形运输机\",\"K11\":\"大型普通客车\",\"K12\":\"大型双层客车\",\"K13\":\"大型卧铺客车\",\"K14\":\"大型铰接客车\",\"K15\":\"大型越野客车\",\"K16\":\"大型轿车\",\"K17\":\"大型专用客车\",\"K18\":\"大型专用校车\",\"K21\":\"中型普通客车\",\"K22\":\"中型双层客车\",\"K23\":\"中型卧铺客车\",\"K24\":\"中型铰接客车\",\"K25\":\"中型越野客车\",\"K26\":\"中型轿车\",\"K27\":\"中型专用客车\",\"K28\":\"中型专用校车\",\"B11\":\"重型普通半挂车\",\"B12\":\"重型厢式半挂车\",\"B13\":\"重型罐式半挂车\",\"B14\":\"重型平板半挂车\",\"B15\":\"重型集装箱半挂车\",\"B16\":\"重型自卸半挂车\",\"B17\":\"重型特殊结构半挂车\",\"K31\":\"小型普通客车\",\"B18\":\"重型仓栅式半挂车\",\"K32\":\"小型越野客车\",\"B19\":\"重型旅居半挂车\",\"J11\":\"轮式装载机械\",\"K33\":\"小型轿车\",\"J12\":\"轮式挖掘机械\",\"K34\":\"小型专用客车\",\"J13\":\"轮式平地机械\",\"K38\":\"小型专用校车\",\"K39\":\"小型面包车\",\"B1A\":\"重型专项作业半挂车\",\"B1B\":\"重型低平板半挂车\",\"B1C\":\"重型车辆运输半挂车\",\"B1D\":\"重型罐式自卸半挂车\",\"B1E\":\"重型平板自卸半挂车\",\"B1F\":\"重型集装箱自卸半挂车\",\"B1G\":\"重型特殊结构自卸半挂车\",\"B1H\":\"重型仓栅式自卸半挂车\",\"Z11\":\"大型非载货专项作业车\",\"Z12\":\"大型载货专项作业车\",\"B1J\":\"重型专项作业自卸半挂车\",\"B1K\":\"重型低平板自卸半挂车\"}\n" +
                    "            </cllx>\n" +
                    "            <zt>\n" +
                    "                {\"A\":\"正常\",\"B\":\"转出\",\"C\":\"被盗抢\",\"D\":\"停驶\",\"E\":\"注销\",\"G\":\"违法未处理\",\"H\":\"海关监管\",\"I\":\"事故未处理\",\"J\":\"嫌疑车\",\"K\":\"查封\",\"L\":\"暂扣\",\"M\":\"达到报废标准\",\"N\":\"事故逃逸\",\"O\":\"锁定\",\"P\":\"公告牌证作废后强制注销\",\"Q\":\"逾期未检验\"}\n" +
                    "            </zt>\n" +
                    "            <zpzl>\n" +
                    "                {\"0188\":\"手动机械断电开关拍照\",\"0168\":\"紧急切断装置拍照\",\"0126\":\"校车停车指示标志牌照片\",\"0127\":\"急救箱\",\"0128\":\"校车标志灯照片\",\"0130\":\"辅助制动装置\",\"0197\":\"大中型客车座位后\",\"0198\":\"大中型客车座位前\",\"0132\":\"发动机舱自动灭火装置\",\"0154\":\"右前轮胎规格型号\",\"0199\":\"外观检验其他拍照\",\"0111\":\"车辆左前方斜视45度照片\",\"0133\":\"前轮盘式制动器\",\"0155\":\"左后轮胎规格型号\",\"0112\":\"车辆右后方斜视45度照片\",\"0134\":\"防抱死制动装置自检状态灯\",\"0156\":\"右后轮胎规格型号\",\"0113\":\"车辆识别代号照片\",\"0135\":\"残疾车操纵辅助装置\",\"0157\":\"驾驶人座椅汽车安全带\",\"0158\":\"车辆正后方照片\",\"0114\":\"车辆正侧方照片\",\"0136\":\"左前轮胎规格型号\",\"0159\":\"校车标牌（前）正面照片\",\"0115\":\"车厢内部照片\",\"0116\":\"灭火器照片\",\"0138\":\"校车、卧铺客车的车内外录像监控系统\",\"0117\":\"应急锤照片\",\"0139\":\"校车的辅助倒车装置\",\"0118\":\"行驶记录装置照片\",\"0119\":\"发动机号或柔性标签\",\"0160\":\"校车标牌（前）反面照片\",\"0161\":\"校车标牌（后）正面照片\",\"0140\":\"教练车副制动踏板\",\"0163\":\"危险货物运输车标志\"}\n" +
                    "            </zpzl>\n" +
                    "            <wjjyxm>\n" +
                    "                {\"44\":\"仪表和指示器\",\"01\":\"号牌号码/车辆类型\",\"45\":\"转向系部件\",\"02\":\"车辆品牌/型号\",\"46\":\"传动系部件\",\"03\":\"车辆识别代号（或整车出厂编号）\",\"47\":\"行驶系部件\",\"04\":\"发动机号码（或电动机号码）\",\"48\":\"制动系部件\",\"05\":\"车辆颜色和形状\",\"49\":\"其它部件\",\"06\":\"外廓尺寸\",\"07\":\"轴距\",\"08\":\"整备质量\",\"09\":\"核定载人数\",\"10\":\"核定载质量\",\"11\":\"栏板高度\",\"12\":\"后轴钢板弹簧片数\",\"13\":\"客车应急出口\",\"14\":\"客车乘客通道和引道\",\"15\":\"货厢\",\"16\":\"车身外观\",\"17\":\"外观标识、标注和标牌\",\"18\":\"外部照明和信号灯具\",\"19\":\"轮胎\",\"20\":\"号牌及号牌安装\",\"21\":\"加装/改装灯具\",\"22\":\"汽车安全带\",\"23\":\"机动车用三角警告牌\",\"24\":\"灭火器\",\"25\":\"行驶记录装置\",\"26\":\"车身反光标识\",\"27\":\"车辆尾部标志板\",\"28\":\"侧后防护装置\",\"29\":\"应急锤\",\"30\":\"急救箱\",\"31\":\"限速功能或限速装置\",\"32\":\"防抱死制动装置\",\"33\":\"辅助制动装置\",\"34\":\"盘式制动器\",\"35\":\"紧急切断装置\",\"36\":\"发动机舱自动灭火装置\",\"37\":\"手动机械断电开关\",\"38\":\"副制动踏板\",\"39\":\"校车标志灯和校车停车指示标志牌\",\"80\":\"联网查询\",\"81\":\"肢体残疾人操纵辅助装置\",\"40\":\"危险货物运输车标志\",\"41\":\"转向系\",\"42\":\"传动系\",\"43\":\"制动系\"}\n" +
                    "            </wjjyxm>\n" +
                    "            <jyhpzl>\n" +
                    "                {\"22\":\"临时行驶车\",\"01\":\"大型汽车\",\"23\":\"警用汽车\",\"02\":\"小型汽车\",\"24\":\"警用摩托\",\"03\":\"使馆汽车\",\"25\":\"原农机号牌\",\"26\":\"香港入出境车\",\"04\":\"领馆汽车\",\"27\":\"澳门入出境车\",\"05\":\"境外汽车\",\"06\":\"外籍汽车\",\"07\":\"普通摩托车\",\"08\":\"轻便摩托车\",\"09\":\"使馆摩托车\",\"51\":\"大型新能源汽车\",\"52\":\"小型新能源汽车\",\"31\":\"武警号牌\",\"32\":\"军队号牌\",\"10\":\"领馆摩托车\",\"99\":\"其他号牌\",\"11\":\"境外摩托车\",\"12\":\"外籍摩托车\",\"13\":\"低速车\",\"14\":\"拖拉机\",\"15\":\"挂车\",\"16\":\"教练汽车\",\"17\":\"教练摩托车\",\"18\":\"试验汽车\",\"19\":\"试验摩托车\",\"41\":\"无号牌\",\"42\":\"假号牌\",\"20\":\"临时入境汽车\",\"43\":\"挪用号牌\",\"21\":\"临时入境摩托车\"}\n" +
                    "            </jyhpzl>\n" +
                    "        </vehisinfo>\n" +
                    "    </body>\n" +
                    "</root>";
            writer.write(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=test")
    public void test(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("test");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String result = "test数据";
            writer.print(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=showAvatar")
    public void showAvatar(HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            String account = getRequestParams(request, "account");
            response.setContentType("image/jpeg");
            System.out.println("showAvatar::" + account);
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

    @RequestMapping(params = "method=downloadApk")
    public void downloadApk(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"mwm.apk\"");
            DataUtil.doDownLoadApk(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(params = "method=accountUpdate")
    public void accountUpdate(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = getRequestParams(request, "beanJson");
            System.out.println("accountUpdate::" + json);
            ResultBean resultBean;
            if (initParams(writer, response, json)) {
                return;
            }
            AccountBean updateBean = GsonUtil.json2Bean(json, AccountBean.class);
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
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=accountLock")
    public void accountLock(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String account = getRequestParams(request, "account");
            ResultBean<String> resultBean;
            if (initParams(writer, response, account)) {
                return;
            }
            AccountBean accountBean = service.getUserByAccount(new AccountBean(account));
            if (null == accountBean) {
                resultBean = new ResultBean<>("-1", "用户名或密码不正确");
            } else resultBean = new ResultBean<>(String.valueOf(accountBean.getLocked()));
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=updateAvatar")
    public void updateAvatar(HttpServletRequest request, HttpServletResponse response) {
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
                        writer.print(GsonUtil.bean2Json(resultBean));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=updateApk")
    public void updateApk(HttpServletRequest request, HttpServletResponse response) {
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

    @RequestMapping(params = "method=feedback")
    public void feedback(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String json = getRequestParams(request, "beanJson");
            ResultBean resultBean;
            if (initParams(writer, response, json)) {
                return;
            }
            int code = service.insertFeedBack(GsonUtil.json2Bean(json, FeedBackBean.class));
            resultBean = new ResultBean(String.valueOf(code), 1 == code ? "反馈成功" : "反馈失败");
            writer.print(GsonUtil.bean2Json(resultBean));
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

    @RequestMapping(params = "method=scan")
    public void scan(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String param = getRequestParams(request, "key");
            if (param.isEmpty()) {
                scanJsp(request, response);
            } else {
                request.setAttribute("message", "信息错误");
                request.getRequestDispatcher("/WEB-INF/page/index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    /**
     * 生成二维码，返回到页面上
     */
    @RequestMapping(params = "method=createQrCode")
    public void createQrCode(HttpServletRequest request, HttpServletResponse response) {
        String url = "http://192.168.0.110:8080/mwm/apk/readme.txt";
        response.setContentType("image/jpeg");
        OutputStream stream = null;
        try {
            stream = response.getOutputStream();
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, 256, 256);
            MatrixToImageWriter.writeToStream(m, "jpg", stream);
            stream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeStream(stream);
        }
    }

    @RequestMapping(params = "method=addContacts")
    public void addContacts(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            String param = getRequestParams(request, "key");
            String beanJson = getRequestParams(request, "beanJson");
            if (param.isEmpty()) {
                writer.print("未找到此用户");
            } else {
                ContactBean contactBean = GsonUtil.json2Bean(beanJson, ContactBean.class);
                contactBean.setAccount(CipherUtil.decode(param));
                ResultBean resultBean = DataUtil.doInsertContact(contactBean);
                writer.print(GsonUtil.bean2Json(resultBean));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=scanJsp")
    private void scanJsp(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("message", "信息错误");
            request.getRequestDispatcher("/loyal/scan.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(params = "method=checkVersion")
    public void checkVersion(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            ResultBean resultBean = DataUtil.queryApkVersion(String.valueOf(request.getLocalPort()), getRequestParams(request, "apkVer"));
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=checkUpdate")
    public void checkUpdate(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            ResultBean<String> resultBean = new ResultBean<>("https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk");
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=getFeedback")
    public void getFeedback(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String account = getRequestParams(request, "account");
            ResultBean<List<FeedBackBean>> resultBean;
            if (initParams(writer, response, account)) {
                return;
            }
            List<FeedBackBean> beanList = service.queryByAccount(account);
            resultBean = new ResultBean<>(beanList);
            System.out.println(GsonUtil.bean2Json(resultBean));
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=deleteFeedback")
    public void deleteFeedback(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        try {
            String json = getRequestParams(request, "beanJson");
            writer = response.getWriter();
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean("-1", "参数不能为空");
                writer.print(GsonUtil.bean2Json(resultBean));
                return;
            }
            int code = service.deleteFeedBack(GsonUtil.json2Bean(json, FeedBackBean.class));
            resultBean = new ResultBean(String.valueOf(code), 1 == code ? "已删除" : "删除失败");
            writer.print(GsonUtil.bean2Json(resultBean));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(writer);
        }
    }

    @RequestMapping(params = "method=accountDestroy")
    public void accountDestroy(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        try {
            writer = response.getWriter();
            String json = getRequestParams(request, "beanJson");
            ResultBean resultBean;
            if (emptyJson(json)) {
                resultBean = new ResultBean("-1", "参数不能为空");
                writer.print(GsonUtil.bean2Json(resultBean));
                return;
            }
            AccountBean accountBean = service.getUserByAccount(GsonUtil.json2Bean(json, AccountBean.class));
            if (null == accountBean)
                resultBean = new ResultBean("-1", "用户名或密码不正确");
            else {
                int code = service.destroyAccount(accountBean);
                resultBean = new ResultBean(String.valueOf(code), 1 == code ? "删除成功" : "删除失败");
            }
            writer.print(GsonUtil.bean2Json(resultBean));
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
            writer.print(GsonUtil.bean2Json(resultBean));
            return true;
        }
        return false;
    }

    private String getRequestParams(HttpServletRequest request, String param) {
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