<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <link rel="stylesheet" type="text/css" href="Public/css/displayRecipe.css">

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
            <form method="POST" action="logout">
                <button type="submit" class="logout-button">Log Out</button>
            </form>
        </div>
    </div>
    <div class="default-page">
        <form class="search-bar" method="POST" action="search">
            <input type="text" name="key" placeholder="Search for recipes">
            <button  class="search-button" type="submit">Search</button>
        </form>
        <div class="recipes">
            <?php if($recipes):?>
                <?php foreach ($recipes as $recipe): ?>
                    <form method="GET" action="recipeDetails">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe->getId()) ?>">
                        <button type="submit" class="recipe-preview">
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
            <?php else: ?>
                <h2>No recipes found</h2>
            <?php endif; ?>
        </div>
    </div>
</div>
</body>





