<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript">

	$(function(){

		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//页面加载完成后，触发分页操作
		pageList(1,2);


		//为创建按钮绑定事件
		$("#addBtn").click(function () {

			//从后台中获得数据
			$.ajax({

				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data) {

					var html="<option></option>";

					$.each(data,function (i,n) {
						//n为每一个用户user
						html+="<option value='"+n.id+"'>"+n.name+"</option>";
					})

					$("#create-owner").html(html);

					//默认值设定为登录用户姓名
					//el表达式在js中使用，一定要放在字符串中
					var id="${sessionScope.user.id}"
					$("#create-owner").val(id);

					//显示该模态窗口
					$("#createActivityModal").modal("show");
				}

			})

			$("#saveBtn").click(function () {
				$.ajax({
					url:"workbench/activity/save.do",
					data:{

						"owner":$.trim($("#create-owner").val()),
						"name":$.trim($("#create-name").val()),
						"startDate":$.trim($("#create-startDate").val()),
						"endDate":$.trim($("#create-endDate").val()),
						"cost":$.trim($("#create-cost").val()),
						"description":$.trim($("#create-description").val())

					},
					type:"post",
					dataType:"json",
					success:function (data) {

						/*
						{data.success:true/false}
						* */
						if(data.success){
							//添加成功后
							//利用局部刷新刷新状态
							//pageList(1,2);

							/*
							*
							* $("#activityPage").bs_pagination('getOption', 'currentPage'):
							* 		操作后停留在当前页
							*
							* $("#activityPage").bs_pagination('getOption', 'rowsPerPage')
							* 		操作后维持已经设置好的每页展现的记录数
							*
							* 这两个参数不需要我们进行任何的修改操作
							* 	直接使用即可
							*
							*
							*
							* */

							//做完添加操作后，应该回到第一页，维持每页展现的记录数

							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							//清空模态窗口中的数据
							/*

								拿到的form表单的Jquery对象
								提供了submit方法提交表单
								没有提供reset方法重置表单

								原生JS对象提供了reset方法(利用数组下标转换为DOM对象)

							 */
							$("#activityAddForm")[0].reset();

							//关闭操作的模态窗口
							$("#createActivityModal").modal("hide");

						}else{
							//添加市场活动失败
							alert("添加市场活动失败");
						}
					}

				})
			})

		})


		//为查询按钮绑定事件
		$("#search-button").click(function () {

			/*
				将搜索框中的内容保存到隐藏域中
			 */
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			pageList(1,2);
		})

		//全选按钮绑定事件
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked)

		})

		//其他按钮绑定事件
		//动态生成的元素不能利用普通绑定方式，应用on方式触发
		/*$("input[name=xz]").click失效*/
		/*
			正确方式：
			$("有效的对象外层元素").on("事件名称",绑定事件的元素,回调函数)
		 */
		$("#activityBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

		})

		//为删除按钮绑定事件
		$("#deleteBtn").click(function () {

			var $xz=$("input[name=xz]:checked");

			if($xz.length==0){

				alert("请选择需要删除的内容")

			}else{

				//提示用户是否删除
				if(confirm("确定删除吗?")){

					//url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx

					//拼接参数
					var param="";

					//将$xz中每一个dom对象的id值取出
					for(var i=0;i<$xz.length;i++){

						param+="id="+$($xz[i]).val();
						//如果不是最后一个记录，追加&
						if(i<$xz.length-1){

							param+="&";

						}
					}

					//alert(param)
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						type:"get",
						dataType:"json",
						success:function (data) {

							/*
                                data={"success":true/false}
                             */
							if(data.success){

								//pageList(1,2);
								//删除成功后
								pageList($(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage')));

							}else{

								alert("删除市场活动失败")

							}
						}

					})

				}


			}



		})


		//为修改按钮绑定事件
		$("#editBtn").click(function () {

			var $xz=$("input[name=xz]:checked");

			if($xz.length==0){

				alert("请选择需要修改的记录");

			}else if($xz.length>1){

				alert("只能选择一条记录进行修改");

			}else{

				var id=$xz.val();

				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{

						"id":id

					},
					type:"get",
					dataType:"json",
					success:function (data) {

						/*
							data
								用户列表
								市场活动对象

								{"uList":[{用户1},{2},{3}],"a":{市场活动}}
						 */

						//处理所有者的下拉框
						var html="<option></option>";

						$.each(data.uList,function (i,n) {

							html+="<option value='"+n.id+"'>"+n.name+"</option>"

						})


						$("#edit-owner").html(html);

						//处理单条Activity
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-description").val(data.a.description);
						$("#edit-cost").val(data.a.cost);
						$("#edit-owner").val(data.a.owner);

						//后台铺值结束后，打开修改的模态窗口
						$("#editActivityModal").modal("show");

					}

				})
			}
		})

		//为更新按钮绑定，执行市场活动更新操作
		$("#updateBtn").click(function () {

			$.ajax({
				url:"workbench/activity/update.do",
				data:{

					"id":$.trim($("#edit-id").val()),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"startDate":$.trim($("#edit-startDate").val()),
					"endDate":$.trim($("#edit-endDate").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-description").val())

				},
				type:"post",
				dataType:"json",
				success:function (data) {

					/*
                    {data.success:true/false}
                    * */
					if(data.success){
						//修改成功后
						//利用局部刷新刷新状态
						//pageList(1,2);

						/*

							修改操作后，应该维持在当前页，维持每页展现的记录数

						 */
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭修改操作的模态窗口
						$("#editActivityModal").modal("hide");

					}else{
						//修改市场活动失败
						alert("修改市场活动失败");
					}
				}

			})

		})


	});


	//pageNo:页码 pageSize:每页展现的记录数
	function pageList(pageNo,pageSize) {

		//将全选的复选框勾除
		$("#qx").prop("checked",false);

		//查询前，将隐藏域内容放入搜索框
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({

			url:"workbench/activity/pageList.do",
			data:{

				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val())

			},
			type:"get",
			dataType:"json",
			success:function (data) {

				//需要市场活动信息列表dataList
				//分页插件需要总查询信息条数total
				//{"total":100,"dataList":[{市场活动1},{2},{3}]}

				var html="";

				//每一个n就是每一个市场活动对象
				$.each(data.dataList,function (i,n) {

					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';

				})

				$("#activityBody").html(html);

				//计算总页数
				var totalPages=data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				//数据处理完毕后，结合分页查询，对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数时在，点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}

		})
	}
	
</script>
</head>
<body>

	<!--隐藏域-->
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<%--隐藏数据：修改条目id--%>
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name"">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>

							<div class="col-sm-10" style="width: 81%;">
								<%--

									标签对一定要以标签对的形式出现
									一定要紧挨着，空格也算是内容
									属于表单域范畴，需要使用val()方法，而不是html()方法

								--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
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
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="search-button" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
						通过点击添加按钮，观察两个属性值

						data-toggle="modal"
							表示点击此按钮，将会生成一个模态窗口

						data-target="#createActivityModal"
							表示要打开哪个模态窗口

						不要写死在属性栏中
					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">

					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage">

				</div>

			</div>
			
		</div>
		
	</div>
</body>
</html>