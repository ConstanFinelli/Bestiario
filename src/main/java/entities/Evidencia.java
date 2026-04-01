package entities;

import java.time.LocalDateTime;

public class Evidencia {
	private int nroEvidencia;
	private LocalDateTime fechaObtencion;
	private String estado;
	private String fileId;
	private TipoEvidencia tipo;
	
	public Evidencia(int nroEvidencia, LocalDateTime fechaObtencion, String estado, String fileId, TipoEvidencia tip) {
		this.nroEvidencia = nroEvidencia;
		this.fechaObtencion = fechaObtencion;
		this.estado = estado;
		this.fileId = fileId;
		this.tipo = tip;
	}
	
	public Evidencia(int nroEvidencia, TipoEvidencia tip) {
		this.nroEvidencia = nroEvidencia;
		this.tipo = tip;
	}
	
	public int getNroEvidencia() {
		return nroEvidencia;
	}
	public void setNroEvidencia(int nroEvidencia) {
		this.nroEvidencia = nroEvidencia;
	}
	public LocalDateTime getFechaObtencion() {
		return fechaObtencion;
	}
	public void setFechaObtencion(LocalDateTime fechaObtencion) {
		this.fechaObtencion = fechaObtencion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public TipoEvidencia getTipo() {
		return tipo;
	}
	public void setTipo(TipoEvidencia tipo) {
		this.tipo = tipo;
	}
	
}
