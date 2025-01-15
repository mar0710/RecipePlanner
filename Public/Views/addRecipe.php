<!DOCTYPE html>
<head>
    <link rel="stylesheet" type="text/css" href="Public/css/style.css">
    <script type="text/javascript" src="./Public/js/addIngredients.js" defer></script>
    <title>ADD RECIPE PAGE</title>
</head>
<body>
    <div class="main-container">
        <div class="SiteHeader">
            <h1>My Recipes</h1>
        </div>
        <form class="default-page" id="components-form" method="POST" action="addRecipe" ENCTYPE="multipart/form-data">
            <div class="recipe-container-up">
                <div class="recipe-container-photo">
                    <div class="file-upload">
                        <input type="file" name="file"/><br/>
                    </div>
                </div>
                <div class="recipe-container-ingrediants">
                    <input name="recipeName" type="text" placeholder="recipe name" class="add-recipe-name" required>
                    <div id="ingredients-container">
                        <!-- Component input rows will be added here -->
                    </div>
                    <button type="button" id="add-ingredient">Add ingredient</button>
                </div>
            </div>
            <div class="recipe-container-down">
                <textarea name="description" rows=10 class="add-recipe-description" placeholder="recipe description" required></textarea>
            </div>
            <button type="submit">Submit</button>
        </form>
    </div>
</body>