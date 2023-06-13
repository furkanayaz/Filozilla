<?php
$response = array();

if(isset($_POST["email"]) && isset($_POST["pw"])) {
    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    $conn->set_charset("utf8");
    
    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }
    
    $email = $_POST["email"];
    $pw = sha1($_POST["pw"]);

    $query = "SELECT * FROM individual, drivinglicense, billing WHERE individual.email = '$email' AND individual.i_id = drivinglicense.d_uid AND individual.i_id = billing.b_uid LIMIT 1";
    $querycontroller = mysqli_query($conn, $query);

    if(mysqli_num_rows($querycontroller) > 0) {
        $row = mysqli_fetch_row($querycontroller);
        
        if($pw == $row[8]) {
            $response["i_id"] = $row[0];
            $response["status"] = $row[1];
            $response["fullname"] = $row[2];
            $response["birthdate"] = $row[3];
            $response["tcknpassport"] = $row[4];
            $response["email"] = $row[5];
            $response["hashed_email"] = $row[6];
            $response["i_phonenumber"] = $row[7];
            $response["pw"] = $_POST["pw"];
            $response["hashed_pw"] = $row[8];
            $response["i_creationdate"] = $row[9];
            $response["d_uid"] = $row[10];
            $response["drivingnumber"] = $row[11];
            $response["drivingchosenclass"] = $row[12];
            $response["drivingpickup"] = $row[13];
            $response["drivingissuedate"] = $row[14];
            $response["d_creationdate"] = $row[15];
            $response["b_uid"] = $row[16];
            $response["b_phonenumber"] = $row[17];
            $response["country"] = $row[18];
            $response["city"] = $row[19];
            $response["county"] = $row[20];
            $response["address"] = $row[21];
            $response["postcode"] = $row[22];
            $response["b_creationdate"] = $row[23];
            $response["success"] = 1;
            $response["message"] = "Successfull";
    
            echo json_encode($response, JSON_UNESCAPED_UNICODE);
        }else {
            $response["success"] = 0;
            $response["message"] = "Failure";

            echo json_encode($response, JSON_UNESCAPED_UNICODE);
        }
        
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