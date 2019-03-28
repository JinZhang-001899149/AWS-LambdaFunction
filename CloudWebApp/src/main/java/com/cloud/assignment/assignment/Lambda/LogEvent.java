package com.cloud.assignment.assignment.Lambda;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;


public class LogEvent implements RequestHandler<SNSEvent, Object>{

    @Override
    public Object handleRequest(SNSEvent request, Context context) {
        String timeStamp = new SimpleDateFormat("yyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        context.getLogger().log("Invocation started: "+timeStamp);
        context.getLogger().log("Request is NULL: "+(request.getRecords().size()));
        String record = request.getRecords().get(0).getSNS().getMessage();
        context.getLogger().log("Record Message: "+ record);
        timeStamp = new SimpleDateFormat("yyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        context.getLogger().log("Invocation completed: "+timeStamp);
        return null;
    }
}
