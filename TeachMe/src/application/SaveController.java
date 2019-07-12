package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SaveController {
	@FXML private TextField FileNameField;
	@FXML private Button closeButton;
	
	@FXML
	public void closeSaveWindow(ActionEvent e) {
		Stage stage = (Stage)closeButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void saveClicked() throws Exception{
		try {
			if(FilesRetriever.getInstance().saveFile(FileNameField.getText())) {
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
