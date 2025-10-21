<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
<meta charset="UTF-8">
<title>Evidencia forms</title>
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
<h1>Conseguir evidencias</h1>
<form action="SvEvidencia" method="GET">
<input type="hidden" name="flag" value="findAll">
<input type="submit" value="Conseguir evidencias">	
</form>
${msjAll}
</div>

<div class="container-md text-center border border-secondary">
<h1>Conseguir Evidencia por ID de Tipo y Nro</h1>
<form action="SvEvidencia" method="GET">
<input type="hidden" name="flag" value="getOne">
<label for="idGetOne">Id del tipo de evidencia: </label>
<input type="number" name="IdTipo" id="idGetOne" required>
<label for="nroEvGetone">Nro de evidencia: </label>
<input type="number" name="nroEvidencia" id="nroEvGetone" required>
<input type="submit" value="Conseguir evidencia">	
</form>
${msjOne}
</div>

<div class="container-md text-center border border-secondary">
<h1>Conseguir todas las Evidencias de un Tipo de Evidencia</h1>
<form action="SvEvidencia" method="GET">
<input type="hidden" name="flag" value="findAllType">
<label for="idFindAllType">ID del tipo de evidencia: </label>
<input type="number" name="IdTipo" id="idFindAllType" required>
<input type="submit" value="Conseguir evidencias">	
</form>
${msjFindAllType}
</div>

<div class="container-md text-center border border-secondary">
<h1>Crear evidencia</h1>
<form action="SvEvidencia" method="POST">
<input type="hidden" name="flag" value="create">
<label for="idSave">ID del tipo de evidencia: </label>
<input type="number" name="idTipoEvidencia" id="idSave" required>
<label for="link" class="form-label">Link: </label>
<input type="text" name="link" id="linkSave" class="form-control" required>
<label for="estadoSave" class="form-label">Estado: </label>
<input type="text" name="estado" id="estadoSave" class="form-control" required>
<label for="fechaSave" class="form-label">Fecha de obtención: </label>
<input type="date" name="fechaObtencion" id="fechaSave" class="form-control" required>
<button type="submit" class="btn btn-outline-primary">Crear evidencia</button>
</form>
${msjCreate}
</div>

</div>

</body>
</html>