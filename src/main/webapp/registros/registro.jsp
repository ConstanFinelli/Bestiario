<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Registro" %>
<%@ page import="entities.Evidencia" %>
<%@ page import="entities.Categoria" %>
<%@ page import="entities.Comentario" %>
<%@ page import="helpers.HttpRoutes, helpers.CloudinaryHelper, helpers.EnvHelper" %>
<!DOCTYPE html>
<html>
    <head>
    	<% 
        	Bestia bestia = (Bestia) request.getAttribute("foundBestia");
    		Registro registro = (Registro) request.getAttribute("foundRegistro");  
    		String UrlImagen = (String) request.getAttribute("UrlImagen");
    		%>  
        <title><%= bestia != null ? bestia.getNombre() : "" %> - Registro de bestia</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
		<link href="https://fonts.googleapis.com/css2?family=Aubrey&family=Iosevka+Charon+Mono:ital,wght@0,300;0,400;0,500;0,700;1,300;1,400;1,500;1,700&family=Outfit:wght@100&family=Smooch+Sans:wght@100..900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath())%>">
        <link rel="stylesheet" href="<%= HttpRoutes.REGISTRO_CSS(request.getContextPath())%>">
        <link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath())%>">
    </head>
    <body>
        <%@ include file="../components/navbar.jsp" %>
        <section class="mainContent">
        <% if(bestia == null){ %>
        <section>
        	<div class="notFound">Bestia no encontrada</div>
        </section>
        <%} %>
        <% if(bestia != null){ 
        	LinkedList<Evidencia> evidencias = bestia.getEvidencias();
        %>
        <div class="registroContent">
            <section>
                <h1><%= bestia.getNombre() %> </h1>
                <% if(registro != null){ %>
                <article class="entrada">
                	<h2>Introducción</h2>
                	<p><%= registro.getContenido().getIntroduccion() %></p>
                </article>
                <article class="entrada">
	                <h2>Descripción</h2>
	                <p><%= registro.getContenido().getDescripcion() %></p>
                </article>
                <article class="entrada">
	                <h2>Historia</h2>
	                <p><%= registro.getContenido().getHistoria() %></p>
                </article>
                <article class="entrada">
	                <h2>Resumen</h2>
	                <p><%= registro.getContenido().getResumen() %></p>
                </article>
                <% }else{ %>
                	<% if(request.getParameter("fecha") == null){ %>
                		<div class="notFound">No hay un registro encontrado para esta bestia.</div>
                	<%}else{ %>
                		<div class="notFound">No hay un registro encontrado para esta bestia en la fecha seleccionada.</div>
                	<%} %>
                <% } %>
                <% if(!evidencias.isEmpty()){ %>
                <h2>Evidencias</h2>
                <ul class="evidencias">
                <% 
                for(Evidencia evidencia : evidencias){ %>
                	<%
                	String teDesc = evidencia.getTipo().getDescripcion();
                	String fechaOb = evidencia.getFechaObtencion().toString();
                	String evText = " Obtenido el " + fechaOb;
                	
                	%>
                	<li class="evidenciasItem">
                		<a class="evidenciasLink" href="javascript:void(0)" 
                		onclick="abrirModal('<%= switch(evidencia.getTipo().getDescripcion().trim().toLowerCase()){
                		case "video" -> CloudinaryHelper.getVideoEvidencia(evidencia.getFileId());
                		case "imagen" -> CloudinaryHelper.getImagenEvidencia(evidencia.getFileId());
                		default -> CloudinaryHelper.getArchivoEvidencia(evidencia.getFileId());
                		}%>')"><span class="tipoEvidenciaText"><%= teDesc %></span><%= evText %></a>
                	</li>
                <%} %>
                </ul>
                <%} %>
            </section>
            <aside class="infoBestia">
            	<img src="<%=UrlImagen%>" alt="Imagen de la bestia">
                <div>
	                <h3>Detalles de la bestia</h3>
	                <ul>
	                    <li>Nombre: <%= bestia.getNombre() %> </li>
	                    <li>Peligrosidad: <%= bestia.getPeligrosidad() %></li>
	                </ul>
                </div>
                <div>
                	<h3>Categorías</h3>
                	<ul>
                	<% if(bestia.getCategorias().isEmpty()){ %>
                    			No tiene categorías definidas.
                    			<%}else{ 
                    				for(Categoria cat:bestia.getCategorias()){
                    			%>
                    				<li>
                    					<span class="catBadge"><%= cat.getNombre() %></span>
                    				</li>
                    			<%} %>
                   			<%} %>
                	</ul>
                </div>
                <div>
	                <h3>Hábitats localizados</h3>
	                <ul>
	                	<% LinkedList<Habitat> habitats = bestia.getHabitats(); %>
	                	<% if(habitats != null && !habitats.isEmpty()){ 
	                	for(Habitat habitat:habitats){
	                	%>
	                    <li><%= habitat.getNombre() %>, <%= habitat.getLocalizacion() %></li>
	                    <%} %>
	                    <% } else{%>
	                    <li>No hay habitats registradas para esta bestia.</li>
	                    <%} %>
	                </ul>
                </div>
                <div>
                	<% if(registro != null){ %>
	                	<h3>Detalles de registro</h3>
	                	<ul>
	                		<li>Publicado por <% if(registro.getPublicador() != null){%><%= registro.getPublicador().getNombre() + " " + registro.getPublicador().getApellido() %><%}else{ %>Falta aprobar<%} %> </li>
	                		<li>Último cambio: <% if(registro.getPublicador() != null){%><%= registro.getFechaAprobacion() %><%}else{ %>Sin cambios <%} %></li>
	                	</ul>
	                	<%} %>
                </div>
                
                <div>
                <h3>Ver ubicaciones conocidas</h3>
                <a href="<%= HttpRoutes.MAPA_BESTIA(request.getContextPath())%>?id=<%= bestia.getIdBestia() %>"><img src="<%= CloudinaryHelper.getImagenMapaButton(EnvHelper.get("MAP_PICTURE_ID")) %>" class="mapaButton"></a>
                </div>
            </aside>
        </div>
            <% } %>
        </section>
        <%
        	if(bestia != null){
				
            		LinkedList<Comentario> comentarios = bestia.getComentarios(); %>
        <section id="comentarios" class="comentarios mainContent">
        	<% if(registro != null){ %>
            	<% if(usuario != null){ %>
            	<form action="<%= HttpRoutes.AGREGAR_COMENTARIO(request.getContextPath()) %>?id=<%= bestia.getIdBestia() %>" method="post">
	            	<input class="inputComentario" type="text" placeholder="Escribir comentario..." name="contenido" required>
	            	<input type="hidden" name="nroRegistro" value=<%= registro.getNroRegistro() %>>
	            	<input type="hidden" name="idUsuario" value="<%=usuario.getIdUsuario()%>">
	            	<input type="hidden" name="idBestia" value="<%=bestia.getIdBestia()%>">
	            	<input class="btnComentario" type="submit">
            	</form>
            	<%} 
            	if(!comentarios.isEmpty()){
            	for(Comentario comentario:comentarios){
            	%>
            	<section class="comentario">
            		<aside class="comentarioDatos"><%= comentario.getPublicador().getCorreo() %> - <span class="fechaComentario">Publicado el <%= comentario.getFecha() %></span></aside>
            		<article class="comentarioContenido"><%= comentario.getContenido() %></article>
            	</section>
            	<%}}else{%>
            	<p>No hay comentarios para esta bestia.</p>
            	<%}} %>
            	<div class="anotherRegistros">
            		<h3>Cargar registro en determinada fecha</h3>
				    <form action="<%=HttpRoutes.OBTENER_REGISTRO_BESTIA_CON_FECHA(request.getContextPath())%>" method="get" class="registrosForm">
				        <input type="hidden" name="id" value="<%= bestia != null ? bestia.getIdBestia() : "" %>">
				        <input type="date" id="fecha" name="fecha" required>
				        <button type="submit" class="btnRegistro">Cargar registro</button>
				    </form>
				</div>
				<%} %>
                <% if(usuario != null){%><a class="btnAgregar" href="<%= HttpRoutes.ACTUALIZAR_REGISTRO(request.getContextPath()) %>?id=<%=bestia.getIdBestia()%>">Proponer nuevo registro</a><%} %>	
                <div id="modal" class="modal-container">
					<div class="modal-content">
						<div id="modal-body"></div>
					</div>
				</div>	
            </section>
        <%@ include file="../components/footer.jsp" %>
        <script>
		// javascript para modal
		function abrirModal(link) {
			if (!link) {
		        alert("Esta evidencia no tiene un archivo multimedia asociado.");
		        return;
		    }
			
			const modalBody = document.getElementById('modal-body');
			modalBody.innerHTML = "";
			const esVideo = link.toLowerCase().match(/\.(mp4|webm|ogg)$/) || link.includes("video/upload");
		    const esImagen = link.toLowerCase().match(/\.(jpg|jpeg|png|gif|webp|svg)$/) || link.includes("image/upload");
		    
		    if (esVideo) {
		    	const video = document.createElement('video');
		        video.src = link;
		        video.controls = true;
		        video.autoplay = true;
		        video.classList.add('modal-evidencia');
		        modalBody.appendChild(video);
		    } 
		    else if (esImagen) {
		    	const img = document.createElement('img');
		        img.src = link;
		        img.classList.add('modal-evidencia');
		        modalBody.appendChild(img);
		    }else {
		    	descarga = document.createElement('div');
		    	descarga.style.textAlign = "center";
		    	descarga.style.padding = "20px";

		        const p = document.createElement('p');
		        p.textContent = "El archivo no se puede previsualizar en el navegador.";
		        p.style.marginBottom = "15px";

		        const a = document.createElement('a');
		        a.href = link;
		        a.download = "";
		        a.target = "_blank";
		        a.textContent = "Haz click para descargar el archivo";
		        a.className = "modal-download"; 
		        
		        a.style.display = "inline-block";
		        a.style.padding = "10px 20px";
		        a.style.background = "#2563eb";
		        a.style.color = "white";
		        a.style.textDecoration = "none";
		        a.style.borderRadius = "5px";

		        descarga.appendChild(p);
		        descarga.appendChild(a);
		        modalBody.appendChild(descarga);
		    }
		    
			document.getElementById('modal').classList.add('is-visible');
		}

		function cerrarModal() {
			document.getElementById('modal').classList.remove('is-visible');
		}

		window.onclick = function(event) {
			let modal = document.getElementById('modal');
			if (event.target == modal) {
				cerrarModal();
			}
		}
	</script>
    </body>
</html>