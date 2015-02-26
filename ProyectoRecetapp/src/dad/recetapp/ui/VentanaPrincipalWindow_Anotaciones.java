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
import dad.recetapp.services.items.TipoAnotacionItem;

public class VentanaPrincipalWindow_Anotaciones extends TablePane implements Bindable {
	@BXML	private PushButton anadirbutton;
	@BXML	private PushButton eliminarbutton;
	@BXML	private TextInput descripciontext;
	@BXML	private TableView anotacionesView;

	private org.apache.pivot.collections.List<TipoAnotacionItem> variablesapache;

	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
		variablesapache = new org.apache.pivot.collections.ArrayList<TipoAnotacionItem>();
		anotacionesView.setTableData(variablesapache);

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
				} catch (ServiceException e) {e.printStackTrace();}
			}
		});

	}

	private void cargarTabla() {
		try {
			List<TipoAnotacionItem> anotacionesutil = ServiceLocator
					.getAnotacionesService().listarTipoAnotacion();
			for (TipoAnotacionItem anot : anotacionesutil) {
				variablesapache.add(anot);
			}
		} catch (ServiceException e) {

		}
	}

	protected void onEliminarButtonPressed() {
		Sequence<?> seleccionados = anotacionesView.getSelectedRows();
		if(seleccionados.getLength()==0){
			new Prompt("Debe seleccionar almenos un campo de la tabla").open(this.getWindow());
		}else{
		
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar las siguientes Anotaciones?\n\n");

		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(),
				new ArrayList<String>("Sí", "No"));
		confirmar.open(this.getWindow(), new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {
				if (confirmar.getResult()&& confirmar.getSelectedOption().equals("Sí")) {

					java.util.List<TipoAnotacionItem> eliminados = new java.util.ArrayList<TipoAnotacionItem>();

					
					for (int i = 0; i < seleccionados.getLength(); i++) {
						eliminados.add((TipoAnotacionItem) seleccionados.get(i));
						variablesapache.remove((TipoAnotacionItem) seleccionados.get(i));
						
					}
					
					for (TipoAnotacionItem e : eliminados) {
						try {
							TipoAnotacionItem c = ServiceLocator
									.getAnotacionesService()
									.obtenerTipoAnotacion(e.getId());
							ServiceLocator.getAnotacionesService()
									.eliminarTipoAnotacion(c.getId());
						} catch (ServiceException e1) {

						}

					}
					

				}
			}
		});
		}
	}

	protected void onAnadirButtonPressed() throws ServiceException {
		if(descripciontext.getText().equals("")){
			new Prompt("Descripción no puede ser vacio").open(this.getWindow());
		}else{
		TipoAnotacionItem nueva = new TipoAnotacionItem();
		nueva.setDescripcion(descripciontext.getText());
		variablesapache.add(nueva);
		descripciontext.setText("");
		ServiceLocator.getAnotacionesService().crearTipoAnotacion(nueva);
		variablesapache.clear();
		cargarTabla();
	}}
}
