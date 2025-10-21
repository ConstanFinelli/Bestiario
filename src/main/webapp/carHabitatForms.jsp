<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<meta charset="UTF-8">
<title>Caracteristicas de habitat forms</title>
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
<div class="pageContainer">
<div class="container-md text-center border border-secondary">
<h1>Conseguir caracteristicas de un habitat</h1>
<form action="SvCaracteristicaHabitat" method="GET">
<label for="idFind" class="form-label">ID del hábitat asociada</label>
<input type="number" name="id" id="idFind" class="form-control" required>
<input type="submit" value="Conseguir caracteristicas">	
</form>
${findAllMsg}
</div>

<div class="container-md text-center border border-secondary">
<h1>Crear característica</h1>
<form action="SvCaracteristicaHabitat" method="POST">
<input type="hidden" name="flag" value="post">
<label for="idSave" class="form-label">ID del hábitat a asociar</label>
<input type="number" name="id" id="idSave" class="form-control" required>
<label for="descripcionSave" class="form-label">Característica</label>
<input type="text" name="descripcion" id="descripcionSave" class="form-control" required>
<button type="submit" class="btn btn-outline-primary">Crear característica</button>
</form>
${saveMsg}
</div>

<div class="container-md text-center border border-secondary">
<h1>Actualizar caracteristica por ID de hábitat</h1>
<form action="SvCaracteristicaHabitat" method="POST">
<input type="hidden" name="flag" value="put">
<label for="idUpdate">ID del hábitat asociado: </label>
<input type="number" name="id" id="idUpdate" required>
<label for="descripcionUpdate">Característica a modificar</label>
<input type="text" name="descripcion" id="descripcionUpdate" required>
<label for="newDescripcionUpdate">Característica nueva</label>
<input type="text" name="newDescripcion" id="newDescripcionUpdate" required>
<input type="submit" value="Actualizar característica">
</form>
${updateMsg}
</div>

<div class="container-md text-center border border-secondary">
<h1>Eliminar caracteristica por ID de hábitat</h1>
<form action="SvCaracteristicaHabitat" method="POST">
<input type="hidden" name="flag" value="delete">
<label for="idDelete">ID del habitat asociado: </label>
<input type="number" name="id" id="idDelete" required>
<label for="descripcionDelete">Característica</label>
<input type="text" name="descripcion" id="descripcionDelete" required>
<input type="submit" value="Eliminar característica">	
</form>
${deleteMsg}
</div>
</div>

</body>
</html>