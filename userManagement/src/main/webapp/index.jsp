<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="js/jquery.min.js">
    </script>
    <script type="text/javascript">
        function findAdmin() {
            alert("findAdmin")
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath }/admin/findAdmin",
                data: 'username=admin&password=123456&start=0&limit=2',
                success: function (json) {
                    var userList=json;
                    for(i=0;i<userList.length;i++){
                        alert(userList[i].id + "," + userList[i].username+","+userList[i].password);
                    }
                }
            });
        }
        function insertAdminWithBackId() {
            alert("insertAdminWithBackId")
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath }/admin/insert",
                contentType: 'application/json;charset=utf-8',
                data: '{"username":"admin","password":"123456"}',
                success: function (json) {
                    alert(json.id + "," + json.username+","+json.password);
                }
            });
        }

        //TODO（adminListCache 里面的缓存什么时候添加的数据）这个运行后在控制台一直在输出，没有停止
        function findAdminByUsername() {
            alert("findAdminByUserName");
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath }/admin/findAdminByUsername",
                data: 'username=admin',
                success: function (json) {
                    var userList=json;
                    for(i=0;i<userList.length;i++){
                        alert(userList[i].id + "," + userList[i].username+","+userList[i].password);
                    }
                }
            });
        }
        function findAdminList() {
            alert("findAdminList")
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath }/admin/findAdminList",
                data: 'username=admin&password=123456&start=0&limit=2',
                success: function (json) {
                    var userList=json;
                    for(i=0;i<userList.length;i++){
                        alert(userList[i].id + "," + userList[i].username+","+userList[i].password);
                    }
                }
            });
        }
        function findAdminById() {
            alert("findAdminById")
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath }/admin/findAdminById",
                data: 'id=1',
                success: function (json) {
                    alert(json.id + "," + json.username+","+json.password);
                }
            });
        }
        function findAdminByIdFromRedis() {
            alert("findAdminByIdFromRedis")
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath }/admin/findAdminByIdFromRedis",
                data: 'id=2',
                success: function (json) {
                    alert(json.id + "," + json.username+","+json.password);
                }
            });
        }
        function findAdminByIdFromRedisHash() {
            alert("findAdminByIdFromRedisHash")
            $.ajax({
                type: "post",
                url: "${pageContext.request.contextPath }/admin/findAdminByIdFromRedisHash",
                data: 'id=2',
                success: function (json) {
                    alert(json.id + "," + json.username+","+json.password);
                }
            });
        }
    </script>
</head>
<body>
<div>自动注册新用户后，没有做唯一判断，登录会报错</div>
<br>

<form method="post" action="admin/register">
    用户名:<input name="username" type="text"><br>
    密码:<input name="password" type="password"><br>
    <input type="submit" value="注册">
</form>
<form method="get" action="admin/login">
    用户名:<input name="username" type="text"><br>
    密码:<input name="password" type="password"><br>
    <input type="submit" value="登录">
</form>
<input type="button" onclick="findAdmin()" value="findAdmin(需要先登录)">
<input type="button" onclick="insertAdminWithBackId()" value="insertAdminWithBackId(插入一条记录并返回id)"><br><br>
<input type="button" onclick="findAdminByUsername()" value="findAdminByUsername(guava cache)">
<input type="button" onclick="findAdminList()" value="findAdminList(guava cache)">
<input type="button" onclick="findAdminById()" value="findAdminById(memcached cache)"><br><br>
<input type="button" onclick="findAdminByIdFromRedis()" value="findAdminByIdFromRedis">
<input type="button" onclick="findAdminByIdFromRedisHash()" value="findAdminByIdFromRedisHash"><br><br>
<form method="post" action="admin/rpcProduct">
    <input name="username" type="text" value="rpcName" hidden>
    <input name="password" type="password" value="rpcPassword" hidden>
    <input type="submit" value="注册(rpc)">
    <p>这个注册--日志写入不到数据库中</p>
</form>
</body>
</html>
