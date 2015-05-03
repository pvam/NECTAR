<%@page import="algorithms.AdditionalInformation"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="org.apache.tomcat.util.http.fileupload.FileItem"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="com.oreilly.servlet.*,nectar.*,java.io.*,java.util.*,hello.*"%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="http://getbootstrap.com/favicon.ico">

<title>N E C T A R output</title>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

<!-- Custom styles for this template -->
<link href="http://getbootstrap.com/examples/cover/cover.css"
	rel="stylesheet">
<link href="round.css" rel="stylesheet">

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js">
	
</script>
<style>
select {
	color: black
}
</style>
</head>
<body>
	<div class="container-fluid">

		<div class="row">
			<h3>N E C T A R</h3>
		</div>

		<div class="row" style="-webkit-text-shadow: none; text-shadow: none;">
			<%
				//Class.forName("Loader");
				String filepath = "c:/uploadedData/";
				MultipartRequest m = new MultipartRequest(request, filepath,
						1024 * 1024, new DefaultFileRenamePolicy());

				//out.println(m.getOriginalFileName("fileid")+ " ,successfully uploaded"); 

				String typeOfGame = m.getParameter("typeOfGame");

				//out.println(typeOfGame);

				boolean isExtensive = false;

				String additionalInformation = null;

				if (typeOfGame != null) {
					isExtensive = typeOfGame.equals("Extensive");
					additionalInformation = typeOfGame.equals("shapley") ? "shapley"
							: "null";
				}

				String algorithmName = m.getParameter("first");
				// out.println("Algorithm received is "+ algorithmName);

				if (algorithmName.equals("Simple Search Method")
						|| (algorithmName.equals("Mixed Integer Programming"))) {
					additionalInformation = m.getParameter("second");
				}

				Helpers helper = new Helpers(algorithmName, new File(filepath + "/"
						+ m.getOriginalFileName("fileid")), isExtensive,
						additionalInformation);
				out.println(" <div class=\"col-sm-8 col1\" style=\"height: 0.7\">");
				out.println("<center><h3> <label> <span class=\"label label-info\">Game</span> </label> </h3> <pre> ");
				out.println(helper.showGame());
				out.println("</pre></center></div>");

				//out.println(" <div class=\"col-sm-6 col2\">");
				out.println(" <div class=\"col-sm-4 col2\">");
				out.println("<center><h3> <label> <span class=\"label label-info\">Output</span> </label> </h3> <pre> ");
				out.println(helper.computeAlgorithm());
				out.println("</pre></center></div>");
			%>
		</div>

	</div>


</body>
</html>