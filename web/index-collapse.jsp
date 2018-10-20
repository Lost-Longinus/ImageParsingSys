<%--
  Created by IntelliJ IDEA.
  User: 金鹏飞
  Date: 2018/10/20
  Time: 11:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>ImageParse</title>
  </head>
  <body>
    <div align="center">
      <br>
      <b>Welcome to ImageParsing System</b><br><br>
      <b>=========================================</b><br><br>
      <form id="form" align="center" action="${pageContext.request.contextPath}/uploadParse" method="post" enctype="multipart/form-data">
        <input type="file" id="file">&nbsp;&nbsp;<br><br>
        <input id="submit" type="submit" value="解析图片"><br>
      </form>
      <br><br>
      <span id="ParsedText"></span><br/>
    </div>
    <script type="text/javascript">
        //创建AJAX异步对象
        function createAJAX(){
            var ajax = null;
            try{
                //如果IE5=IE12的话
                ajax = new ActiveXObject("microsoft.xmlhttp");
            }catch(e1){
                try{
                    //如果是非IE的话
                    ajax = new XMLHttpRequest();
                }catch(e2){
                    alert("你的浏览器中不支持异步对象，请更换浏览器");
                }
            }
            return ajax;
        }
    </script>
    <script type="text/javascript">
        document.getElementById("file").onchange = function(){
            var path = document.getElementById("file").value
            var spanElement = document.getElementById("ParsedText");
            spanElement.innerHTML = "<div align=\"center\">以下为需要解析的图片：</div><br>" + path + "<br>";
        }
    </script>
    <script type="text/javascript">
        document.getElementById("form").onload = function(){
            //var path = document.getElementById("file").value
            //NO1)创建AJAX异步对象
            var ajax = createAJAX();
            //NO2)准备发送请求
            var method = "POST";
            var url = "${pageContext.request.contextPath}/uploadParse?time="+new Date().getTime();
            ajax.open(method,url);
            //设置AJAX请求头
            ajax.setRequestHeader("content-type","application/x-www-form-urlencoded");
            //NO3)
            var content = null;
            ajax.send(content);

            //---------------------------------等待
            //NO4)AJAX异步对象不断监听服务器响应的状态0-1-2-3-【4】
            //一定要状态变化后，才可触发function(){}函数
            //如果状态永远是4-4-4-4-4，是不会触发function(){}函数的
            ajax.onreadystatechange = function(){
                //如果状态码为4的话
                if(ajax.readyState == 4){
                    //如果响应码为200的话
                    if(ajax.status == 200){
                        //NO5)从AJAX异步对象中获取服务器响应的HTML数据
                        var parsedResult = ajax.responseText;

                        //NO6)将结果按照DOM规则，动态添加到web页面指定的标签中
                        var spanElement = document.getElementById("ParsedText");
                        spanElement.innerHTML = parsedResult;
                    }
                }
            }

        }
    </script>
  </body>
</html>
