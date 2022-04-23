# Domain

The idea of this framework is to perform content analysis and classification on different texts. Basically, it will analyze and classify the text information from different sources (provided by data plugins) into various given topics and show the results in different ways (using visualization plugins). The framework provides benefits for reuse because it performs the content classification itself. It's also beneficial to reuse the existing visualizaiton or data plugins.

Data plugins could provide a list of text information with corresponding other information like geographical locations or time stamps, which could include:

* Twitter plugins that takes in a specific twitter user name and gets the Twitter messages from the user along with other information if needed using the Twitter API
* News plugins that takes in a specific source and extracts News descriptions along with other information like published time using the News API
* CSV plugins that takes in a CSV file and extract the data from the file to get text information and other information like time stamps or locations

The framework could provide a map with the given String topics as the keys and a list of String containing text content, other information like location or time as value to visualization plugins. It could also provide a map with topics and frequency as key and value. Visualization plugins could include:

* Word cloud containing classified topics from the text information in a given situation and setting
* Time-series graph displaying the most popular topic in a given situation and setting
* Bar chart or pie chart for classified topics from the text information with frequency


# Generality vs specificity

The framework mainly processes the text information paired with other information like time information or geo information, and does some analysis or classifications according to user requirements.

The generality of the framework is that it's able to adapt to many different kinds of data plugins, such as from Twitter, Youtube and also local files. All these kinds of data plugins will pass a specific kind of text information pairs as String format to the framework. Also, the framework can fit for a variety of visualization plugins, for example different charts and maps. 

The specificity of the framework is that it is mainly responsible for the analysis and classification of content, accompanied by some other side information provided by the plugins to support the visualization of processed results. As mentioned above in the domain part, the framework first extracts the "text" information from the data passed by data plugins, does the analysis and classification, then according to the requirements or commands from the frontend, it provides the wanted data parts for visualization. 

The framework is reusable because it performs the content analysis and classification itself. It only takes a list of Strings of a certain format from different plugins, and processes the data. It will place the input Strings with the same classification results to the same set, and how to visualize the results is up to the user. For example, the framework first classifies the input Strings through topics, and the user can choose to show the word cloud containing classified topics published in a certain location or time period. 

The potential flexibility of plugins can be data extracted from different kinds of source, for example the plugin can load the content and time from Twitter, or the content and  geographical information from Twitter, and the Twitter can also be replaced by other media. From News the plugin can load the title content and source. And for local files, there could also be plugins getting information from CSV files. 


# Project structure

The overall project structure is designed as following:

* We have a `hw6` package which contains a `framework` folder, `plugin` folder and the `App class` for launching. 
* Under the `framework` foler, we have a `core` folder including `ContentClassifyFramework class` and `Plugin interfaces` like `DataPlugin` and `VisualPlugin`. 
* Under the `plugin` folder, we have the `dataPlugin` folder containing data plugins and we also have the `visualPlugin` folder containing visualization plugins.

Key data structures:

* We use a list of `String` as the input to the framework. Each `String` contains the information of text fragments, other information like location or time and is organized like `Text#~#OtherInfo`.
* The framework outputs a map with the given `String` topics as the keys and the input list of `String` as the value after the classification. It also can output a map with given `String` topics as the keys and the frequency of it as the value.

Plugin loading mechanism:

* The plugins are loaded using `ServiceLoader` that loads the plugins listed in the file under `META-INF.services` folder in `resources`. It's done within the `loadPlugins method` in the `App class`, which will return a list of loaded plugins. The result will be used to register by the `framework`.


# Plugin interfaces

### Data Plugin

`String getPluginName`
Gets the name of the plugin. 

`void onRegister(Framework framework)`
Called when the plugin is first registered with the framework, giving the plug-in a chance to perform any initial set-up.

`void onStart()`
Called when this plugin is about to start.

`void onClosed()`
Called when the plugin is closed to allow the plugin to do any final cleanup.

`void processData()`
Process the data to the required `String` format.

`List<String> getProcessedData()` 
Returns the list of `String` to be processed by the framework. 

### Visualization Plugin

`String getVisualName`
Gets the name of the plugin. 

`void onRegister(Framework framework)`
Called when the plugin is first registered with the framework, giving the plug-in a chance to perform any initial set-up.

`void onStart()`
Called when this plugin is about to start.

`void onClosed()`
Called when the plugin is closed to allow the plugin to do any final cleanup.

`void visualizeData()`
Visualize the data and output it as Visualization.html

