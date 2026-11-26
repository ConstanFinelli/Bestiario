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
        	return;
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
                <%
                boolean mostrarAccionesEvidencia = false;
                boolean habilitarPaginacionEvidencias = false;
                %>
                <%@ include file="../components/listaEvidencias.jsp" %>
                <button type="submit" class="btnRegistro">Enviar registro</button>
				</section>
				
				 <script>
				    function previsualizarImagen(event) {
				        const input = event.target;
				        const preview = document.getElementById('previewMainPic');

				        if (input.files && input.files[0]) {
				            const reader = new FileReader();
				            reader.onload = function(e) {
				                preview.src = e.target.result;
				                preview.style.display = 'block';
				            };
				            reader.readAsDataURL(input.files[0]);
				        } else {
				            preview.src = '';
				            preview.style.display = 'none';
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
        <%@ include file="../components/modalEvidencia.jsp" %>
    </body>
</html>
