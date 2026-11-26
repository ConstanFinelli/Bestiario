<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<div id="modal" class="modal-container">
  <div class="modal-content">
    <div id="modal-body"></div>
  </div>
</div>
<script>
  function abrirModalEvidencia(link) {
    if (!link) {
      alert("Esta evidencia no tiene un archivo multimedia asociado.");
      return;
    }

    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = "";
    const esVideo = link.toLowerCase().match(/\.(mp4|webm|ogg)$/) || link.includes("video/upload");
    const esImagen = link.toLowerCase().match(/\.(jpg|jpeg|png|gif|webp|svg)$/) || link.includes("image/upload");

    if (esVideo) {
      const video = document.createElement('video');
      video.src = link;
      video.controls = true;
      video.autoplay = true;
      video.classList.add('modal-evidencia');
      modalBody.appendChild(video);
    } else if (esImagen) {
      const img = document.createElement('img');
      img.src = link;
      img.classList.add('modal-evidencia');
      modalBody.appendChild(img);
    } else {
      const descarga = document.createElement('div');
      descarga.style.textAlign = "center";
      descarga.style.padding = "20px";

      const p = document.createElement('p');
      p.textContent = "El archivo no se puede previsualizar en el navegador.";
      p.style.marginBottom = "15px";

      const a = document.createElement('a');
      a.href = link;
      a.download = "";
      a.target = "_blank";
      a.textContent = "Haz click para descargar el archivo";
      a.className = "modal-download";

      a.style.display = "inline-block";
      a.style.padding = "10px 20px";
      a.style.background = "#2563eb";
      a.style.color = "white";
      a.style.textDecoration = "none";
      a.style.borderRadius = "5px";

      descarga.appendChild(p);
      descarga.appendChild(a);
      modalBody.appendChild(descarga);
    }

    document.getElementById('modal').classList.add('is-visible');
  }

  function cerrarModalEvidencia() {
    document.getElementById('modal').classList.remove('is-visible');
  }

  window.addEventListener('click', function (event) {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
      cerrarModalEvidencia();
    }
  });
</script>
