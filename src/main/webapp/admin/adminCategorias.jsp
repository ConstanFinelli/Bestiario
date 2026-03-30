<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<%@ page import="entities.Categoria" %>
<%@ page import="java.util.LinkedList" %>
<!DOCTYPE html>
<%
	LinkedList<Categoria> categorias = (LinkedList<Categoria>) request.getAttribute("foundCategorias");	
	if(categorias == null ){
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.LISTAR_CATEGORIAS(""));
		rd.forward(request, response);
	}
%>
<h2>Gestionar categorías</h2>
<main class="adminForms">
<section class="formAdmin">
	<h1>Lista de categorías</h1>
    
    <% if(categorias != null && !categorias.isEmpty()) { %>
        <ul class="adminList">
            <% for(Categoria cat : categorias) { 
                if (cat.getNombre() != null) { 
            %>
                <li class="listItem">
                    <div class="itemInfo">
                        <span class="itemTitle"><%= cat.getNombre() %> <small>(ID: <%= cat.getIdCategoria() %>)</small></span>
                        <span class="itemDesc"><%= cat.getDescripcionCategoria() %></span>
                    </div>
                    <form action="<%= HttpRoutes.ELIMINAR_CATEGORIA(request.getContextPath()) %>" method="POST" onsubmit="return confirm('¿Eliminar <%= cat.getNombre() %>?');">
                        <input type="hidden" name="id" value="<%= cat.getIdCategoria() %>">
                        <button type="submit" class="deleteItem">Eliminar</button>
                    </form>
                </li>
            <%  }
             } %>
        </ul>
    <% } else { %>
        <p>No hay categorías registradas en el Bestiario.</p>
    <% } %>
</section>
<div class="formsFlex">
<form class="formAdmin formCrear" action="<%= HttpRoutes.CREAR_CATEGORIA(request.getContextPath()) %>" method="POST">
			<h1>Crear categoría</h1>
			<label for="nombre" class="inputLabel">Nombre</label>
			<input type="text" id="nombre" name="nombre" class="inputForm" placeholder="Ingresar nombre de la categoría..." required/>
			<label for="descripcion" class="inputLabel">Descripción</label>
			<input type="text" id="descripcion" name="descripcion" class="inputForm" placeholder="Ingresar descripción de la categoría..." required>
			<button type="submit" class="submitButton">Crear</button>
</form>
<form class="formAdmin formActualizar" action="<%= HttpRoutes.ACTUALIZAR_CATEGORIA(request.getContextPath()) %>" method="POST">
			<h1>Actualizar categoría</h1>
			<select name="id" class="inputForm">
		    <% if(categorias != null){ %>
		        <% for(Categoria cat : categorias){ 
		            String nombreCat = cat.getNombre();
		            int idCat = cat.getIdCategoria();
		            if (nombreCat != null) { 
		        %>
		            <option value="<%= idCat %>">
		                <%= nombreCat + "(ID: " + idCat + ")" %>
		            </option>
		        <%  }
		         } %>
		    <% } %>
			</select>
			<label for="nombre" class="inputLabel">Nombre</label>
			<input type="text" id="nombre" name="nombre" class="inputForm" placeholder="Ingresar nombre de la categoría..." required/>
			<label for="descripcion" class="inputLabel">Descripción</label>
			<input type="text" id="descripcion" name="descripcion" class="inputForm" placeholder="Ingresar descripción de la categoría..." required>
			<button type="submit" class="submitButton">Actualizar</button>
</form>
</div>
</main>
