<?php
$response = array();

if(isset($_POST["b_uid"]) && isset($_POST["b_phonenumber"]) && isset($_POST["country"]) && isset($_POST["city"]) && isset($_POST["county"]) && isset($_POST["address"]) && isset($_POST["postcode"])) {
    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    $conn->set_charset("utf8");

    if(!$conn) {
    die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $b_uid = $_POST["b_uid"];
    $b_phonenumber = $_POST["b_phonenumber"];
    $country = $_POST["country"];
    $city = $_POST["city"];
    $county = $_POST["county"];
    $address = $_POST["address"];
    $postcode = $_POST["postcode"];
    
    $query = "UPDATE billing SET billing.b_phonenumber = '$b_phonenumber', billing.country = '$country', billing.city = '$city', billing.county = '$county', billing.address = '$address', billing.postcode = $postcode WHERE billing.b_uid = $b_uid";
    $query_controller = mysqli_query($conn, $query);

    if($query_controller) {
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