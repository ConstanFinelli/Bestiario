<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Categoria" %>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<%
	Bestia bestia = (Bestia) request.getAttribute("bestia");
	LinkedList<Habitat> habitats = (LinkedList<Habitat>) request.getAttribute("foundHabitats");
	LinkedList<Categoria> categorias = (LinkedList<Categoria>) request.getAttribute("foundCategorias");
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
                    					<span class="catBadge"><%= cat.getNombre() %>
                    					<button onClick="abrirModal('<%=cat.getIdCategoria()%>', '<%=cat.getNombre()%>', 'categoria')"></button></span>
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
		                    <li><%= habitat.getNombre() %>, <%= habitat.getLocalizacion() %>
		                    	<button onClick="abrirModal('<%=habitat.getId()%>', '<%=habitat.getNombre()%>', 'habitat')"></button>
		                    </li>
	                    <%} %>
	                    <% } else{%>
	                    	<li>No hay habitats registradas para esta bestia.</li>
	                    <%} %>
	                </ul>
                </div>
            </aside>
        <div>
        <div class="formButtons">
        	<button class="changeButton" onclick="changeFormTo('formInfo');">Información general</button>
        	<button class="changeButton" onclick="changeFormTo('formCategoria');">Categorías</button>
        	<button class="changeButton" onclick="changeFormTo('formHabitat');">Habitats</button>
        </div>
        <form class="formEditar" action="" method="POST" id="formInfo">
			<h1>Información general</h1>
			<input type="hidden" name="update" value="update">
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
		<form class="formEditar hiddenForm" action="" method="POST" id="formCategoria">
			<h1>Asignar categoría a bestia</h1>
			<input type="hidden" name="addCategory" value="addCategory">
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
		<form class="formEditar hiddenForm" action="" method="POST" id="formHabitat">
			<h1>Asignar habitat a bestia</h1>
			<input type="hidden" name="addHabitat" value="addHabitat">
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
		</div>
	</section>
	</section>
	<footer>
	</footer>
	<div id="modal" class="modal-container">
		<div class="modal-content">
			<h2 id="modal-titulo"></h2>
			<div id="modal-cuerpo"></div>
			<div class="modalButtons">
				<form action="<%= HttpRoutes.ELIMINAR_CATEGORIA(request.getContextPath()) %>" method="post" style="display:inline;">
					<input type="hidden" name="id" id="idAEliminar">
					<input type="hidden" name="tipo" id="tipo">
					<button type="button" class="closeButton" onclick="cerrarModal()">Volver</button>
					<button type="submit" class="deleteButton" onclick="cerrarModal()">Eliminar</button>
				</form>
			</div>
		</div>
	</div>
	<script>
		// javascript para modal
		function abrirModal(id, nombre, tipo) {
			document.getElementById('modal-titulo').innerText = "ELIMINAR " + (tipo === 'categoria' ?  "CATEGORÍA" : "HÁBITAT");
			document.getElementById('modal-cuerpo').innerHTML = '¿Estas seguro que deseas eliminar ' + nombre + '?';
			document.getElementById('idAEliminar').value = id;
			document.getElementById('tipo').value = tipo;
			document.getElementById('modal').classList.add('is-visible');
		}

		function cerrarModal() {
			document.getElementById('modal').classList.remove('is-visible');
		}

		window.onclick = function(event) {
			let modal = document.getElementById('modal');
			if (event.target == modal) {
				cerrarModal();
			}
		}
		
		function changeFormTo(to){
			const forms = ['formInfo', 'formCategoria', 'formHabitat'];
			for(const form of forms){
				if(form === to){
					document.getElementById(to).classList.remove('hiddenForm');
				}else{
					document.getElementById(form).classList.add('hiddenForm');
				}
			}
		}
	</script>
</body>
</html>