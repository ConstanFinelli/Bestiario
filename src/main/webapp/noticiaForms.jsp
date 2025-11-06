<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<meta charset="UTF-8">
<title>Noticia Forms</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/navbar.css">
</head>
<body>
<%@ include file="components/adminNavbar.jsp" %>	
<div class="mainContent" style="height:fit-content">
	<div class="container-md text-center my-4 border p-4">
		<h2 class="h1">Conseguir noticias</h2>
		<form action="SvNoticia" method="GET">
			<button type="submit" class="btn btn-outline-primary">CONSEGUIR NOTICIAS</button>
		</form>
		${findAllMsg}
	</div>

	<div class="container-md text-center my-4 border p-4">
		<h1>Conseguir noticia por ID</h1>
		<form action="SvNoticia" method="GET">
			<label class="form-label" for="idGetOne">ID de la noticia: </label>
			<input class="form-control" type="number" name="id" id="idGetOne" required>
			<button type="submit" class="btn btn-outline-primary"]>Conseguir noticia</button>	
		</form>
		${getOneMsg}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Crear noticias</h1>
		<form action="SvNoticia" method="POST">
			<input type="hidden" name="flag" value="post">
			<label for="tituloSave" class="form-label">Titulo: </label>
			<input type="text" name="titulo" id="tituloSave" class="form-control" required>
			<label for="contenidoSave" class="form-label">Contenido: </label>
			<input type="text" name="contenido" id="contenidoSave" class="form-control" required>
			<label for="estadoSave" class="form-label">Estado: </label>
			<input type="text" name="estado" id="idUsuarioSave" class="form-control" required>
			<label for="idUsuarioSave" class="form-label">Id usuario: </label>
			<input type="text" name="idUsuario" id="idUsuarioSave" class="form-control" required>
			<button type="submit" class="btn btn-outline-primary">Crear noticia</button>
		</form>
		${saveMsg}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Actualizar noticia por ID</h1>
		<form action="SvNoticia" method="POST">
			<input type="hidden" name="flag" value="put">
			<label for="idUpdate" class="form-label">ID de la noticia: </label>
			<input type="number" name="id" id="idUpdate" class="form-control" required>
			<label for="tituloSave" class="form-label">Titulo: </label>
			<input type="text" name="titulo" id="tituloSave" class="form-control">
			<label for="contenidoSave" class="form-label">Contenido: </label>
			<input type="text" name="contenido" id="contenidoSave" class="form-control">
			<label for="estadoSave" class="form-label">Estado: </label>
			<input type="text" name="estado" id="idUsuarioSave" class="form-control">
			<label for="idUsuarioSave" class="form-label">Id usuario: </label>
			<input type="text" name="idUsuario" id="idUsuarioSave" class="form-control">
			<button type="submit" class="btn btn-outline-primary">Actualizar noticia</button>
		</form>
		${updateMsg}
	</div>
	
	<div class="container-md text-center my-4 border p-4">
		<h1>Eliminar habitat por ID</h1>
		<form action="SvNoticia" method="POST">
			<input type="hidden" name="flag" value="delete">
			<label for="idDelete">ID de la noticia: </label>
			<input type="number" name="id" id="idDelete" required>
			<button type="submit" class="btn btn-outline-primary">Eliminar noticia</button>
		</form>
		${deleteMsg}
	</div>
</div>
	<footer>
	</footer>
</body>
</html>