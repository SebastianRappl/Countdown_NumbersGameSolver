package elements;

import javafx.scene.paint.Color;


public class NumberField extends LabelField{

	private int value;
	
	public NumberField(Color background, Color strokecolor, int width, int height, int strokesize, int value) {
		
		super(background, strokecolor, width, height, strokesize);
		setValue(value);
		
	}
	
	public NumberField(NumberField nf) {
		
		super(nf);
		setValue(nf.getValue());
		
	}
	
	public void setValue(int value) {
		
		this.value = value;
		this.setText(Integer.toString(value));
		
	}
	
	public int getValue() {
		
		return value;
		
	}
	
	
}
