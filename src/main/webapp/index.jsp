<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.io.StringReader"%>
<%@page import="java.io.StringWriter"%>

<%

Exception exception = (Exception) request.getAttribute("exception");
String exceptionMessage = exception.getClass().getSimpleName() + ": "+exception.getMessage();
%>

<html lang="en">
<head>
<meta charset="utf-8">
<title>SAML IdP</title>

<link rel="stylesheet" href="css/style.css">
<!-- Optimize for mobile devices -->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
</head>
<body>
	lkjkjkl
	<div id="content">
		<form action="#" method="get" name='form1' id='login-form'>
            <br /> <img src="images/i-sprint.png">
			<div class="error-box round">
                <%=exceptionMessage%>
            </div>
		</form>
	</div>
</body>
<script type="javascript/text">
	(()=>{
		window.location.href = "http://www.baidu.com?<%=exceptionMessage%>"
	})()
</script>
</html>