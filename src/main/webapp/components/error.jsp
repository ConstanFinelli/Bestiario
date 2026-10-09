<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<style>
.swal2-popup {
    border: 2px solid rgb(142, 110, 113);
    background: color(srgb 0.795 0.7327 0.5764);
}

.swal2-confirm {
    background-image: var(--primary-button);
}

.swal2-confirm:hover{
	filter: brightness(0.9);
}

.swal2-confirm:focus-visible{
	box-shadow: none;
}
</style>
<% 
    Object rawError = request.getAttribute("errorGlobal");
    if (rawError == null && session != null) {
        rawError = session.getAttribute("errorGlobal");
        if (rawError != null) {
            session.removeAttribute("errorGlobal");
        }
    }
    if (rawError != null) { 
        String safeError = rawError.toString()
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\"", "\\\"")
            .replace("\r", "")
            .replace("\n", "\\n")
            .replace("<", "&lt;")
            .replace(">", "&gt;");
%>
    <script>
        window.addEventListener('DOMContentLoaded', (event) => {
            Swal.fire({
                icon: 'error',
                title: '¡Ups!',
                html: '<%= safeError %><br>Por favor, intente mas tarde.',
                confirmButtonText: 'Entendido'
            });
        });
    </script>
<% } %>