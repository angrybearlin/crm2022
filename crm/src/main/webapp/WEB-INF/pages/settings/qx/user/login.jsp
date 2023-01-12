<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
	<base href=<%=basePath%>>
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(window).keydown(function (e) {
			if (e.keyCode == 13) {
				$("#loginBtn").click();
			}
		})

		$(function () {
			$("#loginBtn").click(function () {
				var loginAct = $.trim($("#loginAct").val());
				var loginPwd = $.trim($("#loginPwd").val());
				var isRemPwd = $("#isRemPwd").prop("checked");
				if (loginAct === "") {
					$("#msg").text("用户名不能为空");
					return;
				}
				if (loginPwd === "") {
					$("#msg").text("密码不能为空");
					return;
				}

				$.ajax({
					url: "settings/qx/user/login.do",
					type: "post",
					dateType: "json",
					data: {
						loginAct: loginAct,
						loginPwd: loginPwd,
						isRemPwd: isRemPwd
					},
					success: function (retValue) {
						if (retValue.code === "1") {
							$("#msg").text(retValue.msg);
						} else {
							window.location.href = "workbench/index.do"
						}
					},
					beforeSend: function () { // 当AJAX向后台发送请求之前，会自动执行该函数。该函数的返回值能决定
												// AJAX是否真正向后台发请求:如果该函数返回true，则发请求，为false则不发
						$("#msg").text("正在验证登录。。。");
					}
				})
			})
		})
	</script>
</head>
<body>
	<style type="text/css">
		#msg {
			color: red;
		}
	</style>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2022&nbsp;生气小熊</span></div>
	</div>

	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="loginAct" class="form-control" type="text" value="${cookie.loginAct.value}" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="loginPwd" class="form-control" type="password" value="${cookie.loginPwd.value}" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						<label>
							<c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
								<input id="isRemPwd" type="checkbox" checked>
							</c:if>
							<c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
								<input id="isRemPwd" type="checkbox">
							</c:if>
							 十天内免登录
						</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button id="loginBtn" type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
