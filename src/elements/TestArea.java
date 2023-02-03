package elements;

import java.util.HashMap;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.Calc;

public class TestArea extends VBox{

	private Label infoLabel;
	private LabelField[] placeholders;
	private NumberField[] numbers;
	private OperatorField[][] operators;
	private String[] equation;
	private HashMap<LabelField,Integer> inputMap, equationMap;
	private Label equalLabel, resultLabel;
	private GridPane inputPane, equationPane;
	private VBox gridBox;
	private Pane slidePane;
	private StackPane stackPane;
	private List<Integer> numberlist;
	private int target, result;
	private final Background orangeBackground = new Background(new BackgroundFill(Color.ORANGE,null,null));
	
	
	public TestArea() {
		
		infoLabel = new Label("");
		inputPane = new GridPane();
		equationPane = new GridPane();
		gridBox = new VBox();
		gridBox.getChildren().addAll(infoLabel,inputPane,equationPane);
		slidePane = new Pane();
		stackPane = new StackPane(gridBox,slidePane);
		
		this.getChildren().add(stackPane);
		
	}
	
	public void set(List<Integer> numberlist, int target) {
		
		inputPane.getChildren().clear();
		equationPane.getChildren().clear();
		
		this.numberlist = numberlist;
		this.target = target;
		equation = new String[numberlist.size()*2-1];
		placeholders = new LabelField[numberlist.size()*2-1]; // placeholders for numbers, operators
		numbers = new NumberField[numberlist.size()];
		operators = new OperatorField[numberlist.size()-1][4];
		
		initialize();

	}
	
