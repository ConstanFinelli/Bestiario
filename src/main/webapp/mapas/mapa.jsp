<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map, entities.Bestia, entities.Habitat, helpers.CloudinaryHelper, helpers.HttpRoutes" %>
<%
    Map<Bestia, String> bestias = (Map<Bestia, String>) request.getSession().getAttribute("bestias");
    
    Bestia selectedB = (Bestia)request.getAttribute("selectedBestia");
    int idSelected = -1;

    if (selectedB != null) {
        idSelected = selectedB.getIdBestia();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bestiario-Mapa</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
	<link href="https://fonts.googleapis.com/css2?family=Bad+Script&family=MedievalSharp&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="<%=HttpRoutes.MAPA_CSS(request.getContextPath())%>">
    <link rel="stylesheet" href="<%=HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css"/>
</head>
<body>

<div class="contenedorMapa">
    <div id="map" class="mapa"></div>
    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
    <script>
        var selectedId = <%= idSelected %>;
        
        var map = L.map('map',{
        	maxBounds: [[-90, -180], [90, 180]],
        	maxBoundsViscosity: 1,
        	maxZoom: 8,
            minZoom: 3,
        }).setView([20, 0], 3);
        L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Physical_Map/MapServer/tile/{z}/{y}/{x}', {
        	attribution: 'Tiles &copy; Esri',
            noWrap: true,
            bounds: [[-90, -180], [90, 180]],
        }).addTo(map);
        
        function adaptarCoordenada(coord){
        	return coord + (Math.random() - 0.5)*0.3;
        }

        <% if (bestias != null) {
            for(Bestia b : bestias.keySet()) {
                for(Habitat h : b.getHabitats()) { %>
                
                L.marker([adaptarCoordenada(<%= h.getLatitud() %>), adaptarCoordenada(<%= h.getLongitud() %>)], {opacity: 0})
                  .addTo(map)
                  .on("dblclick", function(){
                      var idMarcador = <%= b.getIdBestia() %>;
                      if(idMarcador != selectedId){
                          window.location.href = "<%=HttpRoutes.MAPA_BESTIAS(request.getContextPath())%>" + "?selectedId=" + idMarcador;	  
                      } else {
                          console.log("Bestia ya seleccionada");  
                      }
                  })
                  .bindPopup('<b><%= b.getNombre() %></b><br><%= h.getLocalizacion()%>')
                  .bindTooltip(
                        '<img src="<%=CloudinaryHelper.getImagenMapa(bestias.get(b))%>" width="60" style="border-radius: 10px; cursor: pointer;">',
                        {permanent: true, direction: 'top', offset: L.point(-15	, 0)}
                      );

        <%      } 
            } 
        } %>
    </script>
</div>

<div class="contenedorOpciones">
    <% if(selectedB != null){ %>
        <h1 class="tituloHabitat">Habitats de <%=selectedB.getNombre()%></h1>
        <div class="contenedorHabitats">
            <% for(Habitat h: selectedB.getHabitats()){ %>
                <div class="HabitatCard" onClick="map.flyTo([<%= h.getLatitud()%>, <%= h.getLongitud()%>], 8)">
                    <h1 class="nombreHabitat"><%= h.getNombre()%></h1>
                    <h2>Localización: <%= h.getLocalizacion()%></h2>
                    <h2>Coordenadas: <%= h.getLatitud()%>, <%= h.getLongitud()%></h2>
                </div>
            <% } %>
        </div>
    <% } else { %>
        <h1 class="tituloHabitat">Selecciona un marcador en el mapa</h1>
        <div class="contenedorHabitats">
        <h2 class= "unselectedText">Cuando seleccione una bestia, aqui podra ver las ubicaciones donde consideramos habita la misma.</h2>
        <h2 class= "unselectedText">Estas ubicaciones fueron determinadas utilizando la informacion suministrada por la comunidad
        y verificada por nuestros investigadores.</h2>
        <h2 class="unselectedText"> Seleccione una bestia y podra ir a sus diferentes habitats mas agilmente.</h2>
        </div>
    <% } %>

    <div class="btnMapa">
        <a href="<%=HttpRoutes.HOME_JSP(request.getContextPath())%>">Volver</a>
    </div>
</div>

</body>
</html>