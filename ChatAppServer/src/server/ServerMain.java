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

/**
 * This class handles the application:
 *  - get data from the ServerConnection
 *  - update GUI
 *       + display the time server started
 *       + display the time clients connected (multi clients)
 *       + receive and display messages from clients
 *       + send messages to all clients
 */
package server;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerMain extends Application {

    private TextArea messages = new TextArea(); // for displaying messages
    private ServerConnection server = createServer(); // create server

    /**
     * Create the GUI content of the stage:
     *  - TextArea : display messages
     *  - layout: VBOX
     *  - this will return Parent for setting the scene in the start method
     */
    private Parent createContent() {
        messages.setPrefHeight(600);
        messages.setEditable(false);
        VBox root = new VBox(20, messages);
        root.setPrefSize(600, 600);
        return root;
    }

    /**
     * Start the server thread *
     */
    @Override
    public void init() throws Exception {
        server.startConnection();
    }

    /**
     * Start the application   *
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle("Multi-Threaded Server");
        primaryStage.show();

    }

    /**
     * will close the server when the application is stopped    *
     */
    @Override
    public  void stop() throws Exception{
        server.closeConnection();
    }

    /**
     * create server, establish connections
     * - update GUI: use Platform.runlater to make sure the server thread does not block the application thread
     * @return ServerConnection
     */
    private ServerConnection createServer() {
        return new ServerConnection(data ->{
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    /**
     * main method to launch the app
     */
    public static void main(String[] args) {
        launch(args);
    }
}

