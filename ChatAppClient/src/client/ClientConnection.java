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

/** This class will:
 *    - establish connection to server
 *    - manage client's name
 *    - send and receive messages from server *
 */
package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientConnection {

    String ip = "localhost";
    int port = 8000;

    private ClientThread clientThread = new ClientThread();
    private Consumer<Serializable> onReceiveMsg;

    /**
     * manage client's name
     */
    private String clientName;
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public String getClientName() {
        return clientName;
    }

    // constructor of ClientConnection
    public ClientConnection(Consumer<Serializable> onReceiveMsg) {
        this.onReceiveMsg = onReceiveMsg;
        this.clientThread.setDaemon(true); // make sure it does not prevent the JVM from exiting
    }

    /**
     * Start the client thread
     */
    public void startConnection() throws Exception {
        clientThread.start(); // start the thread
    }

    /**
     * Send messages to server
     */
    public void send(Serializable data) throws Exception {
        clientThread.out.writeObject(data);
    }

    /**
     * close the client connection
     */
    public void closeConnection() throws Exception {
        clientThread.socket.close();
    }

    /**
     * An inner class:
     * - establish socket
     * - obtain streams
     * - receive messages
     */
    private class ClientThread extends Thread {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        @Override
        public void run(){
            try {

                Socket socket = new Socket(ip, port); // create new Socket object

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                this.socket = socket;
                this.out = out;
                this.in = in;
                socket.setTcpNoDelay(true);

                // keep receiving messages from server
                while (true) {
                    Serializable data = (Serializable) in.readObject();
                    onReceiveMsg.accept(data);
                }

            } catch (Exception e) {
                onReceiveMsg.accept("Connection closed");
            } finally { // close streams, connection
                try {
                    out.close();
                    in.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

