        
        <div id="footer">
            <div class="container">
                <div class="left">
                    <p>Copyright &copy; <?php echo date('Y'); ?> Code 66 - <a href="https://1amdev.com" target="_blank">Website By&nbsp;<img src="/assets/images/webdev/logo.png" alt="1amdev" width="65" draggable="false"></a></p>
                </div>
                <div class="right">
                    <p><a href="/support">support</a>&nbsp;/&nbsp;<a href="/tos">terms of service</a></p>
                </div>
            </div>
        </div>
        
        <script type="text/javascript" src="/assets/js/jquery.min.js"></script>
        <script type="text/javascript" src="/assets/js/bootstrap.min.js"></script>
        <script type="text/javascript">
        $(document).ready(function(){
            // Enable tooltips
            $('[data-toggle="tooltip"]').tooltip(); 
            // Check scrolling and enable/disable menu styling
            var scrollPos = 0;
            $(document).scroll(function() { 
                scroll_pos = $(this).scrollTop();
                if (scroll_pos > 5) {
                    $(".navbar-inverse a").css('padding-top', '15px');
                    $(".navbar-inverse a").css('padding-bottom', '10px');
                    $(".navbar-inverse").css('background-color', '#2b2b2b');
                } else {
                    $(".navbar-inverse a").css('padding-top', '35px');
                    $(".navbar-inverse a").css('padding-bottom', '35px');
                    $(".navbar-inverse").css('background-color', '#363636');
                }
            });
        });
        </script>
        
    </body>
    
</html>