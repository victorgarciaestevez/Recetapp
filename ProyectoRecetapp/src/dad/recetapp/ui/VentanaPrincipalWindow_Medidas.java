package dad.recetapp.ui;

import java.net.URL;

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
import dad.recetapp.services.items.MedidaItem;

public class VentanaPrincipalWindow_Medidas extends TablePane implements
		Bindable {
	private org.apache.pivot.collections.List<MedidaItem> variablesapache;

	@BXML
	private TableView medidasView;
	@BXML
	private TextInput nombretext;
	@BXML
	private TextInput abreviaturaText;
	@BXML
	private PushButton anadirButton;
	@BXML
	private PushButton eliminarButton;

	@Override
	public void initialize(Map<String, Object> namespace, URL location,
			Resources resources) {
		variablesapache = new org.apache.pivot.collections.ArrayList<MedidaItem>();
		medidasView.setTableData(variablesapache);

		cargarTabla();

		anadirButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onAnadirButtonPressed();
			}
		});

		eliminarButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onEliminarButtonPressed();
			}
		});
	}

	private void cargarTabla() {
		try {
			java.util.List<MedidaItem> aux = ServiceLocator.getMedidasService()
					.listarMedidas();
			for (MedidaItem c : aux) {
				variablesapache.add(c);
			}
		} catch (ServiceException e) {

		}
	}

	protected void onAnadirButtonPressed() {
		if(nombretext.getText().equals("")||abreviaturaText.getText().equals("")){
			new Prompt("No puede añadir con campos vacios").open(this.getWindow());
		}else{
		MedidaItem nueva = new MedidaItem();
		nueva.setNombre(nombretext.getText());
		nueva.setAbreviatura(abreviaturaText.getText());
		try {
			ServiceLocator.getMedidasService().crearMedida(nueva);
		} catch (ServiceException e) {

		}
		variablesapache.add(nueva);
		variablesapache.clear();
		cargarTabla();
		nombretext.setText("");
		abreviaturaText.setText("");
	}
	}
	protected void onEliminarButtonPressed() {
		Sequence<?> seleccionados = medidasView.getSelectedRows();
		if(seleccionados.getLength()==0){
			new Prompt("Debe seleccionar almenos un campo de la tabla").open(this.getWindow());
		}else{
		
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("¿Desea eliminar las medidas seleccionadas?\n\n");

		Prompt confirmar = new Prompt(MessageType.WARNING, mensaje.toString(),
				new ArrayList<String>("Sí", "No"));
		confirmar.open(this.getWindow(), new SheetCloseListener() {
			public void sheetClosed(Sheet sheet) {

				if (confirmar.getResult()
						&& confirmar.getSelectedOption().equals("Sí")) {

					java.util.List<MedidaItem> eliminados = new java.util.ArrayList<MedidaItem>();
					//Sequence<?> seleccionados = medidasView.getSelectedRows();
					for (int i = 0; i < seleccionados.getLength(); i++) {
						eliminados.add((MedidaItem) seleccionados.get(i));
						variablesapache.remove((MedidaItem) seleccionados
								.get(i));
					}
					for (MedidaItem e : eliminados) {
						try {
							MedidaItem c = ServiceLocator.getMedidasService()
									.obtenerMedida(e.getId());
							ServiceLocator.getMedidasService().eliminarMedida(
									c.getId());
						} catch (ServiceException e1) {

						}
					}

				}
			}
		});
	}}
}
