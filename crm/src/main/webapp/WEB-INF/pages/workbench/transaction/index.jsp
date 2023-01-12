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
<link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">

	$(function(){
		// 给创建按钮添加单击事件
		$("#createTranBtn").click(function () {
			window.location.href = "workbench/transaction/toSave.do";
		});

		queryTranByConditionForPage(1, 10);

		// 给查询按钮添加单击事件
		$("#queryTranBtn").click(function () {
			queryTranByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
		})
	});

	function queryTranByConditionForPage(pageNo, pageSize) {
		// 收集参数
		var source = $("#query-source").val();
		var owner = $("#query-owner").val();
		var stage = $("#query-stage").val();
		var name = $("#query-name").val();
		var customerName = $("#query-customerName").val();
		var type = $("#query-type").val();
		var contactsName = $("#query-contactsName").val();
		// var pageNo = 1;
		// var pageSize = 10;
		// 发送请求
		$.ajax({
			url: 'workbench/transaction/queryTranByConditionForPages.do',
			data: {
				source: source,
				stage: stage,
				name: name,
				type: type,
				owner: owner,
				customerName: customerName,
				contactsName: contactsName,
				pageNo: pageNo,
				pageSize: pageSize
			},
			type: 'post',
			dataType: 'json',
			success: function (data) {
				// 显示总条数
				// $("#totalRowsB").text(data.totalRows)
				var htmlStr = "";
				$.each(data.tranList, function (index, obj) {
					htmlStr += "<tr>";
					htmlStr += "	<td><input type=\"checkbox\" /></td>";
					htmlStr += "	<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/transaction/toTranDetail.do?tranId="+obj.id+"';\">"+obj.name+"</a></td>";
					htmlStr += "	<td>"+obj.customerName+"</td>";
					htmlStr += "	<td>"+obj.stage+"</td>";
					htmlStr += "	<td>"+obj.type+"</td>";
					htmlStr += "	<td>"+obj.owner+"</td>";
					htmlStr += "	<td>"+obj.source+"</td>";
					htmlStr += "	<td>"+obj.contactsName+"</td>";
					htmlStr += "</tr>";
				});
				$("#tBody").html(htmlStr);

				// 取消全选按钮
				$("#checkAll").prop("checked", false);

				// 计算总页数
				var totalPages = 1;
				if (data.totalRows % pageSize == 0) {
					totalPages = data.totalRows / pageSize;
				} else {
					totalPages = parseInt(data.totalRows / pageSize) + 1;
				}

				// 对容器调用bs_pagination工具函数，显示翻页信息
				$("#mydiv").bs_pagination({
					currentPage: pageNo, // 当前页号，相当于pageNo
					rowsPerPage: pageSize, // 每页显示条数，相当于pageSize
					totalRows: data.totalRows, // 总条数
					totalPages: totalPages, // 总页数，必填

					visiblePageLinks: 5, // 最多可以显示的卡片数

					showGoToPage: true, // 是否显示"跳转到"部分，默认是true
					showRowsPerPage: true, // 是否显示"每页显示条数"部分，默认是true
					showRowsInfo: true, // 是否显示记录的信息，默认是true

					// 用户每次切换页号都会触发本函数
					// 每次返回切换页号之后的pageNo和pageSize
					onChangePage: function (event, pageObj) {
						// alert(pageObj.currentPage);
						// alert(pageObj.rowsPerPage);
						queryTranByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
					}
				})
			}
		});
	}

</script>
</head>
<body>
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>

	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="query-customerName">
				    </div>
				  </div>

				  <br>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="query-stage">
					  	<option></option>
					  	<c:forEach items="${stageList}" var="s">
							<option value="${s.id}">${s.value}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="query-type">
					  	<option></option>
						  <c:forEach items="${transactionTypeList}" var="tt">
							  <option value="${tt.id}">${tt.value}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="query-source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="so">
							  <option value="${so.id}">${so.value}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="query-contactsName">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="queryTranBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createTranBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" onclick="window.location.href='edit.html';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>


			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>
                            <td>动力节点</td>
                            <td>谈判/复审</td>
                            <td>新业务</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>李四</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="mydiv"></div>
			</div>

			<%--<div style="height: 50px; position: relative;top: 20px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>

		</div>

	</div>
</body>
</html>
