const ingredientsContainer = document.getElementById('ingredients-container');
const addIngredientButton = document.getElementById('add-ingredient');

// Function to add a new component input row
const addIngredientRow = () => {
  const row = document.createElement('div');
  row.classList.add('component-row');

  row.innerHTML = `
      <input type="text" name="amount[]" placeholder="Amount" required />
      <input type="text" name="product[]" placeholder="Product" required />
      <button type="button" class="remove-row">Remove</button>
    `;

  // Add event listener for the "Remove" button
  row.querySelector('.remove-row').addEventListener('click', () => {
  row.remove();
  });

  ingredientsContainer.appendChild(row);
};

// Add initial row
addIngredientRow();

// Add new row when "Add Component" button is clicked
addIngredientButton.addEventListener('click', addIngredientRow);
