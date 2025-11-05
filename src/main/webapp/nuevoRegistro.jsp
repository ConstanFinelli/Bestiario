<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Registro" %>
<%@ page import="entities.Evidencia" %>
<%@ page import="entities.Categoria" %>
<%@ page import="entities.TipoEvidencia" %>
<!DOCTYPE html>
<html>
    <head>
    	<% 
        	Bestia bestia = (Bestia) request.getAttribute("bestia");
    		Registro registro = (Registro) session.getAttribute("registro");
    		LinkedList<TipoEvidencia> tes = (LinkedList<TipoEvidencia>) request.getAttribute("tes");
        %>  
        <title><%= bestia != null ? bestia.getNombre() : "" %> - Actualización de bestia</title>
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/registro.css">
        <link rel="stylesheet" href="css/navbar.css">
    </head>
    <body>
        <%@ include file="components/navbar.jsp" %>
        <% if(bestia == null){ %>
        <section>
        	<div class="notFound">Bestia no encontrada</div>
        </section>
        <%} %>
        <% if(bestia != null){ 
        	LinkedList<Evidencia> evidencias = bestia.getEvidencias();
        %> 
        <form action="SvBestia?action=actualizacion&id=<%= bestia.getIdBestia()%>" method="POST" enctype="multipart/form-data">
        <section class="mainContent">
        <section>
                <h1><%= bestia.getNombre() %></h1>
                <% if(registro != null){ %>
                	<input type="hidden" name="bestia" value="<%= bestia.getIdBestia() %>">
	                <article class="entrada">
	                	<h2>Introducción</h2>
	                	<textarea name="introduccion"><%= registro.getContenido().getIntroduccion() %></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Descripción</h2>
		                <textarea name="descripcion"><%= registro.getContenido().getDescripcion() %></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Historia</h2>
		                <textarea name="historia"><%= registro.getContenido().getHistoria() %></textarea>
	                </article>
	                <article class="entrada">
		                <h2>Resumen</h2>
		                <textarea name="resumen"><%= registro.getContenido().getResumen() %></textarea>
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
				<input type="file" name="mainPic" accept="image/*">
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
							<li><a href="SvCategoria?action=bestias"><%= cat.getNombre() %></a>
							</li>
							<%}} %>
						</ul>
					</div>
					<div>
						<h3>Hábitats localizados</h3>
						<ul>
							<% LinkedList<Habitat> habitats = bestia.getHabitats(); %>
							<% if(habitats != null){ 
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
        		<section class="mainContent evidenciasContent">
                        <% if(!evidencias.isEmpty()){ %>
                <h2>Evidencias</h2>
                <ul class="evidencias">
                <% for(Evidencia evidencia : evidencias){ %>
                	<%
                	String teDesc = evidencia.getTipo().getDescripcion();
                	String fechaOb = evidencia.getFechaObtencion().toString();
                	String evText = teDesc + ", obtenido el " + fechaOb;
                	
                	%>
                	<li class="evidencias-item">
                		<a href="<%= evidencia.getLink() %>"><%= evText %></a>
                	</li>
                <%} %>
                </ul>
                <%} %>
                <button type="button" id="agregar">Agregar otra evidencia</button>
                <section class="contenedorEvidencias" id="contenedorEvidencias">
				</section>
				<button type="submit" class="btnRegistro">Enviar registro</button>
				</section>
				            <script> // javascript para agregar evidencias
	            let evidenciaCounter = 0;

	            const btnAgregar = document.getElementById('agregar');
	            const contenedor = document.getElementById('contenedorEvidencias');
	
	            btnAgregar.addEventListener('click', function() {
	                agregarNuevaEvidencia();
	            });
	
	            function agregarNuevaEvidencia() {
	               
	                const newArticle = document.createElement('article');
	                newArticle.classList.add('evidenciaForm'); 
	
	                const newH2 = document.createElement('h2');
	                newH2.textContent = 'Nueva evidencia'; 
	
	                const fechaId = 'fechaObtencion-' + evidenciaCounter;
	                
	                const labelFecha = document.createElement('label');
	                labelFecha.setAttribute('for', fechaId); 
	                labelFecha.textContent = 'Fecha de obtención';
	                
	                const inputFecha = document.createElement('input');
	                inputFecha.type = 'date';
	                inputFecha.id = fechaId;
	                inputFecha.name = 'fechaObtencion'; 
	                inputFecha.required = true;
	
	                const tipoId = 'tipo-' + evidenciaCounter;
	                
	                const labelTipo = document.createElement('label');
	                labelTipo.setAttribute('for', tipoId);
	                labelTipo.textContent = 'Tipo';
	
	                const inputTipo = document.createElement('select');
	                inputTipo.id = tipoId;
	                inputTipo.name = 'tipo';
	                inputTipo.required = true;
	                <% if(tes != null){%>
	                const tiposDeEvidencia = [
	                    <% for(TipoEvidencia te : tes) { %>
	                        { 
	                          id: <%= te.getId() %>, 
	                          descripcion: '<%= te.getDescripcion() %>'
	                        },
	                    <% } %>
	                ];
	                for (const te of tiposDeEvidencia) {
				        const option = document.createElement('option');
				        option.value = te.id;
				        option.textContent = te.descripcion; 
				        inputTipo.appendChild(option);
			    	}
	                <%}%>
	                const linkId = 'link-' + evidenciaCounter;
	                
	                const labelLink = document.createElement('label');
	                labelLink.setAttribute('for', linkId);
	                labelLink.textContent = 'Link';
	
	                const inputLink = document.createElement('input');
	                inputLink.type = 'text';
	                inputLink.id = linkId;
	                inputLink.name = 'link'; 
	                inputLink.required = true;
	
	                newArticle.appendChild(newH2);
	                newArticle.appendChild(labelFecha);
	                newArticle.appendChild(inputFecha);
	                newArticle.appendChild(labelTipo);
	                newArticle.appendChild(inputTipo);
	                newArticle.appendChild(labelLink);
	                newArticle.appendChild(inputLink);
	
	                contenedor.appendChild(newArticle);

	                evidenciaCounter++;
	            }
		    </script>
         </form>
        <% } %>
        <footer>
        </footer>
    </body>
</html>