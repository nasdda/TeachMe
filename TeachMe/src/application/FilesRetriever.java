package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class FilesRetriever {
	private static FilesRetriever instance= new FilesRetriever();
	private static File directory = new File("C:/TeachMeData");//Files directory.
	private ObservableList<File> fileNames = FXCollections.observableArrayList(); 
	private ButtonType replace = new ButtonType("Replace",ButtonBar.ButtonData.OK_DONE);
	private ButtonType close = new ButtonType("Close",ButtonBar.ButtonData.CANCEL_CLOSE);
	private static File currentFile; //File to modify when Reset Button or save button is clicked.
	
	public static FilesRetriever getInstance() {
		return instance;
	}
	
	public void setCurrentFile(File file) {
		currentFile = file;
	}
	
	public static File getCurrentFile() {
		return currentFile;
	}
	
	public boolean saveFile(String s) throws Exception{ // returns true if save complete/replace existing save
		s=s.trim();
		if(s.isEmpty()) {
			Alert emptyAlert = new Alert(AlertType.WARNING,"The data file name cannot be empty.");
			emptyAlert.showAndWait();
			return false;
		}
		File file = new File(directory+"/"+s);
		
		if(!fileNames.contains(file)) {//Save current data into file if the file doesn't already exist.
			fileNames.add(file);
			try {
				saveToFile(currentFile, file);
			} catch (Exception e) {
				System.out.println("error when saving file.");
			}	
			return true;
		}
			
		Alert alreadyExistAlert = new Alert(AlertType.WARNING,"The data file already exist.",replace,close);  //Asks user if they want to replace existing file if file already exists.
		Optional<ButtonType> result = alreadyExistAlert.showAndWait();
		
		if(result.orElse(close).equals(replace)) {
			fileNames.remove(file);
			fileNames.add(file);
			try {
				saveToFile(currentFile, file);
				
			}catch(Exception e) {
				System.out.println("error when saving file");
			}
				return true;
		}
		else {
			alreadyExistAlert.close();
			return false;
		}
		
	
	}
	
	
	public void saveToFile(File currentFile,File newFile) throws Exception{
		if(newFile.exists()) {
			newFile.delete();
		}
		
		newFile.createNewFile();
		
		FileWriter fw = new FileWriter(newFile);
		BufferedWriter bw = new BufferedWriter(fw);
		
		FileReader fr = new FileReader(currentFile);
		BufferedReader br = new BufferedReader(fr);
		String line;
		try {
			while((line = br.readLine())!=null&&!line.trim().isEmpty()) {//Saves entries in current file into new file.
				bw.write(line);
				bw.newLine();
			}
		}catch(Exception e) {
			System.out.println("Error in FilesRetriever saveToFile");
		}finally {
			br.close();
			fr.close();
			bw.close();
			fw.close();
		}
	}
	
	
	public List<File> getFileNames(){
		return fileNames;
	}
	
	public void startUp() throws Exception{
		fileNames.clear();
		try(Stream<Path> walk = Files.walk(Paths.get(directory.getAbsolutePath()))){//Saves the paths of the files in the directory into fileNames list.
			
			List<String> result = walk.filter(Files::isRegularFile)
									  .map(x->x.toString())
									  .collect(Collectors.toList());
			result.forEach(f->{
				File file = new File(f);
				fileNames.add(file);
			});
		}catch(IOException e) {
			System.out.println("Error at startup load");
		}
	}
	

}
