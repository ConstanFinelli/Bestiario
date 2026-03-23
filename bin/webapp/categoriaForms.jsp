<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Categoria forms</title>
</head>
<body>

<div class="container-md text-center">
<h1>Conseguir Categorias</h1>
<form action="SvCategoria" method="GET">
<input type="submit" value="Conseguir categorias">
</form>
${msjAll}
</div>

<div class="container-md text-center">
<h1>Conseguir categoria por ID</h1>
<form action = "SvCategoria" method = "GET">
<label for="idGetOne">ID de la categoria: </label>
<input type="number" name="id" id="idGetOne" required>
<input type="submit" value="conseguir categoria"> 
</form>
${msjOne}
</div>

<div class="container-md text-center">
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

<div>
<h1>Actualizar categoria por Id</h1>
<form action="SvCategoria" method="POST">
<label for="idUpdate">ID de la categoria</label>
<input type="number" name="id" id="idUpdate" required>
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

<div>
<h1>Eliminar categoria</h1>
<form action="SvCategoria" method="POST">
<label for="idDelete">ID de la categoria</label>
<input type="number" name="id" id="idDelete" required>
<input type="hidden" name="flag" value="delete">
<button type="submit" class="btn btn-outline-primary">Modificar Categoria</button>
</form>
${msjDelete}
</div>

</body>
</html>