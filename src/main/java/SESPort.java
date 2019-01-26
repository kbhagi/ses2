import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Properties;

public class SESPort {

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "admin@capitalfloat.com";
    static final String FROMNAME = "Capital Float";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    static final String TO = "eartherk5yb@gmail.com";

    // Replace smtp_username with your Amazon SES SMTP user name.
    static final String SMTP_USERNAME = "AKIAJ53XE6UORIIJ45GQ";

    // Replace smtp_password with your Amazon SES SMTP password.
    static final String SMTP_PASSWORD = "AgCtlPa5souHnHQc9j548UPJGke0mcgfiKp1x4GwlWkw";
    // The name of the Configuration Set to use for this message.
    // If you comment out or remove this variable, you will also need to
    // comment out or remove the header below.
    static final String CONFIGSET = "ConfigSet";
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.us-west-2.amazonaws.com";
    // The port you will connect to on the Amazon SES SMTP endpoint.
    static final int PORT = 587;
    private static String RECIPIENT_TO = "bhargava.k@capitalfloat.com";
    private static String RECIPIENT_CC = "bhargavak37@gmail.com";
    // Specify a configuration set. If you do not want to use a configuration
    // set, comment the following variable, and the
    // ConfigurationSetName=CONFIGURATION_SET argument below.
    private static String CONFIGURATION_SET = "ConfigSet";
    // The subject line for the email.
    private static String SUBJECT = "IndusInd UCIC and Promoters Files";
    // The full path to the file that will be attached to the email.
    // If you're using Windows, escape backslashes as shown in this variable.
    private static String ATTACHMENT = "/home/bhargava/doc_section/dwtasks/1018695p.TXT";
    private static String ATTACHMENT1 = "/home/bhargava/doc_section/dwtasks/1018695.TXT";
    private static String SENDER = "bhargava.k@study42.com";
    private static String BODY_TEXT = "Hello,\r\n"
            + "Please see the attached UCIC and Promoters file ";

//    static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";

//    static final String BODY = String.join(
//            System.getProperty("line.separator"),
//            "<h1>Amazon SES SMTP Email Test</h1>",
//            "<p>This email was sent with Amazon SES using the ",
//            "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
//            " for <a href='https://www.java.com'>Java</a>."
//    );

    public static void main(String[] args) throws Exception {

        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties.
//        Session session = Session.getDefaultInstance(props);
//
//        // Create a message with the specified information.
//        MimeMessage msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress(FROM,FROMNAME));
//        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
//        msg.setSubject(SUBJECT);
//        msg.setContent(BODY,"text/html");

        // Add a configuration set header. Comment or delete the
        // next line if you are not using a configuration set
        // msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);

        // Create a transport.
        ByteArrayOutputStream outputStream = null;
        Session session = Session.getDefaultInstance(new Properties());

        // Create a new MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Add subject, from and to lines.
        message.setSubject(SUBJECT, "UTF-8");
        message.setFrom(new InternetAddress(FROM));
        setReceipientsTO(RECIPIENT_TO, message);
        setReceipientsCC(RECIPIENT_CC, message);
        // message.addRecipients(Message.RecipientType.CC, InternetAddress.parse("deepthi.meduri@capitalfloat.com"));

        // Create a multipart/alternative child container.
        MimeMultipart msg_body = new MimeMultipart("alternative");

        // Create a wrapper for the HTML and text parts.
        MimeBodyPart wrap = new MimeBodyPart();

        // Define the text part.
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(BODY_TEXT, "text/plain; charset=UTF-8");

        // Define the HTML part.
//        MimeBodyPart htmlPart = new MimeBodyPart();
//        htmlPart.setContent(BODY_HTML,"text/html; charset=UTF-8");

        // Add the text and HTML parts to the child container.
        msg_body.addBodyPart(textPart);
//        msg_body.addBodyPart(htmlPart);

        // Add the child container to the wrapper object.
        wrap.setContent(msg_body);

        // Create a multipart/mixed parent container.
        MimeMultipart msg = new MimeMultipart("mixed");

        // Add the parent container to the message.
        message.setContent(msg);

        // Add the multipart/alternative part to the message.
        msg.addBodyPart(wrap);

        // Define the attachment.
        //MimeBodyPart att1;

        // Add the attachment to the message.
        msg.addBodyPart(attachFile(ATTACHMENT));
        msg.addBodyPart(attachFile(ATTACHMENT1));
        Transport transport = session.getTransport();
        // Replace US_WEST_2 with the AWS Region you're using for
        // Amazon SES.


        // Print the raw email content on the console
        PrintStream out = System.out;
        message.writeTo(out);

        // Send the email.
        outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);
        RawMessage rawMessage =
                new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));


        SendRawEmailRequest rawEmailRequest =
                new SendRawEmailRequest(rawMessage);
        // Send the message.
        try {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            // Send the email.

            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }

    private static MimeBodyPart attachFile(String attachment) throws MessagingException {
        MimeBodyPart part = new MimeBodyPart();
        DataSource fds = new FileDataSource(attachment);
        part.setDataHandler(new DataHandler(fds));
        part.setFileName(fds.getName());
        return part;
    }

    private static void setReceipientsTO(String receipientsList, MimeMessage message) throws MessagingException {
        String recipient = receipientsList;
        String[] recipientList = recipient.split(",");
        InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
        int counter = 0;
        for (String recipients : recipientList) {
            recipientAddress[counter] = new InternetAddress(recipients.trim());
            counter++;
        }
        message.setRecipients(Message.RecipientType.TO, recipientAddress);
    }


    private static void setReceipientsCC(String receipientsList, MimeMessage message) throws MessagingException {
        String recipient = receipientsList;
        String[] recipientList = recipient.split(",");
        InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
        int counter = 0;
        for (String recipients : recipientList) {
            recipientAddress[counter] = new InternetAddress(recipients.trim());
            counter++;
        }
        message.setRecipients(Message.RecipientType.CC, recipientAddress);
    }
}
