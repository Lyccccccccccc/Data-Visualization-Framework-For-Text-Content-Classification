import Chart from "chart.js";
import Handlebars from "handlebars"
import { Component } from 'react'
import './App.css'

var oldHref = "http://localhost:3000"

interface Props {
}

interface Plugin {
  name: String;
  link: String;
}

interface GameState {
  name: String;
  footer: String;
  dataPlugins: Array<Plugin>;
  visualPlugins: Array<Plugin>;
  numColStyle: String;
  currentPlayer: String;
  gameOverMsg: String;
  template: HandlebarsTemplateDelegate<any>;
}

class App extends Component<Props, GameState> {

  constructor(props: Props) {
    super(props);
    this.state = {
      template: this.loadTemplate(),
      name : "A Game Framework",
      footer : "Please Choose a Data plugin~ ",
      dataPlugins : [
        { name: "Load Data Plugins", link:"/start"},
      ],
      visualPlugins : [
        { name: "Load Visual Plugins", link:"/start"},
      ],
      numColStyle : "auto",
      currentPlayer : "",
      gameOverMsg : "",
    };

  }

  loadTemplate (): HandlebarsTemplateDelegate<any> {
    const src = document.getElementById("handlebars");
    return Handlebars.compile(src?.innerHTML, {});
  }

  convertToDataPlugin(p: any): Array<Plugin> {
    const newPlugins: Array<Plugin> = [];
    for (var i = 0; i < p["dataPlugins"].length; i++) {
      var plug: Plugin = {
        name: p["dataPlugins"][i]["name"],
        link: p["dataPlugins"][i]["link"],
      };
      newPlugins.push(plug);
    }

    return newPlugins;
  }

  convertToVisualPlugin(p: any): Array<Plugin> {
    const newPlugins: Array<Plugin> = [];
    for (var i = 0; i < p["visualPlugins"].length; i++) {
      var plug: Plugin = {
        name: p["visualPlugins"][i]["name"],
        link: p["visualPlugins"][i]["link"],
      };
      newPlugins.push(plug);
    }

    return newPlugins;
  }


  async start(){
    const href = "start";
    // const href = "dataPlugin";
    const response = await fetch(href);
    const json = await response.json();
    const newDataPlugins: Array<Plugin> = this.convertToDataPlugin(json);
    const newVisualPlugins: Array<Plugin> = this.convertToVisualPlugin(json);
    this.setState({ dataPlugins: newDataPlugins, visualPlugins: newVisualPlugins})
  }


  async chooseDataPlugin(url: String){
    const href = "dataPlugin?"+url.split("?")[1];
    const response = await fetch(href);
    const json = await response.json();

    const newDataPlugins: Array<Plugin> = this.convertToDataPlugin(json);
    const newVisualPlugins: Array<Plugin> = this.convertToVisualPlugin(json);
    this.setState({ dataPlugins: newDataPlugins, visualPlugins: newVisualPlugins, name: json["name"],footer:json["footer"],numColStyle : json["numColStyle"],
      currentPlayer : json["currentPlayer"],
      gameOverMsg : json["gameOverMsg"] })
  }
  async chooseVisualPlugin(url: String){
    const href = "visualPlugin?"+url.split("?")[1];
    const response = await fetch(href);
    const json = await response.json();

    const newDataPlugins: Array<Plugin> = this.convertToDataPlugin(json);
    const newVisualPlugins: Array<Plugin> = this.convertToVisualPlugin(json);
    this.setState({ dataPlugins: newDataPlugins, visualPlugins: newVisualPlugins, name: json["name"],footer:json["footer"],numColStyle : json["numColStyle"],
      currentPlayer : json["currentPlayer"],
      gameOverMsg : json["gameOverMsg"] })
  }

  async switch() {
    if (
      window.location.href.split("?")[0] === "http://localhost:3000/dataPlugin" &&
      oldHref !== window.location.href
    ) {
      this.chooseDataPlugin(window.location.href);
      oldHref = window.location.href;
      this.setState({ footer: "Processing... (Please wait for about 20 sec) " })

    }
    else if (
      window.location.href.split("?")[0] === "http://localhost:3000/visualPlugin" &&
      oldHref !== window.location.href
    ) {
      this.chooseVisualPlugin(window.location.href);
      oldHref = window.location.href;
    }

    else if (
      window.location.href === "http://localhost:3000/" ||
      window.location.href === "http://localhost:3000/start" ||
      window.location.href === "http://localhost:3000/loadDataPlugin" ||
      window.location.href === "http://localhost:3000/loadVisualPlugin"
    ){
      this.start();
      oldHref = window.location.href;
    }
  };
  render() {
    this.switch()

    return (
      <div className="App">
        <div
          dangerouslySetInnerHTML={{
            __html: this.state.template({name: this.state.name, footer: this.state.footer,
              dataPlugins: this.state.dataPlugins,
              visualPlugins: this.state.visualPlugins,
              numColStyle: this.state.numColStyle, gameOverMsg: this.state.gameOverMsg,
              currentPlayer : this.state.currentPlayer}),
          }}
        />
      </div>
    )
  };


};

export default App;