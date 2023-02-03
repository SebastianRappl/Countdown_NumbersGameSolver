package elements;

import javafx.scene.paint.Color;

public class OperatorField extends LabelField{

	private String operator = null;
	private static final String[] operators = {"+","-","*","/"};
	
	public OperatorField(Color background, Color strokecolor, int width, int height, int strokesize, String operator) {
		
		super(background, strokecolor, width, height, strokesize);
		setOperator(operator);
		
	}

	public OperatorField(OperatorField of) {
		
		super(of);
		setOperator(of.getOperator());
		
	}

	public String getOperator() {
		
		return operator;
		
	}

	public void setOperator(String operator) {
		
		for(String o: operators) {
			
			if(o.equals(operator)) {
				
				this.operator = operator;
				this.setText(operator);
				return;
				
			}
			
		}
		
	}
	
	public static final String[] getOperators() {
		
		return operators;
		
	}
	
}
