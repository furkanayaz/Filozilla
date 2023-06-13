<?php
$response = array();

require_once __DIR__ . "/db_config.php";

$conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
$conn->set_charset("utf8");
$query = "SELECT * FROM cities";

$query_controller = mysqli_query($conn, $query);

if(mysqli_num_rows($query_controller) > 0) {
    $response["cities"] = array();

    while($row = mysqli_fetch_assoc($query_controller)) {
        $cities = array();

        $cities["id"] = $row["id"];
        $cities["plate"] = $row["plate"];
        $cities["countrycode"] = $row["countrycode"];
        $cities["city"] = $row["city"];

        array_push($response["cities"], $cities);
    }

    $response["success"] = 1;

    echo json_encode($response, JSON_UNESCAPED_UNICODE);
}else {
    $response["success"] = 0;
    $response["message"] = "";

    echo json_encode($response, JSON_UNESCAPED_UNICODE);
}
?>