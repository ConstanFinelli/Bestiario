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
${mensaje}
</div>

<div class="container">
<h1>Conseguir habitat por ID</h1>
<form action="SvHabitat" method="GET">
<input type="text" id="id">
<input type="submit" value="Conseguir habitats">	
</form>
${mensaje}
</div>

<div class="container">
<h1>Crear habitat</h1>
<form action="SvHabitat" method="POST">
<label for="nombre">Nombre de hábitat</label>
<input type="text" name="nombre" id="nombre">
<label for="localizacion">Localizacion de hábitat</label>
<input type="text" name="localizacion" id="localizacion">
<input type="submit" value="Crear habitat">
</form>
</div>

</body>
</html>