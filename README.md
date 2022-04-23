# Hw6 Analytics Framework

## Idea
The framework is called content classify framework, which means its major function is to classify the text content in the input and store the result in various forms like a map with classified topics as key and list of input or frequency as values.

## Extend
To extend the functionalities, follow the interface for data plugin and visualization plugin to write specific plugins.

## Setup

### MacOS (Recommended)

#### Backend
* Open folder `Backend/java` with intellij.
* Use `mvn install` to install the dependencies.
* Change the `run -> configuration` of `App`. To use Google NLP, the credential key `GOOGLE_APPLICATION_CREDENTIALS=./GoogleAPI_KEY.json`needs to be set as follow ![alt text](https://github.com/CMU-17-214/hw6-analytics-framework-doraemon/blob/main/Files/intellij%20set%20key.jpg)
* Run `App.java`.

#### Frontend
* Open folder `Frontend/hw6-frontend-app`.
* Run `npm install --force`.
* Modify the `node_modules/handlebars/package.json` file. Add `"fs": false` under line 81.
* Run `npm run compile`.
* Run `npm run start`.

#### Web browser
* Chrome (MacOS)
* If you choose a visualization plugin, but nothing change, you can try ***REFRESH*** the page (needed for Windows)

#### Sample
* You can open `Files/screen record.mov` to check sample use.


