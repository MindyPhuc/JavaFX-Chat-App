# JavaFX-Chat-App

The project demonstrates the simple chatting scenario in which server is in the middle handling the conversations between multiple clients
  * Using multiple threading in Java
  * Develop basic GUI using JavaFX
  
### How to run the program:
  * Run the server first by navigating to the folder that contains the ChatAppServer.jar file (ChatAppServer\out\artifacts\ChatAppServer_jar), then open the terminal and type the command line to run the server:
	"java --module-path "<path to javafx on your computer>" --add-modules javafx.controls,javafx.fxml,javafx.swing -jar ChatAppServer.jar"
  
  * Run the client (run multiple times for multiple clients) by navigating to the folder that contains the ChatAppClient.jar file (ChatAppClient\out\artifacts\ChatAppClinet_jar) then open the terminal and type the command line to run th client:
	"java --module-path "<path to javafx on your computer>" --add-modules javafx.controls,javafx.fxml,javafx.swing -jar ChatAppClient.jar"
  
  * Note: your computer needs to have JavaFX installed and the path set up properly.
