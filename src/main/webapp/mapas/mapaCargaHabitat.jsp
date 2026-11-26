<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes"%>    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Mapa Cargar Habitat</title>
<link rel="stylesheet" href="<%=HttpRoutes.MAPA_CSS(request.getContextPath())%>">
<link rel="stylesheet" href="<%=HttpRoutes.MAIN_CSS(request.getContextPath())%>">
	<link rel="stylesheet" href="https://unpkg.com/leaflet/dist/leaflet.css"/>
</head>
<body>
    <%@ include file="../components/error.jsp" %>
    <div class="contenedorMapa">
     <div id="map" class="mapa"></div>
    <script src="https://unpkg.com/leaflet/dist/leaflet.js"></script>
    <script src="<%=HttpRoutes.MAPA_BASE_JS(request.getContextPath())%>"></script>
    <script>
         var puntoSeleccionado = null;
         var map = crearMapaBase();

        map.on("click", function(e){
            var lat = e.latlng.lat;
            var lng = e.latlng.lng;

            document.getElementById('latitud').innerText = lat.toFixed(6);
            document.getElementById('longitud').innerText = lng.toFixed(6)

            if(puntoSeleccionado){
                puntoSeleccionado.setLatLng(e.latlng);
            }else{
                puntoSeleccionado = L.marker(e.latlng, {draggable: true}).addTo(map);
            }
			var position;
            puntoSeleccionado.on('dragend', function(event){
                position = puntoSeleccionado.getLatLng();
                document.getElementById('latitud').innerText = position.lat.toFixed(6);
                document.getElementById('longitud').innerText = position.lng.toFixed(6)
            })
        })
        
        	function volarAlPunto() {
  	 		 var latText = document.getElementById('latitud').innerText;
   			 var lngText = document.getElementById('longitud').innerText;
    
    			if (latText !== "--" && lngText !== "--") {
		      var lat = parseFloat(latText);
		      var lng = parseFloat(lngText);
		
		      map.flyTo([lat, lng], 8, {
		          animate: true,
		          duration: 1.5 
	  });
    }
}
    </script>
    </div>

    <div class="contenedorOpciones">
        <div class="contenedorHabitats">
           <div class= "HabitatCard" onClick="volarAlPunto()">
           <h1 class="nombreHabitat">Punto Seleccionado</h1>
           <h2>Latitud: <span id="latitud">--</span> </h2>
           <h2>Longitud: <span id="longitud">--</span></h2>
           </div>
        </div>
    </div>

</body>
</html>