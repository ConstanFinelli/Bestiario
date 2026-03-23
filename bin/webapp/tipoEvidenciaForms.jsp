<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tipo de Evidencia Forms</title>
</head>
<body>

<div class="container-md text-center">
<h1>Conseguir Tipos de evidencia</h1>
<form action="SvTipoEvidencia" method="GET">
<input type="submit" value="Conseguir tipos de evidencia">
</form>
${msjAll}
</div>

<div class="container-md text-center">
<h1>Conseguir Tipo de evidencia por ID</h1>
<form action="SvTipoEvidencia" method="GET">
<label for="idGetOne">ID del tipo de evidencia: </label>
<input type="number" name="id" id="idGetOne" required>
<input type="submit" value="Conseguir tipo de evidencia">
</form>
${msjOne}
</div>

<div class="container-md text-center">
<h1>Crear tipo de evidencia</h1>
<form action="SvTipoEvidencia" method="POST">
<label for="descripcionSave">Descripcion del tipo de evidencia: </label>
<input type="text" name="descripcion" id="descripcionSave" required>
<input type="hidden" name="flag" value="create">
<input type="submit" value="Crear tipo de evidencia">
</form>
${msjCreate}
</div>

<div class="container-md text-center">
<h1>Actualizar tipo de evidencia</h1>
<form action="SvTipoEvidencia" method="POST">
<label for="idUpdate">Id del tipo a modificar</label>
<input type="number" name="id" id="idUpdate" required>
<br><br>
<label for="descripcionUpdate">Nueva descripcion</label>
<input type="text" name="descripcion" id="descripcionUpdate" required>
<input type="hidden" name="flag" value="update">
<br><br>
<input type="submit" value="Modificar tipo de evidencia">
</form>
${msjUpdate}
</div>

<div class="container-md text-center">
<h1>Eliminar tipo de evidencia</h1>
<form action="SvTipoEvidencia" method="POST">
<label for="idDelete">Id del tipo a eliminar</label>
<input type="number" name="id" id="idDelete" required>
<br><br>
<input type="hidden" name="flag" value="delete">
<input type="submit" value="Eliminar tipo de evidencia">
</form>
${msjDelete}
</div>
</body>

</html>