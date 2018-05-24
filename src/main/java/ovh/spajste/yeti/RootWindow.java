package ovh.spajste.yeti;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.concurrent.*;

import java.io.IOException;
import java.util.List;

public class RootWindow extends Application {
    public static SerialCommunication serial = new SerialCommunication();
    public static ELMInterface elmInterface;
    public static AnchorPane root;
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        try {
            root = FXMLLoader.load(RootWindow.class.getResource("/fxml/RootWindow.fxml"));
            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Yeti by SpajsTech Ltd. 2018");
            primaryStage.show();
            elmInterface = new ELMInterface();
            elmInterface.initialize("ttyUSB0");
            Task rpmTask = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					List<Readout> readouts;
					while(!isCancelled()) {
						readouts = elmInterface.getReadoutsData();
						updateMessage(readouts.get(0).getValue()+" "+readouts.get(0).getUnit());
						Thread.sleep(666);
					}
					return null;
				}
            	
            };
            Label rpmlabel = (Label) scene.lookup("#rpmlabel");
            rpmlabel.textProperty().bind(rpmTask.messageProperty());
          
            new Thread(rpmTask).start();
        } catch(IOException ioe) {
            System.err.println("IOException caught during start: "+ioe.getLocalizedMessage());
        }
    }
}
