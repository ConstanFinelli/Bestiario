<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Categoria" %>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<meta charset="UTF-8">
<title>Categoria forms</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/navbar.css">
</head>
<body>
<%@ include file="components/adminNavbar.jsp" %>
<%
		LinkedList<Categoria> categorias = (LinkedList<Categoria>) request.getAttribute("categorias");
	%>	
<div class="mainContent" style="height:fit-content">
	<div class="container-md text-center my-4 border p-4">
		<h1>Conseguir Categorias</h1>
		<form action="SvCategoria" method="GET">
		<input type="submit" value="Conseguir categorias">
		</form>
		${msjAll}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Conseguir categoria por nombre</h1>
		<form action = "SvCategoria" method = "GET">
		<label for="idGetOne">Nombre de la categoria: </label>
		<select class="form-control" name="id" id="idGetOne" required>
						<%
						if(categorias != null && !categorias.isEmpty()) {
						for(Categoria categoria:categorias){ %>
						<option value="<%= categoria.getIdCategoria() %>"><%= categoria.getNombre() %></option>
						<%}} %>
		</select>
		<input type="submit" value="conseguir categoria"> 
		</form>
		${msjOne}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Crear Categoria</h1>
		<form action="SvCategoria" method="POST">
		<label for=nombreSave class="form-label">Nombre de la categoria</label>
		<input type="text" name="nombre" id=nombreSave>
		<br><br>
		<label for="descripcionSave" class="form-label">Desripcion de la categoria</label>
		<input type="text" name="descripcion" id="descripcionSave" class="form-control" required>
		<input type="hidden" name="flag" value="create" required>
		<button type="submit" class="btn btn-outline-primary">Crear categoria</button>
		</form>
		${msjSave}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Actualizar categoria por nombre</h1>
		<form action="SvCategoria" method="POST">
		<label for="idUpdate">ID de la categoria</label>
		<select class="form-control" name="id" id="idUpdate" required>
						<%
						if(categorias != null && !categorias.isEmpty()) {
						for(Categoria categoria:categorias){ %>
						<option value="<%= categoria.getIdCategoria() %>"><%= categoria.getNombre() %></option>
						<%}} %>
		</select>
		<input type="hidden" name="flag" value="update"> 
		<br><br>
		<label for="nombreUpdate" class="form-label">Nuevo nombre</label>
		<input type="text" name="nombre" id="nombreUpdate">
		<br><br>
		<label for="descripcionUpdate">Nueva descripcion</label>
		<input type="text" name="descripcion" id="descripcionUpdate" required> 
		<br><br>
		<button type="submit" class="btn btn-outline-primary">Modificar Categoria</button>
		</form>
		${msjUpdate}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Eliminar categoria</h1>
		<form action="SvCategoria" method="POST">
		<label for="idDelete">Nombre de la categoria</label>
		<select class="form-control" name="id" id="idDelete" required>
						<%
						if(categorias != null && !categorias.isEmpty()) {
						for(Categoria categoria:categorias){ %>
						<option value="<%= categoria.getIdCategoria() %>"><%= categoria.getNombre() %></option>
						<%}} %>
		</select>
		<input type="hidden" name="flag" value="delete">
		<button type="submit" class="btn btn-outline-primary">Modificar Categoria</button>
		</form>
		${msjDelete}
	</div>
</div>
	<footer>
	</footer>
</body>
</html>