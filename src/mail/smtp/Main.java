package mail.smtp;

import java.io.IOException;

class LPException extends Exception {}

public class Main {
    public static void main(String[] args) throws IOException {
        final String CONF_FILE_NAME = "smtpfile.properties";
        final String TEMPLATE_FILE = "template.txt";

        try {
            if (args.length != 2) {
                throw new LPException();
            }
            else {
                SendMail.send(CONF_FILE_NAME, TEMPLATE_FILE, args[0], args[1]);
            }
        }
        catch (LPException e) {
            System.err.println("login/password not found.");
        }
    }
}
