<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<%@ page import="entities.Habitat" %>
<%@ page import="java.util.LinkedList" %>
<!DOCTYPE html>
<%
	LinkedList<Habitat> habitats = (LinkedList<Habitat>) request.getAttribute("habitats");	
	if(habitats == null ){
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.LISTAR_HABITATS("") + "?flag=admin");
		rd.forward(request, response);
	}
%>
<h2>Gestionar habitats</h2>
<main class="adminForms">
<section class="formAdmin">
	<h1>Lista de habitats</h1>
    
    <% if(habitats != null && !habitats.isEmpty()) { %>
        <ul class="adminList">
            <% for(Habitat hab : habitats) { 
                if (hab.getNombre() != null) { 
            %>
                <li class="listItem">
                    <div class="itemInfo">
                        <span class="itemTitle"><%= hab.getNombre() %> <small>(ID: <%= hab.getId() %>)</small></span>
                        <span class="itemDesc"><%= hab.getLocalizacion() %></span>
                    </div>
                    <div class="editButtons">
	                    <form action="<%= HttpRoutes.ELIMINAR_HABITAT(request.getContextPath()) %>" method="POST" onsubmit="return confirm('¿Eliminar <%= hab.getNombre() %>?');">
	                        <input type="hidden" name="id" value="<%= hab.getId() %>">
	                        <button type="submit" class="deleteItem">Eliminar</button>
	                    </form>
	                    <a class="caractItem" href="<%= HttpRoutes.LISTAR_CARACTERISTICAHABITAT(request.getContextPath()) %>?id=<%=hab.getId()%>">Ver mas</a>
                    </div>
                </li>
            <%  }
             } %>
        </ul>
    <% } else { %>
        <p>No hay habitats registradas en el Bestiario.</p>
    <% } %>
</section>
<div class="formsFlex">
<form class="formAdmin formCrear" action="<%= HttpRoutes.CREAR_HABITAT(request.getContextPath()) %>" method="POST">
			<h1>Crear habitat</h1>
			<label for="nombre" class="inputLabel">Nombre</label>
			<input type="text" id="nombre" name="nombre" class="inputForm" placeholder="Ingresar nombre de el habitat..." required/>
			<label for="localizacion" class="inputLabel">Localización</label>
			<input type="text" id="localizacion" name="localizacion" class="inputForm" placeholder="Ingresar localización de el habitat..." required>
			<label for="latitud" class="inputLabel">Datos Geograficos</label>
			<div class="datosGeograficos">
			<input type="text" id="latitud" name="latitud" class="inputForm" placeholder="Ingrese la latitud."/>
			<input type="text" id="longitud" name="longitud" class="inputForm" placeholder="Ingrese la longitud."/>
			</div>
			<button type="submit" class="submitButton">Crear</button>
</form>
<form class="formAdmin formActualizar" action="<%= HttpRoutes.ACTUALIZAR_HABITAT(request.getContextPath()) %>" method="POST">
			<h1>Actualizar habitat</h1>
			<select name="id" class="inputForm">
		    <% if(habitats != null){ %>
		        <% for(Habitat hab : habitats){ 
		            String nombreHab = hab.getNombre();
		            int idHab = hab.getId();
		            if (nombreHab != null) { 
		        %>
		            <option value="<%= idHab %>">
		                <%= nombreHab + "(ID: " + idHab + ")" %>
		            </option>
		        <%  }
		         } %>
		    <% } %>
			</select>
			<label for="nombre" class="inputLabel">Nombre</label>
			<input type="text" id="nombre" name="nombre" class="inputForm" placeholder="Ingresar nombre de el habitat..." required/>
			<label for="localizacion" class="inputLabel">Localización</label>
			<input type="text" id="localizacion" name="localizacion" class="inputForm" placeholder="Ingresar localización de el habitat..." required>
			<label for="latitudUpdate" class="inputLabel">Datos Geograficos</label>
			<div class="datosGeograficos">
			<input type="text" id="latitudUpdate" name="latitud" class="inputForm" placeholder="Ingrese la latitud."/>
			<input type="text" id="longitud" name="longitud" class="inputForm" placeholder="Ingrese la longitud."/>
			</div>	
			<button type="submit" class="submitButton">Actualizar</button>
</form>
</div>
</main>
