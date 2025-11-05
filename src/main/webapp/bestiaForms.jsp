<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Categoria" %>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<meta charset="UTF-8">
<title>BESTIA CRUD</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/navbar.css">
</head>
<body>
	<%@ include file="components/adminNavbar.jsp" %>
	<%
		LinkedList<Bestia> bestias = (LinkedList<Bestia>) request.getAttribute("bestias");
		LinkedList<Habitat> habitats = (LinkedList<Habitat>) request.getAttribute("habitats");
		LinkedList<Categoria> categorias = (LinkedList<Categoria>) request.getAttribute("categorias");
	%>
	<section class="mainContent" style="height:fit-content">
	<div class="container-md text-center my-4 border p-4">
		<h1>Conseguir bestia por ID</h1>
		<form action="SvBestia?action=form" method="GET">
			<label class="form-label" for="idGetOne">Nombre de la bestia: </label>
			<select class="form-control" name="id" id="idGetOne" required>
				<%
				if(bestias != null && !bestias.isEmpty()) {
				for(Bestia bestia:bestias){ %>
				<option value="<%= bestia.getIdBestia() %>"><%= bestia.getNombre() %></option>
				<%}} %>
			</select>
			<button type="submit" class="btn btn-outline-primary">Conseguir bestia</button>	
		</form>
		${getOneMsg}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Crear bestias</h1>
		<form action="SvBestia?action=form" method="POST">
			<input type="hidden" name="flag" value="post">
			<label for="nombre" class="form-label">Nombre: </label>
			<input type="text" name="nombre" id="nombre" class="form-control" required>
			<label for="peligrosidad" class="form-label">Peligrosidad: </label>
			<input type="text" name="peligrosidad" id="peligrosidad" class="form-control" required>
			<button type="submit" class="btn btn-outline-primary">Crear bestia</button>
		</form>
		${saveMsg}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Actualizar bestia por ID</h1>
		<form action="SvBestia?action=form" method="POST">
			<input type="hidden" name="flag" value="put">
			<label class="form-label" for="idUpdate">Nombre de la bestia: </label>
			<select class="form-control" name="id" id="idUpdate" required>
				<%
				if(bestias != null && !bestias.isEmpty()) {
				for(Bestia bestia:bestias){ %>
				<option value="<%= bestia.getIdBestia() %>"><%= bestia.getNombre() %></option>
				<%}} %>
			</select>
			<label for="nombre" class="form-label">Nombre: </label>
			<input type="text" name="nombre" id="nombre" class="form-control">
			<label for="peligrosidad" class="form-label">Peligrosidad: </label>
			<input type="text" name="peligrosidad" id="peligrosidad" class="form-control">
			<button type="submit" class="btn btn-outline-primary">Actualizar bestia</button>
		</form>
		${updateMsg}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
	<h1>Eliminar bestia por ID</h1>
	<form action="SvBestia?action=form" method="POST">
		<input type="hidden" name="flag" value="delete">
		<label for="idDelete">Nombre de la bestia: </label>
		<select class="form-control" name="id" id="idDelete" required>
				<%
				if(bestias != null && !bestias.isEmpty()) {
				for(Bestia bestia:bestias){ %>
				<option value="<%= bestia.getIdBestia() %>"><%= bestia.getNombre() %></option>
				<%}} %>
			</select>
		<button type="submit" class="btn btn-outline-primary">Eliminar bestia</button>
	</form>
	${deleteMsg}
	</div>
	<div class="container-md text-center my-4 border p-4">
		<h1>Agregar categoría a bestia</h1>
		<form action="SvBestia?action=form" method="POST">
			<input type="hidden" name="flag" value="put">
			<label class="form-label" for="idUpdate">Nombre de la bestia: </label>
			<select class="form-control" name="id" id="idUpdate" required>
				<%
				if(bestias != null && !bestias.isEmpty()) {
				for(Bestia bestia:bestias){ %>
				<option value="<%= bestia.getIdBestia() %>"><%= bestia.getNombre() %></option>
				<%}} %>
			</select>
			<label for="nombre" class="form-label">Categoría a agregar: </label>
			<select class="form-control" name="idCategoria" id="idCategoria" required>
				<%
				if(categorias != null && !categorias.isEmpty()) {
				for(Categoria categoria:categorias){ %>
				<option value="<%= categoria.getIdCategoria() %>"><%= categoria.getNombre() %></option>
				<%}} %>
			</select>
			<button type="submit" class="btn btn-outline-primary">Actualizar bestia</button>
		</form>
	</div>
	<div class="container-md text-center my-4 border p-4">
		<h1>Agregar habitat a bestia</h1>
		<form action="SvBestia?action=form" method="POST">
			<input type="hidden" name="flag" value="put">
			<label class="form-label" for="idUpdateHt">Nombre de la bestia: </label>
			<select class="form-control" name="id" id="idUpdateHt" required>
				<%
				if(bestias != null && !bestias.isEmpty()) {
				for(Bestia bestia:bestias){ %>
				<option value="<%= bestia.getIdBestia() %>"><%= bestia.getNombre() %></option>
				<%}} %>
			</select>
			<label for="nombre" class="form-label">Habitat a agregar: </label>
			<select class="form-control" name="idHabitat" id="idHabitat" required>
				<%
				if(habitats != null && !habitats.isEmpty()) {
				for(Habitat habitat:habitats){ %>
				<option value="<%= habitat.getId() %>"><%= habitat.getNombre() %></option>
				<%}} %>
			</select>
			<button type="submit" class="btn btn-outline-primary">Actualizar bestia</button>
		</form>
	</div>
	</section>
	<footer>
	</footer>
</body>
</html>