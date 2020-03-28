package ipc.pop3.server.dialog;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {


    private SSLServerSocket servSocket;

    public Server(int port){
        try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            servSocket = (SSLServerSocket) factory.createServerSocket(port);
            String[] suites = servSocket.getSupportedCipherSuites();
            ArrayList<String> anon_suites = new ArrayList<>();
            for (int i = 0; i < suites.length; i++) {
                if (suites[i].contains("_anon_")) { anon_suites.add(suites[i]); System.err.println(suites[i]); }
            }
            servSocket.setEnabledCipherSuites(anon_suites.toArray(String[]::new));
            servSocket.setEnabledProtocols(servSocket.getSupportedProtocols());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Le port est inaccessible");
        }
    }

    private SSLSocket recevoirConnection(){
        try {
            return (SSLSocket)servSocket.accept();
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
            } catch (Exception e) {
                System.out.println("Impossible d'établir la connection : ");
                System.out.println(e.getMessage());
            }

        }
    }


}
