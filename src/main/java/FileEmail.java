import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AWSJavaMailTransport;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

;

public class FileEmail {
       public static final String SENDER="bhargava.k@study42.com";
        public static final String  []  RECIPIENT = {"bhargava.k@capitalfloat.com"};
        public static final String BODY_TEXT = "Test";
        public static final String ATTACHMENT = "/home/bhargava/doc_section/dwtasks/1018695p.TXT";
    public static final String ATTACHMENT1 = "/home/bhargava/doc_section/dwtasks/1018695.TXT";
        public  static  final String SUBJECT="test";

        public static void main(String[] args) throws MessagingException, IOException {
                String fileToAttachPath = ATTACHMENT;
                sendMail(SUBJECT,"s",ATTACHMENT,"1018695p.TXT","text/plain",SENDER, RECIPIENT);


        }
        public static void sendMail(String subject, String message, String attachement, String fileName, String contentType, String from, String[] to) {
                try {
                        // JavaMail representation of the message
                        Session s = Session.getInstance(new Properties(), null);
                        MimeMessage mimeMessage = new MimeMessage(s);

                        // Sender and recipient
                        mimeMessage.setFrom(new InternetAddress(from));
                        for (String toMail : to) {
                                mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toMail));
                        }

                        // Subject
                        mimeMessage.setSubject(subject);

                        // Add a MIME part to the message
                        MimeMultipart mimeBodyPart = new MimeMultipart();
                        BodyPart part = new MimeBodyPart();
                        part.setContent(message, "text/plain");
                        mimeBodyPart.addBodyPart(part);

                        // Add a attachement to the message
                        part = new MimeBodyPart();
                        DataSource source = new FileDataSource(attachement);
                        part.setDataHandler(new DataHandler(source));
                        part.setFileName(fileName);
                        mimeBodyPart.addBodyPart(part);

                    BodyPart part1 = new MimeBodyPart();
                    DataSource source1 = new FileDataSource(ATTACHMENT1);
                    part1.setDataHandler(new DataHandler(source1));
                    part1.setFileName("1018695.TXT");
                    mimeBodyPart.addBodyPart(part1);

                        mimeMessage.setContent(mimeBodyPart);

                        // Create Raw message
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        mimeMessage.writeTo(outputStream);
                        RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

                        // Credentials
                        String keyID = "AKIAJBT73QO3WS7TSEPQ";// <your key id>
                        String secretKey = "n+aK3d3AWOlmwJts7tXPXfau5rj1t6teS09u7X3s";// <your secret key>
                        AWSCredentials credentials = new BasicAWSCredentials(keyID, secretKey);
                        AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
                        client.setRegion(Region.getRegion(Regions.EU_WEST_1));
                        // Send Mail
                        SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
                        rawEmailRequest.setDestinations(Arrays.asList(to));
                        rawEmailRequest.setSource(from);
                        client.sendRawEmail(rawEmailRequest);

                } catch (IOException | MessagingException e) {
                        // your Exception
                        e.printStackTrace();
                }
        }
        }
