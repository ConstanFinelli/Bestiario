<%
    session.invalidate(); // Cierra la sesi�n
    response.sendRedirect("home.jsp"); // redirige al inicio
%>