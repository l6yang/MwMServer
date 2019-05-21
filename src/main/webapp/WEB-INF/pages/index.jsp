<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>MwMServer 首页</title>

    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"/>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"/>
    <![endif]-->
</head>
<body>
<h1>这里是MwMServer首页</h1>

<h3>出现此页面，说明配置成功。</h3>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"/>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"/>
<a href="<c:url value="/loyal/apk_upload.jsp"/>"><h3> 上传Apk </h3></a>
<h4>扫码下载程序</h4>
<!--如果用HTML  直接将<img>的src属性设置为url-->
<img class="q_code" src="http://192.168.0.110:8080/mwm/action.do?method=createQrCode" alt=""/>
</body>
</html>