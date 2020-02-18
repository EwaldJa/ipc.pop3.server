package ipc.pop3.server;

import ipc.pop3.server.dialog.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		Server serv = new Server(1110);
		new Thread(serv).start();
	}

}
