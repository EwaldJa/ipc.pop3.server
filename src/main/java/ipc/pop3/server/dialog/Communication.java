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
        int requestreturn = 0;
        try {
            String line = in.readLine();

            String[] head = line.split(" ");
            String request = head[0];
            switch (request) {
                //TODO
                case "APOP":
                    switch (etat) {
                        case "AUTH":
                            String username = head[1];
                            String passHashed = head[2];
                            try {
                                user = userService.logUser(username, passHashed, timestamp);
                                mails = mailService.findByUser(user);
                                out.write("+OK maildrop has " + mails.getMailTotalNumber() + " message" + ((mails.getMailTotalNumber() > 1) ? "s " : " ") + "(" + mails.getOctetSize() + " octet" + ((mails.getOctetSize() > 1) ? "s)" : ")"));
                                out.flush();
                            } catch (InvalidUsernameException | InvalidPasswordException e) {
                                out.write("-ERR permission denied");
                                out.flush();
                            }
                            //TODO: etat
                            return false;

                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }
                case "USER":
                    switch (etat) {
                        case "AUTH":
                            //TODO un jour
                            //TODO: etat
                            return false;
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }
                case "PASS":
                    switch (etat) {
                        case "WAIT PASS":
                            //TODO un jour
                            //TODO: etat
                            return false;
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }
                case "QUIT":
                    switch (etat) {
                        case "TRANSACTION":
                            mailService.update(mails);
                            out.write("+OK");
                            out.flush();
                            //TODO: etat
                            return true;
                        default:
                            out.write("+OK");
                            out.flush();
                            //TODO: etat
                            return true;
                    }
                case "STAT":
                    switch (etat) {
                        case "TRANSACTION":
                            out.write("+OK " + mails.toPOP3StatString());
                            out.flush();
                            //TODO: etat
                            return false;
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }
                case "LIST":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO un jour
                            //TODO: etat
                            return false;
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }
                case "RETR":
                    switch (etat) {
                        case "TRANSACTION":
                            try {
                                Mail mail = mails.getMail(Integer.parseInt(head[1]));
                                out.write("+OK " + mail.getSize() + " octet" + ((mail.getSize() > 1) ? "s" : ""));
                                out.write(mail.toPOP3String());
                                out.flush();
                            } catch (NumberFormatException e) {
                                out.write("-ERR impossible to parse message number : '" + head[1] + "'");
                                out.flush();
                            } catch (NoSuchMessageException e) {
                                out.write("-ERR no such message : '" + head[1] + "'");
                                out.flush();
                            } catch (MarkedAsDeletedMessageException e) {
                                out.write("-ERR message '" + head[1] + "' is marked as deleted");
                                out.flush();
                            } catch (InvalidMailNumberException e) {
                                out.write("-ERR message number is not valid : '" + head[1] + "'");
                                out.flush();
                            }
                            //TODO: etat
                            return false;

                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }
                case "NOOP":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO un jour
                            //TODO: etat
                            return false;
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }
                case "DELE":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO un jour
                            //TODO: etat
                            return false;
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }
                case "RSET":
                    switch (etat) {
                        case "TRANSACTION":
                            //TODO un jour
                            //TODO: etat
                            return false;
                        default:
                            out.write("-ERR action indisponible à ce stade");
                            out.flush();
                            //TODO: etat
                            return false;
                    }


                default:
                    out.write("-ERR action inconnue");
                    out.flush();
                    //TODO: etat
                    return false;

            }



        } catch (IOException e) {
            _log.error("Erreur lors de la réception");
            _log.error(e.getMessage());
            //TODO: etat
            return false;
        } catch (NullPointerException e) {
            _log.info("Erreur NullPointer à la réception : requete vide");
            _log.debug(e.getMessage());
            //TODO: etat
            return false;
        }
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