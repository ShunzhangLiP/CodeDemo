package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    private List<FoodItem> filteredList;
    
    private int bpBranchFactor = 3;
    
    //calories, fat, carbs, fiber, protien BPTrees
    private BPTree<Double, FoodItem> calories;
    private BPTree<Double, FoodItem> fat;
    private BPTree<Double, FoodItem> carbs;
    private BPTree<Double, FoodItem> fiber;
    private BPTree<Double, FoodItem> protein;
    
    private String[] nutNames = new String[] {"calories", "fat", "carbohydrate", "fiber", "protein"};
    
    /**
     * Public constructor
     */
    public FoodData() {
    	foodItemList = new ArrayList<FoodItem>(); 
    	filteredList = new ArrayList<FoodItem>();
    	indexes = new HashMap<String, BPTree<Double, FoodItem>>();//starts nutrient hashMap
    	calories = new BPTree<Double, FoodItem>(bpBranchFactor);
    	fat = new BPTree<Double, FoodItem>(bpBranchFactor);
    	carbs = new BPTree<Double, FoodItem>(bpBranchFactor);
    	fiber = new BPTree<Double, FoodItem>(bpBranchFactor);
    	protein = new BPTree<Double, FoodItem>(bpBranchFactor);
    }
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
        // TODO : Complete
    	Scanner loadFile = null;
    	try {
    		//loading file
    		File foodFile = new File(filePath);
    		loadFile = new Scanner(foodFile);
    		//for each new line:
    		while(loadFile.hasNextLine()) {
    			String line = loadFile.nextLine();
    			//check format
    			if(!isCorrectFormat(line)) {
    				continue; //skip line if not correct format
    			}
    			//split and analyze each correctly formatted line
    			String[] lParts = line.split(",");
    			//Initialize new food item
    			FoodItem newItem = new FoodItem(lParts[0], lParts[1]);
    			foodItemList.add(newItem);
    			//adding nutrients to FoodItem
    			for(int i = 2; i < lParts.length-1; i += 2) {
    				newItem.addNutrient(lParts[i], Double.valueOf(lParts[i+1]));
    			}
    			mapNutrients(newItem); //map the new item's nutrients in BPTrees
    		}
			//sort list
			Collections.sort(foodItemList, new Comparator<FoodItem>(){
	    		@Override
				public int compare(FoodItem item1, FoodItem item2) {
	    			return item1.getName().toLowerCase().compareTo(item2.getName().toLowerCase());
	    		}
	    	});
    	}catch(FileNotFoundException noFile) {
    		// message saying file does not exist
    		System.out.println("Error: File with name " + filePath + " was not found.");
    	}catch(Exception e) {
    		//something went wrong
    		System.out.println("Unexpected Error. Contact Developers.");
    		e.printStackTrace();
    	}finally {
    		loadFile.close();
    	}
    }
    
    /**
     * Helper method that maps nutrient values in BPTrees
     * @param item
     */
    private void mapNutrients(FoodItem item) {
    	//Calories
    	calories.insert(item.getNutrientValue(nutNames[0]), item);
    	indexes.put(nutNames[0], calories);
    	//Fat
    	fat.insert(item.getNutrientValue(nutNames[1]), item);
    	indexes.put(nutNames[1], fat);
    	//Carbs
    	carbs.insert(item.getNutrientValue(nutNames[2]), item);
    	indexes.put(nutNames[2], carbs);
    	//Fiber
    	fiber.insert(item.getNutrientValue(nutNames[3]), item);
    	indexes.put(nutNames[3], fiber);
    	//Protein
    	protein.insert(item.getNutrientValue(nutNames[4]), item);
    	indexes.put(nutNames[4], protein);
    	
    	return;
    }
    /**
     * This is a helper method for loadFoodItems. It determines if a line has the correct
     * format.
     * @param line
     * @return true if the line has the correct format; false otherwise
     */
    private boolean isCorrectFormat(String line) {
    	//Checks if line is alphanumeric with commas and underscores
    	if(!line.matches("[a-zA-Z0-9_,.]+")) {
			return false;
		}
    	
    	//Checks each field
    	String[] parts = line.split(",");
    	String[] nutrients = new String[] {"calories", "fat", "carbohydrate", "fiber", "protein"};
    	
    	//Checks if there are enough fields of info
    	if(parts.length!=12) {
    		return false;
    	}
    	
    	//check if ID exists in ArrayList
    	if(!foodItemList.isEmpty()) {
    		ArrayList<String> idList = new ArrayList<String>();
    		for(int i = 0; i < foodItemList.size(); i++) {
    			idList.add(foodItemList.get(i).getID());
    		}
    		if(idList.contains(parts[0])) {
    			return false;
    		}
    	}
    	
    	//checks nutrients are in correct order and nonnegative
    	for(int i = 2; i < parts.length-1; i+=2) {
    		if(!parts[i].toLowerCase().equals(nutrients[(i/2)-1])) {
        		return false;
        	}else if(parts[i+1].matches("[0-9.]+")){
        		if(Double.valueOf(parts[i+1])<0) {
        			return false;
        		}
    		}else return false;
    	}
    	
    	return true;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
    	//There may be a quicker way to do this
    	//In the meantime, this should work
    	//Iterates through entire ArrayList.
    	filteredList = new ArrayList<FoodItem>(); //always starts with empty list
    	for(int i = 0; i < foodItemList.size(); i++) {
    		if(foodItemList.get(i).getName().toLowerCase().contains(substring.toLowerCase())) {
    			filteredList.add(foodItemList.get(i));
    		}
    	}
    	
        return filteredList;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
    	//for each set of rules
    	for(int i = 0; i < rules.size(); i++) {
    		String[] rulePart = rules.get(i).split(" "); //analyze rule
    		if(rulePart.length != 3) {
    			continue; // skip if wrong format
    		}
    		//First Valid rule populates Filtered List if not filtered by name first
    		if(filteredList.isEmpty()) {
    			switch(rulePart[0].toLowerCase()) {
    			case "calories":
    				filteredList.addAll(calories.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			case "fat":
    				filteredList.addAll(fat.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			case "carbohydrate":
    				filteredList.addAll(carbs.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			case "fiber":
    				filteredList.addAll(fiber.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			case "protein":
    				filteredList.addAll(protein.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			default:
    				continue; //doesn't match any nutrient filter available
    			}
    		}else { //After First Valid rule or after filterByName, delete items that don't follow rules
    			switch(rulePart[0].toLowerCase()) {
    			case "calories":
    				filteredList.retainAll(calories.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			case "fat":
    				filteredList.retainAll(fat.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			case "carbohydrate":
    				filteredList.retainAll(carbs.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			case "fiber":
    				filteredList.retainAll(fiber.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			case "protein":
    				filteredList.retainAll(protein.rangeSearch(Double.valueOf(rulePart[2]), rulePart[1]));
    				break;
    			default:
    				continue; //doesn't match any nutrient filter available
    			}
    		}
    	}
        return filteredList;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
    	if(!foodItemList.isEmpty()) {
    		ArrayList<String> idList = new ArrayList<String>();
    		for(int i = 0; i < foodItemList.size(); i++) {
    			idList.add(foodItemList.get(i).getID());
    		}
    		if(!idList.contains(foodItem.getID())) {
    			foodItemList.add(foodItem);
    			mapNutrients(foodItem);//add item to hash table of nutrients
    			//sorts master food list
    			Collections.sort(foodItemList, new Comparator<FoodItem>(){
    				@Override
    				public int compare(FoodItem item1, FoodItem item2) {
    					return item1.getName().toLowerCase().compareTo(item2.getName().toLowerCase());
    				}
    			});
    		}
    	}else {
    		foodItemList.add(foodItem);
    		mapNutrients(foodItem);//add item to hash table of nutrients
    	}
    	return;
    	
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
    	filteredList.clear(); //clears filters
        return foodItemList; //that should be it
    }
    
    /**
     * Saves the current master list to the input file path.
     */
    public void saveFoodItems(String filename) {
         try {
            FileWriter writer = new FileWriter(filename);
            for(FoodItem food: foodItemList) {
            	HashMap<String, Double> nuts = food.getNutrients();
            	//ID,name,calories,calories value
                writer.write(food.getID() + "," + food.getName() + ",calories," + food.getNutrientValue("calories"));
                //,fat,fat value
                writer.write(",fat," + nuts.get("fat"));
                //,carbohydrate,carbs value
                writer.write(",carbohydrate," + nuts.get("carbohydrate"));
                //,fiber,fiber value
                writer.write(",fiber," + nuts.get("fiber"));
                //,protein, protein value and new line
                writer.write(",protein," + nuts.get("protein"));
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
