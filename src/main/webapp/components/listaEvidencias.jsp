<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%
boolean esInvestigador = (usuario != null && "investigador".equals(usuario.getEstado()));
boolean hayEvidenciasVisibles = false;
if (evidencias != null && !evidencias.isEmpty()) {
    for (Evidencia ev : evidencias) {
        if (esInvestigador || "aprobado".equalsIgnoreCase(ev.getEstado())) {
            hayEvidenciasVisibles = true;
            break;
        }
    }
}
if (hayEvidenciasVisibles) {
%>
<h2<%= habilitarPaginacionEvidencias ? " id=\"tituloEvidencias\"" : "" %>>Evidencias</h2>
<ul class="evidencias"<%= habilitarPaginacionEvidencias ? " id=\"listaEvidencias\"" : "" %>>
<%
for (Evidencia evidencia : evidencias) {
    boolean esAprobada = "aprobado".equalsIgnoreCase(evidencia.getEstado());
    if (!esInvestigador && !esAprobada) {
        continue;
    }
    String teDesc = evidencia.getTipo().getDescripcion();
    String fechaOb = evidencia.getFechaObtencion().toString();
    String evText = " Obtenido el " + fechaOb;
%>
    <li class="evidenciasItem">
        <div class="evidenciaInfo">
            <a class="evidenciasLink" href="javascript:void(0)"
            onclick="abrirModalEvidencia('<%= CloudinaryHelper.getUrlEvidencia(evidencia.getTipo().getResourceType(), evidencia.getFileId()) %>')"><span class="tipoEvidenciaText"><%= teDesc %></span><%= evText %></a>
            <% if (esInvestigador && !esAprobada) { %>
                <span class="badgeEstado badgePendiente">Pendiente</span>
            <% } %>
        </div>
        <% if (mostrarAccionesEvidencia && esInvestigador) { %>
            <div class="evAcciones">
                <% if (!esAprobada) { %>
                    <form action="<%= HttpRoutes.APROBAR_EVIDENCIA(request.getContextPath()) %>" method="POST">
                        <input type="hidden" name="nroEvidencia" value="<%= evidencia.getNroEvidencia() %>">
                        <input type="hidden" name="idTipoEvidencia" value="<%= evidencia.getTipo().getId() %>">
                        <input type="hidden" name="idBestia" value="<%= bestia.getIdBestia() %>">
                        <button type="submit" class="evAprobar">Aprobar</button>
                    </form>
                <% } %>
                <button type="button" class="evDelete" onclick="abrirModalForm('ELIMINAR EVIDENCIA', '¿Estás seguro que deseas eliminar esta evidencia?', '<%= HttpRoutes.ELIMINAR_EVIDENCIA(request.getContextPath()) %>', { nroEvidencia: '<%= evidencia.getNroEvidencia() %>', idTipoEvidencia: '<%= evidencia.getTipo().getId() %>', idBestia: '<%= bestia.getIdBestia() %>' })">Eliminar</button>
            </div>
        <% } %>
    </li>
<% } %>
</ul>
<% } %>
