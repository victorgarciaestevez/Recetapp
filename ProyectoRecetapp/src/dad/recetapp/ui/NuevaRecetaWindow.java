package dad.recetapp.ui;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.Spinner;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.RecetAppApplication;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.RecetaItem;
import dad.recetapp.services.items.SeccionItem;

public class NuevaRecetaWindow extends Window implements Bindable {


	@BXML	private PushButton cancelarButton;
	@BXML	private ComponenteReceta componenteReceta;
	@BXML	private TextInput nombreText;
	@BXML	private TextInput paraText;
	@BXML	private ListButton paraListButton;
	@BXML	private static ListButton categoriaListButton;
	@BXML	private Spinner tTotalMSpinner;
	@BXML	private Spinner tTotalSSpinner;
	@BXML	private Spinner tThermoMSpinner;
	@BXML	private Spinner tThermoSSpinner;
	@BXML	private PushButton crearButton;
	
	@BXML public static TabPane pestanaNueva;
	private static List<ComponenteReceta> componentes;

	@SuppressWarnings("unused")
	private RecetAppApplication recetapp;
	@SuppressWarnings("rawtypes")
	private static  org.apache.pivot.collections.List categoriasBD;

	@Override
	public void initialize(Map<String, Object> namespace, URL location,Resources resources) {
		componentes = new ArrayList<ComponenteReceta>();
		crearPestanaComponente();
	
		
		cancelarButton.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button button) {
				onCerrarButtonPressed();
			}
		});

		crearButton.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button button) {
				onCrearButtonPressed();
				
				
			}
		});

		// Cargar combo
		try {
			recargarCategoriaListButton();
		} catch (ServiceException e) {e.printStackTrace();
		}
		pestanaNueva.getComponentMouseButtonListeners().add(new ComponentMouseButtonListener.Adapter() {
			 @Override
			 public boolean mouseClick(Component arg0,org.apache.pivot.wtk.Mouse.Button arg1, int arg2,int arg3, int arg4) {
			 if (pestanaNueva.getSelectedIndex() == pestanaNueva.getTabs().getLength()-1) {
			 crearPestanaComponente();
			 }
			 return false;
			 }
			 });

	}

	protected void onCrearButtonPressed() {
		if(nombreText.getText().equals("")||paraText.getText().equals("")){
			new Prompt("No puede tener campos vacios").open(this.getWindow());
		}else if(categoriaListButton.getSelectedIndex()==0){
			new Prompt("Debe seleccionar una categoria").open(this.getWindow());
		}else if(tThermoMSpinner.getSelectedIndex()==0|| tTotalMSpinner.getSelectedIndex()==0){
			new Prompt("No puede introducir recetas sin tiempo").open(this.getWindow());
		}else{
		int minutosTher = tThermoMSpinner.getSelectedIndex() * 60;
		int segundosTher = tThermoSSpinner.getSelectedIndex() + minutosTher;
		int minutosTot = tTotalMSpinner.getSelectedIndex() * 60;
		int segundosTot = tTotalSSpinner.getSelectedIndex() + minutosTot;

		RecetaItem recetaNueva = new RecetaItem();
		recetaNueva.setNombre(nombreText.getText());
		recetaNueva.setCantidad(Integer.parseInt(paraText.getText()));
		recetaNueva.setPara(String.valueOf(paraListButton.getSelectedItem()));
		recetaNueva.setCategoria((CategoriaItem) categoriaListButton.getSelectedItem());
		recetaNueva.setTiempoThermomix(segundosTher);
		recetaNueva.setTiempoTotal(segundosTot);
		
		for (ComponenteReceta componente : componentes) {
			 if(!componente.getSeccion().getNombre().equals("")){
			 recetaNueva.getSecciones().add(componente.getSeccion());
			 }
			 }
		
		try {
			ServiceLocator.getRecetasService().crearReceta(recetaNueva);
			
			onCerrarButtonPressed();
		} catch (ServiceException e) {e.printStackTrace();}
		}
		
	}

	protected void onCerrarButtonPressed() {
		close();
		RecetasPane.initRecetasView();
	}

	public void setRecetapp(RecetAppApplication recetapp) {
		this.recetapp = recetapp;
		componenteReceta.setWindowsApp(recetapp);
	}

	// cargar combo
	@SuppressWarnings("unchecked")
	public static void recargarCategoriaListButton() throws ServiceException {
		CategoriaItem categoriaTitle = new CategoriaItem();
		categoriaTitle.setId(null);
		categoriaTitle.setDescripcion("<Seleccione una categoría>");
		categoriasBD = convertirList(ServiceLocator.getCategoriasService().listaCategoria());
		categoriasBD.insert(categoriaTitle, 0);
		categoriaListButton.setListData(categoriasBD);
		categoriaListButton.setSelectedItem(categoriaTitle);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static org.apache.pivot.collections.List convertirList(
			java.util.List<?> listaUtil) {
		org.apache.pivot.collections.List listaApache = new org.apache.pivot.collections.ArrayList();
		for (int i = 0; i < listaUtil.size(); i++) {
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}
	protected void crearPestanaComponente() {
		 try {
		 URL bxmlUrl = getClass().getResource("ComponenteReceta.bxml");
		 BXMLSerializer serializer = new BXMLSerializer();
		 ComponenteReceta componenteReceta = (ComponenteReceta) serializer.readObject(bxmlUrl);
		 componenteReceta.setParent(this);
		 pestanaNueva.getTabs().insert(componenteReceta,pestanaNueva.getLength() - 2);
		 pestanaNueva.setSelectedTab(componenteReceta);
		 componentes.add(componenteReceta);

		 } catch (IOException | SerializationException e) {
		 e.printStackTrace();
		 }
		 }
	
	public void cambiarNombrePestana(String titulo) {
		pestanaNueva.setTabData(pestanaNueva.getSelectedTab(),titulo);
		 }
	
	public static void eliminarPestana() {
		 if (pestanaNueva.getTabs().getLength() > 0) {
		 int posicion = pestanaNueva.getSelectedIndex();
		 if (posicion == 0) {
			 pestanaNueva.setSelectedIndex(posicion+1);
		 } else {
			 pestanaNueva.setSelectedIndex(posicion-1);
		 }
		 componentes.remove(posicion);
		 pestanaNueva.getTabs().remove(posicion, 1);
		 } else if (pestanaNueva.getTabs().getLength()==2) {

			 pestanaNueva.setSelectedIndex(pestanaNueva.getTabs().getLength()-1);
			 pestanaNueva.getTabs().remove(pestanaNueva.getSelectedIndex()-1, 1);
		 componentes.remove(pestanaNueva.getSelectedIndex()-1);
		 }
		 }


}
