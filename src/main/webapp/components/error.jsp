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
<% if (request.getAttribute("errorGlobal") != null) { %>
    <script>
        window.addEventListener('DOMContentLoaded', (event) => {
            Swal.fire({
                icon: 'error',
                title: '¡Ups!',
                text: '<%= request.getAttribute("errorGlobal") %>',
                confirmButtonText: 'Entendido'
            });
        });
    </script>
<% } %>