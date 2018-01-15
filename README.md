# ssm_demo
企业项目开发
参考地址：  https://github.com/justxzm/ssm

父模块的pom.xml文件的<packaging>标签内容为pom；而需要部署的子项目为war；只是作为其他项目的工具的子项目为jar
使用<modules>标签管理所有的子模块，以后再有新的子模块，只需要在<modules>添加新的<module>子标签即可

建议：
"子模块的groupId"设为"父模块的groupId.父模块的artifactId"
"子模块的artifactId"设为"父模块的artifactId-子模块的的名称"，"父模块的artifactId-子模块的的名称"也就是子模块的项目名
无论父模块还是子模块，建议同一个pom.xml文件中的artifactId与name标签内容相同


运行ssmm-userManagement
浏览器执行"http://localhost:8080/admin/register?username=canglang25&password=1457890"
注意：这里使用了8080端口

运行ssmm0-rpcWeb
浏览器执行"http://localhost:8081/mq/receive"
注意：
这里使用了8081端口
执行该URL后，浏览器会一直在转圈（即一直在等待接收消息），直到关闭jetty服务器
说明：jetty在不同的端口下可以同时启动，在同一端口下后边启动的服务会覆盖之前启动的服务
