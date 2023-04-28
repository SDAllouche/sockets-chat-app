package ma.enset.blocking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientChat extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Client Chat");
        //BorderPane borderPane=new BorderPane();
        //Scene scene=new Scene(borderPane,500,400);
        BorderPane borderPane= FXMLLoader.load(getClass().getResource("view.fxml"));
        Scene scene=new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
}
