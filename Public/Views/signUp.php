<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <script type="text/javascript" src="./Public/js/script.js" defer></script>
    <title>SIGN UP PAGE</title>
</head>
<body>
    <div class="container">
        <div class="Welcome-text">
            <h3>Welcome to<br>Recipe Planner</h3>
        </div>
        <div class="Welcome-logo">
            <img src="Public/img/logo.svg">
        </div>
        <form class="signUpSheet-container" action="signUp" method="POST">
            <div class="messages">
                <?php
                if(isset($messages)){
                    foreach($messages as $message) {
                        echo $message;
                    }
                }
                ?>
            </div>
            <p class="createAccount-text">Create your account</p>
            <input name="username" type="text" placeholder="username" required>
            <input name="email" type="email" placeholder="e-mail" required>
            <input name="password" type="password" placeholder="password" required>
            <p class="passwordRule-text">Your password should have at least 5 letters</p>
            <input name="confirmedPassword" type="password" placeholder="repeat password" required>
            <button class="create-account-button" type="submit">Create my account </button>
        </form>
    </div>
</body>