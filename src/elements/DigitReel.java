package elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class DigitReel extends VBox{

	Polygon up, down;
	Label digit;
	int value;
	
	public DigitReel(double width, double height, int minDigit, int maxDigit) {
		
		this.value = minDigit;
		
		up = new Polygon();
		down = new Polygon();
		digit = new Label();
		
		up.getPoints().addAll(0.0, height/3, width, height/3, width/2, 0.0);
		down.getPoints().addAll(0.0, 0.0, width, 0.0, width/2, height/3);
		
		up.setFill(Color.BLACK);
		down.setFill(Color.BLACK);
		
		up.setOnMousePressed(e -> {
			
			this.value++;
			if(this.value > maxDigit) {
				
				this.value = minDigit;
				
			}
			digit.setText(Integer.toString(this.value));
			
		});
		
		down.setOnMousePressed(e -> {
			
			this.value--;
			if(this.value < minDigit) {
				
				this.value = maxDigit;
				
			}
			digit.setText(Integer.toString(this.value));
			
		});
		
		digit.setPrefWidth(width);
		digit.setAlignment(Pos.CENTER);
		digit.setTextAlignment(TextAlignment.CENTER);
		digit.setFont(Font.font(height/4));
		
		digit.setText(Integer.toString(this.value));
		
		this.getChildren().addAll(up,digit,down);
		
	}
	
	public int getValue() {
		
		return this.value;
		
	}
	
}
