<?php
$response = array();

if(isset($_POST["i_id"]) && isset($_POST["pw"])) {
    require_once __DIR__ . '/db_config.php';

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    $conn->set_charset("utf8");

    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $i_id = $_POST["i_id"];
    $new_pw = $_POST["pw"];
    $hashed_pw = sha1($new_pw);

    $query = "UPDATE individual SET individual.pw = '$hashed_pw' WHERE individual.i_id = $i_id";
    mysqli_query($conn, $query);
    $query_controller = mysqli_affected_rows($conn) == 1;

    if($query_controller) {
        $response['success'] = 1;
        $response['message'] = "Successfull";

        echo json_encode($response, JSON_UNESCAPED_UNICODE);
    }else {
        $response['success'] = 0;
        $response['message'] = "Failure";

        echo json_encode($response, JSON_UNESCAPED_UNICODE);
    }
    
    mysqli_close($conn);
}else {
    $response['success'] = 0;
    $response['message'] = "Missing";

    echo json_encode($response, JSON_UNESCAPED_UNICODE);
}
?>