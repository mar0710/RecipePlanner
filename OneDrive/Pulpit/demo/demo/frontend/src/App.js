import logo from './logo.svg';
import './App.css';
import {Component} from "react";
class App extends Component {
  state = {
    recipes: []
  };

  async componentDidMount() {
    const response = await fetch('/recipes');
    const body = await response.json();
    this.setState({recipes: body});
  }

  render() {
    const {recipes} = this.state;
    return (
        <div className="App">
          <header className="App-header">
            {/*<img src={logo} className="App-logo" alt="logo" />*/}
            <div className="App-intro">
              <h2>Recipes</h2>
              <div className="recipe-list">
                {recipes.map(recipe =>
                    <div className="recipe-box" key={recipe.id}>
                      <div className="recipe-name">
                        {recipe.name}
                      </div>
                      <div className="description">
                        {recipe.description}
                      </div>
                    </div>
                )}
              </div>
            </div>
          </header>
        </div>
    );
  }
}
export default App;
