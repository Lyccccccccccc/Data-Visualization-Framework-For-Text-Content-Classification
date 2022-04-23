package edu.cmu.cs214.hw6.framework.core;

public interface VisualPlugin {

    String SAVE_PATH = "../../Frontend/hw6-frontend-app/public/visualization.html";

    /**
     * Gets the name of the plug-in Visual.
     * @return the name of the Visual
     */
    String getVisualName();

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
     * Visualize the data and output it as Visualization.html.
     */
    void visualizeData();
    
}
