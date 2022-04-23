package edu.cmu.cs214.hw6.framework.GUI;

import edu.cmu.cs214.hw6.framework.core.ContentClassifyFramework;

import java.util.Arrays;
import java.util.List;

public final class FrameworkState {

    private final String name;
    private String footer = "(footer)";
    private final DPlugin[] dPlugins;
    private final VPlugin[] vPlugins;
    private final String numColStyle;
    private final String currentPlayer;
    private final String gameOverMsg;

    private FrameworkState(String name, String footer, DPlugin[] dPlugins, VPlugin[] vPlugins, String numColStyle, String currentPlayer, String gameOverMsg) {
        this.name = name;
        this.footer = footer;
        this.dPlugins = dPlugins;
        this.vPlugins = vPlugins;
        this.numColStyle = numColStyle;
        this.currentPlayer = currentPlayer;
        this.gameOverMsg = gameOverMsg;
    }

    public static FrameworkState forFramework(ContentClassifyFramework game) {
        String name = game.getDataName();
        String footer = game.getMsg();
        DPlugin[] dPlugins = getDPlugins(game);
        VPlugin[] vPlugins = getVPlugins(game);
        String numColStyle = "NumColStyle";
        String currentPlayer = "CurrentPlayer";
        String gameOverMsg = "GameOverMsg";
        return new FrameworkState(name,footer,dPlugins, vPlugins, numColStyle, currentPlayer, gameOverMsg);
    }

    public void setFooter(String str) {
        this.footer = str;
    }

    private static DPlugin[] getDPlugins(ContentClassifyFramework game) {
        List<String> gamePlugins = game.getRegisteredDataPluginName();
        DPlugin[] plugins = new DPlugin[gamePlugins.size()];
        for (int i=0; i<gamePlugins.size(); i++){
            String link = "/dataPlugin?i="+ i;
            plugins[i] = new DPlugin(gamePlugins.get(i), link);
        }
        return plugins;
    }

    private static VPlugin[] getVPlugins(ContentClassifyFramework game) {
        List<String> gamePlugins = game.getRegisteredVisualPluginName();
        VPlugin[] plugins = new VPlugin[gamePlugins.size()];
        for (int i=0; i<gamePlugins.size(); i++){
            String link = "/visualPlugin?i="+ i;
            plugins[i] = new VPlugin(gamePlugins.get(i), link);
        }
        return plugins;
    }

    @Override
    public String toString() {
        return ("{ \"name\": \"" + this.name + "\"," +
                " \"footer\": \"" + this.footer + "\"," +
                " \"dataPlugins\": " +  Arrays.toString(dPlugins) + "," +
                " \"visualPlugins\": " +  Arrays.toString(vPlugins) + "," +
                " \"numColStyle\": \"" + this.numColStyle + "\"," +
                " \"currentPlayer\": \"" + this.currentPlayer + "\"," +
                " \"gameOverMsg\": \"" + this.gameOverMsg + "\"}").replace("null", "");

    }

}
