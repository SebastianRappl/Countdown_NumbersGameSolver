package application;

import elements.CustomGameDialog;
import elements.NumberField;
import elements.TestArea;
import game.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Handler extends VBox{

	private HBox buttonbox, solvebuttonbox, targetBox, numbersBox, resultBox;
	private Button randomgamebutton, customgamebutton, solvebutton;
	private NumberField targetField;
	private NumberField[] numberField = new NumberField[6];
	private Game game;
	private Label resultlabel;
	private TestArea testArea;
	
	public Handler() {
		
		Insets VerticalPadding = new Insets(20,0,20,0);
		
		buttonbox = new HBox();
		randomgamebutton = new Button("New random game");
		randomgamebutton.setStyle("-fx-background-color: rgba(133, 238, 242, 0.8)");
		randomgamebutton.setOnAction(e -> { 
			
			game.setRandomGame(this);
			resultlabel.setText("press solve to see closest solution");
			
		});
		
		customgamebutton = new Button("New custom game");
		customgamebutton.setStyle("-fx-background-color: rgba(210, 159, 214, 0.8)");
		customgamebutton.setOnAction(e -> { 
			
			new CustomGameDialog(this,this.getWidth()/2,2*this.getHeight()/3);
			
		});
		
		buttonbox.getChildren().addAll(randomgamebutton,customgamebutton);
		
		targetBox = new HBox();
		targetBox.setAlignment(Pos.CENTER);
		targetField = new NumberField(Color.BLACK, Color.YELLOW, 200, 100, 40,0);
		targetField.setText("");
		targetBox.getChildren().add(targetField);
		
		numbersBox = new HBox();
		numbersBox.setAlignment(Pos.CENTER);
		
		// loop to fill in the six numberfields 	
		for(int i=0; i<6; i++) {
			
			numberField[i] = new NumberField(Color.NAVY, Color.WHITE, 100, 50, 20,0);
			numberField[i].setText("");
			numbersBox.getChildren().add(numberField[i]);
			
		}
		
		solvebuttonbox = new HBox();
		solvebuttonbox.setPadding(VerticalPadding);
		solvebuttonbox.setAlignment(Pos.CENTER);
		solvebutton = new Button("solve");
		solvebutton.setDisable(true);
		solvebutton.setOnAction(e -> {
			
			solvebutton.setDisable(true);
			game.solve(this);

		});
		
		solvebuttonbox.getChildren().add(solvebutton);
		
		resultBox = new HBox();
		resultBox.setPadding(VerticalPadding);
		resultBox.setAlignment(Pos.CENTER);
		resultlabel = new Label();
		resultlabel.setFont(Font.font(20));
		resultlabel.setTextAlignment(TextAlignment.CENTER);
		resultBox.getChildren().add(resultlabel);
		
		testArea = new TestArea();
		testArea.setAlignment(Pos.CENTER);
		testArea.setPadding(VerticalPadding);
		
		this.getChildren().addAll(buttonbox, targetBox, numbersBox, solvebuttonbox, resultBox, testArea);
		
		game = new Game(this);
		
	}
	
	public Label getResultLabel() {
		
		return resultlabel;
		
	}
	
	public Button getSolveButton() {
		
		return solvebutton;
		
	}
	
	public NumberField getTargetField() {
		
		return targetField;
		
	}
	
	public NumberField[] getNumberFields() {
		
		return numberField;
		
	}
	
	public Game getGame() {
		
		return game;
		
	}
	
	public TestArea getTestArea() {
		
		return testArea;
		
	}
	
	public void setTestArea(TestArea testArea) {
		
		this.testArea = testArea;
		
	}
	
}
