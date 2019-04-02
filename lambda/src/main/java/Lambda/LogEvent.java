package Lambda;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;


public class LogEvent implements RequestHandler<SNSEvent, Object> {

//    private DynamoDB dynamoDB;
//    private Regions REGION = Regions.US_EAST_1;
//    protected static final String DYNAMODB_ENDPOINT = "dynamodb.us-east-1.amazonaws.com";
//    protected static String token;
//    protected static String username;
//    protected static String ses_from_address;
//    protected static final String EMAIL_SUBJECT = "Password Reset Link";
//    protected static String htmlBody;
//    private static String textBody;


    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "csye6225";
    private Regions REGION = Regions.US_EAST_1;
    private static String FROM = "csye@"+System.getenv("DOMAIN");
    static final String SUBJECT = "Reset Password Link";
    static String HTMLBODY = "<h1>Csye 6225 Password reset link</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    private String textBody;


    private void initDynamoDbClient() {
//        String accessKey = System.getenv("accessKey");
//        String secretKey = System.getenv("secretKey");

        AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }


    public Object handleRequest(SNSEvent request, Context context) {

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());

        context.getLogger().log("Invocation started: " + timeStamp);

        context.getLogger().log("1: " + (request == null));

        context.getLogger().log("2: " + (request.getRecords().size()));

        context.getLogger().log(request.getRecords().get(0).getSNS().getMessage());

        timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());

        context.getLogger().log("Invocation completed: " + timeStamp);

        String username = request.getRecords().get(0).getSNS().getMessage();
        String token = UUID.randomUUID().toString();
        this.initDynamoDbClient();
        Item existUser = this.dynamoDb.getTable(DYNAMODB_TABLE_NAME).getItem("Id", username);

        //Number expire = System.currentTimeMillis()/1000L+1200;
        Number expire = 1200;
        context.getLogger().log("Token will expire at the time "+expire);

        if (existUser == null) {
            this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                    .putItem(
                            new PutItemSpec().withItem(new Item()
                                    .withString("Id", username)
                                    .withString("token", token)
                                    .withNumber("TTL",expire)));

            textBody = "https://csye6225-spring2019.com/reset?email=" + username + "&token=" + token;
            try {
                AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1).build();

                HTMLBODY = "<h1>Csye 6225 Password reset link</h1>"
                        + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
                        + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
                        + "AWS SDK for Java</a>"
                        + "<p>The password reset link is as below, it will expire after 20 minutes";

                String body = HTMLBODY + "<p> " + textBody + "</p>";
                SendEmailRequest emailRequest = new SendEmailRequest()
                        .withDestination(
                                new Destination().withToAddresses(username))
                        .withMessage(new Message()
                                .withBody(new Body()
                                        .withHtml(new Content()
                                                .withCharset("UTF-8").withData(body))
                                        .withText(new Content()
                                                .withCharset("UTF-8").withData(textBody)))
                                .withSubject(new Content()
                                        .withCharset("UTF-8").withData(SUBJECT)))
                        .withSource(FROM);
                client.sendEmail(emailRequest);


                System.out.println("Email sent successfully!");
            } catch (Exception ex) {
                System.out.println("The email was not sent. Error message: "
                        + ex.getMessage());
            }
        } else {

            try {
                AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                        .withRegion(Regions.US_EAST_1).build();
                context.getLogger().log("Text body: "+textBody);

                HTMLBODY = "<h1>Csye 6225 Password reset link</h1>"
                        + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
                        + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
                        + "AWS SDK for Java</a>"
                        + "<p>Please wait the link expire";

                String body = HTMLBODY + "<p>Password reset link already sent</p>";

                SendEmailRequest emailRequest = new SendEmailRequest()
                        .withDestination(
                                new Destination().withToAddresses(username))
                        .withMessage(new Message()
                                .withBody(new Body()
                                        .withHtml(new Content()
                                                .withCharset("UTF-8").withData(body))
                                )
                                .withSubject(new Content()
                                        .withCharset("UTF-8").withData(SUBJECT)))
                        .withSource(FROM);
                client.sendEmail(emailRequest);


                System.out.println("Email sent successfully!");
            } catch (Exception ex) {
                System.out.println("The email was not sent. Error message: "
                        + ex.getMessage());
            }
        }

        return null;
    }
}







//
//        String timeStamp = new SimpleDateFormat("yyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
//        context.getLogger().log("Invocation started: "+timeStamp);
//        context.getLogger().log("Request is NULL: "+(request.getRecords().size()));
//
//
//
//        username = request.getRecords().get(0).getSNS().getMessage();
//        context.getLogger().log("Email address: "+ username);
//        token = UUID.randomUUID().toString();
//        context.getLogger().log("token: "+token);
//
//        this.initDynamoDBClient(context);
//        context.getLogger().log("Build dynamoDB client successfully");
//
//        String table = System.getenv("DynamoDB_TableName");
//        ses_from_address = System.getenv("From_EmailAddress");
//
//        Table tableEntity = dynamoDB.getTable(table);
//        if(tableEntity!=null){
//            context.getLogger().log("Get the table from dynamoDB: "+table);
//        }else{
//            return null;
//        }
//
//        if((tableEntity.getItem("id",username))==null){
//            context.getLogger().log("A new token will be created and an email will be sent");
//            Number expire = System.currentTimeMillis()/1000L+1200;
//            context.getLogger().log("Token will expire at the time "+expire);
//            this.dynamoDB.getTable(table)
//                    .putItem(
//                            new PutItemSpec().withItem(
//                                    new Item()
//                                    .withString("id",username)
//                                    .withString("token",token)
//                                    .withNumber("TTL",expire)
//                            )
//                    );
//            textBody = "https://csye6225-spring2019/reset?email="+username+"&token="+token;
//            context.getLogger().log("Text body: "+textBody);
//            htmlBody="<h2>Password Reset Request Successfully</h2>"
//                     + "<p>The password reset link is as below, it will expire after 20 minutes"
//                     + "<br/> https://csye6225-spring2019/reset?email="+username+"&token="+token+"</p>";
//            context.getLogger().log("HTML body: "+htmlBody);
//
//            try{
//                AmazonSimpleEmailService sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
//                        .withRegion(REGION).build();
//                SendEmailRequest emailRequest = new SendEmailRequest()
//                        .withDestination(
//                                new Destination().withToAddresses(username)
//                        )
//                        .withMessage(new Message()
//                                .withBody(new Body()
//                                        .withHtml(new Content()
//                                                .withCharset("UTF-8").withData(htmlBody)
//                                        )
//                                        .withText(new Content()
//                                                .withCharset("UTF-8").withData(textBody)
//                                        )
//                                )
//                                .withSubject(new Content()
//                                        .withCharset("UTF-8").withData(EMAIL_SUBJECT)
//                                )
//                        )
//                        .withSource(
//                                ses_from_address
//                        );
//                sesClient.sendEmail(emailRequest);
//                System.out.println("Email successfully sent!");
//            }catch(Exception e){
//                System.out.println("Email sent fail!");
//            }
//        }else{
//            context.getLogger().log("The request exists");
//        }
//        timeStamp = new SimpleDateFormat("yyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
//        context.getLogger().log("Invocation completed: "+timeStamp);
//        return null;
//    }
//
//
//
//    private void initDynamoDBClient(Context context){
//        context.getLogger().log("Initiating dynamoDB client ...");
//
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
//
//
//         context.getLogger().log("Create dynamoDB client");
//         context.getLogger().log("DynamoDB clirnt:" + client.toString());
//         this.dynamoDB = new DynamoDB(client);
//
//    }
//}
