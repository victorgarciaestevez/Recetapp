package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.ListButtonSelectionListener;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.Spinner;
import org.apache.pivot.wtk.SpinnerSelectionListener;
import org.apache.pivot.wtk.TablePane;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.TextInput;

import dad.recetapp.RecetAppApplication;
import dad.recetapp.services.ServiceException;
import dad.recetapp.services.ServiceLocator;
import dad.recetapp.services.items.CategoriaItem;
import dad.recetapp.services.items.RecetaItem;
import dad.recetapp.services.items.RecetaListItem;

public class RecetasPane extends TablePane implements Bindable {

	private static List<RecetaListItem> recetas;
	private RecetAppApplication windowsApp;
	@BXML
	private PushButton abrirAnadir;
	@BXML
	private PushButton abrirEditar;
	@BXML
	private PushButton eliminarButton;
	@BXML
	private TableView recetasView;
	@BXML
	private TextInput nombretext;
	@BXML
	private Spinner filtrarMinutos, filtrarSegundos;
	@SuppressWarnings("rawtypes")
	private List lista;

	@BXML
	private static ListButton comboReceta;
	private static List<CategoriaItem> categoriasBD;
	RecetaListItem recetaEditar;
	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {

		recetas = new ArrayList<RecetaListItem>();
		recetasView.setTableData(recetas);

		initRecetasView();

		try {
			recargarCategoriaListButton();
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}

		comboReceta.getListButtonSelectionListeners().add(
				new ListButtonSelectionListener.Adapter() {
					public void selectedItemChanged(ListButton listButton,
							Object previousSelectedItem) {
						filtroTabla();
					}
				});

		nombretext.getComponentKeyListeners().add(
				new ComponentKeyListener.Adapter() {
					@Override
					public boolean keyTyped(Component arg0, char arg1) {
						try {
							filtroTabla();
						} catch (NullPointerException e) {
						}
						return false;
					}
				});
		filtrarMinutos.getSpinnerSelectionListeners().add(
				new SpinnerSelectionListener() {
					@Override
					public void selectedItemChanged(Spinner arg0, Object arg1) {
						filtroTabla();
					}

					@Override
					public void selectedIndexChanged(Spinner arg0, int arg1) {
						filtroTabla();
					}
				});
		filtrarSegundos.getSpinnerSelectionListeners().add(
				new SpinnerSelectionListener() {

					@Override
					public void selectedItemChanged(Spinner arg0, Object arg1) {
						filtroTabla();
					}

					@Override
					public void selectedIndexChanged(Spinner arg0, int arg1) {
						filtroTabla();
					}
				});

		abrirAnadir.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button arg0) {
				onAbrirAñadirButtonPressed();
			}
		});

		abrirEditar.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button arg0) {
				onAbrirEditarButtonPressed();
			}
		});

		eliminarButton.getButtonPressListeners().add(new ButtonPressListener() {
			public void buttonPressed(Button arg0) {
				onEliminarRecetaButtonPressed();
			}
		});
	}

	protected void onEliminarRecetaButtonPressed() {
		Sequence<?> seleccionados = recetasView.getSelectedRows();
		
		if (seleccionados.getLength() == 0) {
			new Prompt("Debe seleccionar almenos un campo de la tabla")
					.open(this.getWindow());
		} else {
			StringBuffer mensaje = new StringBuffer();
			mensaje.append("¿Desea eliminar la receta selecionada?\n\n");

			Prompt confirmar = new Prompt(MessageType.WARNING,mensaje.toString(), new ArrayList<String>("Sí", "No"));
			confirmar.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {

					if (confirmar.getResult()
							&& confirmar.getSelectedOption().equals("Sí")) {

						java.util.List<RecetaListItem> eliminados = new java.util.ArrayList<RecetaListItem>();

						for (int i = 0; i < seleccionados.getLength(); i++) {
							eliminados.add((RecetaListItem) seleccionados
									.get(i));

						}
						
						for (RecetaListItem e : eliminados) {
							try {
								RecetaItem c = ServiceLocator
										.getRecetasService().obtenerReceta(
												e.getId());
								ServiceLocator.getRecetasService()
										.eliminarReceta(c.getId());
								recetasView.clear();
								initRecetasView();
							} catch (ServiceException e1) {
							}

						}
						
					}
				}
			});
		}

	}
	public TableView getRecetasView(){
		return recetasView;
	}

	protected void onAbrirEditarButtonPressed() {
		Sequence<?> seleccionados = recetasView.getSelectedRows();
		if (seleccionados.getLength() == 0) {
			Prompt error = new Prompt("Debe seleccionar alguna Receta.");
			error.open(this.getWindow(), new SheetCloseListener() {
				public void sheetClosed(Sheet sheet) {
				}
			});
		} else {
		recetaEditar = (RecetaListItem) recetasView.getSelectedRow();	
		
		windowsApp.abrirEditarWindow();
		}
	}
	
	public RecetaListItem getReceta(){
		 return this.recetaEditar;
		 }

	protected void onAbrirAñadirButtonPressed() {
		windowsApp.openSecondWindow();

	}

	public void setWindowsApp(RecetAppApplication windowsApp) {
		this.windowsApp = windowsApp;
	}

	public static void initRecetasView() {
		try {
			recetas.clear();
			java.util.List<RecetaListItem> aux = ServiceLocator.getRecetasService().listarRecetas();

			for (RecetaListItem c : aux) {
				recetas.add(c);
			}
		} catch (ServiceException e) {
		}
	}

	@SuppressWarnings("unchecked")
	public static void recargarCategoriaListButton() throws ServiceException {
		CategoriaItem categoriaTitle = new CategoriaItem();
		categoriaTitle.setId(null);
		categoriaTitle.setDescripcion("<Categorías>");
		categoriasBD = convertirList(ServiceLocator.getCategoriasService()
				.listaCategoria());
		categoriasBD.insert(categoriaTitle, 0);
		comboReceta.setListData(categoriasBD);
		comboReceta.setSelectedItem(categoriaTitle);
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

	protected void filtroTabla() {
		CategoriaItem selectedItem = (CategoriaItem) comboReceta
				.getSelectedItem();
		Integer tiempoFinal = (filtrarMinutos.getSelectedIndex() * 60)
				+ filtrarSegundos.getSelectedIndex();
		if (tiempoFinal == 0) {
			tiempoFinal = null;
		}

		if (selectedItem != null) {
			try {
				lista = convertirList(ServiceLocator.getRecetasService()
						.buscarRecetas(nombretext.getText(), tiempoFinal,
								selectedItem.getId()));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		recetasView.setTableData(lista);
	}

}
