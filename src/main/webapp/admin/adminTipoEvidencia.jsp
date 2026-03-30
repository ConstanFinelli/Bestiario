<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<%@ page import="entities.TipoEvidencia" %>
<%@ page import="java.util.LinkedList" %>
<!DOCTYPE html>
<%
	LinkedList<TipoEvidencia> tipos = (LinkedList<TipoEvidencia>) request.getAttribute("foundTipos");	
	if(tipos == null ){
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.LISTAR_TIPOS_EVIDENCIA(""));
		rd.forward(request, response);
	}
%>
<h2>Gestionar tipos de evidencia</h2>
<main class="adminForms">
<section class="formAdmin">
	<h1>Lista de tipos de evidencia</h1>
    
    <% if(tipos != null && !tipos.isEmpty()) { %>
        <ul class="adminList">
            <% for(TipoEvidencia tipo : tipos) { 
                if (tipo.getDescripcion() != null) { 
                	String desc = tipo.getDescripcion();
                	int id = tipo.getId();
            %>
                <li class="listItem">
                    <div class="itemInfo">
                        <span class="itemTitle"><%= desc %> <small>(ID: <%= id %>)</small></span>
                    </div>
	                <form action="<%= HttpRoutes.ELIMINAR_TIPO_EVIDENCIA(request.getContextPath()) %>" method="POST" onsubmit="return confirm('¿Eliminar <%= desc %>?');">
	                    <input type="hidden" name="id" value="<%= id %>">
	                    <button type="submit" class="deleteItem">Eliminar</button>
	                </form>
                </li>
            <%  }
             } %>
        </ul>
    <% } else { %>
        <p>No hay tipos de evidencias registradas en el Bestiario.</p>
    <% } %>
</section>
<div class="formsFlex">
<form class="formAdmin formCrear" action="<%= HttpRoutes.CREAR_TIPO_EVIDENCIA(request.getContextPath()) %>" method="POST">
			<h1>Crear tipo de evidencia</h1>
			<label for="descripcion" class="inputLabel">Descripción</label>
			<input type="text" id="descripcion" name="descripcion" class="inputForm" placeholder="Ingresar descripcion..." required/>
			<button type="submit" class="submitButton">Crear</button>
</form>
<form class="formAdmin formActualizar" action="<%= HttpRoutes.ACTUALIZAR_TIPO_EVIDENCIA(request.getContextPath()) %>" method="POST">
			<h1>Actualizar tipo de evidencia</h1>
			<select name="id" class="inputForm">
		    <% if(tipos != null){ %>
		        <% for(TipoEvidencia tipo : tipos){ 
		            int id = tipo.getId();
		            String desc = tipo.getDescripcion();
		            if (desc != null) { 
		        %>
		            <option value="<%= id %>">
		                <%= desc + "(ID: " + id + ")" %>
		            </option>
		        <%  }
		         } %>
		    <% } %>
			</select>
			<label for="descripcion" class="inputLabel">Descripción</label>
			<input type="text" id="descripcion" name="descripcion" class="inputForm" placeholder="Ingresar descripcion..." required/>
			<button type="submit" class="submitButton">Actualizar</button>
</form>
</div>
</main>
