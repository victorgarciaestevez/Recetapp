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
import org.apache.pivot.wtk.ListButtonSelectionListener;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.MedidaItem;
import dad.recetapp.services.items.SeccionItem;
import dad.recetapp.services.items.TipoIngredienteItem;

public class NuevoIngredienteDialog extends Dialog implements Bindable {

	@BXML private ListButton comboIngrediente;
	@BXML private ListButton comboMedida;
	@BXML private PushButton cancelarNIngrediente;
	@BXML private PushButton anadirIngrediente;
	@BXML private static TextInput cantidadText;
	
	private List<TipoIngredienteItem> ingredientesBD;
	private List<MedidaItem> medidasBD;
	private List lista;
	
	private IngredienteItem ingrediente;
	
	private Boolean aceptar = false;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		ingrediente = new IngredienteItem();
		try {
			recargarIngredienteListButton();
			recargarMedidaListButton();
			
			cancelarNIngrediente.getButtonPressListeners().add(new ButtonPressListener() {	
				public void buttonPressed(Button button) {
					 onCerrarButtonPressed();		
				}
			});
			
			anadirIngrediente.getButtonPressListeners().add(new ButtonPressListener() {
				
				@Override
				public void buttonPressed(Button arg0) {
					onAnadirButtonPressed();
				}
			});
		} catch (ServiceException e) {e.printStackTrace();}
	}
	
	protected void onAnadirButtonPressed() {
		if(cantidadText.getText().equals("")){
			new Prompt("No puede tener campos vacios").open(this.getWindow());
		
		}else{
		ingrediente.setCantidad(Integer.valueOf(cantidadText.getText()));
		ingrediente.setMedida((MedidaItem) comboMedida.getSelectedItem());
		ingrediente.setTipo((TipoIngredienteItem) comboIngrediente.getSelectedItem());
		aceptar = true;
		close();
	}
		
	}
	
	public Boolean getAceptar() {
		return aceptar;
	}

	protected void onCerrarButtonPressed() {		
		close();
	}
	
	private void recargarIngredienteListButton() throws ServiceException {
		TipoIngredienteItem ingredienteTitle = new TipoIngredienteItem();
		ingredienteTitle.setId(null);
		ingredienteTitle.setNombre("<Seleccione el tipo de ingrediente>");
		ingredientesBD = RecetasPane.convertirList(ServiceLocator.getIngredientesService().listarTipoIngrediente());
		ingredientesBD.insert(ingredienteTitle, 0);
		comboIngrediente.setListData(ingredientesBD);
		comboIngrediente.setSelectedItem(ingredienteTitle);
	}
	
	private void recargarMedidaListButton() throws ServiceException {
		MedidaItem medidaTitle = new MedidaItem();
		medidaTitle.setId(null);
		medidaTitle.setNombre("<Seleccione la medida>");
		medidasBD = RecetasPane.convertirList(ServiceLocator.getMedidasService().listarMedidas());
		medidasBD.insert(medidaTitle, 0);
		comboMedida.setListData(medidasBD);
		comboMedida.setSelectedItem(medidaTitle);
	}

	public ListButton getComboIngrediente() {
		return comboIngrediente;
	}

	public void setComboIngrediente(ListButton comboIngrediente) {
		this.comboIngrediente = comboIngrediente;
	}

	public Object getComboMedida() {
		return comboMedida.getSelectedItem();
	}

	public void setComboMedida(Object comboMedida) {
		this.comboMedida.setSelectedItem(comboMedida);
	}

	public TextInput getCantidadText() {
		return cantidadText;
	}

	public void setCantidadText(TextInput cantidadText) {
		this.cantidadText = cantidadText;
	}

	public IngredienteItem getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(IngredienteItem ingrediente) {
		this.ingrediente = ingrediente;
	}

}
