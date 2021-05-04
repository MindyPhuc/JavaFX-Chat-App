/**********************************************
 Workshop 9
 Course:JAC444 - Semester: Winter 2021
 Last Name: Huynh
 First Name: Thi My Phuc (Mindy)
 ID: 149792186
 Section: NBB
 This assignment represents my own work in accordance with Seneca Academic Policy.
 Signature: Thi My Phuc Huynh
 Date: Apr 13, 2021
 **********************************************/

/** This class will handle:
 * - create client
 * - GUI :
 *   + send messages to server
 *   + display messages
 *
 * - There are 2 scenes:
 *     + scene 1: get client's name from client input
 *     + scene 2: display chat messages *
 */

package client;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientMain extends Application {

    private TextArea messages = new TextArea(); // for displaying messages
    private ClientConnection connection = createClient(); // create client, set up connection with server
    Scene scene1, scene2;

    /**
     * start the client connection thread *
     */
    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    /**
     * - set scene 1 , get client's name
     * - change to scene 2, change the scene title, display the chat messages     *
     */

    @Override
    public void start(Stage primaryStage) {

        // initial setting for messages
        messages.setPrefHeight(350);
        messages.setEditable(false);
        messages.appendText("Your name is: ");

        // scene 1
        TextField textField1 = new TextField();
        textField1.setOnAction(event ->{
            connection.setClientName(textField1.getText()); // get client's name
            textField1.clear();
            messages.appendText(connection.getClientName() + "\n");
            primaryStage.setScene(scene2);  // switch to scene 2
            primaryStage.setTitle(connection.getClientName()); // change the title
        });
        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(messages, textField1);
        scene1 = new Scene(layout1, 400, 400);

        // scene 2
        TextField input = new TextField();
        input.setOnAction(event ->{
            String msg = connection.getClientName() + ": ";
            msg += input.getText();
            input.clear();

            try {
                connection.send(msg); // send the message to server
            } catch (Exception e) {
                messages.appendText("Failed to send message\n");
            }
        });
        VBox layout2 = new VBox(20);
        layout2.getChildren().addAll(messages, input);
        scene2 = new Scene(layout2, 400, 400);

        primaryStage.setScene(scene1);
        primaryStage.setTitle("Client");
        primaryStage.show();

    }

    /**
     * close the connection when the app is stopped     *
     */
    @Override
    public  void stop() throws Exception{
        connection.closeConnection();
    }

    /**
     * Create client connection thread
     * update GUI
     */
    private ClientConnection createClient(){
        return new ClientConnection(data ->{
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    /**
     * main method to launch the application *
     */
    public static void main(String[] args) {
        launch(args);
    }
}
