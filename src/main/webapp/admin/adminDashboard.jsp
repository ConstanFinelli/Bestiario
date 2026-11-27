<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Bestiario - Admin Dashboard</title>
	<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath())%>">
	<link rel="stylesheet" href="<%= HttpRoutes.ADMINDASHBOARD_CSS(request.getContextPath())%>">
    <link rel="stylesheet" href=<%= HttpRoutes.NAVBAR_CSS(request.getContextPath())%>>
</head>
<body>
	<%@ include file="../../components/navbar.jsp" %>
	<div class="adminLayout">
		<aside class="dashboardMenu">
			<h2>Menu de Administrador</h2>
			<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>?crud=categorias" class="dashboardLink" id="catLink">Categorías</a>
			<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>?crud=habitats" class="dashboardLink" id="habLink">Habitats</a>
			<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>?crud=usuarios" class="dashboardLink" id="usLink">Usuarios</a>
			<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>?crud=tiposEvidencia" class="dashboardLink" id="teLink">Tipos de evidencia</a>
		</aside>
		<main class="adminContent">
			<% String crud = request.getParameter("crud"); %>
			<% if(crud == null){%>
				<h2>Bienvenido al menú de Admin</h2>
				<p>Seleccione una opción para gestionar en el menú de la izquierda.</p>
			<%} else if ("categorias".equals(crud)){ %>
				<%@ include file="adminCategorias.jsp" %>
				<script type="text/javascript">document.getElementById("catLink").classList.add("activeLink")</script>
			<%} else if ("usuarios".equals(crud)){ %>
				<%@ include file="adminUsuarios.jsp" %>
				<script type="text/javascript">document.getElementById("usLink").classList.add("activeLink")</script>
			<%} else if ("habitats".equals(crud)){ %>
				<%@ include file="adminHabitats.jsp" %>
				<script type="text/javascript">document.getElementById("habLink").classList.add("activeLink")</script>
			<%} else if ("carHabitat".equals(crud)){%>
				<%@ include file="adminCarHabitats.jsp" %>
				<script type="text/javascript">document.getElementById("habLink").classList.add("activeLink")</script>
			<%} else if ("tiposEvidencia".equals(crud)){%>
				<%@ include file="adminTipoEvidencia.jsp" %>
				<script type="text/javascript">document.getElementById("teLink").classList.add("activeLink")</script>
			<%} %>
		</main>
	</div>
	<%@ include file="../components/footer.jsp" %>
	<%@ include file="../components/modalConfirmacion.jsp" %>
	<script type="text/javascript">
		function inicializarPaginacionAdmin() {
			const listas = document.querySelectorAll('.adminList');
			listas.forEach(function(lista) {
				const items = Array.from(lista.querySelectorAll(':scope > .listItem'));
				const itemsPorPagina = parseInt(lista.dataset.itemsPerPage, 10) || 8;
				const etiqueta = lista.dataset.entityLabel || 'elementos';
				const totalItems = items.length;
				const totalPaginas = Math.ceil(totalItems / itemsPorPagina);

				if (totalPaginas <= 1) {
					return;
				}

				const contenedor = document.createElement('div');
				contenedor.className = 'paginacionAdmin';

				const info = document.createElement('span');
				info.className = 'paginacionInfo';

				const controles = document.createElement('div');
				controles.className = 'paginacionControles';

				const btnPrev = document.createElement('button');
				btnPrev.type = 'button';
				btnPrev.className = 'btnPaginacion';
				btnPrev.title = 'Página anterior';
				btnPrev.innerHTML = '&laquo; Anterior';

				const contenedorNumeros = document.createElement('div');
				contenedorNumeros.className = 'paginacionNumeros';

				const btnNext = document.createElement('button');
				btnNext.type = 'button';
				btnNext.className = 'btnPaginacion';
				btnNext.title = 'Página siguiente';
				btnNext.innerHTML = 'Siguiente &raquo;';

				controles.appendChild(btnPrev);
				controles.appendChild(contenedorNumeros);
				controles.appendChild(btnNext);
				contenedor.appendChild(info);
				contenedor.appendChild(controles);

				lista.insertAdjacentElement('afterend', contenedor);

				let paginaActual = 1;

				function irAPagina(pagina, scroll) {
					if (pagina < 1) pagina = 1;
					if (pagina > totalPaginas) pagina = totalPaginas;
					paginaActual = pagina;

					const inicio = (paginaActual - 1) * itemsPorPagina;
					const fin = inicio + itemsPorPagina;

					items.forEach(function(item, idx) {
						if (idx >= inicio && idx < fin) {
							item.style.display = 'flex';
						} else {
							item.style.display = 'none';
						}
					});

					const finMostrado = Math.min(fin, totalItems);
					info.textContent = 'Mostrando ' + (inicio + 1) + ' - ' + finMostrado + ' de ' + totalItems + ' ' + etiqueta + ' (Página ' + paginaActual + ' de ' + totalPaginas + ')';

					btnPrev.disabled = (paginaActual === 1);
					btnNext.disabled = (paginaActual === totalPaginas);

					contenedorNumeros.innerHTML = '';

					function crearBtnNum(num) {
						const btnNum = document.createElement('button');
						btnNum.type = 'button';
						btnNum.className = 'btnPaginacionNum' + (num === paginaActual ? ' activa' : '');
						btnNum.textContent = num;
						btnNum.setAttribute('aria-label', 'Página ' + num);
						if (num === paginaActual) {
							btnNum.setAttribute('aria-current', 'page');
						} else {
							btnNum.addEventListener('click', function() {
								irAPagina(num, true);
							});
						}
						return btnNum;
					}

					function crearEllipsis() {
						const span = document.createElement('span');
						span.className = 'paginacionEllipsis';
						span.textContent = '...';
						return span;
					}

					if (totalPaginas <= 7) {
						for (let i = 1; i <= totalPaginas; i++) {
							contenedorNumeros.appendChild(crearBtnNum(i));
						}
					} else {
						contenedorNumeros.appendChild(crearBtnNum(1));
						if (paginaActual > 3) {
							contenedorNumeros.appendChild(crearEllipsis());
						}
						const start = Math.max(2, paginaActual - 1);
						const end = Math.min(totalPaginas - 1, paginaActual + 1);
						for (let i = start; i <= end; i++) {
							contenedorNumeros.appendChild(crearBtnNum(i));
						}
						if (paginaActual < totalPaginas - 2) {
							contenedorNumeros.appendChild(crearEllipsis());
						}
						contenedorNumeros.appendChild(crearBtnNum(totalPaginas));
					}

					if (scroll) {
						const seccion = lista.closest('section.formAdmin') || lista;
						seccion.scrollIntoView({ behavior: 'smooth', block: 'start' });
					}
				}

				btnPrev.addEventListener('click', function() {
					if (paginaActual > 1) irAPagina(paginaActual - 1, true);
				});

				btnNext.addEventListener('click', function() {
					if (paginaActual < totalPaginas) irAPagina(paginaActual + 1, true);
				});

				irAPagina(1, false);
			});
		}

		if (document.readyState === 'loading') {
			document.addEventListener('DOMContentLoaded', inicializarPaginacionAdmin);
		} else {
			inicializarPaginacionAdmin();
		}
	</script>
</body>
</html>