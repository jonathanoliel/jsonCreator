package classes;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableEntry2 {
	    public final SimpleStringProperty compName;
	    public final SimpleStringProperty versionName;
	    public final SimpleIntegerProperty idValue;
	    public final SimpleStringProperty filePathName;
	    public TableEntry2(String fkey, String fvalue, int fidValue, String fpath) {
	        this.compName = new SimpleStringProperty(fkey);  
	        this.versionName = new SimpleStringProperty(fvalue);
	        this.idValue = new SimpleIntegerProperty(fidValue);
	        this.filePathName = new SimpleStringProperty(fpath);
	    }

	    public String getCompName() {
	        return compName.get();
	    }
	    public void setCompName(String fName) {
	    	compName.set(fName);
	    }
	    public String getVersionName() {
	        return versionName.get();
	    }
	    public void setVersionName(String fName) {
	    	versionName.set(fName);
	    }
	    public int getIdValue() {
	        return idValue.get();
	    }
	    public void setIdValue(int fidValue) {
	    	idValue.set(fidValue);
	    }
	    public String getFilePathName() {
	        return filePathName.get();
	    }
	    public void setFilePathName(String fName) {
	    	filePathName.set(fName);
	    }
	   
	   
	   
}