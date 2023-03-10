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

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;

	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});

		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
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

		$("#remarkDivList").on("mouseover", ".remarkDiv", function () {
			$(this).children("div").children("div").show();
		})

		$("#remarkDivList").on("mouseout", ".remarkDiv", function () {
			$(this).children("div").children("div").hide();
		})

		$("#remarkDivList").on("mouseover", ".myHref", function () {
			$(this).children("span").css("color","red");
		})

		$("#remarkDivList").on("mouseout", ".myHref", function () {
			$(this).children("span").css("color","#E6E6E6");
		})

		$("#createCustomerRemarkBtn").click(function () {
			var noteContent = $.trim($("#remark").val());
			var customerId = '${customer.id}'
			// 表单验证
			if (noteContent == "") {
				alert("备注内容不能为空");
				return;
			}
			// 发送请求
			$.ajax({
				url: 'workbench/customer/insertCustomerRemark.do',
				data: {
					noteContent: noteContent,
					customerId: customerId
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == '0') {
						// 清空输入框
						$("#remark").val("");
						// 刷新备注列表
						var htmlStr="";
						htmlStr += "<div class=\"remarkDiv\" id=\"div_"+data.data.id+"\" style=\"height: 60px;\">";
						htmlStr += "	<img title=\"${sessionScope.sessionUser.name}\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">";
						htmlStr += "		<div style=\"position: relative; top: -40px; left: 40px;\" >";
						htmlStr += "			<h5>"+data.data.noteContent+"</h5>";
						htmlStr += "			<font color=\"gray\">客户</font> <font color=\"gray\">-</font> <b>${customer.name}</b> <small style=\"color: gray;\"> "+data.data.createTime+" 由${sessionScope.sessionUser.name}创建</small>";
						htmlStr += "			<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">";
						htmlStr += "				<a class=\"myHref\" name=\"editA\" remarkId=\""+data.data.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						htmlStr += "				&nbsp;&nbsp;&nbsp;&nbsp;";
						htmlStr += "				<a class=\"myHref\" name=\"deleteA\" remarkId=\""+data.data.id+"\" href=\"javascript:void(0);\"><span class=\"glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>";
						htmlStr += "			</div>";
						htmlStr += "		</div>";
						htmlStr += "</div>";
						$("#remarkDiv").before(htmlStr);
					} else {
						alert(data.msg);
					}
				}
			})
		});

		// 给所有备注的修改图标添加单击事件
		$("#remarkDivList").on("click", "a[name='editA']", function () {
			// 获取备注的id和noteContent
			var id = $(this).attr("remarkId");
			var noteContent = $("#div_"+id+" h5").text();
			$("#edit-id").val(id);
			$("#edit-noteContent").val(noteContent);
			// 弹出修改市场活动备注的模态窗口
			$("#editRemarkModal").modal("show");
		});

		// 给更新按钮添加单击事件
		$("#updateRemarkBtn").click(function () {
			// 收集参数
			var id = $("#edit-id").val();
			var noteContent = $.trim($("#edit-noteContent").val());
			// 表单验证
			if (noteContent == "") {
				alert("备注内容不能为空");
				return;
			}
			// 发送请求
			$.ajax({
				url: 'workbench/customer/updateCustomerRemark.do',
				data: {
					id: id,
					noteContent: noteContent
				},
				dataType: 'json',
				type: 'post',
				success: function (data) {
					if (data.code == '0') {
						$("#editRemarkModal").modal("hide");
						// 刷新备注列表
						$("#div_"+id+" h5").text(data.data.noteContent);
						$("#div_"+id+" small").text(" "+data.data.editTime+" 由${sessionScope.sessionUser.name}修改");
					} else {
						// 提示信息
						alert(data.msg);
						$("#editRemarkModal").modal("show");
					}
				}
			})
		});

		// 给所有删除备注图标添加单击事件
		$("#remarkDivList").on("click", "a[name='deleteA']", function () {
			// 收集参数
			var id = $(this).attr("remarkId");
			// 向后台发送请求
			$.ajax({
				url: 'workbench/customer/deleteCustomerRemark.do',
				data: {
					id: id
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == '0') {
						// 刷新备注列表
						$("#div_"+id).remove();
					} else {
						alert(data.msg);
					}
				}
			})
		});

		// 给所有删除交易按钮添加单击事件
		$("#tranTBody").on("click", "a[name='deleteA']", function () {
			// 收集参数
			var tranId = $(this).attr("tranId");
			if (window.confirm("确定删除吗?")) {
				$.ajax({
					url: 'workbench/transaction/deleteTranById.do',
					data: {
						id: tranId
					},
					type: 'post',
					dataType: 'json',
					success: function (data) {
						if (data.code == '0') {
							$("#tr_"+tranId).remove();
						} else {
							alert(data.msg);
						}
					}
				});
			}
		});

		// 给新建联系人按钮添加单击事件
		$("#createContactBtn").click(function () {
			$("#createContactForm")[0].reset();
			$("#createContactsModal").modal("show");
		});

		// 给保存新建联系人按钮添加单击事件
		$("#saveCreateContactBtn").click(function () {
			var owner             = $("#create-owner            ").val();
			var source            = $("#create-source           ").val();
			var customerId        = '${customer.id}';
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
				url: 'workbench/customer/insertContactOnCustomerDetail.do',
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
						var htmlStr="";
						htmlStr += "<tr id='tr_"+data.data.id+"'>";
						htmlStr += "    <td><a href=\"workbench/contacts/detail.do?id="+data.data.id+"\" style=\"text-decoration: none;\">"+data.data.fullname+"</a></td>";
						htmlStr += "    <td>"+data.data.email+"</td>";
						htmlStr += "    <td>"+data.data.mphone+"</td>";
						htmlStr += "    <td><a href=\"javascript:void(0);\" name='deleteA' contactId=\""+data.data.id+"\" style=\"text-decoration: none;\"><span class=\"glyphicon glyphicon-remove\"></span>删除</a></td>";
						htmlStr += "</tr>";
						$("#contactTBody").append(htmlStr);
					} else {
						alert(data.msg);
						$("#createContactsModal").modal("show");
					}
				}
			})
		});

		// 给删除联系人按钮添加单击事件
		$("#contactTBody").on("click", "a[name='deleteA']", function () {
			// 收集参数
			var contactId = $(this).attr("contactId");
			// 向后台发送请求
			if (window.confirm("确定删除吗?")) {
				$.ajax({
					url: 'workbench/customer/deleteContactOnCustomerDetail.do',
					data: {
						contactId: contactId
					},
					type: 'post',
					dataType: 'json',
					success: function (data) {
						if (data.code == '0') {
							// 刷新备注列表
							$("#tr_"+contactId).remove();
						} else {
							alert(data.msg);
						}
					}
				})
			}
		});
	});

