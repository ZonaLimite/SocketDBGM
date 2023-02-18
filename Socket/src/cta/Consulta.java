package cta;

import java.io.Serializable;
import java.util.Vector;

//Una consulta es un vector de modulos + filtro y su nombre de consulta
public class Consulta implements Serializable{
 String sistemaConsulta;
 String nameConsulta;
 Vector<Modulo> modulosActivos = new Vector<Modulo>();
 String filtro;//filtro de texto del tipo "cadena&cadenaotra|otracadena&cotraadenaotra"
 
	public String getSistemaConsulta() {
			return sistemaConsulta;
	}
	public void setSistemaConsulta(String sistemaConsulta) {
			this.sistemaConsulta = sistemaConsulta;
	}
	
	public String getNameConsulta() {
		return nameConsulta;
	}
	public void setNameConsulta(String nameConsulta) {
		this.nameConsulta = nameConsulta;
	}
	public String getFiltro() {
		return filtro;
	}
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	
	 public Vector<Modulo> getModulosActivos() {
		return modulosActivos;
	}
	public void setModulosActivos(Vector<Modulo> modulos) {
		this.modulosActivos = modulos;
	}
	
	 public void insertarModuloActivo(Modulo mod) {
		 this.getModulosActivos().add(mod);
	 }
	 public String toString() {
		 return this.getNameConsulta();
	 }
}
