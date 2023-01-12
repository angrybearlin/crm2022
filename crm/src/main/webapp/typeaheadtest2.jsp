<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href=<%=basePath%>>
    <meta charset="UTF-8">
    <title>演示自动补全插件</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <script type="text/javascript">
        $(function () {
            // 当容器加载完成之后，对容器调用工具函数
            $("#customerName").typeahead({
                source: function (query, process) { // 每次键盘弹起，都会自动触发本函数，可以向后台发送请求，查询客户表中所有名称，以[]字符串形式返回json
                    $.ajax({
                        url: 'workbench/transaction/queryCustomerNameByName.do',
                        data: {
                            customerName: query
                        },
                        type: 'post',
                        dataType: 'json',
                        success: function (data) { // ['xxx', 'xxx', ...]
                            process(data.data);
                        }
                    })
                }
            });
        })
    </script>
</head>
<body>
<input type="text" id="customerName">
</body>
</html>
