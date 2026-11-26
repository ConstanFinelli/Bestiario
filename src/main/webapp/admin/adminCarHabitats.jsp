<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.CaracteristicaHabitat" %>
<%@ page import="java.util.LinkedList" %>
<%
	LinkedList<CaracteristicaHabitat> carHabs = (LinkedList<CaracteristicaHabitat>) request.getAttribute("foundCaracteristicas");
	Habitat hab = (Habitat) session.getAttribute("associatedHabitat");
	if(carHabs == null || hab == null){
		response.sendRedirect(HttpRoutes.LISTAR_CARACTERISTICAHABITAT(request.getContextPath()) + "?id=" + request.getParameter("id"));
        return;
	}
%>
<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath())%>?crud=habitats" class="backButton">
<jsp:include page="/components/iconoVolver.jsp" />
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
                    <button type="button" class="deleteItem" onclick="abrirModalForm('ELIMINAR CARACTERÍSTICA', '¿Estás seguro que deseas eliminar <%= carHab.getDescripcion().replace("'", "\\'") %>?', '<%= HttpRoutes.ELIMINAR_CARACTERISTICAHABITAT(request.getContextPath()) %>', { id: '<%= carHab.getIdHabitat() %>', descripcion: '<%= carHab.getDescripcion().replace("'", "\\'") %>' })">Eliminar</button>
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
