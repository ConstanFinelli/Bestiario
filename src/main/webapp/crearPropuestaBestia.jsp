<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Proponer Bestia</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bestias.css">
<link rel="stylesheet" href="css/navbar.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>

<section class="mainContent">
    <h1>Proponer Nueva Bestia</h1>
    
    <form action="SvBestia?action=add" method="post" class="formBestia">
        <label for="nombre"><strong>Nombre:</strong></label><br>
        <input type="text" id="nombre" name="nombre" class="inputText" placeholder="Ej: Dragón Carmesí" required><br><br>
        
        <label for="peligrosidad"><strong>Peligrosidad:</strong></label><br>
        <select id="peligrosidad" name="peligrosidad" class="inputSelect" required>
            <option value="">-- Seleccionar nivel --</option>
            <option value="Baja">Baja</option>
            <option value="Media">Media</option>
            <option value="Alta">Alta</option>
        </select><br><br>
        
        <input type="hidden" name="estado" value="<%= (usuario != null && usuario.isEsInvestigador() == true) ? "aprobado" : "pendiente"%>">
	
		<a href="SvBestia?action=list" class="btnBestia">Cancelar</a>
        <button type="submit" class="btnBestia">Continuar →</button>
    </form>
</section>

<footer></footer>
</body>
</html>
