<?php
$response = array();

if(isset($_POST["fullname"]) && isset($_POST["birthdate"]) && isset($_POST["tcknpassport"]) && isset($_POST["email"]) && isset($_POST["i_phonenumber"]) && isset($_POST["pw"])) {
    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
    $conn->set_charset("utf8");
    
    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $fullname = $_POST["fullname"];
    $birthdate = $_POST["birthdate"];
    $tcknpassport = $_POST["tcknpassport"];
    $email = $_POST["email"];
    $hashed_email = sha1($email);
    $i_phonenumber = $_POST["i_phonenumber"];
    $pw = sha1($_POST["pw"]);

    $query = "INSERT INTO individual(status, fullname, birthdate, tcknpassport, email, hashed_email, i_phonenumber, pw) VALUES (0, '$fullname', '$birthdate', '$tcknpassport', '$email', '$hashed_email', '$i_phonenumber', '$pw')";
    $querycontroller = mysqli_query($conn, $query);
    $query2 = "SELECT individual.i_id FROM individual WHERE individual.email = '$email' AND individual.i_phonenumber = '$i_phonenumber' LIMIT 1";
    $querycontroller2 = mysqli_query($conn, $query2);

    if($querycontroller) {
        if(mysqli_num_rows($querycontroller2) > 0) {
            $row = mysqli_fetch_row($querycontroller2);
            $query3 = "INSERT INTO drivinglicense(d_uid, drivingchosenclass, drivingpickup, drivingissuedate) VALUES ($row[0], '', '', '')";
            $query4 = "INSERT INTO billing(b_uid, b_phonenumber, country, city, county, address) VALUES ($row[0], '', '', '', '', '')";
            $querycontroller3 = mysqli_query($conn, $query3);
            $querycontroller4 = mysqli_query($conn, $query4);

            if($querycontroller3 && $querycontroller4) {
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