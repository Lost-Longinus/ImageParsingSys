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
      <b>Welcome to ImageParsing System</b><br>
      <b>=========================================</b><br>
      <input type="file" id="file" name="image" multiple="multiple"><br><br>
      <button id="submit" style="width:100px;height:30px;">解析图片</button><br><br>
      <span id="ParsedText"></span><br/>
    </div>
    <script type="text/javascript">
        function createAJAX(){
            var ajax = null;
            try{
                ajax = new ActiveXObject("microsoft.xmlhttp");
            }catch(e1){
                try{
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
            spanElement.innerHTML = "<div align=\"center\">将要为您解析以下图片：</div><br>" + path + "<br>";
        }
    </script>
    <script type="text/javascript">
        document.getElementById("submit").onclick = function(){
            var file = document.getElementById("file").files[0];
            var formData = new FormData();
            formData.append(file.name, file);
            var ajax = createAJAX();
            var method = "POST";
            var url = "${pageContext.request.contextPath}/bufferParse?time="+new Date().getTime();
            ajax.open(method,url);
            //NO3)
            ajax.send(formData);

            ajax.onreadystatechange = function(){
                if(ajax.readyState == 4){
                    if(ajax.status == 200){
                        var parsedResult = ajax.responseText;
                        var spanElement = document.getElementById("ParsedText");
                        spanElement.innerHTML = parsedResult;
                    }
                }
            }
        }
    </script>
  </body>
</html>