</script>

</head>
<body>

	<!-- 修改客户备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
		<div class="modal-dialog" role="document" style="width: 40%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改备注</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-noteContent" class="col-sm-2 control-label">内容</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-noteContent"></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 删除联系人的模态窗口 -->
	<div class="modal fade" id="removeContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 30%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">删除联系人</h4>
				</div>
				<div class="modal-body">
					<p>您确定要删除该联系人吗？</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">删除</button>
				</div>
			</div>
		</div>
	</div>

    <!-- 删除交易的模态窗口 -->
    <div class="modal fade" id="removeTransactionModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 30%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title">删除交易</h4>
                </div>
                <div class="modal-body">
                    <p>您确定要删除该交易吗？</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">删除</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 创建联系人的模态窗口 -->
	<div class="modal fade" id="createContactsModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" onclick="$('#createContactsModal').modal('hide');">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建联系人</h4>
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
								<%--<input type="hidden" id="create-customerId" value="${customer.id}">--%>
								<input type="text" class="form-control" id="create-customerName" value="${customer.name}" readonly>
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



	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>

	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>${customer.name} <small><a href="${customer.website}" target="_blank">${customer.website}</a></small></h3>
		</div>
		<%--<div style="position: relative; height: 50px; width: 500px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editCustomerModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>--%>
	</div>

	<br/>
	<br/>
	<br/>

	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${customer.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">公司网站</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.website}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">公司座机</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${customer.phone}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${customer.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${customer.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${customer.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${customer.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 40px;">
            <div style="width: 300px; color: gray;">联系纪要</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${customer.contactSummary}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
        <div style="position: relative; left: 40px; height: 30px; top: 50px;">
            <div style="width: 300px; color: gray;">下次联系时间</div>
            <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${customer.nextContactTime}</b></div>
            <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px; "></div>
        </div>
		<div style="position: relative; left: 40px; height: 30px; top: 60px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${customer.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
        <div style="position: relative; left: 40px; height: 30px; top: 70px;">
            <div style="width: 300px; color: gray;">详细地址</div>
            <div style="width: 630px;position: relative; left: 200px; top: -20px;">
                <b>
					${customer.address}
                </b>
            </div>
            <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
        </div>
	</div>

	<!-- 备注 -->
	<div id="remarkDivList" style="position: relative; top: 10px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>

		<c:forEach items="${customerRemarkList}" var="cr">
			<div class="remarkDiv" id="div_${cr.id}" style="height: 60px;">
				<img title="${cr.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
				<div style="position: relative; top: -40px; left: 40px;" >
					<h5>${cr.noteContent}</h5>
					<font color="gray">客户</font> <font color="gray">-</font> <b>${customer.name}</b> <small style="color: gray;"> ${cr.editFlag=='0'?cr.createTime:cr.editTime} 由${cr.editFlag=='0'?cr.createBy:cr.editBy}${cr.editFlag=='0'?'创建':'修改'}</small>
					<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
						<a class="myHref" name="editA" remarkId="${cr.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="myHref" name="deleteA" remarkId="${cr.id}" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
					</div>
				</div>
			</div>
		</c:forEach>
		<%--<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">客户</font> <font color="gray">-</font> <b>北京动力节点</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>

		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">客户</font> <font color="gray">-</font> <b>北京动力节点</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>--%>

		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="createCustomerRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>

	<!-- 交易 -->
	<div>
		<div style="position: relative; top: 20px; left: 40px;">
			<div class="page-header">
				<h4>交易</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable2" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>金额</td>
							<td>阶段</td>
							<td>可能性</td>
							<td>预计成交日期</td>
							<td>类型</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="tranTBody">
						<c:forEach items="${tranList}" var="tran">
							<tr id="tr_${tran.id}">
								<td><a href="workbench/transaction/toTranDetail.do?tranId=${tran.id}" style="text-decoration: none;">${tran.name}</a></td>
								<td>${tran.money}</td>
								<td>${tran.stage}</td>
								<td>${tran.possibility}%</td>
								<td>${tran.expectedDate}</td>
								<td>${tran.type}</td>
								<td><a href="javascript:void(0);" name="deleteA" id="deleteTranBtn" tranId="${tran.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
							</tr>
						</c:forEach>
						<%--<tr>
							<td><a href="../transaction/detail.jsp" style="text-decoration: none;">动力节点-交易01</a></td>
							<td>5,000</td>
							<td>谈判/复审</td>
							<td>90</td>
							<td>2017-02-07</td>
							<td>新业务</td>
							<td><a href="javascript:void(0);" data-toggle="modal" data-target="#removeTransactionModal" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
						</tr>--%>
					</tbody>
				</table>
			</div>

			<div>
				<a href="workbench/customer/toSaveTran.do?customerName=${customer.name}" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建交易</a>
			</div>
		</div>
	</div>

	<!-- 联系人 -->
	<div>
		<div style="position: relative; top: 20px; left: 40px;">
			<div class="page-header">
				<h4>联系人</h4>
			</div>
			<div style="position: relative;top: 0px;">
				<table id="activityTable" class="table table-hover" style="width: 900px;">
					<thead>
						<tr style="color: #B3B3B3;">
							<td>名称</td>
							<td>邮箱</td>
							<td>手机</td>
							<td></td>
						</tr>
					</thead>
					<tbody id="contactTBody">
						<c:forEach items="${contactsList}" var="contact">
							<tr id="tr_${contact.id}">
								<td><a href="workbench/contacts/detail.do?id=${contact.id}" style="text-decoration: none;">${contact.fullname}</a></td>
								<td>${contact.email}</td>
								<td>${contact.mphone}</td>
								<td><a href="javascript:void(0);" name="deleteA" contactId="${contact.id}" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
							</tr>
						</c:forEach>
						<%--<tr>
							<td><a href="../contacts/detail.jsp" style="text-decoration: none;">李四</a></td>
							<td>lisi@bjpowernode.com</td>
							<td>13543645364</td>
							<td><a href="javascript:void(0);" data-toggle="modal" data-target="#removeContactsModal" style="text-decoration: none;"><span class="glyphicon glyphicon-remove"></span>删除</a></td>
						</tr>--%>
					</tbody>
				</table>
			</div>

			<div>
				<a href="javascript:void(0);" id="createContactBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-plus"></span>新建联系人</a>
			</div>
		</div>
	</div>

	<div style="height: 200px;"></div>
</body>
</html>
