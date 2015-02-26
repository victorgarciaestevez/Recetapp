package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Window;




import dad.recetapp.RecetAppApplication;



public class VentanaPrincipalWindow extends Window implements Bindable {
	@BXML private ImageView imagen;
	
	@SuppressWarnings("unused")
	private RecetAppApplication windowsApp;
	
	@BXML 
	private RecetasPane recetasPane;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {

	}


	public void setWindowsApp(RecetAppApplication windowsApp) {
		this.windowsApp = windowsApp;
		this.recetasPane.setWindowsApp(windowsApp);
	}


	public RecetasPane getRecetasPane() {
		return recetasPane;
	}
	
}
