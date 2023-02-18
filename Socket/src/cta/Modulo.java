package cta;

import java.io.Serializable;

public class Modulo implements Serializable {
	String nombre;
	String descripcion;
	String mask;
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getMask() {
		return mask;
	}
	public void setMask(String mask) {
		this.mask = mask;
	}
	public String toString() {
		return this.getDescripcion()+'('+this.getMask()+')';
		
	}
	
}
