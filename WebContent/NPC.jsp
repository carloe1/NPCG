<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title><%request.getParameter("NPC_NAME");%></title>
	</head>
	<body>	
		<embed src=<%request.getParameter("NPC"); %> type="application/pdf" width="100%" height="600px" />
	</body>
</html>