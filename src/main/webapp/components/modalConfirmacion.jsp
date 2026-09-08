<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="modalConfirmacion" class="modal-container">
	<div class="modal-content">
		<h2 id="modal-titulo"></h2>
		<div id="modal-cuerpo"></div>
		<div class="modalButtons">
			<button type="button" class="closeButton" onclick="cerrarModalConfirmacion()">Volver</button>
			<a id="modalLinkConfirmar" href="#" class="deleteButton" style="display:none;" onclick="cerrarModalConfirmacion()">Eliminar</a>
			<form id="modalFormConfirmar" method="POST" style="display:none;">
				<input type="hidden" name="id" id="modalInputId">
				<button type="submit" class="deleteButton" onclick="cerrarModalConfirmacion()">Eliminar</button>
			</form>
		</div>
	</div>
</div>
<script>
	function abrirModalLink(titulo, mensaje, urlDestino) {
		document.getElementById('modal-titulo').innerText = titulo;
		document.getElementById('modal-cuerpo').innerHTML = mensaje;
		var linkBtn = document.getElementById('modalLinkConfirmar');
		var formBtn = document.getElementById('modalFormConfirmar');
		
		linkBtn.href = urlDestino;
		linkBtn.style.display = 'inline-block';
		formBtn.style.display = 'none';
		document.getElementById('modalConfirmacion').classList.add('is-visible');
	}

	function abrirModalForm(titulo, mensaje, actionUrl, paramName, paramValue) {
		document.getElementById('modal-titulo').innerText = titulo;
		document.getElementById('modal-cuerpo').innerHTML = mensaje;
		var linkBtn = document.getElementById('modalLinkConfirmar');
		var formBtn = document.getElementById('modalFormConfirmar');
		var input = document.getElementById('modalInputId');
		
		formBtn.action = actionUrl;
		input.name = paramName || 'id';
		input.value = paramValue || '';
		
		formBtn.style.display = 'inline';
		linkBtn.style.display = 'none';
		document.getElementById('modalConfirmacion').classList.add('is-visible');
	}

	function cerrarModalConfirmacion() {
		document.getElementById('modalConfirmacion').classList.remove('is-visible');
	}

	window.addEventListener('click', function(event) {
		var modal = document.getElementById('modalConfirmacion');
		if (event.target === modal) {
			cerrarModalConfirmacion();
		}
	});
</script>
