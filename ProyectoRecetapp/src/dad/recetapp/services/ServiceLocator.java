package dad.recetapp.services;

import dad.recetapp.services.impl.CategoriasService;
import dad.recetapp.services.impl.MedidasService;
import dad.recetapp.services.impl.RecetasService;
import dad.recetapp.services.impl.TipoAnotacionService;
import dad.recetapp.services.impl.TipoIngredienteService;

public class ServiceLocator {

	private static final ICategoriasService categoriasService = new CategoriasService();
	private static final ITiposAnotacionesService anotacionesService = new TipoAnotacionService();
	private static final ITiposIngredientesService ingredientesService = new TipoIngredienteService();
	private static final IMedidasService medidasService = new MedidasService();
	private static final IRecetasService recetasService = new RecetasService();
	

	private ServiceLocator() {
	}

	public static ICategoriasService getCategoriasService() {
		return categoriasService;
	}
	
	public static ITiposAnotacionesService getAnotacionesService() {
		return anotacionesService;
	}
	
	public static ITiposIngredientesService getIngredientesService(){
		return ingredientesService;
	}
	
	public static IMedidasService getMedidasService(){
		return medidasService;
	}
	
	public static IRecetasService getRecetasService(){
		return recetasService;
	}

}
