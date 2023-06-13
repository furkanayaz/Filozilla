<?php
$response = array();

if(isset($_POST["email"])) {
    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    
    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $email = $_POST["email"];

    $query = "SELECT individual.status FROM individual WHERE individual.email='$email'";
    $query_controller = mysqli_query($conn, $query);

    if(mysqli_num_rows($query_controller) > 0) {
        $row = mysqli_fetch_row($query_controller);

        if($row[0] == 1) {
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
        $response["message"] = "Failure";

        echo json_encode($response);
    }

}else {
    $response["success"] = 0;
    $response["message"] = "Missing";

    echo json_encode($response);
}
?>