<?php

$response = array();

if(isset($_POST["email"]) && isset($_POST["pw"])) {
    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $email = $_POST["email"];
    $pw = sha1($_POST["pw"]);

    $query = "UPDATE individual SET individual.pw = '$pw' WHERE individual.email = '$email'";

    $query_controller = mysqli_query($conn, $query);

    if ($query_controller) {
        $response["success"] = 1;
        $response["message"] = "Successfull";

        echo json_encode($response);
    }else {
        $response["success"] = 0;
        $response["message"] = "Failure";

        echo json_encode($response);
    }
    
    mysqli_close($conn);
}else {
    $response["success"] = 0;
    $response["message"] = "Missing";

    echo json_encode($response);
}

?>