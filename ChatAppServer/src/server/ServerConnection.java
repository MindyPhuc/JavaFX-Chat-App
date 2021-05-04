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

/** This class will handle the server thread and connection thread
 *  - ServerThread: to ensure the running of the server does not hinder the application thread
 *  - ConnectionThread: for handling multiple clients simultaneously
 *  - server will:
 *        + listen from multiple clients
 *        + display the messages
 *        + send the messages to all clients
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

public class ServerConnection {
    int port = 8000; // port number

    private ServerThread sThread = new ServerThread(); // construct new ServerThread object
    private Consumer<Serializable> onReceiveMsg; // when server receive message

    public ServerConnection(Consumer<Serializable> onReceiveMsg) {
        this.onReceiveMsg = onReceiveMsg;
        this.sThread.setDaemon(true); // make sure it does not prevent the JVM from exiting
    }

    /**
     * Start the server thread
     * @throws Exception
     */
    public void startConnection() throws Exception {
        sThread.start(); // start the thread
    }

    /**
     * close the connection
     * @throws Exception
     */
    public void closeConnection() throws Exception {
        sThread.socket.close();
    }

    /**
     * Utility method to format the time for displaying purpose
     * time format example: Wednesday March 27 20:05:26 EDT
     */
     private String dateToString(Date date) {
        String pattern = "EEEEE MMMMM dd yyyy HH:mm:ss z";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern, new Locale("en", "CA"));
        String result = simpleDateFormat.format(date);

        return result;
    }

    /**
     * An inner class:
     *  - create ServerSocket object
     *  - listen for connections (thread) from clients (ArrayList of connections)     *
     */
    private class ServerThread extends Thread {
        ArrayList<ConnectionThread> connections = new ArrayList<>();
        Socket socket;

        @Override
        public void run(){
            try {
                ServerSocket server = new ServerSocket(port); // create ServerSocket object
                onReceiveMsg.accept("Server started at " + dateToString(new Date())); // for display the time server started

                // keep listen for connections from clients
                // establish the connections
                // start the ConnectionThread and add it to the ArrayList of connections
                while (true) {
                    Socket socket = server.accept();
                    this.socket = socket;
                    // create new connection thread, start it and add it tho the arraylist
                    ConnectionThread conn = new ConnectionThread(socket, this);
                    onReceiveMsg.accept("Connection from " + socket + " at " + dateToString(new Date()));
                    conn.start();
                    connections.add(conn);
                }

            } catch (Exception e) {
                onReceiveMsg.accept("Connection closed");
            }         }
    }


    /**
     * An inner class:
     * - will handle connections from clients
     * - obtain the streams for sending and receiving objects
     * - send message to clients
     */
    private class ConnectionThread extends Thread {
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private Socket socket;
        private ServerThread server;

        public ConnectionThread(Socket socket, ServerThread server) {
            this.socket = socket;
            this.server = server;
        }

        // send message to 1 client
        public void sendMsgToClient(Serializable msg) {
            try {
                out.writeObject(msg); // send message to client
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // send string to all clients
        public void sendMsgToAllClients(Serializable msg) {
            for (int i = 0; i < server.connections.size(); i++) {
                ConnectionThread conn = server.connections.get(i);
                conn.sendMsgToClient(msg); // call the sendMsg to 1 client on each connection
            }
        }

        @Override
        public void run() {
            try{

                // obtain inputStream and outputStream
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                // assign them to the inputStream and outputStream of the current connection
                this.out = out;
                this.in = in;

                // keep receiving and sending the messages
                while (true) {
                    Serializable data = (Serializable) in.readObject();
                    onReceiveMsg.accept(data); // for displaying the messages
                    // send data to all clients
                    sendMsgToAllClients(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally { // close streams and connection
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
