<?php
$servername = "localhost";
$username = "root";
$password = "12345678";
$dbname = "memberdb";


$conn = new mysqli($servername, $username, $password, $dbname);
if($conn->connect_error){
    die("Connection Failed: " . $conn->connect_error);
}

$id = $_POST["id"];
$user = $_POST['username'];
$passwd = $_POST['password'];

$sql = "UPDATE `member` SET `username`='$user', `pass` = '$passwd' WHERE id='$id'";
if(mysqli_query($conn, $sql)) {
    echo "Records updated successfully.";
} else {
    echo "ERROR: Could not able to execute $sql." . mysqli_error($conn);
}
mysqli_close($conn);
?>