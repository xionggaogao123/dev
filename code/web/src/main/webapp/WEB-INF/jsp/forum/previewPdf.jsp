<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>预览</title>

    <style type="text/css">

        .pdfobject {
            border: 1px solid #666;
        }
    </style>
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

    <!-- 可选的Bootstrap主题文件（一般不用引入） -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

    <script src="/static/dist/pdfobject/pdfobject.min.js"></script>

    <script language="javascript">


        function request(paras) {
            var url = location.href;
            var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
            var paraObj = {}
            for (i = 0; j = paraString[i]; i++) {
                paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
            }
            var returnValue = paraObj[paras.toLowerCase()];
            if (typeof(returnValue) == "undefined") {
                return "";
            } else {
                return returnValue;
            }
        }

        $(document).ready(function () {

            if (PDFObject.supportsPDFs) {

                var options = {
                    pdfOpenParams: {
                        pagemode: "thumbs",
                        navpanes: 1,
                        toolbar: 1,
                        statusbar: 1,
                        view: "FitV"
                    }
                };

                PDFObject.embed(request('pdf'), "#pdf", options);
            } else {
                console.log("Boo, inline PDFs are not supported by this browser");
            }
        });
    </script>
</head>
<body>
<div class="container">
    <div id="pdf"></div>
</div>
</body>
</html>
