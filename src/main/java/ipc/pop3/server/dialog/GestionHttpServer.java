package ipc.pop3.server.dialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;

public class GestionHttpServer extends GestionHttp {

    private final static String http_version_tag = "HTTP/1.1 ";

    public static int sendFile(BufferedOutputStream bos, String filename) {
        String header = http_version_tag + 200 + " OK\r\n";
        return GestionHttp.sendFile(bos, new File(filename), header);
    }

    public static int writeFile(BufferedInputStream bis, String filename, int length) {

        return GestionHttp.writeFile(bis, "server"+filename, length);
    }
}
