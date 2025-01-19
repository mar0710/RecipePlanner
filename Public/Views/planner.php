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
        <div class="recipes-planner">
            <h2>MONDAY</h2>
            <?php if($recipes1):?>
                <?php foreach ($recipes1 as $recipe1):?>
                    <form method="GET" action="recipeDetails">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe1->getId()) ?>">
                        <button type="submit" class="recipe-preview" id="<?= $recipe1->getId(); ?>">
                            <img src="Public/uploads/<?= $recipe1->getImage(); ?>">
                            <div class="recipe-info">
                                <h2><?= $recipe1->getName(); ?></h2>
                                <div  class="preview-description">
                                    <p><?= $recipe1->getDescription(); ?></p>
                                </div>
                            </div>
                        </button>
                    </form>
                    <form method="POST" action="deleteFromPlanner">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe1->getId()) ?>">
                        <button type="submit" class="delete-button">-------</button>
                    </form>
                <?php endforeach;?>
            <?php endif; ?>
            <h2>TUESDAY</h2>
            <?php if($recipes2):?>
                <?php foreach ($recipes2 as $recipe2):?>
                    <form method="GET" action="recipeDetails">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe2->getId()) ?>">
                        <button type="submit" class="recipe-preview" id="<?= $recipe2->getId(); ?>">
                            <img src="Public/uploads/<?= $recipe2->getImage(); ?>">
                            <div class="recipe-info">
                                <h2><?= $recipe2->getName(); ?></h2>
                                <div  class="preview-description">
                                    <p><?= $recipe2->getDescription(); ?></p>
                                </div>
                            </div>
                        </button>
                    </form>
                <?php endforeach;?>
            <?php endif; ?>
            <h2>WEDNESDAY</h2>
            <?php if($recipes3):?>
                <?php foreach ($recipes3 as $recipe3):?>
                    <form method="GET" action="recipeDetails">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe3->getId()) ?>">
                        <button type="submit" class="recipe-preview" id="<?= $recipe3->getId(); ?>">
                            <img src="Public/uploads/<?= $recipe3->getImage(); ?>">
                            <div class="recipe-info">
                                <h2><?= $recipe3->getName(); ?></h2>
                                <div  class="preview-description">
                                    <p><?= $recipe3->getDescription(); ?></p>
                                </div>
                            </div>
                        </button>
                    </form>
                <?php endforeach;?>
            <?php endif; ?>
            <h2>THURSDAY</h2>
            <?php if($recipes4):?>
                <?php foreach ($recipes4 as $recipe4):?>
                    <form method="GET" action="recipeDetails">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe4->getId()) ?>">
                        <button type="submit" class="recipe-preview" id="<?= $recipe4->getId(); ?>">
                            <img src="Public/uploads/<?= $recipe4->getImage(); ?>">
                            <div class="recipe-info">
                                <h2><?= $recipe4->getName(); ?></h2>
                                <div  class="preview-description">
                                    <p><?= $recipe4->getDescription(); ?></p>
                                </div>
                            </div>
                        </button>
                    </form>
                <?php endforeach;?>
            <?php endif; ?>
            <h2>FRIDAY</h2>
            <?php if($recipes5):?>
                <?php foreach ($recipes5 as $recipe5):?>
                    <form method="GET" action="recipeDetails">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe5->getId()) ?>">
                        <button type="submit" class="recipe-preview" id="<?= $recipe5->getId(); ?>">
                            <img src="Public/uploads/<?= $recipe5->getImage(); ?>">
                            <div class="recipe-info">
                                <h2><?= $recipe5->getName(); ?></h2>
                                <div  class="preview-description">
                                    <p><?= $recipe5->getDescription(); ?></p>
                                </div>
                            </div>
                        </button>
                    </form>
                <?php endforeach;?>
            <?php endif; ?>
            <h2>SATURDAY</h2>
            <?php if($recipes6):?>
                <?php foreach ($recipes6 as $recipe6):?>
                    <form method="GET" action="recipeDetails">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe6->getId()) ?>">
                        <button type="submit" class="recipe-preview" id="<?= $recipe6->getId(); ?>">
                            <img src="Public/uploads/<?= $recipe6->getImage(); ?>">
                            <div class="recipe-info">
                                <h2><?= $recipe6->getName(); ?></h2>
                                <div  class="preview-description">
                                    <p><?= $recipe6->getDescription(); ?></p>
                                </div>
                            </div>
                        </button>
                    </form>
                <?php endforeach;?>
            <?php endif; ?>
            <h2>SUNDAY</h2>
            <?php if($recipes7):?>
                <?php foreach ($recipes7 as $recipe7):?>
                    <form method="GET" action="recipeDetails">
                        <input type="hidden" name="id" value="<?= htmlspecialchars($recipe7->getId()) ?>">
                        <button type="submit" class="recipe-preview" id="<?= $recipe7->getId(); ?>">
                            <img src="Public/uploads/<?= $recipe7->getImage(); ?>">
                            <div class="recipe-info">
                                <h2><?= $recipe7->getName(); ?></h2>
                                <div  class="preview-description">
                                    <p><?= $recipe7->getDescription(); ?></p>
                                </div>
                            </div>
                        </button>
                    </form>
                <?php endforeach;?>
            <?php endif; ?>
        </div>
    </div>
</div>
</body>





