<?php
$servername = "localhost";
$username = "root";
$password = "12345678";
$dbname = "memberdb";

$conn = new mysqli($servername, $username, $password, $dbname);
// check connection
if($conn->connect_error){
    die("Connection Failed: " . $conn->connect_error);
}
$user = $_POST['username'];
$passwd = $_POST['password'];

$sql = "INSERT INTO `member`(`username`, `pass`) VALUES ('$user', '$passwd')";
$result = $conn->query($sql);
print($result);
// close connection
mysqli_close($conn);
?>