<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<meta charset="UTF-8">
<title>Habitats forms</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/navbar.css">
<style>
.pageContainer{
	display: flex;
	gap: 1.5vh;
	flex-direction: column;
}

.container-md{
	padding: 15px;
}

</style>
</head>
<body>
<%@ include file="components/adminNavbar.jsp" %>	
<div class="mainContent" style="height:fit-content">
	<div class="container-md text-center border border-secondary">
		<h1>Conseguir habitats</h1>
		<form action="SvHabitat" method="GET">
		<input type="submit" value="Conseguir habitats">	
		</form>
		${findAllMsg}
	</div>
	
	<div class="container-md text-center border border-secondary">
		<h1>Conseguir habitat por ID</h1>
		<form action="SvHabitat" method="GET">
		<label for="idGetOne">ID del habitat: </label>
		<input type="number" name="id" id="idGetOne" required>
		<input type="submit" value="Conseguir habitat">	
		</form>
		${getOneMsg}
		</div>
		<div class="container-md text-center border border-secondary">
		<h1>Crear habitat</h1>
		<form action="SvHabitat" method="POST">
		<input type="hidden" name="flag" value="post">
		<label for="nombreSave" class="form-label">Nombre de hábitat</label>
		<input type="text" name="nombre" id="nombreSave" class="form-control" required>
		<label for="localizacionSave" class="form-label">Localizacion de hábitat</label>
		<input type="text" name="localizacion" id="localizacionSave" class="form-control" required>
		<button type="submit" class="btn btn-outline-primary">Crear habitat</button>
		</form>
		${saveMsg}
	</div>
	
	<div class="container-md text-center border border-secondary">
		<h1>Actualizar habitat por ID</h1>
		<form action="SvHabitat" method="POST">
		<input type="hidden" name="flag" value="put">
		<label for="idUpdate">ID del habitat: </label>
		<input type="number" name="id" id="idUpdate" required>
		<label for="nombreUpdate">Nombre de hábitat</label>
		<input type="text" name="nombre" id="nombre" required>
		<label for="localizacionUpdate">Localizacion de hábitat</label>
		<input type="text" name="localizacion" id="localizacionUpdate" required>
		<input type="submit" value="Actualizar habitat">
		</form>
		${updateMsg}
	</div>
	
	<div class="container-md text-center border border-secondary">
		<h1>Eliminar habitat por ID</h1>
		<form action="SvHabitat" method="POST">
		<input type="hidden" name="flag" value="delete">
		<label for="idDelete">ID del habitat: </label>
		<input type="number" name="id" id="idDelete" required>
		<input type="submit" value="Eliminar habitat">	
		</form>
		${deleteMsg}
	</div>
</div>
	<footer>
	</footer>
</body>
</html>