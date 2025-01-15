<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <title>MAIN PAGE</title>
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
        </div>
    </div>
    <div class="default-page">
        <input type="text" placeholder="Search for recipes">
        <button>Search</button>
        <section class="recipes">
<!--            --><?php //foreach ($recipes as $recipe): ?>
<!--                <div id="--><?php //= $recipe->getId(); ?><!--">-->
<!--                    <img src="Public/uploads/--><?php //= $recipe->getImage(); ?><!--">-->
<!--                    <div>-->
<!--                        <h2>--><?php //= $recipe->getName(); ?><!--</h2>-->
<!--                        --><?php //foreach ($recipe->getIngredients() as $ingredient):?>
<!--                            <p>--><?php //= $ingredient->getAmount(); ?><!--</p>-->
<!--                            <p>--><?php //= $ingredient->getAmount(); ?><!--</p>-->
<!--                        --><?php //endforeach; ?>
<!--                        <p>--><?php //= $recipe->getDescription(); ?><!--</p>-->
<!--                        <div class="social-section">-->
<!--                            <i class="fas fa-heart"> --><?php //= $recipe->getRating(); ?><!--</i>-->
<!--                        </div>-->
<!--                    </div>-->
<!--                </div>-->
<!--            --><?php //endforeach; ?>
        </section>
    </div>
</div>
</body>y>