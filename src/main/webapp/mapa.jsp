<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entities.Bestia, java.util.*, entities.Habitat, logic.*, helpers.*, java.time.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bestiario</title>

    <!-- ✅ 1. Leaflet CSS (SIEMPRE en el head) -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css"/>
</head>

<body>

    <h1>Mapa de Bestias</h1>
    <!-- ✅ 2. Contenedor del mapa (DENTRO del body) -->
    <div id="map" style="height: 500px;"></div>

    <!-- ✅ 3. Leaflet JS (ANTES de tu script) -->
    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>

    <!-- ✅ 4. Tu script (AL FINAL del body) -->
    <script>
        var map = L.map('map').setView([20, 0], 2);

        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: 'Mapa'
        }).addTo(map);

        <%-- ✅ 5. Código Java dentro del script --%>
        <%
        	LogicBestia controladorBestia = new LogicBestia();
        	LogicRegistro controladorRegistro = new LogicRegistro();
            List<Bestia> bestias = controladorBestia.findAll();
            if (bestias != null) {
                for (Bestia bestia : bestias) {
                	if(bestia.getHabitats() != null){
        %>
     		<%for(Habitat h : bestia.getHabitats()){ %>
            L.marker([<%= h.getLatitud() %>, <%= h.getLongitud() %>])
              .addTo(map)
              .bindPopup('<b><%= bestia.getNombre() %></b><br><%= h.getLocalizacion()%>')
              .bindTooltip(
            		    '<img src="<%=CloudinaryHelper.getImagenRegistro("dnsffrvnfxs37pay9weg")%>" width="60" style="border-radius: 10px;">',
            		    { permanent: true, direction: 'top' }
            		  );
        <%				}
        			} 
                }
            }
        %>
    </script>

</body>
</html>