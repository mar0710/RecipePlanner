<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <link rel="stylesheet" type="text/css" href="Public/css/displayRecipe.css">

    <title>FAVORITES PAGE</title>
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
    <div class="default-page">
        <div class="recipes">
            <?php foreach ($recipes as $recipe): ?>
                <form method="GET" action="recipeDetails">
                    <input type="hidden" name="id" value="<?= htmlspecialchars($recipe->getId()) ?>">
                    <button type="submit" class="recipe-preview" id="<?= $recipe->getId(); ?>">
                        <img src="Public/uploads/<?= $recipe->getImage(); ?>">
                        <div class="recipe-info">
                            <h2><?= $recipe->getName(); ?></h2>
                            <div  class="preview-description">
                                <p><?= $recipe->getDescription(); ?></p>
                            </div>
                        </div>
                    </button>
                </form>
            <?php endforeach; ?>
        </div>
    </div>
</div>
</body>





