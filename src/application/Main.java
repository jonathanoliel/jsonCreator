package application;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

import org.controlsfx.control.CheckListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import classes.*;
import javafx.scene.control.cell.*;

public class Main extends Application {

	public static Scanner reader; 
	public static JSONArray components;
	public static JSONObject mainObj;
	public Gson gson = new Gson();
	
	public Label message, errorMessage;
	
	public TextField ServersControlVersion,VSimControlVersion,AtmelVersion,Server4GVersion,GUIVersion,LogServiceVersion,APConfigurationVersion,OSVersion,OSBaseVersion,OSFilePath;
	public TextField PackageVersion,PackageName,ActionString,BucketId,BucketKey;		
	public TextField CaptivePortalVersion,GUIConfigurationVersion,TelephonyDBVersion,APNVersion,WebServerVersion;
	public TextField[] mandatoryFields,nonMandatoryFields,packageInfoFields;	
	public Button btn,btn2,btn3, openButton, newButton;
	public CheckListView<String> list;
	
    public ObservableList<TableEntry1> packageInfo,configPackageInfo ; 
    public ObservableList<TableEntry2> componentInfo, configComponentInfo; 
    public ObservableList<TableEntry3> optionalInfo, configOptionalInfo;
    public FileChooser fileChooser;
	public TableView<TableEntry1> table1;
	public TableView<TableEntry2> table2;
	public TableView<TableEntry3> table3;
	public TextField GenerateOSName, GenerateJSONName;
	public final String config_file = "configurations.json";
	@Override
	public void start(Stage primaryStage) {
		initFields(); 
       // setGrid(primaryStage);   
    	
        try {
			openFile(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(config_file)),true);
		} catch (Exception e1) {	
			e1.printStackTrace();
		}
        setGrid(primaryStage);
        primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	public void initFields() {
	    	
	    	reader = new Scanner(System.in); 
	    	components = new JSONArray();
	    	mainObj = new JSONObject();
	    	
	    	ServersControlVersion = new TextField("1.12.3");
	    	VSimControlVersion = new TextField("1.12.1");
	    	AtmelVersion = new TextField( "4.4.9.201708301409");
	    	Server4GVersion = new TextField("1.3.0");
	    	GUIVersion = new TextField("1.8.7");
	    	LogServiceVersion = new TextField("1.1.2");
	    	APConfigurationVersion = new TextField("1.2.6");
	    	OSVersion = new TextField();
	    	OSBaseVersion = new TextField();
	    	OSFilePath = new TextField();
	    	
	    	CaptivePortalVersion = new TextField();
	    	GUIConfigurationVersion = new TextField();
	    	TelephonyDBVersion = new TextField();
	    	APNVersion = new TextField();
	    	WebServerVersion = new TextField();
	    	
	    	PackageVersion = new TextField("2.9.0");
	    	PackageName = new TextField("2.9.0");
	    	ActionString = new TextField("4");
	    	BucketId = new TextField("TBD");
	    	BucketKey = new TextField("TBD");
	    	
	    	mandatoryFields = new TextField [] {ServersControlVersion,VSimControlVersion,AtmelVersion,Server4GVersion,GUIVersion,LogServiceVersion,
	    			APConfigurationVersion,OSVersion,OSFilePath};
	    	nonMandatoryFields = new TextField [] {OSBaseVersion, CaptivePortalVersion,GUIConfigurationVersion,TelephonyDBVersion,APNVersion, WebServerVersion};
	    	packageInfoFields = new TextField[] { PackageVersion, PackageName, ActionString, BucketId, BucketKey};
	    	
	    	message = new Label(" Okay");
	    	errorMessage = new Label();
	    	btn = new Button();
	        btn2 = new Button();
	        btn3 = new Button();
	        openButton = new Button();
	        newButton = new Button("New");
	        table1 = new TableView<TableEntry1>();
	        table2 = new TableView<TableEntry2>();
	        table3 = new TableView<TableEntry3>();
	        list = new CheckListView<>();
	        packageInfo = FXCollections.observableArrayList();
	        componentInfo = FXCollections.observableArrayList();
	        optionalInfo = FXCollections.observableArrayList();
	        configPackageInfo = FXCollections.observableArrayList();
	        configComponentInfo = FXCollections.observableArrayList();
	        configOptionalInfo = FXCollections.observableArrayList();
	        fileChooser = new FileChooser();
	        
	        GenerateOSName = new TextField();
	        GenerateJSONName = new TextField();
	        		
	        
	    }
	public void generate() throws IOException{
		
		if (!checkFields())
			message.setText("Error");
		else {
			message.setText(" Okay");
			errorMessage.setText("");
			createMandatoryJson();
			try(FileWriter file = new FileWriter(GenerateJSONName.getText())) { 	
				file.write(mainObj.toString(1));
				System.out.println("Success");
			}
			catch (Exception o) {
				System.out.println("error");
				message.setText("error");
			}
			components = new JSONArray();
	    	mainObj = new JSONObject();
		}
	
	}	
	public void generateOS() throws IOException{
		
		if (!checkOSFields()) 
			message.setText("Error");
		else {
			message.setText(" Okay");
			errorMessage.setText("");
			createOS();
			try(FileWriter file = new FileWriter(GenerateOSName.getText())) { 	
				file.write(mainObj.toString(1));
				System.out.println("Success");
			}
			catch (Exception o) {
				errorMessage.setText(o.getMessage());
			}
			components = new JSONArray();
	    	mainObj = new JSONObject();
		}
	
	}
	private void createMandatoryJson() {
		
		try {
			for (int i = 0; i < packageInfo.size(); i ++) {
				if (packageInfo.get(i).getKeyName().equals("action"))
					mainObj.put(packageInfo.get(i).getKeyName(), Integer.parseInt(packageInfo.get(i).getValueName()));
				else
					mainObj.put(packageInfo.get(i).getKeyName(), packageInfo.get(i).getValueName());
			}
			mainObj.put("components", components);
			for (int i = 0; i < componentInfo.size(); i ++) {
				String version = componentInfo.get(i).getVersionName();
				String filepath = componentInfo.get(i).getFilePathName();
				switch(componentInfo.get(i).getIdValue()) {
				case(0): 
					addObject(0, "ServersControl", 	version , 	filepath, "Server Service", ".apk");
					break;
				case(1):
					addObject(1, "VSimControl", 	version,	filepath, "Vsim services", 	".apk");
					break;
				case(2):
					addObject(2, "Atmel", 			version,  	filepath, "Atmel", 			".sgo");
					break;
				case(3):
					addObject(3, "Server 4G", 		version,  	filepath, "Server 4G", 		".apk");
					break;
				case(4):
					addObject(4, "GUI", 			version,	filepath, "GUI", 			".apk");
					break;
				case(5):
					addObject(5, "Log Service", 	version,	filepath, "Log Service", 	".apk");
					break;
				case(6):
					addObject(6, "AP-Configuration",version, 	filepath, "Configurations", ".json");
					break;
				case(7):
					addObject(7, "OS",				version, 	 OSFilePath.getText(), "OS", ".zip", OSBaseVersion.getText() );  
					break;
				}	
			}
			for (int i = 0; i < optionalInfo.size(); i ++) {
				if (!optionalInfo.get(i).getActive())
					continue;
				String version = optionalInfo.get(i).getVersionName();
				String filepath = optionalInfo.get(i).getFilePathName();
				switch(optionalInfo.get(i).getIdValue()) {
				case(100): 
					addObject(100, "Captive Portal", 		version, 	filepath, 	"Captive Portal", 	".zip");
					break;
				case(101):
					addObject(101, "Gui-Configurations", 	version, 	filepath, 	"Gui-Configuration",".zip");
					break;
				case(102):
					addObject(102, "Telephony DB", 			version, 	filepath, 	"", 				".db");
					break;
				case(103):
					addObject(103, "APN", 					version, 	filepath,	"",					".xml");
					break;
				case(104):
					addObject(104, "WEB SERVER",			 version, 	filepath,	"WEB SERVER", 		".apk");
					break;
				}	
			}
		
		}
		catch (JSONException ex) {
			errorMessage.setText(ex.getMessage());
			System.out.println("error");
		}
		
	}
	private void createOS() {
		
		try {
			for (int i = 0; i < packageInfo.size(); i ++) {
				if (packageInfo.get(i).getKeyName().equals("action"))
					mainObj.put(packageInfo.get(i).getKeyName(), Integer.parseInt(packageInfo.get(i).getValueName()));
				else
					mainObj.put(packageInfo.get(i).getKeyName(), packageInfo.get(i).getValueName());
			}
			mainObj.put("components", components);
			for (int i = 0; i < componentInfo.size(); i ++) {
				switch(componentInfo.get(i).getCompName()) {
				case("OS"):
					addObject(7, "OS",	componentInfo.get(i).getVersionName(),  OSFilePath.getText(), "OS", ".zip", OSBaseVersion.getText() );  
					break;
				}	
			}
		}
		catch (JSONException ex) {
			errorMessage.setText(ex.getMessage());
		}
		
	}
	private boolean checkFields() {
		
		
		if (!checkOSFields()) {
			return false;
		}
		for (int i = 0; i < componentInfo.size(); i ++) {
			
			if (componentInfo.get(i).getVersionName().isEmpty() | componentInfo.get(i).getVersionName().indexOf(',') >= 0) {
				errorMessage.setText("Reason1: " + componentInfo.get(i).getCompName());
				return false;
			}			
		}
		for (int i = 0; i < optionalInfo.size(); i ++) {
			if (optionalInfo.get(i).getActive() && (optionalInfo.get(i).getVersionName().isEmpty() | optionalInfo.get(i).getVersionName().indexOf(',') >= 0)){
				errorMessage.setText("Reason2: " + optionalInfo.get(i).getCompName());
				return false;
			}
		}
		return true;
	}
	private boolean checkOSFields() {
		for (int i = 0; i < packageInfo.size(); i ++) {
			if (packageInfo.get(i).getValueName().isEmpty() | packageInfo.get(i).getValueName().indexOf(',') >= 0) {
				errorMessage.setText("Reason3: " + packageInfo.get(i).getKeyName());
				return false;
			}
		}
		for (int i = 0; i < componentInfo.size(); i ++) {
			if (componentInfo.get(i).getIdValue() == 7) {
				if (componentInfo.get(i).getVersionName().isEmpty() | componentInfo.get(i).getVersionName().indexOf(',') >= 0 ) {
					errorMessage.setText("Reason4: " + componentInfo.get(i).getCompName());
					return false;
				}
				if ((OSBaseVersion.getText().indexOf(',') >= 0)) {
					errorMessage.setText("Reason5: bad base version");
					return false;
				}
			}
		}
		return true;
	
	}
	
