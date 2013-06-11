GCMDemo
=======

Google Cloud Messaging (GCM) Demo

This simple demo was created to show how to use Google Cloud Messaging via HTTP.

**Steps to reuse this demo**

* Visit http://developer.android.com/google/gcm/gs.html and follow the steps to activate GCM, you can stop following that guide once you have a Project ID and an API Key.
* Create a MySQL database named 'gcm' with a table named 'users'
  * The 'users' table will have one field named 'id' of type TEXT
  * Be sure to set the username and password so the script can access the database in /GCM_PHP/config.php
  * [Optional] If you need to change the name of the database this can be done in /GCM_PHP/config.php
* Open /GCM_PHP/gcm_helper.php find the 'sendGCM' function near the bottom and enter your API Key.

```PHP
$apiKey = "Your API Key";
```

* Copy the /GCM_PHP folder to your server's root web directory.

* Open /Android/src/com/example/gcmdemo/Globals.java and update the following variables with your server domain and your Project ID.

```java
public static final String GCM_SENDER_ID = "Your Project ID";
private static final String SERVER_URL = "http://your_server_domain.com/";
```

* Import the Android project into eclipse and install on a device.
* Click the register button, if all went well you will see a long user id appear in the TextView above
* Enter some text in the EditText and hit the send button, you should see a notification appear with the text you entered.
* To confirm the message came from your server install on a seconde device, when a message is sent, it will be received by all devices.

NOTE: If you find any issues with these instructions feel free to comment or open an issue.
