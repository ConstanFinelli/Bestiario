<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GET hábitat</title>
<style>

.container{
	border: 1px solid black;
	margin-bottom: 20px;
}

</style>
</head>
<body>


<div class="container">
<h1>Conseguir habitats</h1>
<form action="SvHabitat" method="GET">
<input type="submit" value="Conseguir habitats">	
</form>
${findAllMsg}
</div>

<div class="container">
<h1>Conseguir habitat por ID</h1>
<form action="SvHabitat" method="GET">
<label for="idGetOne">ID del habitat: </label>
<input type="number" name="id" id="idGetOne">
<input type="submit" value="Conseguir habitat">	
</form>
${getOneMsg}
</div>

<div class="container">
<h1>Crear habitat</h1>
<form action="SvHabitat" method="POST">
<label for="nombreSave">Nombre de hábitat</label>
<input type="text" name="nombre" id="nombreSave">
<label for="localizacionSave">Localizacion de hábitat</label>
<input type="text" name="localizacion" id="localizacionSave">
<input type="submit" value="Crear habitat">
</form>
${saveMsg}
</div>

<div class="container">
<h1>Actualizar habitat por ID</h1>
<form action="SvHabitat" method="POST">
<label for="idUpdate">ID del habitat: </label>
<input type="number" name="id" id="idUpdate">
<label for="nombreUpdate">Nombre de hábitat</label>
<input type="text" name="nombre" id="nombre">
<label for="localizacionUpdate">Localizacion de hábitat</label>
<input type="text" name="localizacion" id="localizacionUpdate">
<input type="submit" value="Actualizar habitat">
</form>
${updateMsg}
</div>

<div class="container">
<h1>Eliminar habitat por ID</h1>
<form action="SvHabitat" method="POST">
<input type="hidden" name="delete" value="delete">
<label for="idDelete">ID del habitat: </label>
<input type="number" name="id" id="idDelete">
<input type="submit" value="Eliminar habitat">	
</form>
${deleteMsg}
</div>


</body>
</html>