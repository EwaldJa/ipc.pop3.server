package ipc.pop3.server.dialog;

import ipc.pop3.server.persistence.model.Mail;
import ipc.pop3.server.persistence.model.User;
import ipc.pop3.server.persistence.service.MailService;
import ipc.pop3.server.persistence.service.UserService;
import ipc.pop3.server.persistence.utils.MailList;
import ipc.pop3.server.utils.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;

public class Communication implements Runnable {

    private static int CommID;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    private Timestamp timestamp;
    private String etat;
    private User user;
    MailList mails;

    private Socket clt_socket;
    private Logger _log = LoggerFactory.getLogger(Communication.class);
    private BufferedReader in;
    private PrintWriter out;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;

    public Communication(Socket socket) throws IOException {
        CommID++;
        clt_socket = socket;
        bos = new BufferedOutputStream(clt_socket.getOutputStream());
        bis = new BufferedInputStream(clt_socket.getInputStream());

    }


    private boolean recevoir() {
        boolean closeConnection = false;
        int requestreturn = 0;
        try {
            String line = in.readLine();

            String[] head = line.split(" ");
            String request = head[0];
            switch (request) {
                //TO DO
                case "APOP":
                    switch (etat) {
                        case "AUTH":
                            String username = head[1];
                            String passHashed = head[2];
                            try {
                                user = userService.logUser(username, passHashed, timestamp);
                                mails = mailService.findByUser(user);
                                out.write("+OK maildrop has " + mails.getMailTotalNumber() + " message"+((mails.getMailTotalNumber() > 1)?"s ":" ")+"(" + mails.getOctetSize() + " octet"+((mails.getOctetSize() > 1)?"s)":")"));
                                out.flush();
                            } catch (InvalidUsernameException | InvalidPasswordException e) {
                                out.write("-ERR permission denied");
                                out.flush();
                            }

                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }
                case "USER":
                    switch (etat) {
                        case "AUTH":
                            //TODO un jour
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }
                case "PASS":
                    switch (etat) {
                        case "WAIT PASS":
                            //TODO un jour
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }
                case "QUIT":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO
                            //update
                            mailService.update(mails);
                            out.write("+OK");
                            out.flush();
                            return false;
                        default:
                            out.write("+OK");
                            out.flush();
                            return false;
                    }
                case "STAT":
                    switch (etat) {
                        case "TRANSACTION":
                            out.write("+OK " + mails.toPOP3ListString());
                            out.flush();
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }
                case "LIST":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO un jour
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }
                case "RETR":
                    switch (etat) {
                        case "TRANSACTION":
                            boolean errorOccured = false;
                            int numMessage = 0;
                            try {
                                numMessage = Integer.parseInt(head[1]);
                            }
                            catch (NumberFormatException e) {
                                out.write("-ERR impossible to parse message number : '" + head[1] + "'");
                                out.flush();
                                errorOccured = true;
                            }
                            if (!errorOccured) {
                                Mail mail = null;
                                try {
                                    mail = mails.getMail(numMessage);
                                    }
                                catch (NoSuchMessageException e) {
                                    out.write("-ERR no such message : '" + numMessage + "'");
                                    out.flush();
                                    errorOccured = true;
                                }
                                catch (MarkedAsDeletedMessageException e) {
                                    out.write("-ERR message '" + numMessage + "' is marked as deleted");
                                    out.flush();
                                    errorOccured = true;
                                }
                                catch (InvalidMailNumberException e) {
                                    out.write("-ERR message number is not valid : '" + numMessage + "'");
                                    out.flush();
                                    errorOccured = true;
                                }
                                if (!errorOccured) {
                                    String message = mails.getMail(numMessage).getMessage();
                                    int nombreOctets = mails.getMail(numMessage).getSize();
                                    out.write("+OK " + mail.getSize() + " octet"+((mail.getSize() > 1)?"s":""));
                                    out.write(mail.toPOP3String());
                                    out.flush(); } }

                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }
                case "NOOP":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO un jour
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }
                case "DELE":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO un jour
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }
                case "RSET":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO un jour
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                    }


                default:
                    out.write("-ERR action inconnue");
                    out.flush();

            }


            String filename = "";
            for (int i = 1; i < head.length - 2; i++) {
                filename += head[i] + " ";
                System.out.println(filename);
            }
            filename += head[head.length - 2];
            if (filename.charAt(0) == '/') {
                filename = filename.substring(1);
            }
            String httpVersion = head[head.length - 1];

            _log.debug("Requête reçue : " + line + " , requete " + request + ", fichier " + filename + ", http " + httpVersion);

            if (!httpVersion.equals("HTTP/1.1")) {
                sendError(505);
                return true;
            }

            int length = 0;
            boolean headerskipped = false;
            while (!headerskipped) {
                line = in.readLine();
                if (line.equals("")) {
                    headerskipped = true;
                    break;
                }
                String[] field = line.split(" ");
                _log.debug("field complet : " + line + " , champ : " + field[0]);
                _log.debug("valeur : " + field[1]);
                if (field[0].equals("Connection:")) {
                    closeConnection = (field[1].toLowerCase().equals("close"));
                    if (closeConnection && request.equals("END") && filename.equals("nothing")) {
                        return false;
                    }
                }
                if (field[0].equals("Content-Length:")) {
                    length = Integer.parseInt(field[1]);
                }
                _log.debug("headerskipped:" + headerskipped);
            }
            _log.debug("Header passé");
            switch (request) {
                case "GET":
                    _log.debug("Appel à sendFile");
                    requestreturn = GestionHttpServer.sendFile(bos, filename);
                    _log.debug("sendFile réalisé, retour : " + requestreturn);
                    break;
                case "PUT":
                    _log.debug("Appel à writeFile");
                    requestreturn = GestionHttpServer.writeFile(bis, filename, length);
                    _log.debug("writeFile réalisé, retour : " + requestreturn);
                    break;
                default:
                    requestreturn = 400;
                    break;
            }

        } catch (IOException e) {
            _log.error("Erreur lors de la réception");
            _log.error(e.getMessage());
            requestreturn = 500;
        } catch (NullPointerException e) {
            _log.info("Erreur NullPointer à la réception : requete vide");
            _log.debug(e.getMessage());
            requestreturn = 500;
        } finally {
            if (requestreturn != 0) {
                sendError(requestreturn);
                closeConnection = true;
            }
            return closeConnection;
        }
    }

    private void sendError(int error_code) {
        String error_name;
        switch (error_code) {
            case 400:
                error_name = "Bad Request";
                break;
            case 404:
                error_name = "Not Found";
                break;
            case 500:
                error_name = "Internal Server Error";
                break;
            case 505:
                error_name = "HTTP Version not supported";
                break;
            default:
                error_code = 500;
                error_name = "Internal Server Error";
                break;
        }
        _log.debug("Appel à sendError : " + error_code + ":" + error_name);
        String request = "HTTP/1.1 " + error_code + " " + error_name + "\r\n";
        String connection = "Connection: close\r\n";
        out.write(request + connection);
        out.flush();
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clt_socket.getInputStream()));
        } catch (IOException e) {
            _log.error("Erreur lors de l'initialisation de la réception");
            _log.error(e.getMessage());
        }
        try {
            out = new PrintWriter(clt_socket.getOutputStream());
        } catch (IOException e) {
            _log.error("Erreur lors de l'initialisation de l'émission");
            _log.error(e.getMessage());
        }
        timestamp = new Timestamp(System.currentTimeMillis());
        out.write("+OK Server ready <" + timestamp + "@EwaldEtLucas.ipc>");
        out.flush();
        etat = "authorisation";
        while (!recevoir()) {/*Reçoit et répond au client*/}
        _log.debug("Fermeture de la communication" + CommID);
        try {
            clt_socket.close();
        } catch (IOException e) {
            _log.error("Erreur à la fermeture du BufferedReader de la réception");
            _log.error(e.getMessage());
        }

    }

    public void finalize() throws Throwable {
        clt_socket.close();
        super.finalize();
    }
}