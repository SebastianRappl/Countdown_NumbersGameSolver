package elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import application.Handler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import util.Calc;

public class CustomGameDialog extends Dialog<List<Integer>> {

	private StackPane stackpane;
	private Pane slidepane;
	private NumberField[] smallNumbers, bigNumbers, chosenNumbers;
	private VBox contentbox, slidebox, buttonbox;
	private ReelBox reelbox;
	private GridPane smallNumberPane, bigNumberPane, chosenNumberPane;
	private List<Integer> gameNumbers;
	private boolean selected = false;
	private double dragXOffset, dragYOffset;
	private Color gb,rb;
	private Background greenBackground, redBackground;
	private Button confirmButton;
	
	public CustomGameDialog(Handler handler, double width, double height) {
		
		this.setTitle("Custom game setup");
		this.setWidth(width);
		this.setHeight(height);
		setLayout(handler,width);
		
		smallNumbers = new NumberField[40];
		bigNumbers = new NumberField[4];
		chosenNumbers = new NumberField[6];
		
		initializeNumberFields(50,50);
		
		gameNumbers = new ArrayList<Integer>();
		
		setCloseOperation();
		
		this.show();
		
	}
	
	private void initializeNumberFields(int width, int height) {
		
		for(int i=0; i<smallNumbers.length; i++) {
			
			smallNumbers[i] = new NumberField(Color.DARKGRAY, Color.WHITE, width, height, 20, i/2+1);
			makeActive(smallNumbers[i]);
			smallNumberPane.add(smallNumbers[i], (i/2)%10, (i/2)/10);
			
		}
		
		for(int i=0; i<bigNumbers.length; i++) {
			
			bigNumbers[i] = new NumberField(Color.DARKGRAY, Color.WHITE, width, height, 20, 25*(i+1));
			makeActive(bigNumbers[i]);
			bigNumberPane.add(bigNumbers[i], i, 0);
			
		}
		
		for(int i=0; i<chosenNumbers.length; i++) {
			
			chosenNumbers[i] = new NumberField(Color.DARKGREEN, Color.WHITE, width, height, 20, 0);
			chosenNumbers[i].setText("");
			chosenNumberPane.add(chosenNumbers[i], i, 0);
			
		}
		
		setRemove(chosenNumbers);
		
	}
	
