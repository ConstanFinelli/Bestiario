<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.List, java.util.Map, entities.Bestia, entities.Habitat, helpers.CloudinaryHelper, helpers.HttpRoutes" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario</title>

<link rel="stylesheet" href="../css/mapa.css">
<link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css"/>

</head>
<body>


<div class="contenedorMapa">

 <div id="map" class="mapa"></div>
 <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
 <script>
        var map = L.map('map').setView([20, 0], 2);
        L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Physical_Map/MapServer/tile/{z}/{y}/{x}', {
            attribution: 'Tiles &copy; Esri',
            maxZoom: 8
        }).addTo(map);
        
    <% Map<Bestia, String> bestias = (Map<Bestia, String>) request.getAttribute("bestias");
    Bestia selectedB = null;
    if(bestias.size() == 1){
    	selectedB = bestias.entrySet().iterator().next().getKey();
    }
        for(Bestia b : bestias.keySet()){
            for(Habitat h : b.getHabitats()) {%>
            L.marker([<%= h.getLatitud() %>, <%= h.getLongitud() %>])
              .addTo(map)
              .bindPopup('<b><%= b.getNombre() %></b><br><%= h.getLocalizacion()%>')
              .bindTooltip(
            		    '<img src="<%=CloudinaryHelper.getImagenMapa(bestias.get(b))%>" width="60" style="border-radius: 10px;">',
            		    { permanent: true, direction: 'top' }
            		  );

        <%} 
         }%>
</script>

</div>

<div class="contenedorOpciones">
	<div class="contenedorHabitats">
	<%for(Habitat h: selectedB.getHabitats()){%>
		<div class="HabitatCard">
		<h2><%= h.getNombre()%></h2>
		<h2><%= h.getLocalizacion()%></h2>
		<h2>Coordenadas: <%= h.getLatitud()%>, <%= h.getLongitud()%></h2>
		</div>
	<%} %>
	</div>
</div>>
</body>

</html>

