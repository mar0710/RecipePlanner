<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <link rel="stylesheet" type="text/css" href="Public/css/displayRecipe.css">

    <title>SHOPPING LIST PAGE</title>
</head>
<body>
<div class="main-container">
    <div class="nav">
        <img src="Public/img/logo.svg">
        <div class="buttons">
            <li><a href="recipes">recipes</a></li>
            <li><a href="shoppingList">shopping list</a></li>
            <li><a href="planner">planner</a></li>
            <li><a href="favorites">favorites</a></li>
            <li><a href="myAccount">my account</a></li>
            <form method="POST" action="logout">
                <button type="submit" class="logout-button">Log Out</button>
            </form>
        </div>
    </div>
    <?php foreach ($products as $product): ?>
        <lable class="shopping-list-line"><?= $product->getAmount(); ?>   <?= $product->getProduct();?>
            <input type="checkbox">
            <span class="checkmark">X</span>
        </lable>
    <?php endforeach; ?>
</div>
</body>





