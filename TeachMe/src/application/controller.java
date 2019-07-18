package application;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class controller{
	private static Map<String,String> registeredResponses = new HashMap<>(); // Map to store each input with the desired response
	private Map<String,String> chatHistory = new HashMap<>(); //Map for displaying sent/recieved chat messages on text area
	
	//////Buttons//////
	private ButtonType replace = new ButtonType("Replace",ButtonBar.ButtonData.OK_DONE);
	private ButtonType clear = new ButtonType("Clear",ButtonBar.ButtonData.OK_DONE);
	private ButtonType cancel = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
	private ButtonType close = new ButtonType("Close",ButtonBar.ButtonData.CANCEL_CLOSE);
	
	
	private boolean checkIfRunning = false; //State of backgroundColorChanges - Makes sure to stop the thread before changing to specified theme
	private int count = 0; //For backgroundColorChanges. Keeps track of which color to change to
	
	private static controller instance = new controller(); //singleton
	
	private Service<Void> backgroundColorChanges; //background color changing thread

	
	@FXML private TextField inputTextField, responseTextField, messageTextField; 
	@FXML private TextArea chatTextArea;
	@FXML private ComboBox<String> themeComboBox; 
	@FXML private GridPane pane;
	@FXML private Label responseLabel, inputLabel,themeLabel;
	@FXML private ImageView helpIconImageView, titleImageView;
	
	
	public static controller getInstance() {
		return instance;
	}
	
	public Map<String, String> getMap(){ 
		return controller.registeredResponses;
	}
	
	public void setMap(Map<String,String> dataMap) {
		registeredResponses = dataMap;
	}
	
	public GridPane getPane() {
		return pane;
	}
	
	
	@FXML
	public void initialize() {
		registeredResponses = Data.getInstance().getSaveEntries();
		if (registeredResponses == null) {//Requires restart after the ResponseData file is first created
		      System.out.println("\nFirst launch file setup complete. \nPlease restart program.");
		      System.exit(0);
		} 
		
		///Themes///
		themeComboBox.getItems().add("Default");
		themeComboBox.getItems().add("Dark");
		themeComboBox.getItems().add("Blue-Gold");
		themeComboBox.getItems().add("Red-Gold");
		themeComboBox.getItems().add("Color-Switch");
	
		themeComboBox.setValue("Default"); //Theme is set to default at start
		
		try { //sets the title icon and help icon
			InputStream titleFile = getClass().getResourceAsStream("/title.PNG");
			BufferedImage titleBI =  ImageIO.read(titleFile);
			Image titleimg = SwingFXUtils.toFXImage(titleBI, null);
			titleImageView.setImage(titleimg);
			
			
			InputStream helpFile = getClass().getResourceAsStream("/Help-icon.png");
			BufferedImage helpBI =  ImageIO.read(helpFile);
			Image helpimg = SwingFXUtils.toFXImage(helpBI, null);
			helpIconImageView.setImage(helpimg);	
		} catch (IOException e) {
			System.out.println("Error loading images");
		}
		
		messageTextField.setOnKeyPressed(e->{//If ENTER key is pressed while on messageTextField, it is equivalent as pressing the send button.
			if(e.getCode()==KeyCode.ENTER)
				sendButtonClicked();
		});
		
		inputTextField.setOnKeyPressed(e->{ //Pressing ENTER when input/response textfield is selected is equivalent as pressing the add button.
			if(e.getCode()==KeyCode.ENTER)
				try {
					addButtonClick();
				} catch (Exception e1) {
					System.out.println("Exception when adding input-response.");
				}
		});
		
		responseTextField.setOnKeyPressed(e->{
			if(e.getCode()==KeyCode.ENTER)
				try {
					addButtonClick();
				} catch (Exception e1) {
					System.out.println("Exception when adding input-response.");
				}
		});
		
		try { //Changes bg color every second if theme is set to 'Color-Switch'
		backgroundColorChanges = new Service<Void>() {
			@Override
			protected synchronized Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected synchronized Void call() throws Exception {
						
						String BGC; //background color
						String textC; //text color
						while(true) {
							switch(count) {
							case 0:{
								synchronized(backgroundColorChanges) {
								BGC="#F96167";
								textC="#FCE77D";
								pane.setStyle("-fx-background-color:"+BGC);
								inputLabel.setTextFill(Color.web(textC));
								responseLabel.setTextFill(Color.web(textC));
								themeLabel.setTextFill(Color.web(textC));
								Thread.sleep(1000);};
								synchronized(backgroundColorChanges) {
									count=1;}
								break;
								
							}
							case 1:{
								synchronized(backgroundColorChanges) {
								BGC="#f5d36c";
								textC = "#ffffff";
								pane.setStyle("-fx-background-color:"+BGC);
								inputLabel.setTextFill(Color.web(textC));
								responseLabel.setTextFill(Color.web(textC));
								themeLabel.setTextFill(Color.web(textC));
								Thread.sleep(1000);
								};
								synchronized(backgroundColorChanges) {
									count=2;}
								break;
							}
							case 2:{
								synchronized(backgroundColorChanges) {BGC="#ff9999";
								textC = "#ffffff";
								pane.setStyle("-fx-background-color:"+BGC);
								inputLabel.setTextFill(Color.web(textC));
								responseLabel.setTextFill(Color.web(textC));
								themeLabel.setTextFill(Color.web(textC));
								Thread.sleep(1000);
								};
								synchronized(backgroundColorChanges) {
									count=3;}
								break;
							}
							case 3:{
								synchronized(backgroundColorChanges) {
								BGC="#ffc2da";
								textC = "#636363";
								pane.setStyle("-fx-background-color:"+BGC);
								inputLabel.setTextFill(Color.web(textC));
								responseLabel.setTextFill(Color.web(textC));
								themeLabel.setTextFill(Color.web(textC));
								Thread.sleep(1000);
								};
								synchronized(backgroundColorChanges) {
									count=4;}
								break;
							}
							case 4:{
								synchronized(backgroundColorChanges) {
								BGC="#4e5157";
								
								textC="white";
								pane.setStyle("-fx-background-color:"+BGC);
								inputLabel.setTextFill(Color.web(textC));
								responseLabel.setTextFill(Color.web(textC));
								themeLabel.setTextFill(Color.web(textC));
								Thread.sleep(1000);};
								synchronized(backgroundColorChanges) {
									count=5;}
								break;
							}
							case 5:{
								synchronized(backgroundColorChanges) {
								BGC="#4a85d9";
								textC="#fccd4c";
								pane.setStyle("-fx-background-color:"+BGC);
								inputLabel.setTextFill(Color.web(textC));
								responseLabel.setTextFill(Color.web(textC));
								themeLabel.setTextFill(Color.web(textC));
								Thread.sleep(1000);
								};
								synchronized(backgroundColorChanges) {
									count=0;}
								break;
							}
							}
						}
					}
				};
			}
		};}catch(Exception e) {
			backgroundColorChanges.cancel();
			count = 0;
			backgroundColorChanges.restart();
		}
	}
	
	@FXML
	public boolean addButtonClick() throws Exception{
		//retrieves text from input and response textfields.
		//makes input to lower case to check if input entry already exists, ignoring the case
		String input = inputTextField.getText().toLowerCase().trim();
		String response = responseTextField.getText();
		
	
		
		if(input.trim().isEmpty() || response.trim().isEmpty()) { //Warns user that field(s) are empty
			Alert emptyAlert = new Alert(AlertType.WARNING,"The input or response is empty.",close);
			if(!input.trim().isEmpty() || !response.trim().isEmpty()) {
				emptyAlert.showAndWait();
				emptyAlert.close();
				return false;
			}
			inputTextField.setText("");
			responseTextField.setText("");
			emptyAlert.showAndWait();
			emptyAlert.close();
			return false;
			}
			else if(registeredResponses.containsKey(input)) {//Prompts user if they want to replace pre-existing input-response
			Alert alreadyRegisteredAlert = new Alert(AlertType.WARNING);
			alreadyRegisteredAlert.setTitle("Input Already Registered");
			alreadyRegisteredAlert.getDialogPane().setMinHeight(150);
			alreadyRegisteredAlert.getDialogPane().setMinWidth(300);
			Text text = new Text("This input is already associated with a reponse.");
			text.setWrappingWidth(250);
			alreadyRegisteredAlert.getDialogPane().setContent(text);
			alreadyRegisteredAlert.getButtonTypes().clear();
			alreadyRegisteredAlert.getButtonTypes().add(replace);
			alreadyRegisteredAlert.getButtonTypes().add(cancel);
			Optional<ButtonType> replaceOrNot = alreadyRegisteredAlert.showAndWait();
			
		if(replaceOrNot.orElse(cancel) == replace) {
				registeredResponses.put(input, response);
				Data.getInstance().setEntry(registeredResponses);
				inputTextField.setText("");
				responseTextField.setText("");
				Data.getInstance().updateData();
			}
			else {
				alreadyRegisteredAlert.close();
				return false;	
			}	
		}
		else {//saves the input-response to current save file
			registeredResponses.put(input, response);
			inputTextField.setText("");
			responseTextField.setText("");
			Data.getInstance().setEntry(registeredResponses);
			Data.getInstance().updateData();
		}
		
		return true;
	}
	
	
	
	@FXML 
	public boolean sendButtonClicked() {
		if(registeredResponses.containsKey(messageTextField.getText().toLowerCase().trim())) { //Fetches corresponding response if the input exists in current save file
			String message = registeredResponses.get(messageTextField.getText().toLowerCase().trim());
			chatHistory.put("You: "+messageTextField.getText(), "Program: " + message); //Formats and adds the input and response to chatHistory
		}else if(messageTextField.getText().trim().isEmpty()){//If message field is empty, empty out any white space and do nothing
			messageTextField.setText("");
			return false;
		}else {//If input is not associated with any response, notify user.
			Alert noEntryAlert = new Alert(AlertType.WARNING,"The input is not associated with any response.\n Please check your spelling.",close);
			noEntryAlert.showAndWait();
			noEntryAlert.close();
			return false;
		}
		
		
		for(Map.Entry<String, String> entry: chatHistory.entrySet()) { //Updates chatTextArea with entries in chatHistory
			chatTextArea.appendText(entry.getKey());
			messageTextField.setText("");
			chatTextArea.appendText("\n"+entry.getValue()+"\n");
			chatHistory.remove(entry.getKey());
		}
		return true;
	
	}	
	
	@FXML
	public boolean clearButtonClicked() throws Exception {
		if(registeredResponses.size()==0) { //Does nothing if there is no input-response saved in current file
			return true;
		}
		
		Alert clearAlert = new Alert(AlertType.WARNING,
					"This action cannot be undone",clear,cancel);
		clearAlert.setTitle("Clear registered Input-Reponse?");
		Optional<ButtonType> clearOrNot = clearAlert.showAndWait();
		if(clearOrNot.orElse(cancel) == clear) { //Clears entries in current save file
			registeredResponses.clear();
			Data.getInstance().getSaveEntries().clear();
			chatTextArea.clear();
			messageTextField.setText("");
			inputTextField.setText("");
			responseTextField.setText("");
			Data.getInstance().updateData();
		}
		return true;
	}

	
	@FXML
	public boolean themeChange() { //Changes theme according to what is selected on the themeComboBox
		String theme = themeComboBox.getValue().toString();
		String bgColor="white";
		String textColor="black";
		
		try {
			switch(theme) {
			case "Color-Switch":{
				if(!checkIfRunning) {//Checks if service is already running.
					synchronized(backgroundColorChanges) {
						backgroundColorChanges.restart();
						checkIfRunning=true;
						}
					}
						return true;
					}
			case "Default":{
				if(checkIfRunning) {
					backgroundColorChanges.cancel();
					checkIfRunning = false;
				}
				bgColor="white";
				textColor="#4e5157";
				break;}
			case "Dark":{
				if(checkIfRunning) {
					backgroundColorChanges.cancel();
					checkIfRunning = false;
				}
				bgColor="#4e5157";
				textColor="white";
				break;}
			case "Blue-Gold":{
				if(checkIfRunning) {
					backgroundColorChanges.cancel();
					checkIfRunning = false;
				}
				bgColor="#4a85d9";
				textColor="#fccd4c";
				break;}
			case "Red-Gold":{
				if(checkIfRunning) {
					backgroundColorChanges.cancel();
					checkIfRunning = false;
				}
				bgColor="#F96167";
				textColor="#FCE77D";
				break;}
			default:{
				bgColor="white";
				textColor="#4e5157";
				}	
			}
		}catch(Exception e) {
			backgroundColorChanges.cancel();
			count =0;
			backgroundColorChanges.restart();
			return false;
		}
		
		pane.setStyle("-fx-background-color:"+bgColor);
		inputLabel.setTextFill(Color.web(textColor));
		responseLabel.setTextFill(Color.web(textColor));
		themeLabel.setTextFill(Color.web(textColor));
		return true;
	}
	
	
	public void helpClicked() {//Shows instruction when help icon is clicked
		Alert info = new Alert(AlertType.INFORMATION);
		Text instruction = new Text("In the 'Input' textbox, enter a message."
				+ " In the 'Response' textbox, enter the message you want the program "
				+ "to reply when the message in the corresponding ‘Input’ textbox is sent. After doing so,"
				+ " press the 'Add' button to save the Input-Response"
				+ ". The Input-Response is saved each time "
				+ "you add an entry. You can delete the existing data by clicking the 'Reset' button."  );
		instruction.setWrappingWidth(300);
		
		info.getDialogPane().setContent(instruction);
		info.setHeaderText("Instructions");
		info.getDialogPane().setStyle("-fx-padding: 20px 20px 20px 20px;");
		info.showAndWait();
		info.close();
	}
	
	public void dataClicked() throws Exception{//Shows DataWindow when Data button is clicked
		DataController dataWindow = new DataController();
		dataWindow.showDataWindow();
		
	}
	
}
