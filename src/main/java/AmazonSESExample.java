import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Properties;

// JavaMail libraries. Download the JavaMail API
// from https://javaee.github.io/javamail/
// AWS SDK libraries. Download the AWS SDK for Java
// from https://aws.amazon.com/sdk-for-java

public class AmazonSESExample {

    // Replace sender@example.com with your "From" address.
    // This address must be verified with Amazon SES.
    private static String SENDER = "bhargava.k@study42.com";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    private static String RECIPIENT_TO = "";
    private static String RECIPIENT_CC = "";

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
    // The email body for recipients with non-HTML email clients.
    private static String BODY_TEXT = "Hello,\r\n"
            + "Please see the attached UCIC and Promoters file ";


    public static void main(String[] args) throws AddressException, MessagingException, IOException {
        ByteArrayOutputStream outputStream = null;
        Session session = Session.getDefaultInstance(new Properties());

        // Create a new MimeMessage object.
        MimeMessage message = new MimeMessage(session);

        // Add subject, from and to lines.
        message.setSubject(SUBJECT, "UTF-8");
        message.setFrom(new InternetAddress(SENDER));
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


        // Try to send the email.
        try {
            System.out.println("Attempting to send an email through Amazon SES "
                    + "using the AWS SDK for Java...");

            // Instantiate an Amazon SES client, which will make the service
            // call with the supplied AWS credentials.
            AWSCredentialsProvider credentialsProvider = new AWSCredentialsProvider() {

                public void refresh() {
                }

                public AWSCredentials getCredentials() {
                    return new AWSCredentials() {

                        public String getAWSSecretKey() {
                            return "";
                        }

                        public String getAWSAccessKeyId() {
                            return "";
                        }
                    };
                }
            };
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
                    .standard().withCredentials(credentialsProvider).withRegion(Regions.EU_WEST_1).build();
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

            client.sendRawEmail(rawEmailRequest);
            System.out.println("Email sent!");
            // Display an error if something goes wrong.
        } catch (Exception ex) {
            System.out.println("Email Failed");
            System.err.println("Error message: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            outputStream.close();
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
