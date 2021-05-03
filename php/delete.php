<?php
$servername = "localhost";
$username = "root";
$password = "12345678";
$dbname = "memberdb";

$conn = new mysqli($servername, $username, $password, $dbname);

if($conn->connect_error){
    die("Connect failed: ".$conn->connect_error);
}

$id = $_POST['id'];

$sql = "DELETE FROM `member` WHERE id = $id ";
if(mysqli_query($conn, $sql)) {
    echo "Records delete successfully.";
} else {
    echo "ERROR: Could not able to execute $sql." . mysqli_error($conn);
}
mysqli_close($conn);
?>