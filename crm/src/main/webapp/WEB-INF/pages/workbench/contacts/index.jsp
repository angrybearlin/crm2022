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
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
<script type="text/javascript">

	$(function(){
		queryContactByConditionForPage(1,10);

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

		// 当容器加载完成之后，对容器调用工具函数
		$("#create-customerName").typeahead({
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

		// 给查询按钮添加单击事件
		$("#queryContactBtn").click(function () {
			queryContactByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
		});

		// 给创建按钮添加单击事件
		$("#createContactBtn").click(function () {
			$("#createContactForm")[0].reset();
			$("#createContactsModal").modal("show");
		});

		// 给保存创建联系人按钮添加单击事件
		$("#saveCreateContactBtn").click(function () {
			var owner             = $("#create-owner            ").val();
			var source            = $("#create-source           ").val();
			var customerId        = $.trim($("#create-customerName     ").val());
			var fullname          = $.trim($("#create-fullname         ").val());
			var appellation       = $("#create-appellation      ").val();
			var email             = $.trim($("#create-email            ").val());
			var mphone            = $.trim($("#create-mphone           ").val());
			var job               = $.trim($("#create-job              ").val());
			var description       = $.trim($("#create-description      ").val());
			var contactSummary    = $.trim($("#create-contactSummary   ").val());
			var nextContactTime   = $("#create-nextContactTime  ").val();
			var address           = $.trim($("#create-address          ").val());

			if (fullname ==  null) {
				alert("姓名不能为空");
				return;
			}
			var mphoneRegExp = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
			if (!mphoneRegExp.test(mphone)) {
				alert("手机号填写格式不规范");
				return;
			}
			var emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			if (!emailRegExp.test(email)) {
				alert("邮箱填写格式不规范");
				return;
			}

			$.ajax({
				url: 'workbench/contacts/createContact.do',
				data: {
					owner          : owner          ,
					source         : source         ,
					customerId     : customerId     ,
					fullname       : fullname       ,
					appellation    : appellation    ,
					email          : email          ,
					mphone         : mphone         ,
					job            : job            ,
					description    : description    ,
					contactSummary : contactSummary ,
					nextContactTime: nextContactTime,
					address        : address
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == '0') {
						$("#createContactsModal").modal("hide");
						queryContactByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						alert(data.msg);
						$("#createContactsModal").modal("show");
					}
				}
			})
		});

		// 给修改按钮添加单击事件
		$("#editContactBtn").click(function () {
			// 获取列表中被选中的checkbox
			var checkIds = $("#tBody input[type='checkbox']:checked");
			if (checkIds.size() == 0) {
				alert("请选择要修改的联系人");
				return;
			}
			if (checkIds.size() > 1 ){
				alert("每次只能修改一个联系人");
				return;
			}
			var id = checkIds[0].value;
			$.ajax({
				url: 'workbench/contacts/selectContactForUpdate.do',
				data: {
					id: id
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					$("#edit-id").val(data.data.id)
					$("#edit-owner").val(data.data.owner);
					$("#edit-source").val(data.data.source);
					$("#edit-fullname").val(data.data.fullname);
					$("#edit-appellation").val(data.data.appellation);
					$("#edit-job").val(data.data.job);
					$("#edit-mphone").val(data.data.mphone);
					$("#edit-email").val(data.data.email);
					$("#edit-customerName").val(data.data.customerId);
					$("#edit-description").val(data.data.description);
					$("#edit-contactSummary").val(data.data.contactSummary);
					$("#edit-nextContactTime").val(data.data.nextContactTime);
					$("#edit-address").val(data.data.address);

					$("#editContactsModal").modal("show");
				}
			});
		});

		// 给更新按钮添加单击事件
		$("#updateContactBtn").click(function () {
			var id              = $("#edit-id").val();
			var owner           = $("#edit-owner").val();
			var source          = $("#edit-source").val();
			var fullname        = $.trim($("#edit-fullname").val());
			var appellation     = $("#edit-appellation").val();
			var job             = $.trim($("#edit-job").val());
			var mphone          = $.trim($("#edit-mphone").val());
			var email           = $.trim($("#edit-email").val());
			var customerId      = $.trim($("#edit-customerName").val());
			var description     = $.trim($("#edit-description").val());
			var contactSummary  = $.trim($("#edit-contactSummary").val());
			var nextContactTime = $.trim($("#edit-nextContactTime").val());
			var address         = $.trim($("#edit-address").val());

			// 表单验证
			// 带星号的非空
			if (fullname == "") {
				alert("姓名不能为空");
				return;
			}
			// 正则表达式验证
			var mphoneRegExp = /^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
			if (!mphoneRegExp.test(mphone)) {
				alert("手机填写不规范");
				return;
			}
			var emailRegExp = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			if (!emailRegExp.test(email)) {
				alert("邮箱填写不规范");
				return;
			}

			// 发送请求
			$.ajax({
				url: "workbench/contacts/updateContact.do",
				data: {
					id         : id             ,
					owner      : owner          ,
					source     : source         ,
					fullname   : fullname       ,
					appellation: appellation    ,
					job        : job            ,
					mphone     : mphone         ,
					email      : email          ,
					customerId : customerId     ,
					description    : description    ,
					contactSummary : contactSummary ,
					nextContactTime: nextContactTime,
					address        : address

				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == '0') {
						$("#editContactsModal").modal("hide");
						queryContactByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
					} else {
						alert(data.msg);
						$("#editContactsModal").modal("show");
					}
				}
			});
		});

		// 给删除按钮添加单击事件
		$("#deleteContactBtn").click(function () {
			// 收集参数
			// 获取列表中所有被选中的checkbox
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if (checkedIds.size() == 0) {
				alert("请选择要删除的联系人");
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
					url: 'workbench/contacts/deleteContacts.do',
					data: ids,
					type: 'post',
					dataType: 'json',
					success: function (data) {
						if (data.code == '0') {
							queryContactByConditionForPage(1, $("#mydiv").bs_pagination('getOption', 'rowsPerPage'));
						} else {
							// 提示信息
							alert(data.msg);
						}
					}
				});
			}
		});
	});

	function queryContactByConditionForPage(pageNo, pageSize) {
		// 收集参数
		var owner = $("#query-owner").val();
		var fullname = $("#query-fullname").val();
		var customerName = $("#query-customerName").val();
		var source = $("#query-source").val();
		// var pageNo = 1;
		// var pageSize = 10;
		// 发送请求
		$.ajax({
			url: 'workbench/contacts/queryContactByCondition.do',
			data: {
				owner: owner,
				fullname: fullname,
				customerName: customerName,
				source: source,
				pageNo: pageNo,
				pageSize: pageSize
			},
			type: 'post',
			dataType: 'json',
			success: function (data) {
				var htmlStr = "";
				$.each(data.data.contactsList, function (index, obj) {
					htmlStr += "<tr>";
					htmlStr += "	<td><input type=\"checkbox\" value='"+obj.id+"'/></td>";
					htmlStr += "	<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/contacts/detail.do?id="+obj.id+"';\">"+obj.fullname+"</a></td>";
					htmlStr += "	<td>"+obj.customerId+"</td>";
					htmlStr += "	<td>"+obj.owner+"</td>";
					htmlStr += "	<td>"+obj.source+"</td>";
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
						queryContactByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
					}
				})
			}
		});
	}

