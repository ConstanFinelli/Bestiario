<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="logic.LogicRegistro" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Registro" %>
<%@ page import="entities.Categoria" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="helpers.HttpRoutes" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Bestias</title>
<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.BESTIAS_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath()) %>">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.css" />
<script
	src="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.js"></script>
</head>
<body>
<%@ include file="../../components/navbar.jsp" %>	
<section class="mainContent">
<h1>Bestias registradas</h1>
	<% 
		String searchedFilter = (String) request.getAttribute("searchedFilter");
	
		if (searchedFilter == null) { searchedFilter = "";}
	 %>
	<form action="<%= HttpRoutes.LISTAR_BESTIAS(request.getContextPath()) %>" method="get">
		<input type="hidden" name="action" value="list">
    	
    	<input placeholder="Ingresar categoría..." name="filter" class="inputFilter" type="search" id="filter" value=<%= searchedFilter %>>
    	
    	<button class="btnBestia" type="submit">Buscar</button>
	</form>
	
	<%
    	String errorMsg = (String) session.getAttribute("errorMsg");
    	if (errorMsg != null) {
	%>
    <div class="notFound"><%= errorMsg %></div>
	<%
        session.removeAttribute("errorMsg"); // para que no se repita
    	}
	%>


		<section class="slides">
			<%
			boolean verTodo = false;
			if (usuario != null) {
				if (usuario.getEstado().equals("investigador")) {
					verTodo = true;
				}
			}
			LinkedList<Bestia> bestias = (LinkedList<Bestia>) request.getAttribute("bestias");
			LogicRegistro controladorRegistro = new LogicRegistro();
			if (bestias != null) {
			%>
			<div class="swiper mySwiper">
				<div class="swiper-wrapper">
					<%
					for (Bestia bestia : bestias) {
						if (bestia.getEstado().equals("aprobado") || verTodo == true) {
					%>
					<article class="bestia swiper-slide">
						<h1><%=bestia.getNombre()%></h1>
						<% Registro ultRegistro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
							String imagen = null;
							if(ultRegistro != null){
								imagen = ultRegistro.getMainPic(); 
							}
						%>
						<img src="<%= HttpRoutes.IMAGENES(request.getContextPath()) %>?file=<%=imagen != null ? imagen : "default.png"%>" alt="Imagen de: <%=bestia.getNombre()%>">
						<div class="overlay-buttons">
							<a class="btnBestia"
								href="<%= HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) %>?id=<%=bestia.getIdBestia()%>">Examinar</a>
							<%
							if (usuario != null && usuario.getEstado().equals("investigador")) {
							%>
							<a class="btnBestia"
								href="<%= HttpRoutes.OBTENER_REGISTROS_PENDIENTES_BESTIA(request.getContextPath()) %>&id=<%=bestia.getIdBestia()%>">Ver
								Reg. Pendientes</a>
							<%}%>
							<%
							if (bestia.getEstado().equals("pendiente")) {
							%>
							<form action="<%= HttpRoutes.ACTUALIZAR_BESTIA(request.getContextPath()) %>" method="post" style="display: inline;">
								<input type="hidden" name="flag" value="put"> <input
									type="hidden" name="id" value="<%=bestia.getIdBestia()%>">
								<input type="hidden" name="nombre"
									value="<%=bestia.getNombre()%>"> <input type="hidden"
									name="peligrosidad" value="<%=bestia.getPeligrosidad()%>">
								<input type="hidden" name="estado" value="aprobado">
								<button type="submit" class="btnBestia">Aprobar Bestia</button>
							</form>
							<%
							}
							if (verTodo == true) {
							%>
							<button type="button" class="btnEliminar"
								onClick="abrirModal('<%=bestia.getIdBestia()%>', '<%=bestia.getNombre()%>')">Eliminar</button>
									<%
						}
						%>
						</div>
					</article>
					<%}
						} %>
				</div>
					<div class="swiper-button-next"></div>
					<div class="swiper-button-prev"></div>
			</div>
			<%
			} else {
			%>

			<section>
				<div class="notFound">No se pudieron encontrar Bestias.</div>
			</section>
			<%
			}
			%>


		</section>
		<%
		if (usuario != null) {
		%>
        <a class="btnAgregar" href="<%= HttpRoutes.CREAR_PROPUESTA_BESTIA_JSP(request.getContextPath())%>"> + Proponer Nueva Bestia</a>
        <%
        }
        %>
</section>
<footer>
</footer>
	<div id="modal" class="modal-container">
		<div class="modal-content">
			<h2 id="modal-titulo"></h2>
			<div id="modal-cuerpo"></div>
			<div class="modalButtons">
				<form action="<%= HttpRoutes.ELIMINAR_BESTIA(request.getContextPath()) %>>" method="post" style="display:inline;">
					<input type="hidden" name="flag" value="delete">
					<input type="hidden" name="id" id="idAEliminar">
					<button type="button" class="closeButton" onclick="cerrarModal()">Volver</button>
					<button type="submit" class="deleteButton" onclick="cerrarModal()">Eliminar</button>
				</form>
			</div>
		</div>
	</div>
	<script>
		// javascript para modal
		function abrirModal(id, nombre) {
			document.getElementById('modal-titulo').innerText = "ELIMINAR BESTIA";
			document.getElementById('modal-cuerpo').innerHTML = '¿Estas seguro que deseas eliminar a la bestia '
					+ nombre + '?';
			document.getElementById('idAEliminar').value = id;
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
	<script>
		var swiper = new Swiper(".mySwiper", {
			effect : "coverflow",
			grabCursor : true,
			centeredSlides : true,
			slidesPerView : "auto",
			loop : false,
			observer: true,
		    observeParents: true,
			loopPreventsSliding : false,
			coverflowEffect : {
				rotate : 30,
				stretch : 0,
				depth : 150,
				modifier : 1,
				slideShadows : true,
			},
			slideToClickedSlide : true,
			navigation : {
				nextEl : '.swiper-button-next',
				prevEl : '.swiper-button-prev',
			},
		});
	</script>
</body>
</html>