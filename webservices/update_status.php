<?php
if(isset($_GET["token"])) {
    require_once __DIR__ . "/db_config.php";

    $conn = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

    if(!$conn) {
        die("An error occurred while accessing the database (" . mysqli_connect_error() . ").");
    }

    $token = $_GET["token"];
    
    $query = "UPDATE individual SET individual.status = 1 WHERE individual.hashed_email = '$token'";
    mysqli_query($conn, $query);
    $query_controller = mysqli_affected_rows($conn) == 1;

    if($query_controller) {
        echo "Filozilla üyeliğiniz başarıyla onaylandı.";
    }else {
        echo "Filozilla üyeliğiniz onaylanırken hata meydana geldi.";
    }
    
    mysqli_close($conn);
}else {
        echo "Filozilla üyeliğiniz onaylanırken hata meydana geldi.";
}

?>