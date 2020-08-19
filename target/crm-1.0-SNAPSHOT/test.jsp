<%--
  Created by IntelliJ IDEA.
  User: MACHENIKE
  Date: 2020/8/15
  Time: 19:09
  To change this template use File | Settings | File Templates.
--%>
<%--时间控件--%>
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

$(".time").datetimepicker({
minView: "month",
language:  'zh-CN',
format: 'yyyy-mm-dd',
autoclose: true,
todayBtn: true,
pickerPosition: "bottom-left"
});

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    //ajax模板
    $.ajax({
    url:"",
    data:{

    },
    type:"",
    dataType:"json",
    success:function (data) {

    }

    })

    //当前系统时间
    String createTime = DateTimeUtil.getSysTime();
    String createBy = ((User)request.getSession().getAttribute("user")).getName();

    ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
</head>
<body>

</body>
</html>
