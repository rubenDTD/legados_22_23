package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class MainApp extends Application {
	
    public static Stage primaryStage;
	private String Titulo = "Wrapper 3270";
    
	@Override
	public void start(Stage primaryStage) throws IOException {	
		this.primaryStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("Portada.fxml")); 
        Scene scene = new Scene(root, 800, 500); 
        primaryStage.setTitle(Titulo);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
