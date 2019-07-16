package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class SaveController {
	@FXML private TextField FileNameField;
	@FXML private Button closeButton;
	
	@FXML 
	public void initialize() {
		FileNameField.setOnKeyPressed(e->{
			if(e.getCode()==KeyCode.ENTER)
					saveClicked();				
		});
	}
	
	@FXML
	public void closeSaveWindow(ActionEvent e) {
		Stage stage = (Stage)closeButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void saveClicked(){
		try {
			if(FilesRetriever.getInstance().saveFile(FileNameField.getText())) {//Saves current data into a file with the name in the FileNameField.
				FileNameField.clear();
			}
		} catch (Exception e) {
			System.out.println("Error when saving file.");
		}finally {
			DataController.getDataStage().close();
			Stage stage = (Stage)closeButton.getScene().getWindow();
			stage.close();
		}
	
		
	}

}
