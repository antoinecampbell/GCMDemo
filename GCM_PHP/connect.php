
<?php
/* Connect to database using credential in config.php */
$db = new mysqli();
$db->connect($dbserver, $dbuser, $dbpassword, $dbname);

if (mysqli_connect_error()) {
    echo "Failure $db->error<br />";
}
?>