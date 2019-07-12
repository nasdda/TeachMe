package application;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main
  extends Application
{
  private static File directory = new File("C:/TeachMeData"); //repository for save datas
  private static File fileDirectory = new File(directory + "/files"); //repository for the TeachMeFiles file which contains name of all save data files
  
  public void start(Stage arg0) throws Exception {
    Parent root = (Parent)FXMLLoader.load(getClass().getResource("window.fxml"));
    arg0.setResizable(false);
    arg0.setTitle("TeachMe");
    arg0.setScene(new Scene(root));
    arg0.show();
    arg0.setOnHidden(e -> Platform.exit());
  }

  
  public static void main(String[] args) { launch(args); }



  
  public void stop() { Data.getInstance().updateData(); //Saves the data before exiting. Just in case.
  } 
  




  
  public void init() throws Exception{
    if (!Data.getInstance().getDatadirectory().exists()) {
      Data.getInstance().getDatadirectory().mkdir();
    }
    
    if (!fileDirectory.exists()) {
      fileDirectory.mkdir();
    }

    
    if (!Data.getInstance().getDataFile().createNewFile()) {
      Data.getInstance().loadEntries();
    }
    
    FilesRetriever.getInstance().setCurrentFile(Data.getInstance().getDataFile());
    FilesRetriever.getInstance().startUp();
  }
}
