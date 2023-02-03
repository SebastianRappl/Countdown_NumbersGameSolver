package util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javafx.scene.layout.Region;

public class Calc {

	// applies operator (returns input value if operation is division with remainder != 0)
	public static int applyOperator(int result, String operator, int number) {
		
		if(operator.equals("+")){
		      
			result += number;

		} else if(operator.equals("-")){
       
			result -= number;
       
		} else if(operator.equals("*")){
       
			result *= number;
      
		} else if(operator.equals("/")){
       
			if(result%number == 0){
       
				result /= number;
       
			} 
      
		}
		
		return result;
		
	}
	
	public static boolean isInRegion(double x, double y, Region r) {
		
		if(x >= r.getLayoutX() && x <= r.getLayoutX()+r.getWidth() && y >= r.getLayoutY() && y <= r.getLayoutY()+r.getHeight()) {
			
			return true;
			
		}
		
		return false;
		
	}
	
	public static <T, E> T getKeyByUniqueValue(Map<T, E> map, E value) {
	    
		for (Entry<T, E> entry : map.entrySet()) {
			
	        if (Objects.equals(value, entry.getValue())) {
	            
	        	return entry.getKey();
	        	
	        }
	        
	    }
		
	    return null;
	}
	
}
