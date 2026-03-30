<%@ page import="helpers.HttpRoutes" %>
<style>
	nav a:last-child{
		margin-left: 0;
	}
	nav{
		justify-content: space-between;
	}
</style>
<nav>			
            <a class="navTitle" href="<%= HttpRoutes.HOME_JSP(request.getContextPath()) %>">Bestiario</a>
            <a class="navLink" href="<%= HttpRoutes.BESTIA_FORMS_JSP(request.getContextPath()) %>">Bestias</a>
            <a class="navLink" href="<%= HttpRoutes.CATEGORIA_FORM_JSP(request.getContextPath()) %>">Categorías</a>
            <a class="navLink" href="SvHabitat?action=form">Hábitats</a>
            <a class="navLink" href="SvCaracteristicaHabitat">Características de habitats</a>
            <a class="navLink" href="SvTipoEvidencia">Tipo de evidencias</a>
</nav>