<%
    session.invalidate(); // Cierra la sesión
    response.sendRedirect("home.jsp"); // redirige al inicio
%>