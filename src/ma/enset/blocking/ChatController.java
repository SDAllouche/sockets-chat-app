package ma.enset.blocking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    ObservableList<String> observableList= FXCollections.observableArrayList();
    @FXML
    ListView<String> listView=new ListView<>();

    @FXML
    private TextField textHost,textPort,textMessage;

    private PrintWriter pw;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void connect() throws IOException {
        String host=textHost.getText();
        String port=textPort.getText();
        if (!host.isEmpty() && !port.isEmpty()) {
            int portInt=Integer.parseInt(port);
            Socket socket=new Socket(host,portInt);
            InputStream is=socket.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            OutputStream os=socket.getOutputStream();
            pw=new PrintWriter(os,true);
            listView.setItems(observableList);
            new Thread(() -> {
                try {
                    String request;
                    while ((request=br.readLine())!=null){
                        observableList.add(request);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Empty Fields !");
            alert.show();
        }
    }

    public void send() throws IOException {
        String message=textMessage.getText();
        if (!message.isEmpty()) {
            pw.println(message);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Empty Fields !");
            alert.show();
        }
    }
    public void clearText() {
        textHost.clear();
        textPort.clear();
    }
}
