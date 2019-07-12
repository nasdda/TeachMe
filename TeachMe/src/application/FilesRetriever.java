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
	private static File directory = new File("C:/TeachMeData");
	private static File fileDirectory = new File(directory+"/files");
	private static File fileNames = new File(fileDirectory+"/"+"TeachMeFiles");
	private ObservableList<File> names = FXCollections.observableArrayList(); 
	private ButtonType replace = new ButtonType("Replace",ButtonBar.ButtonData.OK_DONE);
	private ButtonType close = new ButtonType("Close",ButtonBar.ButtonData.CANCEL_CLOSE);
	private static File currentFile;
	
	public static FilesRetriever getInstance() {
		return instance;
	}

	
	
	public void getSavedFileNames() {
		
	}
	
	public boolean saveFile(String s) throws Exception{ // returns true if save complete/replace existing save
		s=s.trim();
		if(s.isEmpty()) {
			Alert emptyAlert = new Alert(AlertType.WARNING,"The data file name cannot be empty.");
			emptyAlert.showAndWait();
			return false;
		}
		File file = new File(directory+"/"+s);
		
		if(!names.contains(file)) {
			names.add(file);
			updateFiles(names);
			try {
				saveToFile(currentFile, file);
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("error when saving file.");
			}
			
			return true;
		}
			
		Alert alreadyExistAlert = new Alert(AlertType.WARNING,"The data file already exist.",replace,close);
		Optional<ButtonType> result = alreadyExistAlert.showAndWait();
		
		if(result.orElse(close).equals(replace)) {
			names.remove(file);
			names.add(file);
			updateFiles(names);
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
			while((line = br.readLine())!=null&&!line.trim().isEmpty()) {
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
	
	public void setCurrentFile(File file) {
		currentFile = file;
	}
	
	public static File getCurrentFile() {
		return currentFile;
	}
	
	public void updateFiles(List<File> names) throws Exception {
		fileNames.createNewFile();
			FileWriter fw = new FileWriter(fileNames);
			BufferedWriter bw = new BufferedWriter(fw);
			
			ListIterator<File> iter = names.listIterator();
		try {			
			while(iter.hasNext()) {
				String name = iter.next().getAbsolutePath();
				bw.write(name);
				bw.newLine();
			}
			
			
		} catch (IOException e) {
			System.out.println("Error when updating file names");
		}finally {
			if(bw!=null) {
				bw.close();
				fw.close();
			}
		}
		
	}
	
	
	public void loadDataFile() {
		
	}
	
	public List<File> getNames(){
		return names;
	}
	
	public void startUp() throws Exception{
		names.clear();
		try(Stream<Path> walk = Files.walk(Paths.get(directory.getAbsolutePath()))){
			
			List<String> result = walk.filter(x->{return !(x.toString().equals(fileNames.getAbsoluteFile().toString()));})
									  .filter(Files::isRegularFile)
									  .map(x->x.toString())
									  .collect(Collectors.toList());
			result.forEach(f->{
				File file = new File(f);
				names.add(file);
			});
		}catch(IOException e) {
			System.out.println("Error at startup load");
		}
	}
	
	
	

}
