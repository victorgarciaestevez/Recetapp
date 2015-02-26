package dad.recetapp;

import java.util.List;

import javax.swing.JOptionPane;

import dad.recetapp.db.DataBase;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;

public class MainP {
public static void main (String[] args){
	//prueba de coneccion
	System.out.println("Prueba de conexión " + DataBase.test());
	
	//codigo para añadir categoriar al pulsar botton añadir
	CategoriaItem categoria = new CategoriaItem();
	categoria.setDescripcion("Pescados");	
	try {
		@SuppressWarnings("unused")
		List<CategoriaItem> lista = ServiceLocator.getCategoriasService().listaCategoria();
		ServiceLocator.getCategoriasService().crearCategoria(categoria);
	} catch (ServiceException e) {
		
		JOptionPane.showMessageDialog(null, e.getMessage()+ "/n/nDetalles", "Error", JOptionPane.ERROR);
		e.printStackTrace();
	}
	
}
}