	private static void addObject(int id, String name, String component_version, String filePath, String description, String type) throws JSONException {
		
		filePath =  filePath + component_version + type;
		JSONObject ob = new JSONObject();
		ob.put("id", id);
		ob.put("name",name);
		ob.put("version", component_version);
		ob.put("filepath", filePath);
		ob.put("description", description);
		components.put(ob);		
	}
	//	For Diff 
	private static void addObject(int id, String name, String component_version, String filePath, String description, String type, String baseversion) throws JSONException {
		JSONObject ob = new JSONObject();
		filePath = filePath.endsWith(".zip") ? filePath : filePath + ".zip";		
		ob.put("id", 3);
		ob.put("name",name);
		ob.put("version", component_version);
		ob.put("filepath", filePath);
		ob.put("description", description);
		if (!baseversion.isEmpty())
			ob.put("base_version", baseversion);
		components.put(ob);
	}
	
	private void setGrid(final Stage primaryStage) {
		primaryStage.setTitle("Simgo Package Generator");
		final FileChooser fileChooser = new FileChooser();
		
		
	    
	    btn.setText("Generate JSON");
	    btn.setOnAction(new EventHandler<ActionEvent>() {
	
	        @Override
	        public void handle(ActionEvent event) {
	            try {
					generate();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    });
	    
	    btn3.setText("Generate Diff");
	    btn3.setOnAction(new EventHandler<ActionEvent>() {
	
	        @Override
	        public void handle(ActionEvent event) {
	        	try {
					generateOS();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	    });
	    
	    openButton.setText("Open JSON");
	    openButton.setOnAction( new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                    File file = fileChooser.showOpenDialog(primaryStage);
	                    
	                    if (file != null) {
	                        try {
								openFile(new FileReader(file),false);
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
	                    }
	                }
	            });
	    newButton.setOnAction( new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {   
	                	try {
	                		configPackageInfo.clear(); configComponentInfo.clear(); configOptionalInfo.clear();
	                		packageInfo.clear(); componentInfo.clear(); optionalInfo.clear();
							openFile(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(config_file)),true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
	                    
	                }
	            });
	    
		GridPane mainGrid = new GridPane();
	    mainGrid.setAlignment(Pos.CENTER);
	    mainGrid.setHgap(10);
	    mainGrid.setVgap(10);
	    mainGrid.setPadding(new Insets(25, 25, 25, 25));
	    
	    final HBox hb0 = new HBox();
	    hb0.getChildren().addAll(newButton,openButton);
	    hb0.setSpacing(5);
	    
	    final HBox hb1 = new HBox();
	    hb1.getChildren().addAll(btn3,GenerateOSName);
	    hb1.setSpacing(3);
	    GenerateOSName.setText("jon_package.json");
	    final HBox hb2 = new HBox();
	    hb2.getChildren().addAll(btn,GenerateJSONName);
	    hb2.setSpacing(3);
	    GenerateJSONName.setText("components.json");
	    final HBox hb3 = new HBox();
	    hb3.getChildren().addAll(message,errorMessage);
	    hb3.setSpacing(5);
	    mainGrid.add(hb0, 0, 0);
	    mainGrid.add(hb2, 1, 3);
	    mainGrid.add(hb1, 0, 3);
	//    mainGrid.add(btn2, 2, 3);
	    mainGrid.add(hb3, 2, 3);
	    Scene scene = new Scene(mainGrid, 1200, 500);
	    primaryStage.setScene(scene);
	    setLeft(mainGrid);
	    setCenter(mainGrid);
	    setRight(mainGrid,primaryStage);
	    
	}
	private void openFile(Reader file, boolean first) {	
		try {
			
			JsonParser parser = new JsonParser();
	        JsonElement jsonElement = parser.parse(file);
	        addElements(jsonElement.getAsJsonObject(),first);
	      
	      //  readJson(jsonElement.getAsJsonObject());
	    } catch (Exception ex ) {
	        ex.printStackTrace();
	    }
	}
	
	private void addElements(JsonObject object, boolean first) throws JSONException{
		if (first) {
			JsonArray array = object.getAsJsonArray("components");
			for (Map.Entry<String,JsonElement> entry : object.entrySet()) {
				if (!entry.getKey().equals("components")) {
					packageInfo.add(new TableEntry1( entry.getKey(), entry.getValue().getAsString()));
					configPackageInfo.add(new TableEntry1( entry.getKey(), entry.getValue().getAsString()));
				}	
			}
			for (JsonElement el : array) {
				String name = el.getAsJsonObject().get("name").getAsString();
				String version = el.getAsJsonObject().get("version").getAsString();
				int id =  el.getAsJsonObject().get("id").getAsInt();
				String filePath =  el.getAsJsonObject().get("filepath").getAsString();
				if (  id < 100) {
					componentInfo.add(new TableEntry2(name,version,id,filePath));
					configComponentInfo.add(new TableEntry2(name,version,id,filePath));
					if (id  == 7) {
						if ((el.getAsJsonObject()).has("base_version"))
							OSBaseVersion.setText((el.getAsJsonObject()).get("base_version").getAsString());
						OSFilePath.setText(filePath);
					}
				}
				else {
					optionalInfo.add(new TableEntry3(name,version,id,filePath,false));
					configOptionalInfo.add(new TableEntry3(name,version,id,filePath,false));
				}
				
			}
			
			
		}
		else {
			//componentInfo.clear(); optionalInfo.clear();
			componentInfo.removeAll(componentInfo);
			optionalInfo.removeAll(optionalInfo);
		
			JsonArray array = object.getAsJsonArray("components");
			packageInfo.clear();
			for (Map.Entry<String,JsonElement> entry : object.entrySet()) {
				if (!entry.getKey().equals("components")) {
					packageInfo.add(new TableEntry1( entry.getKey(), entry.getValue().getAsString()));
				}	
			}
			for (JsonElement el : array) {
				String name = el.getAsJsonObject().get("name").getAsString();
				String version = el.getAsJsonObject().get("version").getAsString();
				int id =  el.getAsJsonObject().get("id").getAsInt();
				String filePath =  el.getAsJsonObject().get("filepath").getAsString();
				if (  id < 100) {
						for (int i = 0; i < configComponentInfo.size(); i ++) {
							if (configComponentInfo.get(i).getIdValue() == id) {
							//	componentInfo.get(i).setVersionName(version);
							//		filePath = componentInfo.remove(i).getFilePathName();
									componentInfo.add(new TableEntry2(name,version,id,configComponentInfo.get(i).getFilePathName()));
								
							}
						}
						if (id  == 7) {
							if ((el.getAsJsonObject()).has("base_version"))
								OSBaseVersion.setText((el.getAsJsonObject()).get("base_version").getAsString());
							OSFilePath.setText((el.getAsJsonObject()).get("filepath").getAsString());
						}
				}
				else {
					for (int i = 0; i < configOptionalInfo.size(); i ++) {
						if (configOptionalInfo.get(i).getIdValue() == id) {
							//optionalInfo.get(i).setVersionName(version);
							//	filePath = optionalInfo.remove(i).getFilePathName();				
								optionalInfo.add(new TableEntry3(name,version,id,configOptionalInfo.get(i).getFilePathName(),true));
						}			
					}
				}
			}
			for (int i = 0; i < configOptionalInfo.size(); i ++) {
				String name = configOptionalInfo.get(i).getCompName();
				String version = configOptionalInfo.get(i).getVersionName();
				int id =  configOptionalInfo.get(i).getIdValue();
				boolean found = false;
				for (int j = 0; j < optionalInfo.size(); j++) {
					if (configOptionalInfo.get(i).getIdValue() == optionalInfo.get(j).getIdValue()) {
							found = true;
					}
				}
				if (!found) {
					optionalInfo.add(new TableEntry3(name,version,id,configOptionalInfo.get(i).getFilePathName(),false));
				}
			}
		//	componentInfo.add(new TableEntry2("","",-1,""));
		//	optionalInfo.add(new TableEntry3("","",-1,"",false));
			table1.refresh();
			table2.refresh();
			table3.refresh();
		}
	}
	
	private void setLeft(GridPane mainGrid) {
		final TextField addKey;
		final TextField addVal;
		Button addButton,removeButton;
		addKey = new TextField();
		addVal = new TextField();
		addButton = new Button("Add");
		removeButton = new Button("Remove");
		
		Text title = new Text("Package Info:");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
	   
	    table1.setEditable(true);
	    TableColumn keyCol = new TableColumn("Key");
	    keyCol.setMinWidth(100);
	    keyCol.setCellValueFactory(new PropertyValueFactory<TableEntry1,String>("keyName"));
	    TableColumn valCol = new TableColumn("Value");
	    valCol.setMinWidth(200);
	    valCol.setCellValueFactory(new PropertyValueFactory<TableEntry1,String>("valueName"));
	
	    table1.setItems(packageInfo);
	    table1.getColumns().addAll(keyCol, valCol);
	
	    addKey.setPromptText("Add Key");
	    addKey.setMaxWidth(keyCol.getPrefWidth());
	    addVal.setMaxWidth(valCol.getPrefWidth());
	    addVal.setPromptText("Add Value");
	    
	    valCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    valCol.setOnEditCommit(
	        new EventHandler<CellEditEvent<TableEntry1, String>>() {
	            @Override
	            public void handle(CellEditEvent<TableEntry1, String> t) {
	                if (!t.getNewValue().isEmpty())
	                	((TableEntry1) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValueName(t.getNewValue());
	            }
	        }
	    );
	    
	    addButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	if (!addKey.getText().isEmpty() & !addVal.getText().isEmpty() )
	        		packageInfo.add(new TableEntry1( addKey.getText(),addVal.getText()));
	    		addKey.clear();
	            addVal.clear();
	        }
	    });
	    removeButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	packageInfo.remove(table1.getSelectionModel().getSelectedItem());
	    		addKey.clear();
	            addVal.clear();
	        }
	    });
	   
	    final HBox hb = new HBox();
	    hb.getChildren().addAll(addKey, addVal,addButton, removeButton);
	    hb.setSpacing(3);
	    
	    
	    final VBox vbox = new VBox();
	    vbox.setSpacing(5);
	    vbox.setPadding(new Insets(10, 0, 0, 10));
	    vbox.getChildren().addAll(title, table1,hb);
	   
	    mainGrid.add(vbox, 0, 1);
	 
	
	    
	}
	private void setCenter(GridPane mainGrid) {
		
		TextField addKey,addVal;
		Button addButton,removeButton;
		addKey = new TextField();
		addVal = new TextField();
		addButton = new Button("Add");
		removeButton = new Button("Remove");
		Text title = new Text("Components Info:");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
	   
	    table2.setEditable(true);
	    TableColumn<TableEntry2, String> keyCol = new TableColumn<>("Component");
	    keyCol.setMinWidth(130);
	    keyCol.setCellValueFactory(new PropertyValueFactory<TableEntry2,String>("compName"));
	    TableColumn<TableEntry2, String> valCol = new TableColumn<>("Version");
	    valCol.setMinWidth(250);
	    valCol.setCellValueFactory(new PropertyValueFactory<TableEntry2,String>("versionName"));
	   
	    table2.setItems(componentInfo);
	    table2.getColumns().addAll(keyCol, valCol);
	
	    addKey.setPromptText("Add Key");
	    addKey.setMaxWidth(keyCol.getPrefWidth());
	    addVal.setMaxWidth(valCol.getPrefWidth());
	    addVal.setPromptText("Add Value");
	    
	    valCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    valCol.setOnEditCommit(
	        new EventHandler<CellEditEvent<TableEntry2, String>>() {
	            @Override
	            public void handle(CellEditEvent<TableEntry2, String> t) {
	            	if (!t.getNewValue().isEmpty())
	            		((TableEntry2) t.getTableView().getItems().get( t.getTablePosition().getRow())).setVersionName(t.getNewValue());
	            }
	        }
	    );
	 /*   
	    addButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	if (!addKey.getText().isEmpty() & !addVal.getText().isEmpty() )
	        		componentInfo.add(new TableEntry2( addKey.getText(),addVal.getText()));
	    		addKey.clear();
	            addVal.clear();
	        }
	    });
	    removeButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	componentInfo.remove(table2.getSelectionModel().getSelectedItem());
	    		addKey.clear();
	            addVal.clear();
	        }
	    });
	   
	    final HBox hb = new HBox();
	    hb.getChildren().addAll(addKey, addVal,addButton, removeButton);
	    hb.setSpacing(3);
	    
	   */
	    Label OSBaseLabel = new Label("OS Base Version: ");
	    Label OSFilePathLabel = new Label("OS Filepath: ");
	    final HBox hb1 = new HBox();
	    hb1.getChildren().addAll(OSBaseLabel, OSBaseVersion);
	    hb1.setHgrow(OSBaseLabel, Priority.ALWAYS);
	    hb1.setHgrow(OSBaseVersion, Priority.ALWAYS);
	    hb1.setSpacing(3);
	    final HBox hb2 = new HBox();
	    hb2.getChildren().addAll(OSFilePathLabel, OSFilePath);
	    hb1.setHgrow(OSFilePathLabel, Priority.ALWAYS);
	    hb1.setHgrow(OSFilePath, Priority.ALWAYS);
	    hb2.setSpacing(3);
	    final VBox vbox = new VBox();
	    vbox.setSpacing(5);
	    vbox.setPadding(new Insets(10, 0, 0, 10));
	    vbox.getChildren().addAll(title, table2, hb1, hb2);
	   
	    mainGrid.add(vbox, 1, 1);
	
	}
	private void setRight(GridPane mainGrid,final Stage primaryStage) {
	
		Button addButton;		
		addButton = new Button("Add/Remove Component (Mandatory or Optional)");
		
		Text title = new Text("Optional:");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		
		table3.setEditable(true);
	    TableColumn<TableEntry3,String> keyCol = new TableColumn<>("Component");
	    keyCol.setMinWidth(130);
	    keyCol.setCellValueFactory(new PropertyValueFactory<TableEntry3,String>("compName"));
	    TableColumn<TableEntry3,String> valCol = new TableColumn<>("Version");
	    valCol.setMinWidth(150);
	    valCol.setCellValueFactory(new PropertyValueFactory<TableEntry3,String>("versionName"));
	    TableColumn<TableEntry3,Boolean> activeCol = new TableColumn<>("Include?");
	    activeCol.setMinWidth(50);
	    activeCol.setCellValueFactory(new PropertyValueFactory<TableEntry3,Boolean>("active"));
	   
	    table3.setItems(optionalInfo);
	    table3.getColumns().addAll(keyCol, valCol, activeCol);
	
	    
	
	    valCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    valCol.setOnEditCommit(
	        new EventHandler<CellEditEvent<TableEntry3, String>>() {
	            @Override
	            public void handle(CellEditEvent<TableEntry3, String> t) {
	            	if (!t.getNewValue().isEmpty())
	            		((TableEntry3) t.getTableView().getItems().get( t.getTablePosition().getRow()) ).setVersionName(t.getNewValue());
	            }
	        }
	    );
	    
	    activeCol.setCellValueFactory(new Callback<CellDataFeatures<TableEntry3, Boolean>, ObservableValue<Boolean>>()  {  	 
	        @Override
	        public ObservableValue<Boolean> call(CellDataFeatures<TableEntry3, Boolean> param) {
	        	final TableEntry3 entry = param.getValue();
	
	            SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(entry.getActive());
	            booleanProp.addListener(new ChangeListener<Boolean>() {
	
	                @Override
	                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
	                        Boolean newValue) {
	                	entry.setActive(newValue);
	                }
	            });
	            return booleanProp;
	        }
	    });
	
	    activeCol.setCellFactory(new Callback<TableColumn<TableEntry3, Boolean>, //
	    TableCell<TableEntry3, Boolean>>() {
	        @Override
	        public TableCell<TableEntry3, Boolean> call(TableColumn<TableEntry3, Boolean> p) {
	            CheckBoxTableCell<TableEntry3, Boolean> cell = new CheckBoxTableCell<TableEntry3, Boolean>();
	            cell.setAlignment(Pos.CENTER);
	            return cell;
	        }
	    });
	    
	    
	    
	    
	    addButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	Stage addRemove = new Stage(); // new stage
	        	addRemove.initModality(Modality.APPLICATION_MODAL);
	            // Defines a modal window that blocks events from being
	            // delivered to any other application window.
	        	addRemove.initOwner(primaryStage);
	        	setUpAddRemove(addRemove);
	            
	            addRemove.show();
	            
	        }
	    });
	   
	    final HBox hb = new HBox();
	    hb.getChildren().addAll(addButton);
	    hb.setSpacing(3);
	    hb.setHgrow(addButton, Priority.ALWAYS);
	    hb.setAlignment(Pos.CENTER);
	    
	    final VBox vbox = new VBox();
	    vbox.setSpacing(5);
	    vbox.setPadding(new Insets(10, 0, 0, 10));
	    vbox.getChildren().addAll(title, table3, hb);
	   
	    mainGrid.add(vbox, 2, 1);
	}
	public void setUpAddRemove(final Stage addRemove) {
		addRemove.setTitle("Add and Remove Components");
		
		Text title = new Text("To be completed...");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
		
		
		VBox vb = new VBox(20);
	
	    Button okay = new Button("Okay");
	    okay.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent e) {
	        	addRemove.close();
	            
	        }
	    });
	    vb.getChildren().addAll(title,okay);
	    vb.setAlignment(Pos.CENTER);
	    Scene addRemoveScene = new Scene(vb, 300, 200);
	    addRemove.setScene(addRemoveScene);
	}


}
