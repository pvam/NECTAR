<%
System.out.println(request.toString());
String algorithmName = (String) request.getParameter("first");
out.println("Algorithm received is "+ algorithmName);
%>