package elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public abstract class LabelField extends Label{

	public LabelField(Color background, Color strokecolor, int width, int height, int strokesize) {
		
		this.setMinSize(width, height);
		this.setText("");
		Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, strokesize);
		this.setFont(font);
		this.setTextFill(strokecolor);
		this.setBackground(new Background(new BackgroundFill(background, null, null)));
		this.setTextAlignment(TextAlignment.CENTER);
		this.setAlignment(Pos.CENTER);
		
	}
	
	public LabelField(LabelField nf) {
		
		this.setMinSize(nf.getMinWidth(), nf.getMinHeight());
		this.setText(nf.getText());
		Font font = nf.getFont();
		this.setFont(font);
		this.setTextFill(nf.getTextFill());
		this.setBackground(nf.getBackground());
		this.setTextAlignment(nf.getTextAlignment());
		this.setAlignment(nf.getAlignment());
		
	}
	
}