	private void initialize() {
		
		Insets verticalPadding = new Insets(20,0,20,0);
		
		infoLabel.setFont(Font.font(20));
		infoLabel.setText("Try to get as close to the target as possible:");
		
		gridBox.setAlignment(Pos.CENTER);
		gridBox.setPadding(verticalPadding);
		
		inputPane.setHgap(10);
		inputPane.setVgap(20);
		inputPane.setAlignment(Pos.CENTER);
		inputPane.setPadding(verticalPadding);
		
		equationPane.setHgap(10);
		equationPane.setVgap(20);
		equationPane.setAlignment(Pos.CENTER);
		equationPane.setPadding(verticalPadding);
		
		equationMap = new HashMap<LabelField, Integer>();
		
		for(int i=0; i<placeholders.length; i++) {
			
			equation[i] = "";
			
			if(i%2==0) {
				
				placeholders[i] = new NumberField(Color.BURLYWOOD,Color.BLACK,40,40,25,0);
				
			} else {
				
				placeholders[i] = new OperatorField(Color.BURLYWOOD,Color.BLACK,40,40,25,"");
				
			}	
			
			placeholders[i].setText("");
			equationPane.add(placeholders[i], i, 0);	
			
		}
		
		equalLabel = new Label("=");
		equalLabel.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD,null,null)));
		equalLabel.setMinSize(40, 40);
		equalLabel.setAlignment(Pos.CENTER);
		equalLabel.setTextAlignment(TextAlignment.CENTER);
		equalLabel.setFont(Font.font(25));
		equationPane.add(equalLabel, placeholders.length, 0);
		
		resultLabel = new Label();
		resultLabel.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD,null,null)));
		resultLabel.setMinSize(100, 40);
		resultLabel.setAlignment(Pos.CENTER);
		resultLabel.setTextAlignment(TextAlignment.CENTER);
		resultLabel.setFont(Font.font(25));
		equationPane.add(resultLabel, placeholders.length+1, 0);
		
		inputMap = new HashMap<LabelField,Integer>();
		
		for(int i=0; i<numbers.length; i++) {
			
			numbers[i] = new NumberField(Color.BLACK, Color.AZURE, 40, 40, 25, numberlist.get(i));
			makeActive(numbers[i]);
			inputPane.add(numbers[i], i, 0);
			inputMap.put(numbers[i], i);
			
		}
		
		for(int i=0; i<numbers.length-1; i++) {
			for(int j=0; j<4; j++) {
				
				operators[i][j] = new OperatorField(Color.BLACK, Color.ORANGE, 40, 40, 25, OperatorField.getOperators()[j]);
				makeActive(operators[i][j]);
				inputPane.add(operators[i][j], numbers.length+j, 0);
				inputMap.put(operators[i][j], numbers.length+j);
					
			}
		}
		
		slidePane.setMinSize(equationPane.getWidth(), inputPane.getHeight()+equationPane.getHeight());
		slidePane.setMouseTransparent(true);
		
	}
	
	
	private void makeActive(LabelField l) {
		
		l.setOnMousePressed(p -> {
			
			slidePane.getChildren().add(l);
			
			l.setLayoutX(0);
			l.setLayoutY(0);
			
			l.setTranslateX(p.getSceneX()-l.getWidth()/2-this.getLayoutX());
			l.setTranslateY(p.getSceneY()-l.getHeight()/2-this.getLayoutY()-equationPane.getVgap());
		
			l.setOnMouseDragged(d -> {				
				
				l.setTranslateX(d.getSceneX()-l.getWidth()/2-this.getLayoutX());
				l.setTranslateY(d.getSceneY()-l.getHeight()/2-this.getLayoutY()-equationPane.getVgap());
				
				indicatePlacement(l);
			
			});
			
			l.setOnMouseReleased(r -> {
				
				equationPane.setBackground(null);								
				
				placeLabelField(l);
				
				
			});
			
		});
		
	}
	
	private void indicatePlacement(LabelField l) {
		
		for(int i=0; i<placeholders.length; i++) {
			
			if(isAllowed(l,placeholders[i])) {
				
				equationPane.setBackground(orangeBackground);
				return;
				
			} 
			
		}
		
		equationPane.setBackground(null);
		
	}
	
	private void placeLabelField(LabelField l) {
		
		for(int i=0; i<placeholders.length; i++) {
			
			if(isAllowed(l,placeholders[i])) {
				
				// add LabelField
				l.setTranslateX(0);
				l.setTranslateY(0);
				equationPane.add(l, i, 0);
				equation[i] = l.getText();
				
				if(equationMap.containsValue(i)) {
					
					LabelField lf = Calc.getKeyByUniqueValue(equationMap, i);
					equationMap.remove(lf, i);
					
					if(!inputPane.getChildren().contains(lf) && !l.equals(lf)) {
						
						inputPane.add(lf, inputMap.get(lf), 0);
						
					}
					
				}
				
				if(equationMap.containsKey(l)) {
					
					int index = equationMap.get(l);
					equation[index] = "";
					equationMap.remove(l);
					
				}
				
				equationMap.put(l, i);
				
				updateResult();
				
				return;
				
			} 
						
		}
		
		// put back if number
			
		putBackLabelField(l);		
		
		updateResult();
		
	}
	
	private void putBackLabelField(LabelField nf) {
		
		if(equationMap.containsKey(nf)) {
			
			int c = equationMap.get(nf);
			equation[c] = "";
			equationMap.remove(nf);
			
		}
		
		inputPane.add(nf, inputMap.get(nf), 0);
		nf.setTranslateX(0);
		nf.setTranslateY(0);
		
	}
	
	private boolean isAllowed(LabelField l, LabelField pl) {
		
		double yOffset = infoLabel.getHeight()+inputPane.getHeight()+equationPane.getVgap();
		
		if(l.getClass().equals(pl.getClass()) && Calc.isInRegion(l.getTranslateX()+l.getWidth()/2, l.getTranslateY()+l.getHeight()/2-yOffset, pl)) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	
	private void updateResult() {
		
		if(isComplete()) {
			
			result = Integer.parseInt(equation[0]);
			
			for(int i=1; i<equation.length; i+=2) {
				
				int pr = result; // result prior to operation
				int n = Integer.parseInt(equation[i+1]); // chosen number
				result = Calc.applyOperator(result,equation[i],n);
				
				if(equation[i] == "/" && pr%n != 0) {
					
					resultLabel.setText("n.d.");
					infoLabel.setText("Illegal division yields remainder: no valid result.");
					return;
					
				}
				
			}
			
			if(Math.abs(result) < 1000000) {
				
				resultLabel.setText(Integer.toString(result));
				infoLabel.setText("You are off by "+Math.abs(target-result));
				
			} else {
				
				resultLabel.setText("...");
				infoLabel.setText("You are too far off target.");
				
			}
			
			
			
		} else {
			
			resultLabel.setText("");
			infoLabel.setText("waiting...");
			
		}
		
	}

	private boolean isComplete() {
		
		for(int i=0; i<equation.length; i++) {
			
			if(equation[i].equals("")) {
				
				return false;
				
			}
			
		}
		
		return true;
		
	}
	

		
	
}
