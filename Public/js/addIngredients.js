const ingredientsContainer = document.getElementById('ingredients-container');
const addIngredientButton = document.getElementById('add-ingredient');
const fileInput = document.getElementById('file');
const imagePreview = document.getElementById('imagePreview');
fileInput.addEventListener('change', (event) => {
  const file = event.target.files[0];
  console.log(event.target.files);
  if (file) {
    const reader = new FileReader();

    reader.onload = (e) => {
      imagePreview.innerHTML = `<img src="${e.target.result}" alt="Preview" />`;
    };

    reader.readAsDataURL(file);
  } else {
    imagePreview.innerHTML = '+';
  }
});
const addIngredientRow = () => {
  const row = document.createElement('div');
  row.classList.add('component-row');

  row.innerHTML = `
      <input type="text" name="amount[]" placeholder="Amount" required />
      <input type="text" name="product[]" placeholder="Product" required />
      <button type="button" class="remove-row">Remove</button>
    `;

  row.querySelector('.remove-row').addEventListener('click', () => {
  row.remove();
  });

  ingredientsContainer.appendChild(row);
};

addIngredientRow();

addIngredientButton.addEventListener('click', addIngredientRow);
