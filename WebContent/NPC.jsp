<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<link rel="stylesheet" href="/NPCG/css/index.css" type="text/css">
<html>
	<%
		String name = (String) request.getAttribute("NPC_NAME");
		String path = (String) request.getAttribute("NPC");
	%>
	<head>
		<meta charset="ISO-8859-1">
		<title>NPCG - <%out.println(name);%></title>
	</head>
	<body>
		<div class="page">
		<div class="container">
			<div class="header">
				<h1><a href="index.html">NPC Generator</a></h1>
			</div>
			<p>
				<a>Download: </a>
				<a href="Download?path=<%out.println(path);%>&name=<%out.println(name);%>"><%out.println(name); %></a>
			</p>
			<object data="<%out.println(path);%>">
				<embed src="<%out.println(path);%>" type="application/pdf" width="100%" height="600px"/>
			</object>
		</div> <!-- class="container"-->
		</div> <!-- class="page" -->
	</body>
</html>