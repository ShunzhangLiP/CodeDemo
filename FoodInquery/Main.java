/**
 * CS 400 Final Milestone 3 Dec.11th 2018 Authors: A-team 91
 * BRENNA KINDER, DAVID KLONGLAND, SHUNZHANG LI, WILLIAM WOODWARD, XUAN ZHANG Date: Nov.30th 2018
 * Progress MileStone 3
 * 
 * This class represents the GUI of the FoodQuery project. 
 * It controls the functionality of the program and uses the FoodData and FoodItem class during its runtime
 *
 * @author David Klongland, 
 */


package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;



/**
 * Main method of the UI
 *
 */
@SuppressWarnings("unused")
public class Main extends Application {
	
	FoodData data = new FoodData(); 
	@SuppressWarnings("rawtypes")
	ListView foodList = new ListView();
	ListView mealList = new ListView();
	ArrayList<FoodItem> items = new ArrayList<FoodItem>();
	
	
  @Override
  public void start(Stage primaryStage) {
    try {
      primaryStage.setTitle("Meal Planner");
      BorderPane root = new BorderPane();

      root.setLeft(leftColumn());
      root.setCenter(centerColumn());
      root.setRight(rightColumn());

      Scene scene = new Scene(root, 700, 700);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Left Column of the borderPane
   * Contains foodlist, food query and data input
   * @return
   */
  @SuppressWarnings("unchecked")
  private VBox leftColumn() {
	  
    
    // Food List's title, listview and sample items in it
    Label foodLabel = new Label("Master Food List [?]"); //Added "Master " for clarity
    foodList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    //foodList.getItems().add("Apples");
    //foodList.getItems().add("Apricots");
    //foodList.getItems().add("Avacado");
    Tooltip help = new Tooltip("Mouse over the buttons and labels to get a tip on how to use the program");
    foodLabel.setTooltip(help);
    Tooltip toolTip1 = new Tooltip("Opens a tab where you can add a custom food item, including name and nutrients");
    //a text view to add food into food list
    //Label customFood = new Label("Custom Food");
    //TextField textField = new TextField();
    Button addCustom = new Button("Add custom food");
    addCustom.setTooltip(toolTip1);
    //addCustom button function....
    //when the buttons are clicked, a new window appears with the info needed to be entered
    addCustom.setOnAction(actionEvent ->  {
    		BorderPane ok = new BorderPane();
    		VBox v = new VBox();
    		
    		HBox hName = new HBox();
    		HBox hCal = new HBox();
    		HBox hFat = new HBox();
    		HBox hCarb = new HBox();
    		HBox hFiber = new HBox();
    		HBox hProtein = new HBox();
    		HBox hID = new HBox();
    		
    		hName.setSpacing(10);
    		hName.setStyle("-fx-background-color: #BCDEEC;");
    		hCal.setSpacing(10);
    		hCal.setStyle("-fx-background-color: #BCDEEC;");
    		hFat.setSpacing(10);
    		hFat.setStyle("-fx-background-color: #BCDEEC;");
    		hCarb.setSpacing(10);
    		hCarb.setStyle("-fx-background-color: #BCDEEC;");
    		hFiber.setSpacing(10);
    		hFiber.setStyle("-fx-background-color: #BCDEEC;");
    		hProtein.setSpacing(10);
    		hProtein.setStyle("-fx-background-color: #BCDEEC;");
    		hID.setSpacing(10);
    		hID.setStyle("-fx-background-color: #BCDEEC;");
    		
    	    Label addNameL = new Label("Name");
    	    TextField textName = new TextField();
    	    Label addCal = new Label("Calories");
    	    TextField textCal = new TextField();
    	    Label addFat = new Label("Fat");
    	    TextField textFat = new TextField();
    	    Label addCarb = new Label("Carbs");
    	    TextField textCarb = new TextField();
    	    Label addFiber = new Label("Fiber");
    	    TextField textFiber = new TextField();
    	    Label addProtein = new Label("Protein");
    	    TextField textProtein = new TextField();
    	    Label addID = new Label("ID");
    	    TextField textID = new TextField();
    	    
    	    
    	    
    	    hName.getChildren().addAll(addNameL, textName);
    	    hCal.getChildren().addAll(addCal, textCal);
    	    hFat.getChildren().addAll(addFat, textFat);
    	    hCarb.getChildren().addAll(addCarb, textCarb);
    	    hFiber.getChildren().addAll(addFiber, textFiber);
    	    hProtein.getChildren().addAll(addProtein, textProtein);
    	    hID.getChildren().addAll(addID, textID);
    	    
    	    Button add = new Button("Add Custom Food [?]");
    	    Tooltip tool = new Tooltip("Only letters for the name, only integers for the nutrients, no spaces - underscores are acceptaple");
    	    add.setTooltip(tool);
    	    
    	    
    	    v.getChildren().addAll(hName,hCal,hFat,hCarb,hFiber,hProtein,hID,add);
    	    ok.setCenter(v);
    		Scene root2 = new Scene(ok, 300,220);
    		Stage stage = new Stage();
    	
    		stage.setScene(root2);
    		stage.show();

    	    add.setOnAction(new EventHandler<ActionEvent>() {
    	        @Override public void handle(ActionEvent e) {
        	    	try {
    	        	String name = textName.getText();
    	        	String id;
    	        	if(textID.getText().isEmpty()) {
    	        		id = "1337";
    	        	}
    	        	else {
    	        	id = textID.getText();
    	        	}
    	        	double cal;
    	        double protein;
    	        	double fiber;
    	        	double carb;
    	        	double fat;
    	        	if(!textCal.getText().isEmpty()) {
    	        		cal = Double.parseDouble(textCal.getText());
    	        	}
    	        	else {
    	        		cal = 0;
    	        	}
    	        	if(!textProtein.getText().trim().isEmpty()) {
    	        		protein = Double.parseDouble(textProtein.getText());
    	        	}
    	        	else {
    	        		protein = 0;
    	        	}
    	        	if(!textFiber.getText().trim().isEmpty()) {
    	        		fiber = Double.parseDouble(textFiber.getText());
    	        	}
    	        	else {
    	        		fiber = 0;
    	        	}
    	        	if(!textCarb.getText().trim().isEmpty()) {
    	        		carb = Double.parseDouble(textCarb.getText());
    	        	}
    	        	else {
    	        		carb = 0;
    	        	}
    	        	if(!textFat.getText().trim().isEmpty()) {
    	        		fat = Double.parseDouble(textFat.getText());
    	        	}
    	        	else {
    	        		fat = 0;
    	        	}
    	        	
    	        	FoodItem item = new FoodItem(id, name);
    	        	item.addNutrient("calories", cal);
    	        	item.addNutrient("fat", fat);
    	        	item.addNutrient("protein", protein);
    	        	item.addNutrient("fiber", fiber);
    	        	item.addNutrient("carbohydrate", carb);
    	        	data.addFoodItem(item);
    	        	
    	        	foodList.getItems().clear();

    	    		items = (ArrayList<FoodItem>) data.getAllFoodItems();
    	    		for(int i = 0; i<items.size();i++) {
    	    			foodList.getItems().add(items.get(i).getName() + " " + items.get(i).getID());
    	    		}
    	        	stage.close();
    	        
    	        }
    	    	catch(Exception e1) {
    	    		e1.printStackTrace();
    	    	}

        	    }
    	    });
    });
  
    
    
    //Button to read in data file
    Button addCustomList = new Button("Add custom food list");
    Tooltip toolTip2 = new Tooltip("Opens a tab where you can import a custom food list, make sure to give the correct path to the file");
    addCustomList.setTooltip(toolTip2);
    addCustomList.setOnAction(actionEvent ->  {
    		File filePath;
    		FileChooser fileChooser = new FileChooser();
    		Stage stage = new Stage();
    		File home = new File(".");
    		fileChooser.setInitialDirectory(home);
        filePath = fileChooser.showOpenDialog(stage);
		data.loadFoodItems(filePath.toString());
		items = (ArrayList<FoodItem>) data.getAllFoodItems();
		
		foodList.getItems().clear();
		for(int i = 0; i<items.size();i++) {
			foodList.getItems().add(items.get(i).getName() + " " + items.get(i).getID());
		}
		
    });
    
    //Button to save food list to data file
    Button saveCustomList = new Button("Save custom food list");
    Tooltip toolTip3 = new Tooltip("Saves the currect food list as a file stored in the program files");
    saveCustomList.setTooltip(toolTip3);
    saveCustomList.setOnAction(actionEvent ->  {
		FileChooser fileChooser = new FileChooser();
		Stage stage = new Stage();
		File file = fileChooser.showSaveDialog(stage);
		data.saveFoodItems(file.toString());
		//fileChooser.setInitialDirectory(value);
    });
    
    // list of everything user want to analysis
    Tooltip toolTip4 = new Tooltip("Select a check box and put in a number, which will be the upper bounds of what is allowed, to query the list.");
    VBox checkList = new VBox();
    
    VBox v = new VBox();
	
	HBox hName = new HBox();
	HBox hCal = new HBox();
	HBox hFat = new HBox();
	HBox hCarb = new HBox();
	HBox hFiber = new HBox();
	HBox hProtein = new HBox();
	
	hName.setSpacing(10);
	hName.setStyle("-fx-background-color: #BCDEEC;");
	hCal.setSpacing(10);
	hCal.setStyle("-fx-background-color: #BCDEEC;");
	hFat.setSpacing(10);
	hFat.setStyle("-fx-background-color: #BCDEEC;");
	hCarb.setSpacing(10);
	hCarb.setStyle("-fx-background-color: #BCDEEC;");
	hFiber.setSpacing(10);
	hFiber.setStyle("-fx-background-color: #BCDEEC;");
	hProtein.setSpacing(10);
	hProtein.setStyle("-fx-background-color: #BCDEEC;");
	
    Label addNameL = new Label("Name");
    TextField textName = new TextField();
    Label addCal = new Label("Calories - Lower/Upper");
    TextField textCal = new TextField();
    TextField textCalUp = new TextField();
    textCal.setPrefSize(40, 40);
    textCalUp.setPrefSize(40, 40);
    Label addFat = new Label("Fat - Lower/Upper");
    TextField textFat = new TextField();
    TextField textFatUp = new TextField();
    textFat.setPrefSize(40, 40);
    textFatUp.setPrefSize(40, 40);
    Label addCarb = new Label("Carbs - Lower/Upper");
    TextField textCarb = new TextField();
    TextField textCarbUp = new TextField();
    textCarbUp.setPrefSize(40, 40);
    textCarb.setPrefSize(40, 40);
    Label addFiber = new Label("Fiber - Lower/Upper");
    TextField textFiber = new TextField();
    TextField textFiberUp = new TextField();
    textFiberUp.setPrefSize(40, 40);
    textFiber.setPrefSize(40, 40);
    Label addProtein = new Label("Protein - Lower/Upper");
    TextField textProtein = new TextField();
    TextField textProteinUp = new TextField();
    textProteinUp.setPrefSize(40, 40);
    textProtein.setPrefSize(40, 40);
    
    hName.getChildren().addAll(addNameL, textName);
    hCal.getChildren().addAll(addCal, textCal,textCalUp);
    hFat.getChildren().addAll(addFat, textFat,textFatUp);
    hCarb.getChildren().addAll(addCarb, textCarb,textCarbUp);
    hFiber.getChildren().addAll(addFiber, textFiber,textFiberUp);
    hProtein.getChildren().addAll(addProtein, textProtein,textProteinUp);
    v.getChildren().addAll(hName, hCal, hFat, hCarb, hFiber, hProtein);
    Button query = new Button("Apply filters [?]");
    Tooltip toolTip0 = new Tooltip("First box is lower bound of said value, next is upper bound of said value (doule only). Name is an exception.");
    query.setTooltip(toolTip0);
    
    //button to to take action on above query rules
    query.setOnAction(actionEvent ->  {

        double calLow;
        double calUp;
        double fatLow;
        double fatUp;
        double fiberLow;
        double fiberUp;
        double carbLow;
        double carbUp;
        double proteinUp;
        double proteinLow;
    		items = (ArrayList<FoodItem>) data.filterByName(textName.getText());
    		ArrayList<String> rules = new ArrayList<String>();
    		if(!textCal.getText().isEmpty()) {
    			calLow = Double.parseDouble(textCal.getText());
        	}
        	else {
        		calLow = 0;
        	}
    		if(!textCalUp.getText().isEmpty()) {
    			calUp = Double.parseDouble(textCalUp.getText());
        	}
        	else {
        		calUp = Double.MAX_VALUE;
        	}
        	if(!textProtein.getText().trim().isEmpty()) {
        		proteinLow = Double.parseDouble(textProtein.getText());
        	}
        	else {
        		proteinLow = 0;
        	}
        	if(!textProteinUp.getText().trim().isEmpty()) {
        		proteinUp = Double.parseDouble(textProteinUp.getText());
        	}
        	else {
        		proteinUp = Double.MAX_VALUE;
        	}
        	if(!textFiber.getText().trim().isEmpty()) {
        		fiberLow = Double.parseDouble(textFiber.getText());
        	}
        	else {
        		fiberLow = 0;
        	}
        	if(!textFiberUp.getText().trim().isEmpty()) {
        		fiberUp = Double.parseDouble(textFiberUp.getText());
        	}
        	else {
        		fiberUp = Double.MAX_VALUE;
        	}
        	if(!textCarb.getText().trim().isEmpty()) {
        		carbLow = Double.parseDouble(textCarb.getText());
        	}
        	else {
        		carbLow = 0;
        	}
        	if(!textCarbUp.getText().trim().isEmpty()) {
        		carbUp = Double.parseDouble(textCarbUp.getText());
        	}
        	else {
        		carbUp = Double.MAX_VALUE;
        	}
        	if(!textFat.getText().trim().isEmpty()) {
        		fatLow = Double.parseDouble(textFat.getText());
        	}
        	else {
        		fatLow = 0;
        	}
        	if(!textFatUp.getText().trim().isEmpty()) {
        		fatUp = Double.parseDouble(textFatUp.getText());
        	}
        	else {
        		fatUp = Double.MAX_VALUE;
        	}
    		rules.add("calories <= " + calUp);
    		rules.add("calories >= " + calLow);
    		
    		rules.add("protein <= " + proteinUp);
    		rules.add("protein >= " + proteinLow);

    		rules.add("carbohydrates <= " + carbUp);
    		rules.add("carbohydrates >= " + carbLow);

    		rules.add("fiber <= " + fiberUp);
    		rules.add("fiber >= " + fiberLow);
    		
    		rules.add("fat <= " + fatUp);
    		rules.add("fat >= " + fatLow);
    		items = (ArrayList<FoodItem>) data.filterByNutrients(rules);
    		foodList.getItems().clear();
    		for(int i = 0; i<items.size();i++) {
    			foodList.getItems().add(items.get(i).getName() + " " + items.get(i).getID());
    		}
    		
    });
    
    //Vbox that contains all above
    VBox first = new VBox();
    first.setSpacing(10);
    first.setStyle("-fx-background-color: #BCDEEC;");
    first.getChildren().addAll(foodLabel, foodList, addCustom, addCustomList,
    		saveCustomList,v,query);
    
    return first;
  }

  /**
   * middle pane of the borderpane
   * contains meal list and related functionality
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private VBox centerColumn() {
    // A list view for the meals selected and sample items in it
    Label mealListLabel = new Label("Meal List");
    
    mealList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    //mealList.getItems().add("Ceaser Salad");
    
    //mealList.getItems().add("Chesse Potatos");
    //mealList.getItems().add("Steak");
    
    //Two buttons to add or remove food from the meal
    Button addFood = new Button("Add Food to Meal List");
    Tooltip toolTip1 = new Tooltip("Click to add the seleted food from Food List to Meal List");
    addFood.setTooltip(toolTip1);
    
    //add items selected on food list to the meal list
    addFood.setOnAction(actionEvent ->  {
    	if(foodList.getSelectionModel().getSelectedItem() == null) {
    		
    	}
    	else {
    	mealList.getItems().add(foodList.getSelectionModel().getSelectedItem());
    	}
    });
    
    Button removeFood = new Button("Remove Food from Meal List");
    
    Tooltip toolTip2 = new Tooltip("Click to remove the selected food from Meal List");
    removeFood.setTooltip(toolTip2);
    //removes items selected on meal list
    removeFood.setOnAction(actionEvent ->  {
    	if(mealList.getSelectionModel().getSelectedItem() == null) {
    		
    	}
	mealList.getItems().remove(mealList.getSelectionModel().getSelectedItem());
    });
    
   
    //Vbox contains all above
    VBox second = new VBox();
    second.setSpacing(10);
    second.setStyle("-fx-background-color: #7EE8AF;");
    second.getChildren().addAll(mealListLabel, mealList, addFood, removeFood);
    
    return second;
  }

  /**
   * right pane of the border pane
   * contains meal analysis information
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private VBox rightColumn() {
	    
	    //List view o meal information
	    
	  Label mealLabel = new Label("Meal Info");
	    ListView mealInfo = new ListView();
	    mealInfo.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	  
    // The button to respond to users and analysis the meal 
    Button analyze = new Button("Analzye your Meal List");
    Tooltip toolTip = new Tooltip("Click to analyze the Meal List and put said information in the above Meal Info");
    analyze.setTooltip(toolTip);
    analyze.setOnAction(actionEvent ->  {
    		mealInfo.getItems().clear();
		ArrayList<String> mealItems = new ArrayList<String>();
		ObservableList list = mealList.getItems();
		for(int i = 0; i < list.size(); i++) {
			mealItems.add(list.get(i).toString());
		}
		double cal = 0;
		double fat = 0;
		double fiber = 0;
		double carb = 0;
		double protein = 0;
		FoodItem item;
		for(int i = 0; i < mealItems.size(); i++) {
			String[] string;
			string = mealItems.get(i).split(" ");
			String id = string[1];
			String name = string[0];
			items = (ArrayList<FoodItem>) data.getAllFoodItems();
			for(int i1 = 0; i1 < items.size(); i1++) {
				if(items.get(i1).getID().equals(id) && items.get(i1).getName().equals(name)) {
					item = items.get(i1);
					cal = cal + item.getNutrientValue("calories");
					fat = fat + item.getNutrientValue("fat");
					fiber = fiber + item.getNutrientValue("fiber");
					carb = carb + item.getNutrientValue("carbohydrate");
					protein = protein + item.getNutrientValue("protein");
				}
					
			}
		}
		mealInfo.getItems().add("Total Calories : " + cal);
		mealInfo.getItems().add("Total Fat : " + fat);
		mealInfo.getItems().add("Total Fiber : " + fiber);
		mealInfo.getItems().add("Total Carbohydrates : " + carb);
		mealInfo.getItems().add("Total Protein : " + protein);
		
    });
    //mealInfo.getItems().add("Total Amount of Calories: 4");
    //mealInfo.getItems().add("Total Amount of Carbs: 5");
    //mealInfo.getItems().add("Etc");
    
    //VBox that contains all above
    VBox third = new VBox();
    third.setSpacing(10);
    third.setStyle("-fx-background-color: #C8CA86;");
    third.getChildren().addAll( mealLabel, mealInfo, analyze);
    
    return third;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
