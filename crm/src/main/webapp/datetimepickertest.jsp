<%--
  Created by IntelliJ IDEA.
  User: linkexuan
  Date: 2022/12/28
  Time: 10:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href=<%=basePath%>>
<%--    jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--    BOOTSTRAP框架--%>
    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<%--    BOOTSTRAP_DATETIMEPICKER插件--%>
    <link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <title>演示bs_datetimepicker插件</title>
    <script type="text/javascript">
        $(function () {
            $("#myDate").datetimepicker({
                language:'zh-CN', // 语言
                format: 'yyyy-mm-dd', // 日期的格式
                minView: 'month', // 可以选择的最小视图
                initData: new Date(), // 初始化显示的日期
                autoclose: true, // 选择完日期后是否自动关闭日历
                todayBtn:true, // 设置是否显示"今天"按钮，默认是false
                clearBtn: true // 设置是否显示"清空"按钮，默认是false
            });
        })
    </script>
</head>
<body>
<input type="text" id="myDate" readonly>
</body>
</html>
