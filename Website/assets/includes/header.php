<!DOCTYPE html>

<!--
Code 66 Website
Web Development by 1AM Development (Michael C)
HTTPS://1AMDEV.COM
-->

<html lang="en">
    
    <head>
        
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
        <meta name="description" content="Code 66 - Screensharing Taken To Its Finest">
        <meta name="keywords" content="code66, code, 66, screensharing, no, cheating, minecraft, gaming">
        <meta name="author" content="Michael - 1:AM Development">
        
        <title>Code 66 &rarr; <?php echo $pageTitle; ?></title>
        
        <link rel="icon" href="/assets/images/favicon.ico">
        <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" type="text/css" href="/assets/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="/assets/css/hover.min.css">
        <link rel="stylesheet" type="text/css" href="/assets/css/style.css">
        <link rel="stylesheet" type="text/css" href="/assets/css/responsive.css">
        
    </head>
    
    <body>
        
        <?php
        function isHome() {
            global $pageTitle;
            if ($pageTitle == 'Screensharing Taken To Its Finest') {
                return true;
            }
            else {
                return false;
            }
        }
        ?>
        
        <div id="nav">
            <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
                <div class="container">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                            <span class="sr-only">Toggle Navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="/"><i class="fa fa-superpowers"></i>&nbsp;Code 66</a>
                    </div>
                    <div class="navbar-collapse collapse">
                        <ul class="nav navbar-nav navbar-left">
                            <li><a href="/">Home</a></li>
                            <li><a href="/about">About</a></li>
                            <li><a href="/partners">Partners</a></li>
                            <li><a href="/support">Support</a></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="https://twitter.com/Code66Business" target="_blank" data-toggle="tooltip" data-placement="bottom" title="Twitter"><i class="fa fa-twitter"></i></a></li>
                            <li><a href="https://www.youtube.com/channel/UC8tbxGN212g8yTrlDC71TJQ" target="_blank" data-toggle="tooltip" data-placement="bottom" title="YouTube"><i class="fa fa-youtube-play"></i></a></li>
                            <!--<li><a href="#" target="_blank" data-toggle="tooltip" data-placement="bottom" title="Instagram"><i class="fa fa-instagram"></i></a></li>-->
                            <!--<li><a href="#" target="_blank" data-toggle="tooltip" data-placement="bottom" title="Facebook"><i class="fa fa-facebook"></i></a></li>-->
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="jumbo" class="<?php if (isHome()) { echo 'homeJumbo'; } else { echo 'pageJumbo'; } ?>">
            <div class="container">
                <?php
                if (isHome()) {
                    ?>
                    <div class="row">
                        <div class="col-md-5">
                            <iframe width="100%" height="265" src="https://www.youtube.com/embed/HEgiItX0IU8" frameborder="0" allowfullscreen></iframe>
                        </div>
                        <div class="col-md-7">
                            <p><?php echo $pageTitle; ?></p>
                            <p>Code 66 was a long-term project created by a group of young entrepreneurs with the sole purpose of reducing the amount of cheating in Minecraft. Cheating has been going around in the community for a quite a while now and we at Code 66 believe it's finally time to put a stop to it. Nonetheless, our management & development team has worked nonstop in order to provide you, the client, with custom, daily updated tool that makes screensharing a breeze and leaves client developers speechless.
                            <br><br>We have one goal, one purpose and we will not stop until that is completed.</p>
                            <p><a href="#why" class="hvr-icon-forward">So, why Code 66?</a></p>
                        </div>
                    </div>
                    <?php
                }
                else {
                    echo $pageTitle;
                }
                ?>
            </div>
        </div>
        