	private void setLayout(Handler handler, double width) {
		
		gb = Color.rgb(154, 204, 143);
		rb = Color.rgb(214, 108, 101);
		
		greenBackground = new Background(new BackgroundFill(gb,null,null));
		redBackground = new Background(new BackgroundFill(rb,null,null));
		
		smallNumberPane = new GridPane();
		smallNumberPane.setAlignment(Pos.CENTER);
		smallNumberPane.setHgap(10);
		smallNumberPane.setVgap(10);
		bigNumberPane = new GridPane();
		bigNumberPane.setAlignment(Pos.CENTER);
		bigNumberPane.setHgap(10);
		bigNumberPane.setVgap(10);
		bigNumberPane.setPadding(new Insets(20,0,40,0));
		chosenNumberPane = new GridPane();
		chosenNumberPane.setAlignment(Pos.CENTER);
		chosenNumberPane.setHgap(10);
		chosenNumberPane.setVgap(10);
		chosenNumberPane.setPadding(new Insets(20,0,20,0));
		
		confirmButton = new Button("confirm");
		confirmButton.setDisable(true);
		confirmButton.setOnAction(e -> {			
				
			int target = reelbox.getValue();
			List<Integer> datalist = new ArrayList<Integer>(gameNumbers);
			datalist.add(target);
			this.setResult(datalist);
			handler.getGame().setCustomGame(handler,datalist);
			handler.getResultLabel().setText("press solve to see closest solution");
			this.close();
				
		});
		confirmButton.setAlignment(Pos.CENTER);
		
		buttonbox = new VBox(confirmButton);
		buttonbox.setAlignment(Pos.CENTER);
		buttonbox.setMinHeight(confirmButton.getHeight()+50);
		
		slidebox = new VBox();
		slidebox.setAlignment(Pos.CENTER);
		slidebox.getChildren().addAll(smallNumberPane,bigNumberPane,chosenNumberPane,buttonbox);
		
		slidepane = new Pane();
		slidepane.setMinWidth(this.getWidth());
		slidepane.setMinHeight(this.getHeight());
		slidepane.setMouseTransparent(true);
		
		stackpane = new StackPane();
		stackpane.getChildren().addAll(slidebox,slidepane);
		
		reelbox = new ReelBox(width,width/3);
		
		contentbox = new VBox();
		contentbox.setAlignment(Pos.CENTER);
		contentbox.getChildren().addAll(reelbox,stackpane);
		
		this.getDialogPane().setContent(contentbox);
		
	}
	
	
	private void makeActive(NumberField numberfield) {
		
		numberfield.setOnMousePressed(e -> {
			
			if(e.getButton() == MouseButton.PRIMARY && !selected) {
				
				selected = true;
				GridPane parent = (GridPane) numberfield.getParent();
				parent.getChildren().remove(numberfield);
				dragXOffset = parent.getHgap();
				dragYOffset = parent.getVgap();
				numberfield.setTranslateX(e.getSceneX()-numberfield.getLayoutX()-numberfield.getWidth()/2-dragXOffset);
				numberfield.setTranslateY(e.getSceneY()-numberfield.getLayoutY()-numberfield.getHeight()/2-dragYOffset-reelbox.getHeight());
				slidepane.getChildren().add(numberfield);
				slidepane.setMouseTransparent(false);
				
			}
			
		});
		
		numberfield.setOnMouseDragged(e -> {
			
			if(selected) {
				
				numberfield.setTranslateX(e.getSceneX()-numberfield.getLayoutX()-numberfield.getWidth()/2-dragXOffset);
				numberfield.setTranslateY(e.getSceneY()-numberfield.getLayoutY()-numberfield.getHeight()/2-dragYOffset-reelbox.getHeight());
				
				if(Calc.isInRegion(e.getSceneX(),e.getSceneY()-reelbox.getHeight(),chosenNumberPane)) {
					
					if(gameNumbers.size() < chosenNumbers.length) {
						
						chosenNumberPane.setBackground(greenBackground);
						
					} else {
						
						chosenNumberPane.setBackground(redBackground);
						
					}
					
					
				} else {
					
					chosenNumberPane.setBackground(null);
					
				}
				
			}
			
			
			
		});
		
		numberfield.setOnMouseReleased(e -> {
			
			if(selected) {
				
				selected = false;
				slidepane.setMouseTransparent(true);
				slidepane.getChildren().remove(numberfield);
				chosenNumberPane.setBackground(null);

				int index = gameNumbers.size();
				int number = numberfield.getValue();
				
				if(Calc.isInRegion(e.getSceneX(),e.getSceneY()-reelbox.getHeight(),chosenNumberPane) && gameNumbers.size() < chosenNumbers.length) {

					gameNumbers.add(index,number);
					chosenNumbers[index].setValue(number);
					updateChosenNumbers();
					
				} else {
					
					numberfield.setTranslateX(0);
					numberfield.setTranslateY(0);
					
					if(number > smallNumbers.length/2) {
						
						bigNumberPane.add(numberfield, (number/25)-1, 0);
						
					} else {
						
						smallNumberPane.add(numberfield, (number-1)%10, (number-1)/10);
						
					}
					
				}
				
			}
			
		});
		
	}
	
	private void setRemove(NumberField[] numberfields) {
		
		for(NumberField nf:numberfields) {
			
			nf.setOnMouseClicked(e -> {
				
				int number = nf.getValue();
				
				if(e.getButton() == MouseButton.SECONDARY && number > 0) {
					
					putBackNumber(number);
					
					removeIntFromList(number,gameNumbers);
					nf.setValue(0);
					nf.setText("");
					updateChosenNumbers();
					
				}
				
			});
			
		}	
		
	}
	
	private void putBackNumber(int value) {
		
		NumberField nf;
		
		if(value > 20) {
			
			nf = new NumberField(bigNumbers[value/25-1]);
			bigNumberPane.add(nf,value/25-1,0);
			
		} else {
			
			nf = new NumberField(smallNumbers[2*value-2]);
			smallNumberPane.add(nf,(value-1)%10,value/10);
			
		}
		
		makeActive(nf);
		
	}
	
	private void updateChosenNumbers() {
		
		Collections.sort(gameNumbers);
		
		for(int i=0; i<chosenNumbers.length; i++) {
			
			if(i < gameNumbers.size()) {
				
				chosenNumbers[i].setValue(gameNumbers.get(i));
				chosenNumbers[i].setText(Integer.toString(chosenNumbers[i].getValue()));
				
			} else {
				
				chosenNumbers[i].setValue(0);
				chosenNumbers[i].setText("");
				
			}
			
		}
		
		if(gameNumbers.size() == 6) {
			
			confirmButton.setDisable(false);
			
		} else {
			
			confirmButton.setDisable(true);
			
		}
		
	}
	
	private void removeIntFromList(int n, List<Integer> intlist) {
		
		for(int i=0; i<intlist.size(); i++) {
			
			if(intlist.get(i) == n) {
				
				intlist.remove(i);
				break;
				
			}
			
		}
		
	}
	
	
	private void setCloseOperation() {
		
		Window w = this.getDialogPane().getScene().getWindow();
		w.setOnCloseRequest(e -> {
			
			this.close();
			
		});
		
	}
	
}
