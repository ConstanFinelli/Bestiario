<%@ page import="helpers.HttpRoutes" %>
<%
    session.invalidate(); // Cierra la sesión
    response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath())); // redirige al inicio
%>