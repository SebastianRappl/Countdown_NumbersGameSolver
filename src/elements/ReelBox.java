package elements;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class ReelBox extends HBox{

	DigitReel One, Two, Three;
	
	public ReelBox(double width, double height) {
		
		One = new DigitReel(width/3,height,1,9);
		Two = new DigitReel(width/3,height,0,9);
		Three = new DigitReel(width/3,height,0,9);
		
		this.setAlignment(Pos.CENTER);
		this.getChildren().addAll(One,Two,Three);
		
	}
	
	public int getValue() {
		
		return One.getValue()*100+Two.getValue()*10+Three.getValue();
		
	}
	
}
