<%--
  Created by IntelliJ IDEA.
  User: MACHENIKE
  Date: 2020/8/15
  Time: 19:09
  To change this template use File | Settings | File Templates.
--%>
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
</head>
<body>

</body>
</html>