</script>
</head>
<body>


	<!-- 创建联系人的模态窗口 -->
	<div class="modal fade" id="createContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabelx">创建联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="createContactForm">

						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-source" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
								  <option></option>
									<c:forEach items="${sourceList}" var="source">
										<option value="${source.id}">${source.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="create-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-fullname">
							</div>
							<label for="create-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-appellation">
								  <option></option>
									<c:forEach items="${appellationList}" var="app">
										<option value="${app.id}">${app.value}</option>
									</c:forEach>
								</select>
							</div>

						</div>

						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
						</div>

						<div class="form-group" style="position: relative;">
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
							<label for="create-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
							</div>
						</div>

						<div class="form-group" style="position: relative;">
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
					<button type="button" class="btn btn-primary" id="saveCreateContactBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改联系人的模态窗口 -->
	<div class="modal fade" id="editContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">修改联系人</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
									<option></option>
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-source" class="col-sm-2 control-label">来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
								  <option></option>
									<c:forEach items="${sourceList}" var="source">
										<option value="${source.id}">${source.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-fullname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-fullname" value="李四">
							</div>
							<label for="edit-appellation" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation">
								  <option></option>
									<c:forEach items="${appellationList}" var="app">
										<option value="${app.id}">${app.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
							<label for="edit-customerName" class="col-sm-2 control-label">客户名称</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-customerName" placeholder="支持自动补全，输入客户不存在则新建" value="动力节点">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">这是一条线索的描述信息</textarea>
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
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴区大族企业湾</textarea>
                                </div>
                            </div>
                        </div>
					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateContactBtn">更新</button>
				</div>
			</div>
		</div>
	</div>





	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>联系人列表</h3>
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
				      <div class="input-group-addon">姓名</div>
				      <input class="form-control" type="text" id="query-fullname">
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
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="query-source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="source">
							  <option value="${source.id}">${source.value}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>

				  <%--<div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">生日</div>
				      <input class="form-control" type="text">
				    </div>
				  </div>--%>

				  <button type="button" class="btn btn-default" id="queryContactBtn">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createContactBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editContactBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteContactBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>


			</div>
			<div style="position: relative;top: 20px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>姓名</td>
							<td>客户名称</td>
							<td>所有者</td>
							<td>来源</td>
<%--							<td>生日</td>--%>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四</a></td>
							<td>动力节点</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>2000-10-10</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四</a></td>
                            <td>动力节点</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>2000-10-10</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="mydiv"></div>
			</div>

			<%--<div style="height: 50px; position: relative;top: 10px;">
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
