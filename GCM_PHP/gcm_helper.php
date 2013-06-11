<?php

/**
 * Creates or appends to specified log file
 * @param string $filename
 * The name of the log file you wish to append to e.g. 'error_log'
 * @param string $message
 * The message appended to the log
 */
function myLog($filename, $message)
{
	$string = "[".date("F j, Y, g:i a")."] - ";
	$string .= $message;
	$string .= "\r\n";
	error_log($string, 3, $filename);
}

function checkValue($globalVar, $param)
{
	if(isset($globalVar[$param]) && !empty($globalVar[$param]))
	{
		return $globalVar[$param];
	}
	return false;
}

function getUsers($db)
{	
	$ids = array();
	$query = "SELECT id FROM users";
	$result = $db->query($query);
	if($result)
	{
		while($row = $result->fetch_assoc())
		{
			$ids[] = $row[id];
		}
	}
	else
	{
		//myLog("gcm_helper_log", "ERROR: $db->error");
		echo "ERROR $db->error";
	}
	return $ids;
}

function addUser($id, $db)
{
	$query = "INSERT IGNORE INTO users (id) VALUES('$id')";
	$result = $db->query($query);
	if($result)
	{
		return $db->affected_rows;
	}
	else
	{
		//myLog("gcm_helper_log", "ERROR: $db->error");
		echo "ERROR $db->error";
	}
	return 0;
}

function removeUser($id, $db)
{
	$query = "DELETE FROM users WHERE id='$id'";
	$result = $db->query($query);
	if($result)
	{
		return $db->affected_rows;
	}
	else
	{
		//myLog("gcm_helper_log", "ERROR: $db->error");
		echo "ERROR $db->error";
	}
	return 0;
}

function sendGCM($message, $db)
{
	// Replace with real BROWSER API key from Google APIs
	$apiKey = "Your API Key";
	
	// Replace with real client registration IDs
	$registrationIDs = getUsers($db);
	
	// Message to be sent
	//$message = "x";
	
	// Set POST variables
	$url = 'https://android.googleapis.com/gcm/send';
	
	$fields = array(
			'registration_ids'  => $registrationIDs,
			'data'              => array( "message" => $message ),
	);
	
	$headers = array(
			'Authorization: key=' . $apiKey,
			'Content-Type: application/json'
	);
	
	// Open connection
	$ch = curl_init();
	
	// Set the url, number of POST vars, POST data
	curl_setopt( $ch, CURLOPT_URL, $url );
	
	curl_setopt( $ch, CURLOPT_POST, true );
	curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers);
	curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
	
	curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $fields ) );
	
	// Execute post
	$result = curl_exec($ch);
	
	// Close connection
	curl_close($ch);
	
	return $result;
	
}








?>
