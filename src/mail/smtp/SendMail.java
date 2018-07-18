package mail.smtp;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class SendMail {
    public static void send(String cfgFileName, String patternFile,
                            String senderLogin, String senderPassword) throws IOException {
        Properties props = new Properties();

        try (FileInputStream cfg = new FileInputStream(cfgFileName)) {
            props.load(new InputStreamReader(cfg, Charset.forName("UTF-8")));

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(senderLogin,
                                    senderPassword);
                        }
                    });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderLogin));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(props.getProperty("addresses")));

                ParsePattern pp = new ParsePattern(patternFile, props);

                message.setSubject(pp.getTitle());
                message.setText(pp.getBody());

                Transport.send(message);

                System.out.println("Email sent.");

            } catch (MessagingException e) {
                System.err.println("Failed to sent email.");
                throw new RuntimeException(e);
            }
        }
    }
}
