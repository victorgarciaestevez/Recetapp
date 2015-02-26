package dad.recetapp.ui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.Prompt;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextInput;


import dad.recetapp.services.items.InstruccionItem;

public class NuevaInstruccionDialog extends Dialog implements Bindable {

	@BXML private PushButton cancelarButton;
	@BXML private PushButton anadirButton;
	@BXML private TextInput ordenText;
	@BXML private TextArea descripcionText;
	
	private InstruccionItem instruccion;
	
	private Boolean aceptar = false;
	
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		instruccion = new InstruccionItem();
		cancelarButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				onCancelarButtonButtonPressed();
			}
		});
		
		anadirButton.getButtonPressListeners().add(new ButtonPressListener() {
			
			@Override
			public void buttonPressed(Button arg0) {
				onAnadirButtonPressed();
			}
		});
	}

	protected void onAnadirButtonPressed() {
		if(ordenText.getText().equals("")||descripcionText.getText().equals("")){
			new Prompt("No puede tener campos vacios").open(this.getWindow());
		
		}else{
		instruccion.setOrden(Integer.valueOf(ordenText.getText()));
		instruccion.setDescripcion(descripcionText.getText());
		
		aceptar = true;
		close();
		}
	}

	protected void onCancelarButtonButtonPressed() {
		close();
		
	}

	public InstruccionItem getInstruccion() {
		return instruccion;
	}

	public void setInstruccion(InstruccionItem instruccion) {
		this.instruccion = instruccion;
	}

	public Boolean getAceptar() {
		return aceptar;
	}

}
