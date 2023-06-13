<?php
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;

require 'Exception.php';
require 'PHPMailer.php';
require 'SMTP.php';

$response = array();

if(isset($_POST["email"]) && isset($_POST["fullname"]) && isset($_POST["subject"]) && isset($_POST["body"])) {
    $email = $_POST["email"];
    $fullname = $_POST["fullname"];
    $subject = $_POST["subject"];
    $body = $_POST["body"];

    if(sendMail($email, $fullname, $subject, $body) == 1) {
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

function sendMail($email, $fullname, $subject, $body) {
    $mail = new PHPMailer(true);
    
    try {
        $mail->isSMTP();
        $mail->CharSet = 'UTF-8';
        $mail->Host       = 'mail.filozilla.com';
        $mail->SMTPAuth   = true;
        $mail->Username   = 'admin@filozilla.com';
        $mail->Password   = 'YFd%DCNbYBRr';
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_SMTPS;
        $mail->Port       = 465;
    
        $mail->setFrom('admin@filozilla.com', 'Filozilla Postacı');
        $mail->addAddress($email, $fullname);
        
        $mail->isHTML(true);
        $mail->Subject = $subject;
        $mail->Body    = $body;
    
        $mail->send();
        
        return 1;
        
    } catch (Exception $e) {
        return 0;
    }
}

?>