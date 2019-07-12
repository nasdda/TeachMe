package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javafx.collections.FXCollections;



public class Data
{
  private static final Data instance = new Data();
  
  private Map<String, String> saveEntries;
  
  private static File directory = new File("C:/TeachMeData");
  private static File dataFile = new File("C:/TeachMeData/ResponseData");

  
  public File getDatadirectory() { return directory; }


  
  public File getDataFile() { return dataFile; }



  
  public static Data getInstance() { return instance; }

  
  public void updateData() {
    try {
      FileWriter fw = new FileWriter(dataFile);
      Iterator<Map.Entry<String, String>> iter = this.saveEntries.entrySet().iterator();
      BufferedWriter bw = new BufferedWriter(fw);
      try {
        while (iter.hasNext()) {
          Map.Entry<String, String> entry = (Map.Entry<String,String>)iter.next();
          bw.write(String.format("%s\t%s", new Object[] { entry.getKey(), entry.getValue() }));
          bw.newLine();
        } 
      } catch (IOException e) {
      
      } finally {
        if (bw != null) {
          bw.close();
          fw.close();
        } 
      } 
    } catch (Exception e) {
      System.out.println("Exception when saving");
    } 
  }

  
  public Map<String, String> getSaveEntries() { return this.saveEntries; }


  
  public void setEntry(Map<String, String> map) {
    try {
      Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry<String, String> entry = (Map.Entry<String,String>)iter.next();
        this.saveEntries.put((String)entry.getKey(), (String)entry.getValue());
      } 
    } catch (Exception e) {
      System.out.println("Error at setEntry");
    } 
  }

  
  public void loadEntries() throws Exception{
    this.saveEntries = null;
    this.saveEntries = FXCollections.observableHashMap();
    FileReader fr = new FileReader(dataFile);
    BufferedReader br = new BufferedReader(fr);
    try {
      try {
        String line;
        while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
          String[] parts = line.split("\t");
          this.saveEntries.put(parts[0], parts[1]);
        } 
      } finally {
        if (br != null) {
          br.close();
          fr.close();
        } 
      } 
    } catch (Exception e) {
      System.out.println("Exception at loadEntries");
    } 
  }




  
  public void setMainFile(File file) { dataFile = file; }

  
  public File getMainFile() { return dataFile; }
}
