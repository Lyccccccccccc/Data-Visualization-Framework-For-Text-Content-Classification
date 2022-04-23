package edu.cmu.cs214.hw6.framework.core;

import java.io.IOException;
import java.util.List;

public interface DataPlugin {

    String SPLIT_REGEX = "#~#";

    int MIN_LENGTH = 25;

    /**
     * Gets the name of the plug-in data.
     * @return the name of the data
     */
    String getDataName();

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     *
     * @param framework The {@link ContentClassifyFramework} instance with which the plug-in
     *                  was registered.
     */
    void onRegister(ContentClassifyFramework framework);

    /**
     * Called when this plugin is about to start.
     */
    void onStart();

    /**
     * Called when the plugin is closed to allow the plugin to do any final cleanup.
     */
    void onClosed();

    /**
     * The data plugin will pull the data according to input and process the data to a required String format.
     * Separating the text content and other information with SPLIT_REGEX: "#~#"
     */
    void processData() throws IOException;

    /**
     * Get the processed data for the framework to analyze.
     * @return the list of String to be processed by the framework
     */
    List<String> getProcessedData() throws IOException;
}
