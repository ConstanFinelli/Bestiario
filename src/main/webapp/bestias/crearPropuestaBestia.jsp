<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Proponer Bestia</title>
<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath())%>">
<link rel="stylesheet" href="<%= HttpRoutes.BESTIAS_CSS(request.getContextPath())%>">
<link rel="stylesheet" href="<%= HttpRoutes.NUEVABESTIA_CSS(request.getContextPath())%>">
<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath())%>">
</head>
<body>
<%@ include file="../components/navbar.jsp" %>

<section class="mainContent">
    <h1>Proponer Nueva Bestia</h1>
    
    <form action="<%=HttpRoutes.CREAR_BESTIA(request.getContextPath()) %>" method="post" class="formBestia">
        <label for="nombre" class="inputLabel"><strong>Nombre:</strong></label><br>
        <input type="text" id="nombre" name="nombre" class="inputText" placeholder="Ej: Dragón Carmesí" required><br><br>
        
        <label for="peligrosidad" class="inputLabel"><strong>Peligrosidad:</strong></label><br>
        <select id="peligrosidad" name="peligrosidad" class="inputSelect" required>
            <option value="">-- Seleccionar nivel --</option>
            <option value="Baja">Baja</option>
            <option value="Media">Media</option>
            <option value="Alta">Alta</option>
        </select><br><br>
        
        <input type="hidden" name="estado" value="<%= (usuario != null && usuario.getEstado().equals("investigador")) ? "aprobado" : "pendiente"%>">
	
		<a href=<%=HttpRoutes.LISTAR_BESTIAS(request.getContextPath())%> class="btnBestia">Cancelar</a>
        <button type="submit" class="btnBestia">Continuar →</button>
    </form>
</section>
<%@ include file="../components/footer.jsp" %>
</body>
</html>
