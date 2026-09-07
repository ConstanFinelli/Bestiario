<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Registro" %>
<%@ page import="entities.Evidencia" %>
<%@ page import="entities.Categoria" %>
<%@ page import="helpers.HttpRoutes, helpers.CloudinaryHelper" %>
<!DOCTYPE html>
<html>
    <head>
    	<% 
        	Bestia bestia = (Bestia) request.getAttribute("foundBestia");
    		Registro registro = (Registro) request.getAttribute("foundRegistro");
        %>  
      
        <title><%= bestia != null ? bestia.getNombre() : "" %> - Actualización de bestia</title>
        <link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
        <link rel="stylesheet" href="<%= HttpRoutes.REGISTRO_CSS(request.getContextPath()) %>">
        <link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath()) %>">
    </head>
    <body>
        <%@ include file="../components/navbar.jsp" %>

        <% if(bestia == null){ %>
        <section>
        	<div class="notFound">Bestia no encontrada</div>
        </section>
        <%} %>
        <% if(bestia != null){ 
        	LinkedList<Evidencia> evidencias = bestia.getEvidencias();
        %> 
        	<% if(usuario == null){
        	response.sendRedirect(HttpRoutes.REGISTRO_JSP(request.getContextPath()) + "?id="+bestia.getIdBestia());
        } %>
        <form action="<%= HttpRoutes.ACTUALIZAR_REGISTRO(request.getContextPath()) %>?id=<%= bestia.getIdBestia()%>" method="POST" enctype="multipart/form-data">
        <section class="mainContent">
        <div class="registroContent">
        <section>
                <h1><%= bestia.getNombre() %></h1>
                <% if(registro != null){ %>
                	<input type="hidden" name="bestia" value="<%= bestia.getIdBestia() %>">
	                <article class="entrada">
	                	<h2>Introducción</h2>
	                	<textarea name="introduccion"><%= registro.getIntroduccion() %></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Descripción</h2>
		                <textarea name="descripcion"><%= registro.getDescripcion() %></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Historia</h2>
		                <textarea name="historia"><%= registro.getHistoria() %></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Resumen</h2>
		                <textarea name="resumen"><%= registro.getResumen() %></textarea>
	                </article>
                
                <% }else{ %>
                 	<input type="hidden" name="bestia" value="<%= bestia.getIdBestia() %>">
	                <article class="entrada">
	                	<h2>Introducción</h2>
	                	<textarea name="introduccion" required></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Descripción</h2>
		                <textarea name="descripcion" required></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Historia</h2>
		                <textarea name="historia" required></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Resumen</h2>
		                <textarea name="resumen" required></textarea>
	                </article>
                <% } %>
            </section>
         
            <aside class="infoBestia">
				<label>Ingresar imagen de la bestia (En caso de no haber ninguna se asignara la del registro anterior)</label>
				<input type="file" name="mainPic" accept="image/*" onchange="previsualizarImagen(event)">
				<img id="previewMainPic" src="<%=CloudinaryHelper.getImagenRegistro(registro != null? registro.getMainPic() : null)%>" alt="Vista previa de la imagen" style="display:block; max-width:100%; margin-top:10px; border-radius:8px;">
					<div>
						<h3>Detalles de la bestia</h3>
						<ul>
							<li>Nombre: <%= bestia.getNombre() %>
							</li>
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
							<li><span><%= cat.getNombre() %></span>
							</li>
							<%}} %>
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
			</aside>
        </section>
        </div>
        		<section class="mainContent evidenciasContent">
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
                <button type="submit" class="btnRegistro">Enviar registro</button>
				</section>
				
				<div id="modal" class="modal-container">
					<div class="modal-content">
						<div id="modal-body"></div>
					</div>
				</div>	
				 <script>
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

					document.querySelector('form').addEventListener('submit', function(e) {
						const submitBtn = this.querySelector('button[type=submit]');
						if (submitBtn) {
							submitBtn.disabled = true;
							submitBtn.textContent = 'Enviando registro...';
							submitBtn.style.opacity = '0.7';
						}
					});
				</script>
         </form>
        <% } %>
        <%@ include file="../components/footer.jsp" %>
    </body>
</html>
