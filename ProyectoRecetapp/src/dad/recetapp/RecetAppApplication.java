package dad.recetapp;

import java.io.IOException;
import java.net.URL;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

import dad.recetapp.services.items.RecetaItem;
import dad.recetapp.ui.EditarRecetaWindow;
import dad.recetapp.ui.NuevaInstruccionDialog;
import dad.recetapp.ui.NuevaRecetaWindow;
import dad.recetapp.ui.NuevoIngredienteDialog;
import dad.recetapp.ui.VentanaCargaWindow;
import dad.recetapp.ui.VentanaPrincipalWindow;

public class RecetAppApplication implements Application {

	private Display primaryDisplay = null;

	private VentanaPrincipalWindow ventanaPrincipalWindow = null;
	
	public VentanaPrincipalWindow getVentanaPrincipalWindow() {
		return ventanaPrincipalWindow;
	}


	private NuevaRecetaWindow nuevaRecetaWindow = null;
	private EditarRecetaWindow editarRecetaWindow = null;
	private VentanaCargaWindow ventanaCargaWindow = null;
	private NuevoIngredienteDialog nuevoIngredienteWindow = null;
	private NuevaInstruccionDialog nuevaInstruccionWindow = null;
	@Override
	public void suspend() throws Exception {
	}

	@Override
	public void resume() throws Exception {
	}

	@Override
	public boolean shutdown(boolean optional) throws Exception {
		return false;
	}

	@Override
	public void startup(Display display, Map<String, String> properties)
			throws Exception {
		primaryDisplay = display;
		
		ventanaCargaWindow = (VentanaCargaWindow) loadWindow("/dad/recetapp/ui/VentanaCargaWindow.bxml");
		ventanaCargaWindow.setWindowsApp(this);
		ventanaCargaWindow.open(primaryDisplay);
		

//		ventanaPrincipalWindow = (VentanaPrincipalWindow) loadWindow("/dad/recetapp/ui/VentanaPrincipalWindow.bxml");
//		ventanaPrincipalWindow.setWindowsApp(this);
//		ventanaPrincipalWindow.open(primaryDisplay);

		// nuevaRecetaWindow = (NuevaRecetaWindow)
		// loadWindow("dad/recetapp/ui/NuevaRecetaWindow.bxml");
		// nuevaRecetaWindow.open(display);
	}

	public static Window loadWindow(String bxmlFile) throws IOException,
			SerializationException {
		URL bxmlUrl = RecetAppApplication.class.getResource(bxmlFile);
		BXMLSerializer serializer = new BXMLSerializer();
		return (Window) serializer.readObject(bxmlUrl);
	}
	
	public void AbrirPricipal(){
		try {
			ventanaPrincipalWindow = (VentanaPrincipalWindow) loadWindow("/dad/recetapp/ui/VentanaPrincipalWindow.bxml");
			ventanaPrincipalWindow.setWindowsApp(this);
			ventanaPrincipalWindow.open(primaryDisplay);
		}catch (IOException | SerializationException e) {
			e.printStackTrace();
		}
	}

	public void openSecondWindow() {
		try {
			nuevaRecetaWindow = (NuevaRecetaWindow) loadWindow("/dad/recetapp/ui/NuevaRecetaWindow.bxml");
			nuevaRecetaWindow.open(primaryDisplay);
		} catch (IOException | SerializationException e) {
			e.printStackTrace();
		}
	}

	public void abrirEditarWindow() {
		try {
			editarRecetaWindow = (EditarRecetaWindow) loadWindow("/dad/recetapp/ui/EditarRecetaWindow.bxml");
			editarRecetaWindow.setRecetapp(this);
			editarRecetaWindow.open(primaryDisplay);
		} catch (IOException | SerializationException e) {
			e.printStackTrace();
		}		
	}
	
	
	public void abrirNuevoIngrediente(){
		try {
			nuevoIngredienteWindow = (NuevoIngredienteDialog) loadWindow("/dad/recetapp/ui/NuevoIngredienteWindow.bxml");
			nuevoIngredienteWindow.open(primaryDisplay);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
