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
		queryCustomerByConditionForPage(1, 10);

		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });

		$("#create-nextContactTime").datetimepicker({
			language:'zh-CN', // 语言
			format: 'yyyy-mm-dd', // 日期的格式
			minView: 'month', // 可以选择的最小视图
			initData: new Date(), // 初始化显示的日期
			autoclose: true, // 选择完日期后是否自动关闭日历
			todayBtn:true, // 设置是否显示"今天"按钮，默认是false
			clearBtn: true // 设置是否显示"清空"按钮，默认是false
		});

		$("#edit-nextContactTime").datetimepicker({
			language:'zh-CN', // 语言
			format: 'yyyy-mm-dd', // 日期的格式
			minView: 'month', // 可以选择的最小视图
			initData: new Date(), // 初始化显示的日期
			autoclose: true, // 选择完日期后是否自动关闭日历
			todayBtn:true, // 设置是否显示"今天"按钮，默认是false
			clearBtn: true // 设置是否显示"清空"按钮，默认是false
		});

		$("#queryCustomerBtn").click(function () {
			queryCustomerByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
		});

		$("#createCustomerBtn").click(function () {
			$("#createCustomerForm")[0].reset;
			$("#createCustomerModal").modal("show");
		});

		$("#saveCustomerBtn").click(function () {
			var name = $.trim($("#create-name").val());
			var owner = $("#create-owner").val();
			var phone = $.trim($("#create-phone").val());
			var website = $.trim($("#create-website").val());
			var description = $.trim($("#create-description").val());
			var contactSummary = $.trim($("#create-contactSummary").val());
			var nextContactTime = $.trim($("#create-nextContactTime").val());
			var address = $.trim($("#create-address").val());

			var phoneRegExp = /^((\d{3,4}-)|\d{3.4}-)?\d{7,8}$/;
			if (!phoneRegExp.test(phone)) {
				alert("公司座机填写不规范");
				return;
			}
			var websiteRegExp = /[a-zA-z]+:\/\/[^\s]*/;
			if (!websiteRegExp.test(website)) {
				alert("公司网站填写不规范");
				return;
			}
			$.ajax({
				url: 'workbench/customer/insertCustomer.do',
				data: {
					name           : name           ,
					owner          : owner          ,
					phone          : phone          ,
					website        : website        ,
					description    : description    ,
					contactSummary : contactSummary ,
					nextContactTime: nextContactTime,
					address        : address
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == '0') {
						$("#createCustomerModal").modal("hide");
						queryCustomerByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						alert(data.msg);
						$("#createCustomerModal").modal("show");
					}
				}
			})
		});

		// 给修改按钮添加单击事件
		$("#editCustomerBtn").click(function () {
			// 获取列表中被选中的checkbox
			var checkIds = $("#tBody input[type='checkbox']:checked");
			if (checkIds.size() == 0) {
				alert("请选择要修改的线索");
				return;
			}
			if (checkIds.size() > 1 ){
				alert("每次只能修改一条线索");
				return;
			}
			var id = checkIds[0].value;
			$.ajax({
				url: 'workbench/customer/selectCustomerForUpdate.do',
				data: {
					id: id
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					$("#edit-id").val(data.data.id)
					$("#edit-owner").val(data.data.owner);
					$("#edit-name").val(data.data.name);
					$("#edit-phone").val(data.data.phone);
					$("#edit-website").val(data.data.website);
					$("#edit-description").val(data.data.description);
					$("#edit-contactSummary").val(data.data.contactSummary);
					$("#edit-nextContactTime").val(data.data.nextContactTime);
					$("#edit-address").val(data.data.address);

					$("#editCustomerModal").modal("show");
				}
			});
		});

		// 给更新按钮添加单击事件
		$("#updateCustomerBtn").click(function () {
			var id = $("#edit-id").val();
			var owner = $("#edit-owner").val();
			var name = $.trim($("#edit-name").val());
			var phone = $.trim($("#edit-phone").val());
			var website = $.trim($("#edit-website").val());
			var description = $.trim($("#edit-description").val());
			var contactSummary = $.trim($("#edit-contactSummary").val());
			var nextContactTime = $.trim($("#edit-nextContactTime").val());
			var address = $.trim($("#edit-address").val());

			// 表单验证
			// 带星号的非空
			if (name == "") {
				alert("名称不能为空");
				return;
			}
			// 正则表达式验证
			var phoneRegExp = /^((\d{3,4}-)|\d{3.4}-)?\d{7,8}$/;
			if (!phoneRegExp.test(phone)) {
				alert("公司座机填写不规范");
				return;
			}
			var websiteRegExp = /[a-zA-z]+:\/\/[^\s]*/;
			if (!websiteRegExp.test(website)) {
				alert("公司网站填写不规范");
				return;
			}

			// 发送请求
			$.ajax({
				url: "workbench/customer/updateCustomer.do",
				data: {
					id             : id             ,
					name           : name           ,
					owner          : owner          ,
					phone          : phone          ,
					website        : website        ,
					description    : description    ,
					contactSummary : contactSummary ,
					nextContactTime: nextContactTime,
					address        : address
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == '0') {
						$("#editCustomerModal").modal("hide");
						queryCustomerByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						alert(data.msg);
						$("#editCustomerModal").modal("show");
					}
				}
			});
		});

		// 给删除按钮添加单击事件
		$("#deleteCustomerBtn").click(function () {
			// 收集参数
			// 获取列表中所有被选中的checkbox
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if (checkedIds.size() == 0) {
				alert("请选择要删除的客户");
				return;
			}

			if (window.confirm("确定删除吗？")) {
				var ids = "";
				$.each(checkedIds, function () {
					ids += "ids=" + this.value + "&";
				});
				ids.substr(0, ids.length-1);
				// 发送请求
				$.ajax({
					url: 'workbench/customer/deleteCustomer.do',
					data: ids,
					type: 'post',
					dataType: 'json',
					success: function (data) {
						if (data.code == '0') {
							queryCustomerByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
						} else {
							// 提示信息
							alert(data.msg);
						}
					}
				});
			}
		});
	});

	function queryCustomerByConditionForPage(pageNo, pageSize) {
		// 收集参数
		var name = $("#query-name").val();
		var website = $("#query-website").val();
		var phone = $("#query-phone").val();
		var owner = $("#query-owner").val();
		// var pageNo = 1;
		// var pageSize = 10;
		// 发送请求
		$.ajax({
			url: 'workbench/customer/selectCustomerByConditionForPages.do',
			data: {
				name: name,
				website: website,
				phone: phone,
				owner: owner,
				pageNo: pageNo,
				pageSize: pageSize
			},
			type: 'post',
			dataType: 'json',
			success: function (data) {
				var htmlStr = "";
				$.each(data.data.customerList, function (index, obj) {
					htmlStr += "<tr>";
					htmlStr += "	<td><input type=\"checkbox\" value='"+obj.id+"'/></td>";
					htmlStr += "	<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/customer/detail.do?id="+obj.id+"';\">"+obj.name+"</a></td>";
					htmlStr += "	<td>"+obj.owner+"</td>";
					htmlStr += "	<td>"+obj.phone+"</td>";
					htmlStr += "	<td>"+obj.website+"</td>";
					htmlStr += "</tr>";
				});
				$("#tBody").html(htmlStr);

				// 取消全选按钮
				$("#checkAll").prop("checked", false);

				// 计算总页数
				var totalPages = 1;
				if (data.data.totalRows % pageSize == 0) {
					totalPages = data.data.totalRows / pageSize;
				} else {
					totalPages = parseInt(data.data.totalRows / pageSize) + 1;
				}

				// 对容器调用bs_pagination工具函数，显示翻页信息
				$("#mydiv").bs_pagination({
					currentPage: pageNo, // 当前页号，相当于pageNo
					rowsPerPage: pageSize, // 每页显示条数，相当于pageSize
					totalRows: data.data.totalRows, // 总条数
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
						queryCustomerByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
					}
				})
			}
		});
	}

</script>
</head>
<body>

	<!-- 创建客户的模态窗口 -->
	<div class="modal fade" id="createCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="createCustomerForm">

						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-name">
							</div>
						</div>

						<div class="form-group">
                            <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-website">
                            </div>
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
						</div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="create-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCustomerBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改客户的模态窗口 -->
	<div class="modal fade" id="editCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
							<label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" value="动力节点">
							</div>
						</div>

						<div class="form-group">
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
                            </div>
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>

						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="edit-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateCustomerBtn">更新</button>
				</div>
			</div>
		</div>
	</div>




	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>客户列表</h3>
			</div>
		</div>
	</div>

	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="query-phone">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司网站</div>
				      <input class="form-control" type="text" id="query-website">
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="queryCustomerBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createCustomerBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editCustomerBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteCustomerBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>

			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
							<td>所有者</td>
							<td>公司座机</td>
							<td>公司网站</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点</a></td>
							<td>zhangsan</td>
							<td>010-84846003</td>
							<td>http://www.bjpowernode.com</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点</a></td>
                            <td>zhangsan</td>
                            <td>010-84846003</td>
                            <td>http://www.bjpowernode.com</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="mydiv"></div>
			</div>

			<%--<div style="height: 50px; position: relative;top: 30px;">
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
