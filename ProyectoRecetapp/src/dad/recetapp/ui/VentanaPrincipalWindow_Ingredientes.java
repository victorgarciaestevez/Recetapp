package dad.recetapp.ui;

import java.net.URL;
import java.util.List;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.TipoIngredienteItem;

public class VentanaPrincipalWindow_Ingredientes extends TablePane implements
		Bindable {
	@BXML
	private PushButton anadirbutton;
	@BXML
	private PushButton eliminarbutton;
	@BXML
	private TextInput nombretext;
	@BXML
	private TableView ingredientesView;

	private org.apache.pivot.collections.List<TipoIngredienteItem> variablesapache;

	@Override
	public void initialize(Map<String, Object> namespace, URL location,
			Resources resources) {
		variablesapache = new org.apache.pivot.collections.ArrayList<TipoIngredienteItem>();
		ingredientesView.setTableData(variablesapache);

		cargarTabla();

		eliminarbutton.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button button) {
				onEliminarButtonPressed();
			}
		});

		anadirbutton.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button button) {
				try {
					onAnadirButtonPressed();
				} catch (ServiceException e) {					
					e.printStackTrace();
				}
			}
		});

	}

	private void cargarTabla() {
		try {
			List<TipoIngredienteItem> anotacionesutil = ServiceLocator
					.getIngredientesService().listarTipoIngrediente();
			for (TipoIngredienteItem anot : anotacionesutil) {
				variablesapache.add(anot);
			}
		} catch (ServiceException e) {

		}
	}

	protected void onEliminarButtonPressed() {
		Sequence<?> seleccionados = ingredientesView.getSelectedRows();
		if(seleccionados.getLength()==0){
			new Prompt("Debe seleccionar almenos un campo de la tabla").open(this.getWindow());
		}else{
		
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar los ingredientes selecionados?\n\n");
		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(),
				new ArrayList<String>("Sí", "No"));
		confirmar.open(this.getWindow(), new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {

				if (confirmar.getResult()
						&& confirmar.getSelectedOption().equals("Sí")) {

					java.util.List<TipoIngredienteItem> eliminados = new java.util.ArrayList<TipoIngredienteItem>();
					
					for (int i = 0; i < seleccionados.getLength(); i++) {
						eliminados.add((TipoIngredienteItem) seleccionados
								.get(i));
						variablesapache
								.remove((TipoIngredienteItem) seleccionados
										.get(i));
					}
				
					for (TipoIngredienteItem e : eliminados) {
						try {
							TipoIngredienteItem c = ServiceLocator
									.getIngredientesService()
									.obteneTipoIngrediente(e.getId());
							ServiceLocator.getIngredientesService()
									.eliminarTipoIngrediente(c.getId());
						} catch (ServiceException e1) {

						}

					}
				
				}
			}
		});
		}
	}

	protected void onAnadirButtonPressed() throws ServiceException {
		if(nombretext.getText().equals("")){
			new Prompt("Ingrediente no puede ser vacio").open(this.getWindow());
		}else{
		TipoIngredienteItem nueva = new TipoIngredienteItem();
		nueva.setNombre(nombretext.getText());
		variablesapache.add(nueva);
		nombretext.setText("");
		ServiceLocator.getIngredientesService().crearTipoIngrediente(nueva);
		variablesapache.clear();
		cargarTabla();
		}
	}

}
