package dad.recetapp.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TabPane;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.DialogCloseListener;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import dad.recetapp.RecetAppApplication;
import dad.recetapp.services.items.IngredienteItem;
import dad.recetapp.services.items.InstruccionItem;
import dad.recetapp.services.items.SeccionItem;

public class ComponenteReceta extends TablePane implements Bindable {

	

	@BXML
	private PushButton aniadirIngrediente;
	@BXML
	private PushButton aniadirInstruccion;
	@BXML
	private PushButton eliminarIngrediente;
	@BXML
	private PushButton editarIngrediente;
	@BXML
	private PushButton editarInstruccion;
	@BXML
	private PushButton eliminarInstruccion;
	@BXML
	private TableView ingredientesView;
	@BXML
	private TableView instruccionesView;
	@BXML
	private TextInput secciontext;
	@BXML
	private PushButton eliminarPentana;

	private NuevoIngredienteDialog nuevoIngredienteDialog = null;
	private NuevaInstruccionDialog nuevaInstruccionDialog = null;
	private EditarIngredienteDialog editarIngredienteDialog = null;
	private EditarInstruccionDialog editarInstruccionDialog = null;
	public Window parent;
	private org.apache.pivot.collections.List<IngredienteItem> datosIngrediente;
	private org.apache.pivot.collections.List<InstruccionItem> datosInstruccion;
	@SuppressWarnings("unused")
	private RecetAppApplication windowsApp;
	private int pos;

	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
		datosIngrediente = new org.apache.pivot.collections.ArrayList<IngredienteItem>();
		datosInstruccion = new org.apache.pivot.collections.ArrayList<InstruccionItem>();

		editarIngrediente.getButtonPressListeners().add(
				new ButtonPressListener() {
					public void buttonPressed(Button arg0) {
						onEditarIngredienteButtonPressed();
					}
				});
		eliminarPentana.getButtonPressListeners().add(
				new ButtonPressListener() {
					public void buttonPressed(Button arg0) {
						onEliminarPestañaButtonPressed();
					}
				});

		aniadirIngrediente.getButtonPressListeners().add(
				new ButtonPressListener() {
					public void buttonPressed(Button arg0) {
						onAbrirAnadirIngredienteButtonPressed();
					}
				});

		aniadirInstruccion.getButtonPressListeners().add(
				new ButtonPressListener() {
					public void buttonPressed(Button arg0) {
						onAbrirAnadirInstruccionButtonPressed();
					}
				});

		editarInstruccion.getButtonPressListeners().add(
				new ButtonPressListener() {
					public void buttonPressed(Button arg0) {
						onAbrirEditarInstruccionButtonPressed();
					}
				});

		eliminarIngrediente.getButtonPressListeners().add(
				new ButtonPressListener() {
					public void buttonPressed(Button arg0) {
						onEliminarIngredienteButtonPressed();
					}
				});

		eliminarInstruccion.getButtonPressListeners().add(
				new ButtonPressListener() {
					public void buttonPressed(Button arg0) {
						onEliminarInstruccionButtonPressed();
					}
				});

