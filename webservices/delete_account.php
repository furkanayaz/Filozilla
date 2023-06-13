<?php
$response = array();

if(isset($_POST["i_id"])) {
    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $i_id = $_POST["i_id"];

    $query = "DELETE FROM individual WHERE individual.i_id = $i_id";
    $query2 = "DELETE FROM drivinglicense WHERE drivinglicense.d_uid = $i_id";
    $query3 = "DELETE FROM billing WHERE billing.b_uid = $i_id";
    
    mysqli_query($conn, $query);
    mysqli_query($conn, $query2);
    mysqli_query($conn, $query3);
    
    $query_controller = mysqli_affected_rows($conn) == 1;

    if($query_controller) {
        $response["success"] = 1;
        $response["message"] = "Successfull";

        echo json_encode($response);
    }else {
        $response["success"] = 0;
        $response["message"] = "Failure";

        echo json_encode($response);
    }

}else {
    $response["success"] = 0;
    $response["message"] = "Missing";

    echo json_encode($response);
}

?>