<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<%@ page import="entities.Lector" %>
<%@ page import="java.util.LinkedList" %>
<!DOCTYPE html>
<%
	LinkedList<Lector> lectores = (LinkedList<Lector>) request.getAttribute("foundLectores");	
	if(lectores == null ){
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.LISTAR_LECTORES(""));
		rd.forward(request, response);
	}
%>
<h2>Gestionar usuarios</h2>
<main class="adminForms">
<section class="formAdmin">
	<h1>Lista de lectores</h1>
    
    <% if(lectores != null && !lectores.isEmpty()) { %>
        <ul class="adminList">
            <% for(Lector lector : lectores) { 
                if (lector.getCorreo() != null) { 
            %>
                <li class="listItem">
                    <div class="itemInfo">
                        <span class="itemTitle"><%= lector.getCorreo() %> <small>(ID: <%= lector.getIdUsuario() %>)</small></span>
                        <span class="itemDesc">Fecha de nacimiento: <%= lector.getFechaNacimiento() %></span>
                    </div>
	                <form action="<%= HttpRoutes.ELIMINAR_LECTOR(request.getContextPath()) %>" method="POST" onsubmit="return confirm('¿Eliminar <%= lector.getCorreo() %>?');">
	                        <input type="hidden" name="id" value="<%= lector.getIdUsuario() %>">
	                        <button type="submit" class="deleteItem">Eliminar</button>
	                </form>
                </li>
            <%  }
             } %>
        </ul>
    <% } else { %>
        <p>No hay lectores registrados en el Bestiario.</p>
    <% } %>
</section>
<div class="formsFlex">
<form class="formAdmin formCrear" action="<%= HttpRoutes.REGISTER(request.getContextPath()) %>" method="POST">
			<h1>Crear usuario</h1>
			<input type="hidden" name="flag" value="admin">
			<label for="nombre" class="inputLabel">Correo electrónico</label>
			<input type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" id="correo" name=correo class="inputForm" placeholder="Ingresar correo de usuario..." required/>
			<label for="password" class="inputLabel">Contraseña</label>
			<input type="password" id="password" name="password" class="inputForm" placeholder="Ingresar contraseña de el lector..." required>
			<label for="fechaNacimiento" class="inputLabel">Fecha de nacimiento</label>
			<input type="date" id="fechaNacimiento" name="fechaNacimiento" min="1000-01-01" max="9999-12-31" required/>
			<button type="submit" class="submitButton">Crear</button>
</form>
<form class="formAdmin formActualizar" action="<%= HttpRoutes.ACTUALIZAR_LECTOR(request.getContextPath()) %>" method="POST">
			<h1>Actualizar usuario</h1>
			<select name="id" class="inputForm">
		    <% if(lectores != null){ %>
		        <% for(Lector lector : lectores){ 
		            String mail = lector.getCorreo();
		            int idLector = lector.getIdUsuario();
		            if (mail != null) { 
		        %>
		            <option value="<%= idLector %>">
		                <%= mail + "(ID: " + idLector + ")" %>
		            </option>
		        <%  }
		         } %>
		    <% } %>
			</select>
			<label for="nombre" class="inputLabel">Correo electrónico</label>
			<input type="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$" id="email" name="email" class="inputForm" placeholder="Ingresar correo de usuario..." required/>
			<label for="password" class="inputLabel">Contraseña</label>
			<input type="password" id="password" name="password" class="inputForm" placeholder="Ingresar contraseña de el lector..." required>
			<label for="fechaNacimiento" class="inputLabel">Fecha de nacimiento</label>
			<input type="date" id="fechaNacimiento" name="fechaNacimiento" min="1000-01-01" max="9999-12-31" required/>
			<button type="submit" class="submitButton">Actualizar</button>
</form>
</div>
</main>
