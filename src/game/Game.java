package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import application.Handler;
import util.Calc;

public class Game {

	private int target;
	private List<Integer> numbersList;
	// split available numbers in two groups ('small' and 'large')
	private List<Integer> smallNumbersList;
	private List<Integer> largeNumbersList;
	private Random r;
	private final String[] operators = {"+","-","*","/"};
	private List<List<Integer>> permIndexList; // stores all possible permutations of number indices (1 to 6)
	private List<List<String>> combOperatorsList; // stores all possible combinations of operators (5 operations for 6 numbers)
	
	private final int LOW = 100, HIGH = 999; // target number limits
	private final int[] POWERS_OF_100 = {1, 100, 10000, 1000000, 100000000}; // stores all powers of 100 that fit into an int (utility)
	
	public Game(Handler handler) {
		
		r = new Random();
		
		numbersList = new ArrayList<Integer>();
		smallNumbersList = new ArrayList<Integer>();	
		largeNumbersList = new ArrayList<Integer>();
		
		setIndexAndOperatorsLists();
		
	}
	
	public synchronized void setRandomGame(Handler handler) {
		
		reset();
		
		target = r.nextInt(HIGH-LOW) + LOW;
		handler.getTargetField().setValue(target);
		setRandomNumbers();
		sendNewNumbersList(handler);
		
		handler.getSolveButton().setDisable(false);
		
		handler.getTestArea().set(numbersList, target);
		
	}
	
	public synchronized void setCustomGame(Handler handler, List<Integer> numbers) {
		
		reset();
		
		target = numbers.get(numbers.size()-1);
		handler.getTargetField().setValue(target);
		numbersList = numbers.subList(0, numbers.size()-1);
		sendNewNumbersList(handler);
		
		handler.getSolveButton().setDisable(false);
		
		handler.getTestArea().set(numbersList, target);
		
	}
	
	private void reset() {
		
		smallNumbersList.clear();
		largeNumbersList.clear();
		numbersList.clear();
		
		for(int i=1; i<=20; i++) {
			
			smallNumbersList.add(i);
			smallNumbersList.add(i);
			
		}
		
		Collections.addAll(largeNumbersList, 25,50,75,100);
		
	}
	
	private synchronized void setIndexAndOperatorsLists() {
		
		combOperatorsList = new ArrayList<List<String>>();		
		List<String> combList = new ArrayList<String>();
		setCombList(combOperatorsList,combList,operators,5,0);
		
		permIndexList = new ArrayList<List<Integer>>();
		List<Integer> indexList = new ArrayList<Integer>();
		
		for(int i=0; i<6; i++) {
			
			indexList.add(i);
			
		}
		
		getPermList(permIndexList,indexList,0);
		
	}
	
	private void setRandomNumbers() {

		int nlarge = r.nextInt(5); // randomly choose how many large numbers are to be used
		
		for(int i=0; i<nlarge; i++) {
			
			int s = largeNumbersList.size();
			int index = r.nextInt(s);
			numbersList.add(largeNumbersList.get(index));
			largeNumbersList.remove(index);
			
		}
		
		int nsmall = 6 - nlarge;
		
		for(int i=0; i<nsmall; i++) {
			
			int s = smallNumbersList.size();
			int index = r.nextInt(s);
			numbersList.add(smallNumbersList.get(index));
			smallNumbersList.remove(index);
			
		}
		
		Collections.sort(numbersList);
		
	}

	
	private void sendNewNumbersList(Handler handler) {
		
		for(int i=0; i<6; i++) {
			
			handler.getNumberFields()[i].setText(Integer.toString(numbersList.get(i)));
			
		}
		
		
	}
	
	public int getTarget() {
		
		return target;
		
	}
	
	public List<Integer> getNumbersList(){
		
		return numbersList;
		
	}
	
	private void setCombList(List<List<String>> combOpsList, List<String> l, String[] ops, int limit, int level) {
		
		
		if(level == limit) {
			
			combOpsList.add(new ArrayList<>(l));
			return;
			
		}
		
		for(String s:ops) {
			
			l.add(s);
			setCombList(combOpsList,l,ops,limit,level+1);
			l.remove(s);
			
		}
		
		
		
	}
	
	private void getPermList(List<List<Integer>> permlist, List<Integer> indexList, int sid){
		
		// get all permutations of a list of distinct integers
		
		if(sid == indexList.size()-1) {
			
			permlist.add(new ArrayList<>(indexList));
			return;
			
		}
		
		for(int i=sid; i<indexList.size(); i++) {
			
			swap(indexList,i,sid);
			getPermList(permlist,indexList,sid+1);
			swap(indexList,i,sid);
			
		}
		
		
		
		
	}
	
	
	private <T> void swap(List<T> list, int i, int j) {
		
		T temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
		
	}
	

	
	public void solve(Handler handler) {
		
		LinkedHashMap<String,Integer> sortedResults = sortMap(getResults());
		handler.getResultLabel().setText(sortedResults.keySet().iterator().next());
		
	}
	
	private LinkedHashMap<String,Integer> getResults() {
		
		// maps each permutation (of numbers and operators) to their respective result-distance from target e.g. for target=408: "7*18-75*10-100-1" -> 1
		LinkedHashMap<String,Integer> map = new LinkedHashMap<String,Integer>(); 
		
		int result = 0;
		
		for(List<Integer> li: permIndexList){
		  
			int si = li.get(0);

		  	List<Integer> numbers = new ArrayList<Integer>();
		  	
		  	for(int index=0; index<li.size(); index++) {
		  		
		  		numbers.add(numbersList.get(li.get(index)));
		  		
		  	}
		  	
			for(List<String> lop: combOperatorsList){
				
				boolean complete = true;
				result = numbersList.get(si);
				
				
				for(int i=0; i<lop.size();i++) {
					
					String op = lop.get(i);
					int n = numbersList.get(li.get(i+1));
					
					// if outside target range then break
					if(i > 2 && Math.abs(target-result) > POWERS_OF_100[lop.size()-i]) {
	     				
						complete = false;
			     		break;
			     	
					}
			     		
					int pr = result; // result prior to operation
					result = Calc.applyOperator(result,op,n);
					
			     	// if illegal division (=> applyOperator does not change result value) then break
			     	if(op == "/" && pr%n != 0) {
			     		
			     		complete = false;
			     		break;
			     		
			     	}
					
				}
		      	
				// only record result if relevant
				if(complete) {
					
					int dif = Math.abs(target-result);
					Solution S = new Solution(numbers,lop,result);
					String s = S.getEquation();
				    map.put(s,dif);					
					
				}
   
		    }
		      			  	
		  	
		}
		  
		return map;
		
	}

	
	private LinkedHashMap<String,Integer> sortMap(LinkedHashMap<String,Integer> lhm) {
		
		LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
		
		lhm.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> {
			
			sortedMap.put(entry.getKey(),entry.getValue());
			
		});
		
		return sortedMap;
		
	}
	

	
}
