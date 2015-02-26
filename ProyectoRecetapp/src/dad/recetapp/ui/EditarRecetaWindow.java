package dad.recetapp.ui;

import java.net.URL;
import java.util.List;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Spinner;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.RecetAppApplication;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.RecetaItem;
import dad.recetapp.services.items.RecetaListItem;

public class EditarRecetaWindow extends Window implements Bindable {
	

	@BXML private PushButton cancelarButton;
	@BXML private TextInput nombreText;
	@BXML private Spinner tTotalMSpinner;
	@BXML private Spinner tTotalSSpinner;
	@BXML private Spinner tThermoMSpinner;
	@BXML private Spinner tThermoSSpinner;
	@BXML private TextInput cantidadText;
	@BXML private ListButton categoriaListButton;
	private RecetAppApplication recetapp;
	 private RecetaItem recetacombo = null;
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		
		cancelarButton.getButtonPressListeners().add(new ButtonPressListener() {	
			public void buttonPressed(Button button) {
				 onCerrarButtonPressed();		
			}
		});
	}

	protected void onCerrarButtonPressed() {		
		close();
	}
	
	public void setRecetapp(RecetAppApplication recetapp) {
		this.recetapp = recetapp;
		
		cargarReceta();
		cargarCombo();
	}
	
	public void cargarReceta(){		
		
		RecetaListItem recetaeditar = (RecetaListItem) recetapp.getVentanaPrincipalWindow().getRecetasPane().getRecetasView().getSelectedRow();
		try {
			recetacombo=ServiceLocator.getRecetasService().obtenerReceta(recetaeditar.getId());
			
		} catch (ServiceException e) {e.printStackTrace();}
		
		
		tTotalMSpinner.setSelectedIndex(recetaeditar.getTiempoTotal()/60);
		tTotalSSpinner.setSelectedIndex(recetaeditar.getTiempoTotal()%60);
		tThermoMSpinner.setSelectedIndex(recetaeditar.getTiempoThermomix()/60);
		tThermoSSpinner.setSelectedIndex(recetaeditar.getTiempoThermomix()%60);
		cantidadText.setText(String.valueOf(recetaeditar.getCantidad()));
		nombreText.setText(recetaeditar.getNombre());
		
	}
	
	public void cargarCombo(){
		try {
			List<CategoriaItem> arrayUtil = ServiceLocator.getCategoriasService().listaCategoria();
			org.apache.pivot.collections.List<CategoriaItem> listaApache = convertirListcategorias(arrayUtil);
			CategoriaItem categoriaTitle = new CategoriaItem();
			categoriaTitle.setId(null);
			categoriaTitle.setDescripcion(recetacombo.getCategoria().getDescripcion());
			listaApache.insert(categoriaTitle, 0);
			categoriaListButton.setListData(listaApache);
			categoriaListButton.setSelectedItem(categoriaTitle);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected static org.apache.pivot.collections.List convertirListcategorias(java.util.List<CategoriaItem> arrayUtil) {
		  ArrayList<CategoriaItem> listaApache = new ArrayList<CategoriaItem>();
		 for (int i = 0; i < arrayUtil.size(); i++) {
		 listaApache.add(arrayUtil.get(i));
		 }
		 return listaApache;
		 }
}
