<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.CaracteristicaHabitat" %>
<%@ page import="java.util.LinkedList" %>
<!DOCTYPE html>
<%
	LinkedList<CaracteristicaHabitat> carHabs = (LinkedList<CaracteristicaHabitat>) request.getAttribute("foundCaracteristicas");
	Habitat hab = (Habitat) session.getAttribute("associatedHabitat");
	if(carHabs == null || hab == null){
		response.sendRedirect(HttpRoutes.LISTAR_CARACTERISTICAHABITAT(request.getContextPath()) + "?id=" + request.getParameter("id"));
        return;
	}
%>
<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath())%>?crud=habitats" class="backButton">
<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 640"><path d="M73.4 297.4C60.9 309.9 60.9 330.2 73.4 342.7L233.4 502.7C245.9 515.2 266.2 515.2 278.7 502.7C291.2 490.2 291.2 469.9 278.7 457.4L173.3 352L544 352C561.7 352 576 337.7 576 320C576 302.3 561.7 288 544 288L173.3 288L278.7 182.6C291.2 170.1 291.2 149.8 278.7 137.3C266.2 124.8 245.9 124.8 233.4 137.3L73.4 297.3z"/></svg>
Volver atras</a>
<h2>Gestionar características de habitats</h2>
<main class="adminForms">
<section class="formAdmin">
	
	<h1>Características del habitat <%= hab.getNombre() %></h1>
    
    <% if(carHabs != null && !carHabs.isEmpty()) { %>
        <ul class="adminList">
            <% for(CaracteristicaHabitat carHab : carHabs) { 
                if (carHab.getDescripcion() != null) { 
            %>
                <li class="listItem">
                    <div class="itemInfo">
                        <span class="itemTitle"><%= carHab.getDescripcion() %></span>
                    </div>
                    <form action="<%= HttpRoutes.ELIMINAR_CARACTERISTICAHABITAT(request.getContextPath()) %>" method="POST" onsubmit="return confirm('¿Eliminar característica?');">
                        <input type="hidden" name="id" value="<%= carHab.getIdHabitat() %>">
                        <input type="hidden" name="descripcion" value="<%= carHab.getDescripcion() %>">
                        <button type="submit" class="deleteItem">Eliminar</button>
                    </form>
                </li>
            <%  }
             } %>
        </ul>
    <% } else { %>
        <p>No hay características asociadas a este hábitat.</p>
    <% } %>
</section>
<div class="formsFlex">
<form class="formAdmin formCrear" action="<%= HttpRoutes.CREAR_CARACTERISTICAHABITAT(request.getContextPath()) %>" method="POST">
			<h1>Crear característica</h1>
			<input type="hidden" name="id" value="<%= hab.getId() %>">
			<label for="descripcion" class="inputLabel">Descripción</label>
			<input type="text" id="descripcion" name="descripcion" class="inputForm" placeholder="Ingresar descripción..." required>
			<button type="submit" class="submitButton">Crear</button>
</form>
<form class="formAdmin formActualizar" action="<%= HttpRoutes.ACTUALIZAR_CARACTERISTICAHABITAT(request.getContextPath()) %>" method="POST">
			<h1>Actualizar característica</h1>
			<input type="hidden" name="id" value="<%= hab.getId() %>">
			<label for="descripcion">Anterior descripción</label>
			<select name="descripcion" class="inputForm">
		    <% if(!carHabs.isEmpty()){ %>
		        <% for(CaracteristicaHabitat carHab : carHabs){ 
		            String desc = carHab.getDescripcion();
		            if (desc != null) { 
		        %>
		            <option value="<%= desc %>">
		                <%= desc %>
		            </option>
		        <%  }
		         } %>
		    <% } %>
			</select>
			<label for="newDescripcion" class="inputLabel">Nueva descripción</label>
			<input type="text" id="newDescripcion" name="newDescripcion" class="inputForm" placeholder="Ingresar nueva descripción..." required>
			<button type="submit" class="submitButton">Actualizar</button>
</form>
</div>
</main>
