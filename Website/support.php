<?php
$pageTitle = 'Support';
include('assets/includes/header.php');
?>
        
        <div id="support" class="section">
            <div class="container">
                <div class="row">
                    <div class="col-md-6">
                        <p class="title">For Puchasing</p>
                        <p>If you would like to purchase out tool, please send us an email at
                        <a href="mailto:code66business@gmail.com">code66business@gmail.com</a>
                        and we will get right back to you for your purhcase. For other support,
                        please use our contact form.</p>
                    </div>
                    <div class="col-md-6">
                        <p class="title">For Other Support</p>
                        <div id="contactForm">
                            <?php
                                $action = $_REQUEST['action'];
                                if ($action == "") {
                            ?>
                            <form action="" method="POST" enctype="multipart/form-data">
                                <input type="hidden" name="action" value="submit"> 
                                <fieldset class="form-group">
                                    <label for="name">Name</label>
                                    <input type="text" class="form-control" name="name" id="Name" placeholder="Enter your full name" required> 
                                </fieldset>
                                <fieldset class="form-group">
                                    <label for="email">Email</label>
                                    <input type="text" class="form-control" name="email" id="Email" placeholder="Enter email address" required>
                                </fieldset>
                                <fieldset class="form-group">
                                    <label for="theSubject">Subject</label>
                                    <input type="text" class="form-control" name="theSubject" id="theSubject" placeholder="Enter the subject" required>
                                </fieldset>
                                <fieldset class="form-group">
                                    <label for="description">Message</label>
                                    <textarea class="form-control" name="description" id="description" rows="7" placeholder="Message to send" required></textarea>
                                </fieldset>
                                <button type="submit" class="btn btn-default btn-block">Submit</button>
                            </form>
                            <?php
                                }
                                else {
                                    $name = $_REQUEST['name'];
                                    $email = $_REQUEST['email'];
                                    $theSubject = $_REQUEST['theSubject'];
                                    $description = $_REQUEST['description'];
                                    if (($name=="") || ($email=="") || ($theSubject=="") || ($description=="")) {
                                        echo "All fields required. Please fill in the form.";
                                    }
                                    else {
                                        $to = "code66business@gmail.com";
                                        $subject = "Code 66 - Contact Form";
                                        $headers = "From: " . $email . "\r\n";
                                        $headers .= "Reply-To: ". $email . "\r\n";
                                        $headers .= "MIME-Version: 1.0\r\n";
                                        $headers .= "Content-Type: text/html; charset=ISO-8859-1\r\n";
                                        $message = "<html><body>";
                                        $message .="<ul>
                                                    <li>Name: $name</li>
                                                    <li>Email: $email</li>
                                                    <li>Subject: $theSubject</li>
                                                    <li>Message: $description</li>
                                                    </ul>";
                                        $message .= "</body></html>"; 
                                        mail($to, $subject, $message, $headers);
                                        echo '<div class="alert alert-success text-center" role="alert">Contact form sent! We will get back to you shortly.</div>';
                                    }
                                }
                            ?>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
<?php
include('assets/includes/footer.php');
?>