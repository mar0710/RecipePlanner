<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <link rel="stylesheet" type="text/css" href="Public/css/displayRecipe.css">
<!--    <script type="text/javascript" src="./Public/js/rating.js" defer></script>-->

    <title>RECIPE DETAILS PAGE</title>
</head>
<body>
<div class="main-container">
    <div class="SiteHeader">
        <h1><?= $recipe->getName()?></h1>
    </div>
    <div class="recipe-details-container">
        <img class="main-photo" src="Public/uploads/<?=$recipe->getImage()?>">
            <form method = "POST" action="rateRecipe" class="rating">
                <p><?=$recipe->getRating()?></p>
                <img src="Public/img/ratingStar.svg" id="rating-star">
                <input type="text" name="rating" id="rating" placeholder=" ">
                <input type="hidden" name="recipeId" value="<?= $recipe->getId() ?>">
                <button type="submit" class="star-button" id="star-button">Rate</button>
            </form>
        <div class="ingredients">
            <p>Ingredients:</p>
            <?php foreach ($ingredients as $ingredient):?>
                <p><?= $ingredient->getAmount();?>    <?= $ingredient->getproduct(); ?></p>

            <?php endforeach; ?>
            <p><?=$recipe->getDescription()?></p>
        </div>
        <div class="comment-section">
            <p>Comments:</p>
            <form class="search-bar" method="POST" action="addComment">
                <input type="hidden" name="id" value="<?= $recipe->getId() ?>">
                <input type="text" name="comment" placeholder="add comment">
                <button  class="search-button" type="submit">comment</button>
            </form>
            <?php foreach ($comments as $comment):?>
                <p><?= $comment->getUserName(); ?>    added:<?= $comment->getDate(); ?></p>
                <p><?= $comment->getComment(); ?></p>
            <?php endforeach; ?>
        </div>
    </div>
    <form method="POST" action="addToFavorites" >
        <input type="hidden" name="recipeId" value="<?= $recipe->getId() ?>">
        <button class="add-to-favorite" type="submit" >ADD TO FAVORITE</button>
    </form>
    <form method="POST" action="addToPlanner" class="add-to-planner" >
        <input type="hidden" name="recipeId" value="<?= $recipe->getId() ?>">
        <label for="options">Choose a day:</label>
        <select id="options" name="day">
            <option value="Monday">Monday</option>
            <option value="Tuesday">Tuesday</option>
            <option value="Wednesday">Wednesday</option>
            <option value="Thursday">Thursday</option>
            <option value="Friday">Friday</option>
            <option value="Saturday">Saturday</option>
            <option value="Sunday">Sunday</option>
        </select>
        <button type="submit">add to planner</button>
    </form>
</div>
</body>