		secciontext.getComponentKeyListeners().add(
				new ComponentKeyListener.Adapter() {
					@SuppressWarnings("static-access")
					@Override
					public boolean keyTyped(Component arg0, char arg1) {

						if (parent instanceof NuevaRecetaWindow) {
							try {
								((NuevaRecetaWindow) parent)
										.cambiarNombrePestana(secciontext
												.getText());
								((NuevaRecetaWindow) parent).pestanaNueva
										.repaint();
							} catch (NullPointerException e) {
							}
						} else {
							try {
							
							} catch (NullPointerException e) {
							}
						}

						return false;
					}
				});
	}

	protected void onEditarIngredienteButtonPressed() {
		Sequence<?> seleccionados = ingredientesView.getSelectedRows();
		if (seleccionados.getLength() == 1) {
			IngredienteItem ingrediente = (IngredienteItem) seleccionados
					.get(0);

			onAbrirEditarIngredienteButtonPressed(seleccionados);
		} else {
			Prompt error = new Prompt("Debe seleccionar 1 ingrediente.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		}

	}

	protected void onEliminarPestañaButtonPressed() {
		NuevaRecetaWindow.eliminarPestana();

	}

	protected void onEliminarIngredienteButtonPressed() {
		Sequence<?> seleccionados = ingredientesView.getSelectedRows();
		if (seleccionados.getLength() == 0) {
			Prompt error = new Prompt("Debe seleccionar algun ingrediente.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {	
			
			StringBuffer mensaje = new StringBuffer();
			mensaje.append("¿Desea eliminar el ingrediente selecionado?\n\n");

			Prompt confirmardo = new Prompt(MessageType.WARNING,mensaje.toString(),new org.apache.pivot.collections.ArrayList<String>("SI","NO"));
			confirmardo.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {

					if (confirmardo.getResult()
							&& confirmardo.getSelectedOption().equals("Sí")) {
			for (int i = 0; i < seleccionados.getLength(); i++) {
				IngredienteItem ingredienteSeleccionado = (IngredienteItem) seleccionados
						.get(i);
				datosIngrediente.remove(ingredienteSeleccionado);
			}
					}

				}
			});

		}
	}
	protected void onEliminarInstruccionButtonPressed() {

		Sequence<?> seleccionados = instruccionesView.getSelectedRows();
		if (seleccionados.getLength() == 0) {
			Prompt error = new Prompt("Debe seleccionar alguna instruccion.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
			StringBuffer mensaje = new StringBuffer();
			mensaje.append("¿Desea eliminar la instrucción selecionada?\n\n");

			Prompt confirmardo = new Prompt(MessageType.WARNING,mensaje.toString(),new org.apache.pivot.collections.ArrayList<String>("SI","NO"));
			confirmardo.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {

					if (confirmardo.getResult()
							&& confirmardo.getSelectedOption().equals("SI")) {

					for (int i = 0; i < seleccionados.getLength(); i++) {
						InstruccionItem instruccionSeleccionado = (InstruccionItem) seleccionados
								.get(i);
						datosInstruccion.remove(instruccionSeleccionado);
					}
					}

				}
			});

		}
	}

	protected void onAbrirEditarInstruccionButtonPressed() {
		try {
			Sequence<?> seleccionados = instruccionesView.getSelectedRows();
			if (seleccionados.getLength() == 1) {
			editarInstruccionDialog = (EditarInstruccionDialog) RecetAppApplication
					.loadWindow("/dad/recetapp/ui/EditarInstruccionDialog.bxml");
			editarInstruccionDialog.open(getWindow(),
					new DialogCloseListener() {

						@Override
						public void dialogClosed(Dialog arg0, boolean arg1) {
							onCerrarEditarInstruccionDialogClosed();
						}
					});
			}else{
				Prompt error = new Prompt("Debe seleccionar alguna Instrución.");
				error.open(this.getWindow(), new SheetCloseListener() {
					public void sheetClosed(Sheet sheet) {
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}

		
	}

	protected void onCerrarEditarInstruccionDialogClosed() {
		// TODO Auto-generated method stub

	}

	protected void onAbrirEditarIngredienteButtonPressed(
			Sequence<?> seleccionados) {

		try {
			pos = ingredientesView.getSelectedIndex();
			IngredienteItem ingredienteSeleccionado;
			ingredienteSeleccionado = (IngredienteItem) seleccionados.get(0);
			editarIngredienteDialog = (EditarIngredienteDialog) RecetAppApplication
					.loadWindow("/dad/recetapp/ui/EditarIngredienteDialog.bxml");

			editarIngredienteDialog.setIngrediente(ingredienteSeleccionado);
			editarIngredienteDialog.open(getWindow(),
					new DialogCloseListener() {

						@Override
						public void dialogClosed(Dialog arg0, boolean arg1) {
							onCerrarEditarIngredienteDialogClosed(arg0, pos);
						}
					});

		} catch (IOException e) {

			e.printStackTrace();
		} catch (SerializationException e) {

			e.printStackTrace();
		}

		
	}

	protected void onCerrarEditarIngredienteDialogClosed(Dialog dialogo, int pos) {
		EditarIngredienteDialog instr = (EditarIngredienteDialog) dialogo;
		if (instr.getAceptar()) {
			ingredientesView.removeSelectedIndex(pos);			
			ingredientesView.setTableData(datosIngrediente);
		}

	}

	protected void onAbrirAnadirInstruccionButtonPressed() {
		try {
			
			nuevaInstruccionDialog = (NuevaInstruccionDialog) RecetAppApplication
					.loadWindow("/dad/recetapp/ui/NuevaInstruccionDialog.bxml");
			nuevaInstruccionDialog.open(getWindow(), new DialogCloseListener() {

				@Override
				public void dialogClosed(Dialog arg0, boolean arg1) {
					onCerrarInstruccionDialogClosed(arg0);
				}
			});
		} catch (IOException e) {

			e.printStackTrace();
		} catch (SerializationException e) {

			e.printStackTrace();
		}

		
	}

	protected void onCerrarInstruccionDialogClosed(Dialog arg0) {
		NuevaInstruccionDialog instr = (NuevaInstruccionDialog) arg0;
		if (instr.getAceptar()) {

			datosInstruccion.add(instr.getInstruccion());
			instruccionesView.setTableData(datosInstruccion);
		}
	}

	protected void onAbrirAnadirIngredienteButtonPressed() {
		try {
			nuevoIngredienteDialog = (NuevoIngredienteDialog) RecetAppApplication
					.loadWindow("/dad/recetapp/ui/NuevoIngredienteDialog.bxml");
			nuevoIngredienteDialog.open(getWindow(), new DialogCloseListener() {

				@Override
				public void dialogClosed(Dialog arg0, boolean arg1) {
					onCerrarIngredienteDialogClosed(arg0);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerializationException e) {
			e.printStackTrace();
		}

	}

	protected static java.util.List<IngredienteItem> convertirListIngredientes(org.apache.pivot.collections.List listaUtil) {
		List listaApache = new ArrayList();
		for (int i = 0; i < listaUtil.getLength(); i++) {
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}

	protected static java.util.List<InstruccionItem> convertirListInstrucciones(org.apache.pivot.collections.List listaUtil) {
		List listaApache = new ArrayList();
		for (int i = 0; i < listaUtil.getLength(); i++) {
			listaApache.add(listaUtil.get(i));
		}
		return listaApache;
	}

	public SeccionItem getSeccion() {
		SeccionItem seccion = new SeccionItem();
		seccion.setNombre(secciontext.getText());
		seccion.setIngredientes(convertirListIngredientes(datosIngrediente));
		seccion.setInstrucciones(convertirListInstrucciones(datosInstruccion));
		return seccion;
	}

	public void setWindowsApp(RecetAppApplication windowapp) {
		this.windowsApp = windowapp;
	}

	protected void onCerrarIngredienteDialogClosed(Dialog d) {
		NuevoIngredienteDialog nuevoIngre = (NuevoIngredienteDialog) d;
		if (nuevoIngre.getAceptar()) {
			datosIngrediente.add(nuevoIngre.getIngrediente());
			ingredientesView.setTableData(datosIngrediente);
		}
	}

	public void setParent(Window parent) {
		this.parent = parent;
	}

}
