<?php

$response = array();

if(isset($_POST["email"])) {
    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    $conn->set_charset("utf8");

    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $email = $_POST["email"];

    $query = "SELECT individual.fullname, individual.email FROM individual WHERE individual.email = '$email'";
    $query2 = "SELECT corporate.companyname FROM corporate WHERE corporate.companyemail = '$email'";

    //$query = "SELECT individual.fullname, corporate.companyname FROM individual INNER JOIN corporate ON individual.email = '$email' OR corporate.companyemail = '$email'";

    $query_controller = mysqli_query($conn, $query);
    $query_controller2 = mysqli_query($conn, $query2);
    
    $row = mysqli_fetch_row($query_controller);
    $row2 = mysqli_fetch_row($query_controller2);
    

    if (mysqli_num_rows($query_controller) > 0) {
        $response["success"] = 1;
        $response["message"] = "Successfull";
        $response["fullname"] = $row[0];
        $response["email"] = $row[1];

        echo json_encode($response, JSON_UNESCAPED_UNICODE);
    }elseif (mysqli_num_rows($query_controller2) > 0) {
        $response["success"] = 1;
        $response["message"] = "Successfull";
        $response["fullname"] = $row2[0];

        echo json_encode($response, JSON_UNESCAPED_UNICODE);
    }else {
        $response["success"] = 0;
        $response["message"] = "Failure";
        $response["fullname"] = "Empty";
        $response["email"] = "Empty";

        echo json_encode($response, JSON_UNESCAPED_UNICODE);
    }
    
    mysqli_close($conn);

}else {
    $response["success"] = 0;
    $response["message"] = "Missing";
    $response["fullname"] = "Empty";
    $response["email"] = "Empty";

    echo json_encode($response, JSON_UNESCAPED_UNICODE);
}

?>