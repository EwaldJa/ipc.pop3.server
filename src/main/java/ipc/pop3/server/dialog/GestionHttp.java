package ipc.pop3.server.dialog;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GestionHttp {

    private final static String content_length_tag = "Content-Length: ";
    private final static int byte_number_read = 2048;

    protected static int sendFile(BufferedOutputStream bos, File file, String header){
        try{
            int totallength = 0;
            byte[] buff = new byte[byte_number_read];
            FileInputStream fo = new FileInputStream(file);
            int size = fo.read(buff);

            totallength+=size;
            String s = new String(buff, StandardCharsets.ISO_8859_1);
            while (size == byte_number_read) {
                size = fo.read(buff);
                totallength+=size;
                s += new String(buff, StandardCharsets.ISO_8859_1);
            }

            String contentLength = content_length_tag + totallength + "\r\n\r\n";
            String totalRequest = header + contentLength;// + payload;
            bos.write(totalRequest.getBytes(StandardCharsets.UTF_8));
            bos.flush();
            bos.write(s.getBytes(StandardCharsets.ISO_8859_1));
            bos.flush();
            fo.close();
            return 0;
        }
        catch(FileNotFoundException e) {
            return 404;
        }
        catch (IOException e) {
            return 500;
        }
    }

    protected static int writeFile(BufferedInputStream bis, String filename, int length){
        int byteread;
        int writtenbyte = 0;
        try{
            File file = new File(filename);
            FileOutputStream fo = new FileOutputStream(file);
            //System.out.println("ecriture1 : filename=" + filename + ", lenght=" + length);
            while(writtenbyte < length) {
                //System.out.print("lecture octet, valeur:");
                byteread = bis.read();
                //System.out.print(byteread);
                if (byteread != -1) {
                    writtenbyte++;
                    //System.out.println(", writtenbyte=" + writtenbyte);
                    fo.write(byteread);
                } else {
                    //System.out.println("breaké");
                    break;
                }
            }
            //System.out.println("sorti while");
            fo.flush();
            fo.close();
            return 0;
        }catch(FileNotFoundException e){
            return 404;
        }catch(IOException e){
            return 500;
        }
    }

}
