<?php
$response = array();

require_once __DIR__ . "/db_config.php";

$conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
$conn->set_charset("utf8");

if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
}

$query = "SELECT * FROM countries";

$query_controller = mysqli_query($conn, $query);

if(mysqli_num_rows($query_controller) > 0) {
    $response["countries"] = array();

    while($row = mysqli_fetch_assoc($query_controller)) {
        $countries = array(); // temp countries array

        $countries["id"] = $row["id"];
        $countries["country"] = $row["country"];

        array_push($response["countries"], $countries);
    }

    $response["success"] = 1;

    echo json_encode($response, JSON_UNESCAPED_UNICODE);
}else {
    $response["success"] = 0;
    $response["message"] = "";

    echo json_encode($response, JSON_UNESCAPED_UNICODE);
}

mysqli_close($conn);
?>