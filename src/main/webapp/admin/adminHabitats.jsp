<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<%@ page import="entities.Habitat" %>
<%@ page import="java.util.LinkedList" %>
<%
	LinkedList<Habitat> habitats = (LinkedList<Habitat>) request.getAttribute("habitats");	
	if(habitats == null ){%>
		<script>
        window.location.href = "<%= HttpRoutes.LISTAR_HABITATS(request.getContextPath()) %>?flag=admin";
    </script>
	<%}
%>

<!DOCTYPE html>
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
			<div style=display: flex; align-items: center;>
			<label for="latitudCreate" class="inputLabel">Datos Geográficos</label>
			<button type="button" onClick="abrirMapa('latitudCreate', 'longitudCreate')">
			<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="imgButtonModal">
  <path stroke-linecap="round" stroke-linejoin="round" d="M9 6.75V15m6-6v8.25m.503 3.498 4.875-2.437c.381-.19.622-.58.622-1.006V4.82c0-.836-.88-1.38-1.628-1.006l-3.869 1.934c-.317.159-.69.159-1.006 0L9.503 3.252a1.125 1.125 0 0 0-1.006 0L3.622 5.689C3.24 5.88 3 6.27 3 6.695V19.18c0 .836.88 1.38 1.628 1.006l3.869-1.934c.317-.159.69-.159 1.006 0l4.994 2.497c.317.158.69.158 1.006 0Z" />
</svg>
			</button>
			</div>
			<div class="datosGeograficos">
			<input type="text" id="latitudCreate" name="latitud" class="inputForm" placeholder="Ingrese la latitud."/>
			<input type="text" id="longitudCreate" name="longitud" class="inputForm" placeholder="Ingrese la longitud."/>
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
			<div style=display:flex; align-items: center;>
			<label for="latitudUpdate" class="inputLabel">Datos Geográficos</label>
			<button type="button" onClick="abrirMapa('latitudUpdate', 'longitudUpdate')">
			<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="imgButtonModal">
  <path stroke-linecap="round" stroke-linejoin="round" d="M9 6.75V15m6-6v8.25m.503 3.498 4.875-2.437c.381-.19.622-.58.622-1.006V4.82c0-.836-.88-1.38-1.628-1.006l-3.869 1.934c-.317.159-.69.159-1.006 0L9.503 3.252a1.125 1.125 0 0 0-1.006 0L3.622 5.689C3.24 5.88 3 6.27 3 6.695V19.18c0 .836.88 1.38 1.628 1.006l3.869-1.934c.317-.159.69-.159 1.006 0l4.994 2.497c.317.158.69.158 1.006 0Z" />
</svg>
			</button>
			</div>
			<div class="datosGeograficos">
			<input type="text" id="latitudUpdate" name="latitud" class="inputForm" placeholder="Ingrese la latitud."/>
			<input type="text" id="longitudUpdate" name="longitud" class="inputForm" placeholder="Ingrese la longitud."/>
			</div>	
			<button type="submit" class="submitButton">Actualizar</button>
</form>
</div>
<script>
let campoLatDestino = '';
let campoLngDestino = '';

function abrirMapa(idLat, idLng) {
    campoLatDestino = idLat;
    campoLngDestino = idLng;
    document.getElementById('mapModal').style.display = 'flex';
}

function cerrarMapa() {
    document.getElementById('mapModal').style.display = 'none';
}

function obtenerDatosDelIframe() {
   
    const iframe = document.getElementById('iframeMapa');
    const iframeWindow = iframe.contentWindow;
    
  
    const latCalculada = iframeWindow.document.getElementById('latitud').innerText;
    const lngCalculada = iframeWindow.document.getElementById('longitud').innerText;

    if (latCalculada !== "--") {
        document.getElementById(campoLatDestino).value = latCalculada;
        document.getElementById(campoLngDestino).value = lngCalculada;
        
        cerrarMapa();
    } else {
        alert("Por favor, selecciona un punto en el mapa primero.");
    }
}
</script>

<div id="mapModal" class="modalMapa">
	<div class="modalContainer">
	 <div class="modalHeader">
            <span class="closeModal" onclick="cerrarMapa()">&times;</span>
        </div>
        
        <iframe id="iframeMapa" class="iframeMapa" src="<%= HttpRoutes.MAPA_CARGA_HABITAT_JSP(request.getContextPath()) %>">
        </iframe>
        
        <div class="modalFooter">
        	<button type="button" class="submitButton" onClick="obtenerDatosDelIframe()">Confirmar Ubicacion</button>
        </div>
	</div>
</div>

</main>
