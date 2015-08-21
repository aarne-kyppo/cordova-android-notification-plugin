package apps.aarne.kyppo.pushNotificationService;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.Uri;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.app.PendingIntent;
import android.content.Context;

/**
 * This class echoes a string called from JavaScript.
 */
public class pushNotificationService extends CordovaPlugin {
    //Not necessary to send icon metadata many times.
    private String iconkey;
    private String icongroup;
    private CallbackContext cb;
    private Activity activity;
    private Context context;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        /*
        * This function acts only as a dispatcher.
        */
        this.cb = callbackContext;
        if (action.equals("initialize")){
            String iconkey = args.getString(0);
            String icongroup = args.getString(1);

            if(iconkey != null && icongroup != null){
              this.initialize(iconkey,icongroup);
            }
            else{
              cb.error("Icon key or icon group is not defined");
            }
            return true;
        }
        else if (action.equals("sendNotification")) {
            String title = args.getString(0);
            String message = args.getString(1);
            JSONObject data = args.getJSONObject(2);
            if(title != null & message != null && data != null){
              this.sendNotification(title,message,data);
            }
            else{
              cb.error("Notification title, message or dataobject is not defined");
            }
            return true;
        }
        return false;
    }
    private void initialize(String iconkey, String icongroup){
        this.iconkey = iconkey;
        this.icongroup = icongroup;
        this.activity = this.cordova.getActivity();
        this.context = this.activity.getApplicationContext();
        //If user have opened push notification, send data of notification to user
        Intent intent = ((CordovaActivity) this.activity).getIntent();
        if(intent.hasExtra("data")){ //Check if intent includes URL for a job. It will have URL if user have opened notification.
          handleNotification(intent.getStringExtra("data"));
        }
        else{
          sendPluginResult();
        }
    }
    private void handleNotification(String data)
    {
        JSONObject jsondata;
        try{
          jsondata = new JSONObject(data);
        }catch(JSONException je){
          jsondata = new JSONObject();
          cb.error("Given data is not valid");
        }
        sendPluginResult(jsondata);
    }
    private void sendNotification(String title, String message, JSONObject data){

      NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
      mBuilder.setSmallIcon(context.getResources().getIdentifier(this.iconkey,this.icongroup,context.getPackageName()));
      mBuilder.setContentTitle(title);
      mBuilder.setContentText(message);
      Intent resultIntent = new Intent(context,this.activity.getClass());
      resultIntent.putExtra("data",data.toString());

      TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
      stackBuilder.addParentStack(this.activity.getClass());
      stackBuilder.addNextIntent(resultIntent);
      PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
      mBuilder.setContentIntent(resultPendingIntent);
      NotificationManager mNotificationManager = (NotificationManager) this.activity.getSystemService(context.NOTIFICATION_SERVICE);
      mNotificationManager.notify(1000, mBuilder.build());

      sendPluginResult();
    }
    private void sendPluginResult(JSONObject jsondata){
      PluginResult result = new PluginResult(PluginResult.Status.OK,jsondata);
      result.setKeepCallback(true);
      cb.sendPluginResult(result);
    }
    private void sendPluginResult(){
      PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
      result.setKeepCallback(true);
      cb.sendPluginResult(result);
    }
  }
