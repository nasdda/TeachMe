/*@Author Xin yan
*/
package application;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application
{
  private static File directory = new File("C:/TeachMeData"); //repository for savedatas
  
  public void start(Stage arg0) throws Exception {
    Parent root = (Parent)FXMLLoader.load(getClass().getResource("window.fxml"));
    arg0.setResizable(false);
    arg0.setTitle("TeachMe");
    arg0.setScene(new Scene(root));
    arg0.show();
    arg0.setOnHidden(e -> Platform.exit());  //Exits all windows when main stage is closed
  }

  
  public static void main(String[] args) {
	  launch(args);
	  }
  
  
  public void init() throws Exception{
	  
    if (!Data.getInstance().getDatadirectory().exists()) { //Create files directory if specified directory doesn't exist.
      Data.getInstance().getDatadirectory().mkdir();
    }
    
    if (!Data.getInstance().getDataFile().createNewFile()) {//If ResponseData exists, load data. Else, create ResponseData file.
      Data.getInstance().loadEntries();
    }
    
    FilesRetriever.getInstance().setCurrentFile(Data.getInstance().getDataFile());//Current file on startup is set to ResponseData.
    FilesRetriever.getInstance().startUp();
  }
  
  
  public void stop() {
	  Data.getInstance().updateData(); //Saves the data before exiting. Just in case.
  }

}
