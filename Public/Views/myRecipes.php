<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <link rel="stylesheet" type="text/css" href="Public/css/displayRecipe.css">
    <title>MAIN PAGE</title>
</head>
<body>
<div class="main-container">
        <div class="SiteHeader">
            <h1>My Recipes</h1>
        </div>
    </div>
    <div class="default-page">
        <div class="recipes">
            <?php foreach ($myRecipes as $myRecipe): ?>
                <form method="GET" action="recipeDetails">
                    <input type="hidden" name="id" value="<?= htmlspecialchars($myRecipe->getId()) ?>">
                    <button type="submit" class="recipe-preview">
                        <img src="Public/uploads/<?= $myRecipe->getImage(); ?>">
                        <div class="recipe-info">
                            <h2><?= $myRecipe->getName(); ?></h2>
                            <div  class="preview-description">
                                <p><?= $myRecipe->getDescription(); ?></p>
                            </div>
                        </div>
                    </button>
                </form>
            <?php endforeach; ?>
        </div>
    </div>
    <div class="addRecipe">
        <a href="addRecipe"><img src="Public/img/addCircle.svg"></a>
    </div>
</div>
</body>