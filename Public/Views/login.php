<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <title>LOGIN PAGE</title>
</head>
<body>
    <div class="container">
        <div class="logo">
            <img src="Public/img/logo.svg">
        </div>
        <div class="header">
            <h1>Recipe Planner</h1>
        </div>
        <div class="login-container">
            <form class="loginForm" method="POST" action="login">
                <div class="messages">
                    <?php if(isset($messages)){
                        foreach($messages as $message){
                            echo $message;
                        }
                    }?>
                </div>
                <input type="email" name="email" id="email" placeholder="Email" required>
                <input type="password" name="password" id="password" placeholder="Password" required>
                <button type="submit" class="rightArrow"><img src="Public/img/rightArrow.svg"></button>
            </form>
                <p class="signup-text">You don't have an account yet?</p>
                <a href="signUp" class="signup-button">SIGN UP HERE</a>
        </div>
    </div>
</body>
















