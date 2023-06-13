<?php

$response = array();

if(isset($_POST["fullname"]) && isset($_POST["birthdate"]) && isset($_POST["tcknpassport"]) && isset($_POST["email"])) {

    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    $conn->set_charset("utf8");

    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $fullname = $_POST["fullname"];
    $birthdate = $_POST["birthdate"];
    $tcknpassport = $_POST["tcknpassport"];
    $email = $_POST["email"];

    $query = "UPDATE individual SET individual.fullname = '$fullname', individual.birthdate = '$birthdate', individual.tcknpassport = '$tcknpassport' WHERE individual.email = '$email'";

    if(mysqli_query($conn, $query)) {
        $response["success"] = 1;
        $response["message"] = "Successfull";
        
        echo json_encode($response, JSON_UNESCAPED_UNICODE);
    }else {
        $response["success"] = 0;
        $response["message"] = "Failure";

        echo json_encode($response, JSON_UNESCAPED_UNICODE);
    }

    mysqli_close($conn);

}else {
    $response["success"] = 0;
    $response["message"] = "Missing";

    echo json_encode($response, JSON_UNESCAPED_UNICODE);
}

?>