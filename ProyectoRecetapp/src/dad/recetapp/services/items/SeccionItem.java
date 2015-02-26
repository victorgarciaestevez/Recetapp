package dad.recetapp.services.items;

import java.util.ArrayList;
import java.util.List;

public class SeccionItem {
	private Long id;
	private String nombre;
	private List<IngredienteItem> ingredientes = new ArrayList<IngredienteItem>();
	private List<InstruccionItem> instrucciones = new ArrayList<InstruccionItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<IngredienteItem> getIngredientes() {
		return ingredientes;
	}

	public List<InstruccionItem> getInstrucciones() {
		if(instrucciones == null){
			instrucciones= new ArrayList<InstruccionItem>();
		}
		return instrucciones;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof SeccionItem){
			SeccionItem tipo = (SeccionItem) obj;
			return tipo.getId() == this.id;
		}
		return false;
	}
	public void setIngredientes(List<IngredienteItem> ingredientes) {
		 this.ingredientes = ingredientes;
		 }
	
	public void setInstrucciones(List<InstruccionItem> instrucciones) {
		 this.instrucciones = instrucciones;
		 }
	
	@Override
	public String toString(){
		return nombre;
	}

}
