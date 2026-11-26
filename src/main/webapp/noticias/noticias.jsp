<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entities.Noticia" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<% 
	LinkedList<Noticia> noticias = (LinkedList<Noticia>) request.getAttribute("noticias");
	if (noticias == null) {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.LISTAR_NOTICIAS(""));
		rd.forward(request, response);
		return;
	}
%>
<html>
    <head>
        <title>Bestiario - Noticias</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
		<link rel="stylesheet" href="<%= HttpRoutes.NOTICIAS_CSS(request.getContextPath()) %>">
		<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath()) %>">
    </head>
    <body>
        <%@ include file="../components/navbar.jsp" %>
        <section class="mainContent">
			<h2>NOTICIAS</h2>
			<% if (noticias.isEmpty()) { %>
				<div class="notFound">
					No hay noticias cargadas por el momento.
				</div>
			<%} %>
		<%
		if (usuario != null) {
			if (usuario.getEstado().equals("investigador")) {
		%>
		<a class="btnAgregar"
			href="<%=HttpRoutes.REDACTARNOTICIA_JSP(request.getContextPath())%>">
			+ Redactar nueva noticia</a>
		<%
		}
		}
		%>
		<div class="newsContainer">
				<%if(noticias != null){ %>
					<% for(Noticia noticia : noticias){
						boolean esAutorNoticia = (usuario != null && noticia.getPublicador() != null && usuario.getIdUsuario() == noticia.getPublicador().getIdUsuario());
						boolean esInvestigadorNoticia = (usuario != null && "investigador".equals(usuario.getEstado()));
					%>
							<article class="new">
								<div class="newContainer">
									<h2 class="newTitle"><%= noticia.getTitulo() %></h2>
									<span class="newInfo">escrito por 
											<%=noticia.getPublicador().getNombre() %> <%=noticia.getPublicador().getApellido() %> 
											- publicado <%= noticia.getFechaPublicacion() %></span>
									<p class="newContent">
										<%= noticia.getContenido() %>
									</p>
									<% if (esAutorNoticia) { %>
										<form class="noticiaEditForm" action="<%= HttpRoutes.EDITAR_NOTICIA(request.getContextPath()) %>" method="post" style="display: none;">
											<input type="hidden" name="idNoticia" value="<%= noticia.getId() %>">
											<input type="text" name="titulo" class="inputEditarTituloNoticia" value="<%= noticia.getTitulo().replace("\"", "&quot;") %>" required>
											<textarea name="contenido" class="textareaEditarNoticia" rows="5" required><%= noticia.getContenido() %></textarea>
											<div class="noticiaEditAcciones">
												<button type="submit" class="btnGuardarNoticia">Guardar</button>
												<button type="button" class="btnCancelarEdicionNoticia" onclick="cancelarEdicionNoticia(this)">Cancelar</button>
											</div>
										</form>
									<% } %>
									<% if (esAutorNoticia || esInvestigadorNoticia) { %>
										<div class="noticiaAcciones">
											<% if (esAutorNoticia) { %>
												<button type="button" class="btnEditarNoticia" onclick="abrirEdicionNoticia(this)">Editar</button>
											<% } %>
											<% if (esInvestigadorNoticia) { %>
												<a href="javascript:void(0)" 
												   data-id="<%= noticia.getId()%>" 
												   data-titulo="<%= noticia.getTitulo()%>" 
												   onclick="abrirModalLink('ELIMINAR NOTICIA', '¿Estas seguro que deseas eliminar la noticia ' + this.getAttribute('data-titulo') + '?', '<%= HttpRoutes.ELIMINAR_NOTICIA(request.getContextPath()) %>?idNoticia=' + this.getAttribute('data-id'))" 
												   class="btnEliminarNoticia">Eliminar</a>
											<% } %>
										</div>
									<% } %>
								</div>
							</article>
					<%} %>
				<%} %>
				</div>
		</section>
        <%@ include file="../components/footer.jsp" %>
        <%@ include file="../components/modalConfirmacion.jsp" %>
        <script>
            function abrirEdicionNoticia(btn) {
                const card = btn.closest('.newContainer');
                if (!card) return;
                const title = card.querySelector('.newTitle');
                const info = card.querySelector('.newInfo');
                const content = card.querySelector('.newContent');
                const acciones = card.querySelector('.noticiaAcciones');
                const form = card.querySelector('.noticiaEditForm');
                const inputTitulo = card.querySelector('.inputEditarTituloNoticia');
                if (form) {
                    if (title) title.style.display = 'none';
                    if (info) info.style.display = 'none';
                    if (content) content.style.display = 'none';
                    if (acciones) acciones.style.display = 'none';
                    form.style.display = 'flex';
                    if (inputTitulo) inputTitulo.focus();
                }
            }

            function cancelarEdicionNoticia(btn) {
                const card = btn.closest('.newContainer');
                if (!card) return;
                const title = card.querySelector('.newTitle');
                const info = card.querySelector('.newInfo');
                const content = card.querySelector('.newContent');
                const acciones = card.querySelector('.noticiaAcciones');
                const form = card.querySelector('.noticiaEditForm');
                if (form) {
                    form.style.display = 'none';
                    if (title) title.style.display = '';
                    if (info) info.style.display = '';
                    if (content) content.style.display = '';
                    if (acciones) acciones.style.display = 'flex';
                }
            }
        </script>
    </body>
</html>