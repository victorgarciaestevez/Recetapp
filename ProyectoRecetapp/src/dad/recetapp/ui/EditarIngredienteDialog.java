package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.MedidaItem;
import dad.recetapp.services.items.TipoIngredienteItem;

public class EditarIngredienteDialog extends Dialog implements Bindable {

	@BXML private ListButton comboIngrediente;
	@BXML private ListButton comboMedida;
	@BXML private PushButton cancelarEIngrediente;
	@BXML private TextInput cantidadtxt;
	@BXML private PushButton guardarCambiosIngrediente;
	
	
	private List<TipoIngredienteItem> ingredientesBD;
	private List<MedidaItem> medidasBD;
	private List lista;
	private IngredienteItem ingrediente;
	private int medida;
	private boolean aceptar = false;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		cancelarEIngrediente.getButtonPressListeners().add(new ButtonPressListener() {	
			public void buttonPressed(Button button) {
				 onCerrarButtonPressed();		
			}
		});
		
		guardarCambiosIngrediente.getButtonPressListeners().add(new ButtonPressListener() {	
			public void buttonPressed(Button button) {
				 onGuardarButtonPressed();		
			}
		});
	}
	
	

	protected void onGuardarButtonPressed() {
		ingrediente.setCantidad(Integer.valueOf(cantidadtxt.getText()));
		 ingrediente.setMedida((MedidaItem) comboMedida.getSelectedItem());
		 ingrediente.setTipo((TipoIngredienteItem) comboIngrediente.getSelectedItem());
		 aceptar  = true;
		 close();
		
	}



	protected void onCerrarButtonPressed() {		
		close();
	}
	
	private void recargarIngredienteListButton(TipoIngredienteItem t) throws ServiceException {
		TipoIngredienteItem ingredienteTitle = new TipoIngredienteItem();
		ingredienteTitle.setId(t.getId());
		ingredienteTitle.setNombre(t.getNombre());
		ingredientesBD = RecetasPane.convertirList(ServiceLocator.getIngredientesService().listarTipoIngrediente());
		ingredientesBD.insert(ingredienteTitle, 0);
		comboIngrediente.setListData(ingredientesBD);
		comboIngrediente.setSelectedItem(ingredienteTitle);
	}
	
	private void recargarMedidaListButton(MedidaItem m) throws ServiceException {
		MedidaItem medidaTitle = new MedidaItem();
		medidaTitle.setId(m.getId());
		medidaTitle.setNombre(m.getNombre());
		medidasBD = RecetasPane.convertirList(ServiceLocator.getMedidasService().listarMedidas());
		medidasBD.insert(medidaTitle, 0);
		comboMedida.setListData(medidasBD);

		comboMedida.setSelectedItem(medidaTitle);
	}
	
	
	public void setIngrediente(IngredienteItem ingrediente){		
		 this.ingrediente = ingrediente;
		 MedidaItem m = ingrediente.getMedida();
		 TipoIngredienteItem t = ingrediente.getTipo();
		
		 cantidadtxt.setText(String.valueOf(this.ingrediente.getCantidad()));
		 
		 
		 try {
			recargarMedidaListButton(m);
			recargarIngredienteListButton(t);
		} catch (ServiceException e) {e.printStackTrace();}
		
		 }
	
	public Boolean getAceptar() {
		 return aceptar;
		 }
	public IngredienteItem getIngrediente() {
		 return ingrediente;
		 }

}
