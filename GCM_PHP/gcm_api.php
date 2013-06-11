<?php 
/* Handle incoming GCM requests */

include('gcm_helper.php');

if(!checkValue($_REQUEST, "action"))
{
	die("ERROR");
}

switch($_REQUEST["action"])
{
	case "addUser":
		if(!($id = checkValue($_REQUEST, "id")))
		{
			die("ERROR");
		}
		include('config.php');
		include('connect.php');
		echo addUser($id, $db);
		$db->close();
		break;
	case "removeUser":
		if(!($id = checkValue($_REQUEST, "id")))
		{
			die("ERROR");
		}
		include('config.php');
		include('connect.php');
		echo removeUser($id, $db);
		$db->close();
		break;
	case "sendMessage":
		if(!($message = checkValue($_REQUEST, "message")))
		{
			die("ERROR");
		}
		include('config.php');
		include('connect.php');
		echo sendGCM($message, $db);
		break;
	default:
		echo "Error";
		break;
}










?>