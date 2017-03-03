<%--@elvariable id="message" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Apk上传页面</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link type="text/css" rel="stylesheet" href="/mwm/css/writeobj.css"/>
    <link type="text/css" rel="stylesheet" href="/mwm/css/easyui.css"/>
    <link type="text/css" rel="stylesheet" href="/mwm/css/icon.css"/>
    <script type="text/javascript" src="/mwm/js/jquery_min.js"></script>
    <script type="text/javascript" src="/mwm/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/mwm/js/easyui-lang.js"></script>
    <script type="text/javascript" src="/mwm/js/datagridfit.js"></script>
    <script type="text/javascript" src="/mwm/js/msgboxs.js"></script>

    <script type="text/javascript">
        $(function () {
            $('body').append('<div id="overlay" style="display:none;" ></div>');
            $('body').append('<div id="overlay1" align="center" style="display:none;" ><font>正在上传中，请稍后······</font></div>');
        });
        function appFileUpLoad() {
            var path = document.getElementById("app").value;
            if (path == null || path == "") {
                alert("请选择需要上传的APP文件");
                return;
            }
            var pathLength = path.length;
            var pathEnd = path.substring(pathLength - 4, pathLength);
            if (pathEnd != '.apk') {
                alert("请选择正确的APP文件");
                return;
            }
            $('#btnSave').linkbutton({disabled: true});
            docHeight = $(document).height();
            docWidth = $(document).width();
            msgShow();
            queryForm.submit();
        }
        function showProcessMessage(message) {
            msgHide();
            $.messager.alert('提示信息', "<font size='4' color='red' style='align-content: center'>" + message + "</font>");
            $('#btnSave').linkbutton({disabled: false});
        }
    </script>
</head>
<body>
<form name="queryForm" method="post" action="/mwm/action.do?method=doUpdateApk" target="lister"
      enctype="multipart/form-data">
    <table width="75%" align="center" border="0" cellspacing="1" cellpadding="0" bgcolor="#cccccc">
        <tr>
            <td width="100%" align="center">
                <font color="red"><h4>※文件命名格式如下<br>&lt;mwm_1.0.0.apk&gt;</h4></font>
            </td>
        </tr>
    </table>
    <table width="75%" height="40px" align="center" border="0" cellspacing="1" cellpadding="0" bgcolor="#cccccc">
        <tr>
            <td width="10%" align="right" class="td1">APP文件&nbsp;</td>
            <td width="15%" align="left" class="td2" colspan="3" height="100%">
                <input type="file" name="apkFile" id="app" accept=".apk" style="width:97%;height:70%">
            </td>
            <td width="10%" align="right" class="td1">状态&nbsp;</td>
            <td width="15%" align="left" class="td2" height="100%">
                <select id="zt" name="zt" style="width: 100px;height: 70%">
                    <option value=''></option>
                    <option value='0'>0:不可用</option>
                    <option value='1'>1:可用</option>
                </select>
            </td>
            <td width="25%" align="center" class="td2">
                <a href="#" onclick="appFileUpLoad();" class="easyui-linkbutton"
                   data-options="plain:'true',iconCls:'icon-upload'" id="btnSave">上传</a>&nbsp;&nbsp;
            </td>
        </tr>
    </table>
</form>
${message}
</body>
<iframe name="lister" width="0" height="0" style="display: none;"></iframe>
</html>