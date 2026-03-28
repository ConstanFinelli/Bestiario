<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Categoria" %>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<%
	Bestia bestia = (Bestia) session.getAttribute("bestia");
	LinkedList<Habitat> habitats = (LinkedList<Habitat>) session.getAttribute("habitats");
	LinkedList<Categoria> categorias = (LinkedList<Categoria>) session.getAttribute("categorias");
%>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Editar bestia</title>
<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath())%>">
<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath())%>">
<link rel="stylesheet" href="<%= HttpRoutes.EDITARBESTIA_CSS(request.getContextPath())%>">
</head>
<body>
	<%@ include file="../../components/navbar.jsp" %>
	<section class="mainContent">
		<h1>Editando <%= bestia.getNombre() %></h1>
		<section class="forms">
		<aside class="infoBestia">
            	<img src="<%=bestia.getPictureUrl()%>" alt="Imagen de la bestia">
                <div>
	                <h3>Detalles de la bestia</h3>
	                <ul>
	                    <li>Nombre: <%= bestia.getNombre() %> </li>
	                    <li>Peligrosidad: <%= bestia.getPeligrosidad() %></li>
	                </ul>
                </div>
                <div>
                	<h3>Categorías</h3>
                	<ul>
                	<% if(bestia.getCategorias().isEmpty()){ %>
                    			<li>No tiene categorías definidas.</li>
                    			<%}else{ 
                    				for(Categoria cat:bestia.getCategorias()){
                    			%>
                    				<li>
                    					<span class="catBadge"><%= cat.getNombre() %></span>
                    				</li>
                    			<%} %>
                   			<%} %>
                	</ul>
                </div>
                <div>
	                <h3>Hábitats localizados</h3>
	                <ul>
	                	<% if(!bestia.getHabitats().isEmpty()){ 
	                	for(Habitat habitat:bestia.getHabitats()){
	                	%>
		                    <li><%= habitat.getNombre() %>, <%= habitat.getLocalizacion() %></li>
	                    <%} %>
	                    <% } else{%>
	                    	<li>No hay habitats registradas para esta bestia.</li>
	                    <%} %>
	                </ul>
                </div>
            </aside>
        <div>
        <div class="formButtons">
        	<button class="changeButton" onclick="changeFormTo('formInfo');" id="formInfoBtn">Información general</button>
        	<button class="changeButton" onclick="changeFormTo('formCategoria');" id="formCategoriaBtn">Categorías</button>
        	<button class="changeButton" onclick="changeFormTo('formHabitat');" id="formHabitatBtn">Habitats</button>
        </div>
        <form class="formEditar formInfo" action="<%= HttpRoutes.EDITAR_BESTIA(request.getContextPath()) %>?action=info" method="POST">
			<h1>Información general</h1>
			<input type="hidden" name="update" value="update">
			<input type="hidden" name="id" value="<%= bestia.getIdBestia() %>">
			<label for="nombre" class="inputLabel">Nombre</label>
			<input type="text" id="nombre" name="nombre" class="inputForm" value="<%= bestia.getNombre()%>" required/>
			<label for="peligrosidad" class="inputLabel">Peligrosidad</label>
			<select id="peligrosidad" name="peligrosidad" class="inputForm" required>
	            <option value="">-- Seleccionar nivel --</option>
	            <option value="Baja">Baja</option>
	            <option value="Media">Media</option>
	            <option value="Alta">Alta</option>
        	</select>
			<button type="submit" class="submitButton">Guardar cambios</button>
		</form>
		<form class="formEditar hiddenForm formCategoria" action="<%= HttpRoutes.EDITAR_BESTIA(request.getContextPath()) %>?action=category" method="POST">
			<h1>Asignar categoría a bestia</h1>
			<input type="hidden" name="relationChange" value="addCategory">
			<input type="hidden" name="id" value="<%= bestia.getIdBestia() %>">
			<label for="idCategoria" class="inputLabel">Categoría a agregar: </label>
			<select class="inputForm" name="idCategoria" id="idCategoria" required>
				<%
				if(categorias != null && !categorias.isEmpty()) {
				for(Categoria categoria:categorias){ %>
				<option value="<%= categoria.getIdCategoria() %>"><%= categoria.getNombre() %></option>
				<%}} %>
			</select>
			<button type="submit" class="submitButton">Agregar categoría</button>
		</form>
		<form class="formEditar hiddenForm formCategoria" action="<%= HttpRoutes.EDITAR_BESTIA(request.getContextPath()) %>?action=category" method="POST" >
			<h1>Eliminar categoría a bestia</h1>
			<input type="hidden" name="relationChange" value="removeCategory">
			<input type="hidden" name="id" value="<%= bestia.getIdBestia() %>">
			<label for="idCategoria" class="inputLabel">Categoría a eliminar: </label>
			<select class="inputForm" name="idCategoria" id="idCategoria" required>
				<%
				if(!bestia.getCategorias().isEmpty()) {
					for(Categoria categoria: bestia.getCategorias()){ %>
						<option value="<%= categoria.getIdCategoria() %>"><%= categoria.getNombre() %></option>
					<%} %>
				<%} %>
			</select>
			<button type="submit" class="submitButton">Eliminar categoría</button>
		</form>
		<form class="formEditar hiddenForm formHabitat" action="<%= HttpRoutes.EDITAR_BESTIA(request.getContextPath()) %>?action=habitat" method="POST">
			<h1>Asignar habitat a bestia</h1>
			<input type="hidden" name="relationChange" value="addHabitat">
			<input type="hidden" name="id" value="<%= bestia.getIdBestia() %>">
			<label for="idHabitat" class="inputLabel">Habitat a agregar: </label>
			<select class="inputForm" name="idHabitat" id="idHabitat" required>
				<%
				if(habitats != null && !habitats.isEmpty()) {
				for(Habitat habitat:habitats){ %>
				<option value="<%= habitat.getId() %>"><%= habitat.getNombre() %></option>
				<%}} %>
			</select>
			<button type="submit" class="submitButton">Agregar habitat</button>
		</form>
		<form class="formEditar hiddenForm formHabitat" action="<%= HttpRoutes.EDITAR_BESTIA(request.getContextPath()) %>?action=habitat" method="POST">
			<h1>Eliminar habitat a bestia</h1>
			<input type="hidden" name="relationChange" value="removeHabitat">
			<input type="hidden" name="id" value="<%= bestia.getIdBestia() %>">
			<label for="idHabitat" class="inputLabel">Habitat a eliminar: </label>
			<select class="inputForm" name="idHabitat" id="idHabitat" required>
				<%
				if(!bestia.getHabitats().isEmpty()) {
				for(Habitat habitat: bestia.getHabitats()){ %>
				<option value="<%= habitat.getId() %>"><%= habitat.getNombre() %></option>
				<%}} %>
			</select>
			<button type="submit" class="submitButton">Eliminar habitat</button>
		</form>
		</div>
	</section>
	</section>
	<footer>
	</footer>
	<script>
		function changeFormTo(to){
			const forms = ['formInfo', 'formCategoria', 'formHabitat'];
			for(const form of forms){
				if(form === to){
					const hiddenForms = document.getElementsByClassName(to);
					Array.from(hiddenForms).forEach((hiddenForm) =>{
						hiddenForm.classList.remove('hiddenForm')
					})
					document.getElementById(form + 'Btn').classList.add('activeButton');
				}else{
					const shownForms = document.getElementsByClassName(form);
					Array.from(shownForms).forEach((shownForm) =>{
						shownForm.classList.add('hiddenForm')
					})
					document.getElementById(form + 'Btn').classList.remove('activeButton');
				}
			}
		}
	</script>
</body>
</html>