<?php

$response = array();

if(isset($_POST["d_uid"]) && isset($_POST["drivingnumber"]) && isset($_POST["drivingchosenclass"]) && isset($_POST["drivingpickup"]) && isset($_POST["drivingissuedate"])) {

    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    $conn->set_charset("utf8");

    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $d_uid = $_POST["d_uid"];
    $drivingnumber = $_POST["drivingnumber"];
    $drivingchosenclass = $_POST["drivingchosenclass"];
    $drivingpickup = $_POST["drivingpickup"];
    $drivingissuedate = $_POST["drivingissuedate"];

    $query = "UPDATE drivinglicense SET drivinglicense.drivingnumber = $drivingnumber, drivinglicense.drivingchosenclass = '$drivingchosenclass', drivinglicense.drivingpickup = '$drivingpickup', drivinglicense.drivingissuedate = '$drivingissuedate' WHERE drivinglicense.d_uid = $d_uid";

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