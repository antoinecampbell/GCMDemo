package com.example.gcmdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.gcmdemo.R;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService
{

    @Override
    protected void onError(Context context, String regId)
    {
	Log.d(Globals.TAG, "onError CALLED:");
    }

    @Override
    protected void onMessage(Context context, Intent intent)
    {
	Log.d(Globals.TAG, "onMessage CALLED:");
	String message = ((intent.getExtras() == null) ? "Empty Bundle" : intent.getExtras().getString(
		"message"));
	Log.d(Globals.TAG, "MESSAGE: " + message);

	Intent notificationIntent = new Intent(this, GCMActivity.class);
	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

	int color = 0xff0000;
	Notification notification = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_stat_cloud).setAutoCancel(true)
		.setLights(color, Notification.DEFAULT_LIGHTS, 0).setTicker(message)
		.setContentTitle(getString(R.string.app_name)).setContentText(message)
		.setContentIntent(contentIntent).setWhen(System.currentTimeMillis()).build();
	NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	nm.notify(0, notification);
    }

    @Override
    protected void onRegistered(Context context, String regId)
    {
	Log.d(Globals.TAG, "REGISTER USERID: " + regId);
	HttpClient client = new DefaultHttpClient();
	HttpPost httpPost = new HttpPost(Globals.API_URL);
	try
	{
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("action", "addUser"));
	    params.add(new BasicNameValuePair("id", regId));
	    httpPost.setEntity(new UrlEncodedFormEntity(params));
	    HttpResponse response = client.execute(httpPost);

	    StringBuilder sb = new StringBuilder();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity()
		    .getContent()));
	    String line = reader.readLine();
	    while (line != null)
	    {
		sb.append(line);
		line = reader.readLine();
	    }
	    reader.close();
	    Log.d(Globals.TAG, "REGISTRATION OUTPUT: " + sb.toString());
	    
	    /* Inform activity of successful registration */
	    Intent intent = new Intent(context, GCMActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.putExtra("status", true);
	    startActivity(intent);
	}
	catch (UnsupportedEncodingException e)
	{
	    e.printStackTrace();
	}
	catch (ClientProtocolException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    @Override
    protected void onUnregistered(Context context, String regId)
    {
	Log.d(Globals.TAG, "UNREGISTER USERID: " + regId);
	HttpClient client = new DefaultHttpClient();
	HttpPost httpPost = new HttpPost(Globals.API_URL);
	try
	{
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("action", "removeUser"));
	    params.add(new BasicNameValuePair("id", regId));
	    httpPost.setEntity(new UrlEncodedFormEntity(params));
	    HttpResponse response = client.execute(httpPost);

	    StringBuilder sb = new StringBuilder();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity()
		    .getContent()));
	    String line = reader.readLine();
	    while (line != null)
	    {
		sb.append(line);
		line = reader.readLine();
	    }
	    reader.close();
	    Log.d(Globals.TAG, "REGISTRATION OUTPUT: " + sb.toString());
	    
	    /* Inform activity of successful unregistration */
	    Intent intent = new Intent(context, GCMActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.putExtra("status", false);
	    startActivity(intent);
	}
	catch (UnsupportedEncodingException e)
	{
	    e.printStackTrace();
	}
	catch (ClientProtocolException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

}
