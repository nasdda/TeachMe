package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DataController {
	private static DataController instance = new DataController();
	private ButtonType ok = new ButtonType("OK",ButtonBar.ButtonData.CANCEL_CLOSE);
	private static Stage dataStage = new Stage();
	private static File defaultFile = new File("C:/TeachMeData/ResponseData"); //Default save file. Cannot be deleted
	
	@FXML private ListView<File> FileNameListView;
	@FXML private TextArea FileEntries;
	
	
	
	
	@FXML
	public void initialize() throws Exception{
		FileNameListView.getItems().clear();
		FilesRetriever.getInstance().startUp();
		FileNameListView.getItems().setAll(FilesRetriever.getInstance().getNames());
		FileNameListView.setCellFactory(lv-> new ListCell<File>() { //Listview only show file name
			@Override
			protected void updateItem(File file, boolean empty) {
				super.updateItem(file, empty);
				setText(file==null?null:file.getName());
			}
		});
				
		
		FileNameListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {

			@Override
			public void changed(ObservableValue<? extends File> arg0, File arg1, File selectedFile){
				if(selectedFile!=null) {
					File file = new File(FileNameListView.getSelectionModel().getSelectedItem().getAbsolutePath());
					FileEntries.clear();

					try {
						showEntries(file);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
			
			
		});
	}

	public void showDataWindow() throws Exception{ //shows the data management window
		Parent root = FXMLLoader.load(getClass().getResource("DataWindow.fxml"));
		dataStage.setScene(new Scene(root));
		dataStage.setTitle("Data Management");
		dataStage.setResizable(false);
		
		dataStage.show();
		
	}

	
	public void showSaveWindow() throws Exception{
		Stage saveStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("SaveWindow.fxml"));
		saveStage.setScene(new Scene(root));
		saveStage.setResizable(false);
		saveStage.setTitle("Save Data");
		saveStage.show();
		
	}
	
	public void showEntries(File file) throws Exception{
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		String line;
		try {
		while((line = br.readLine())!=null && !(line.trim().isEmpty())) {
			String[] parts = line.split("\t");
			String formattedLine = String.format("Input: %s\nResponse: %s\n\n", parts[0],parts[1]);
			FileEntries.appendText(formattedLine);
		}}
		catch(Exception e) {
			System.out.println("Error in showEntries");
		}
		finally {
			if(br!=null) {
				br.close();
				fr.close();
			}
		}
	}
	
	
	
	public boolean deleteClicked() throws Exception{
		File fileToDelete = FileNameListView.getSelectionModel().getSelectedItem();
		
		if(fileToDelete.equals(defaultFile)) {
			Alert cannotDelete = new Alert(AlertType.INFORMATION,"Cannot delete default file \"ResponseData\",\nuse the 'Reset' button instead",ok);
			cannotDelete.showAndWait();
			cannotDelete.close();
			return false;
		}
		
		if(fileToDelete.exists() && fileToDelete!=null) {
			fileToDelete.delete();
		}
		
		FilesRetriever.getInstance().startUp();
		
		FileNameListView.getItems().setAll(FilesRetriever.getInstance().getNames());
		return true;
	}

	
	public static DataController getInstance() {
		return instance;
	}
	
	public ListView<File> getListView(){
		return this.FileNameListView;
	}
	

	
	public void loadClicked() throws Exception {
		File file = FileNameListView.getSelectionModel().getSelectedItem();
		if(file != null) {
			FilesRetriever.getInstance().setCurrentFile(file);
			File currentFile = FilesRetriever.getCurrentFile();
			Data.getInstance().setMainFile(currentFile);
			Data.getInstance().loadEntries();
			controller.getInstance().setMap(Data.getInstance().getSaveEntries());
			
			  Alert loaded = new Alert(AlertType.INFORMATION,"Data Loaded.",ok);
			  loaded.showAndWait(); 
			  loaded.close();
			 
		}
		
	}
	
	public static Stage getDataStage() {
		return dataStage;
	}

}
