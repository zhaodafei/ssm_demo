<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
rpc web <br>
http://localhost:8080/mq/receive <br>
<a href="http://localhost:8080/mq/receive">http://localhost:8080/mq/receive</a><br>
<ul>
    <li>注意</li>
    <li>这里使用了8080端口</li>
    <li>执行该URL后，浏览器会一直在转圈（即一直在等待接收消息），直到关闭jetty服务器</li>
    <li>说明：jetty在不同的端口下可以同时启动，在同一端口下后边启动的服务会覆盖之前启动的服务</li>
</ul>
</body>
</html>