<?php
$servername = "localhost";
$username = "root";
$password = "12345678";
$dbname = "memberdb";

$conn = new mysqli($servername, $username, $password, $dbname);

if($conn->connect_error){
    die("Connect failed: ".$conn->connect_error);
}

$sql = "SELECT * FROM `member`";
// print($sql);

$result = $conn->query($sql);
if($result != false){
    while($row = mysqli_fetch_assoc($result)){
        $output[] = $row; 
    }
    mysqli_free_result($res);
}else{
    echo "Error cannot query: ".$conn->error;
}
$conn->close();
print(json_encode($output));
?>