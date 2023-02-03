package game;

import java.util.List;

public class Solution {

	private List<String> operators;
	private List<Integer> numbers;
	private int result;
	private String equation;
	
	public Solution(List<Integer> numbers, List<String> operators, int result) {
		
		this.numbers = numbers;
		this.operators = operators;
		this.result = result;
		setEquation();
		
	}
	
	private void setEquation() {
				
		equation = Integer.toString(numbers.get(0));
		
		for(int i=0; i<operators.size(); i++) {
			
			String op = operators.get(i);
			equation = equation.concat(" |"+op);
			String n = Integer.toString(numbers.get(i+1));
			equation = equation.concat(n);
			
		}
		
		equation = equation.concat(" = ");
		equation = equation.concat(Integer.toString(result));
		
	}
	
	public String getEquation() {
		
		return equation;
		
	}
	
}
