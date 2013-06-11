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

import com.example.gcmdemo.R;
import com.google.android.gcm.GCMRegistrar;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class GCMActivity extends FragmentActivity implements OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_gcm);

	final String regId = GCMRegistrar.getRegistrationId(this);
	((TextView) findViewById(R.id.gcm_userid_textview)).setText(regId);
	EditText editText = ((EditText) findViewById(R.id.message_edittext));
	editText.setOnEditorActionListener(new OnEditorActionListener()
	{
	    @Override
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	    {
		switch(actionId)
		{
		    case EditorInfo.IME_ACTION_SEND:
		    {
			String message = v.getText().toString();
			if (message != "")
			{
			    postGCM(message);
			    return true;
			}
		    }
		}
		return false;
	    }
	});
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
	super.onNewIntent(intent);

	/* Reload the Registration ID */
	final String regId = GCMRegistrar.getRegistrationId(this);
	((TextView) findViewById(R.id.gcm_userid_textview)).setText(regId);
    }

    @Override
    public void onClick(View v)
    {

	switch (v.getId())
	{
	    case R.id.register_button:
		/* Register device for GCM */
		try
		{
		    GCMRegistrar.checkDevice(this);
		    GCMRegistrar.checkManifest(this);
		    final String regId = GCMRegistrar.getRegistrationId(this);
		    if (regId.equals(""))
		    {
			GCMRegistrar.register(this, Globals.GCM_SENDER_ID);
		    }
		    else
		    {
			Log.v(Globals.TAG,
				"Already registered as ID: " + GCMRegistrar.getRegistrationId(this));
		    }
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
		break;
	    case R.id.unregister_button:
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		startService(unregIntent);
		break;
	    case R.id.send_message_button:
		String message = ((EditText) findViewById(R.id.message_edittext)).getText().toString();
		if (message != "")
		{
		    postGCM(message);
		}
		break;
	}
    }

    private void postGCM(String message)
    {
	final String msg = message;
	Thread thread = new Thread(new Runnable()
	{

	    public void run()
	    {
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(Globals.API_URL);
		try
		{
		    List<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("action", "sendMessage"));
		    params.add(new BasicNameValuePair("message", msg));
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
		    Log.d(Globals.TAG, "SEND MESSAGE OUTPUT: " + sb.toString());

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
		    Log.d(Globals.TAG, "FAILED URL:" + httpPost.getURI().toString());
		    e.printStackTrace();
		}

	    }
	});
	thread.start();

    }

}
