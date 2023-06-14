package com.uca.spring.model;

public class Cliente {
	
	private String documento = "";
	private String nombres = "";
	private String apellidos = "";
	private String numeroTarjeta = "";
	private String tipoTarjeta = "";
	private String telefono = "";
	private String poligono = "";
	
	public Cliente() {
        // Constructor sin argumentos
    }
	
	public Cliente(String documento, String nombres, String apellidos, String numeroTarjeta, String tipoTarjeta,
			String telefono, String poligono) {
		super();
		this.documento = documento;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.numeroTarjeta = numeroTarjeta;
		this.tipoTarjeta = tipoTarjeta;
		this.telefono = telefono;
		this.poligono = poligono;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getTipoTarjeta() {
		return tipoTarjeta;
	}

	public void setTipoTarjeta(String tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getPoligono() {
		return poligono;
	}

	public void setPoligono(String poligono) {
		this.poligono = poligono;
	}
	
	
	
	
	
}
