package dad.recetapp.ui;

import java.awt.event.ActionEvent;

import java.net.URL;





import javax.swing.Timer;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.Mouse.Button;

import dad.recetapp.RecetAppApplication;

public class VentanaCargaWindow extends Window implements Bindable {
	Timer timer;
	@BXML private ImageView imagen;
	
	private RecetAppApplication windowsApp;

	@SuppressWarnings("unused")
	private VentanaPrincipalWindow ventanaPrincipalWindow = null;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		
		timer = new Timer (4000, new java.awt.event.ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				timer.stop();
				windowsApp.AbrirPricipal();
				
				
				
			}
		});
		
		timer.start();
		
		
		imagen.getComponentMouseButtonListeners().add(new ComponentMouseButtonListener() {
			
			@Override
			public boolean mouseUp(Component arg0, Button arg1, int arg2, int arg3) {return false;}
			
			@Override
			public boolean mouseDown(Component arg0, Button arg1, int arg2, int arg3) {return false;}
			
			@Override
			public boolean mouseClick(Component arg0, Button arg1, int arg2, int arg3,int arg4) {
				timer.stop();
				windowsApp.AbrirPricipal();
				return false;
			}
		});
	
	}
	
	
	
	public void setWindowsApp(RecetAppApplication windowsApp) {
		this.windowsApp = windowsApp;
		
	}
	


}
