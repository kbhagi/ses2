import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.Regions;

    public class Email {

/*SMTP Username:AKIAJOZYT3L565ND6XVQ
SMTP Password: BPnB6AeNJ1q21MQ2lkgDS+jykHQSK+nBzMNOQn9ngXfG*/


        public static void main(String[] args) {
            AWSCredentialsProvider credentialsProvider = new AWSCredentialsProvider() {

                public void refresh() {}

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
            AmazonSimpleEmailService client =  AmazonSimpleEmailServiceClientBuilder
                    .standard().withCredentials(credentialsProvider).withRegion(Regions.EU_WEST_1).build();



            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses("bhargava.k@capitalfloat.com"))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData("<h1>Hello World</h1>"))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData("Hello world 2")))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData("Hello World")))
                    .withSource("bhargava.k@study42.com");

            client.sendEmail(request);
        }
    }

