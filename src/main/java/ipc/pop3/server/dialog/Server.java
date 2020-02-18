package ipc.pop3.server.dialog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {


    private ServerSocket servSocket;

    public Server(int port){
        try {
            servSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Le port est inaccessible");
        }
    }

    private Socket recevoirConnection(){
        try {
            return servSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void run(){
        while(true){
            try {
                new Thread(new Communication(recevoirConnection())).start();
                System.out.println("La connection a été établie");
            } catch (IOException e) {
                System.out.println("Impossible d'établir la connection : ");
                System.out.println(e.getMessage());
            }

        }
    }


}
