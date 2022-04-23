package edu.cmu.cs214.hw6;

import edu.cmu.cs214.hw6.framework.GUI.FrameworkState;
import edu.cmu.cs214.hw6.framework.core.ContentClassifyFramework;
import edu.cmu.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs214.hw6.framework.core.VisualPlugin;
import fi.iki.elonen.NanoHTTPD;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;


public class App extends NanoHTTPD {

    private ContentClassifyFramework framework;
    private List<DataPlugin> dataPlugins;
    private List<VisualPlugin> visualPlugins;
    private static final int PORT = 8080;

    public App() throws IOException {

        super(PORT);
        this.framework = new ContentClassifyFramework();
        dataPlugins = loadDataPlugins();
        visualPlugins = loadVisualPlugins();
        for (DataPlugin p: dataPlugins){
            framework.registerDataPlugin(p);
        }
        for (VisualPlugin p: visualPlugins) {
            framework.registerVisualPlugin(p);
        }

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    public static void main(String[] args) {

        try {
            writeBlankHtml();
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        if (uri.equals("/dataPlugin")) {
            System.out.println("DataPlugin Clicked!");
            try {
                dataPlugins.get(Integer.parseInt(params.get("i"))).processData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            framework.registerDataPlugin(dataPlugins.get(Integer.parseInt(params.get("i"))));
            try {
                framework.startWithNewData(dataPlugins.get(Integer.parseInt(params.get("i"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Process completed.");
            this.framework.setMsg("Process completed. Please choose a Visualization Plugin~");

        } else if (uri.equals("/visualPlugin")) {
            System.out.println("VisualPlugin Clicked!");
//            dataPlugins.get(Integer.parseInt(params.get("i"))).;

            framework.registerVisualPlugin(visualPlugins.get(Integer.parseInt(params.get("i"))));
            framework.startWithNewVisual(visualPlugins.get(Integer.parseInt(params.get("i"))));

        } else if (uri.equals("/start")) {
            System.out.println("Start state");
//            this.framework.setMsg("Processing...");
        }
        // Extract the view-specific data from the game and apply it to the template.
        FrameworkState gameplay = FrameworkState.forFramework(this.framework);
        return newFixedLengthResponse(gameplay.toString());

    }

    /**
     * Load plugins listed in META-INF/services/...
     * @return List of instantiated plugins
     */
    private static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getDataName());
            result.add(plugin);
        }
        return result;
    }

    private static List<VisualPlugin> loadVisualPlugins() {
        ServiceLoader<VisualPlugin> plugins = ServiceLoader.load(VisualPlugin.class);
        List<VisualPlugin> result = new ArrayList<>();
        for (VisualPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getVisualName());
            result.add(plugin);
        }
        return result;
    }

    private static void writeBlankHtml() throws IOException {
        String filename = "../../Frontend/hw6-frontend-app/public/visualization.html";
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Page Title</title>\n" +
                "<style>\n" +
                "h1 {text-align: center;}\n" +
                "p {text-align: center;}\n" +
                "div {text-align: center;}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>(Scroll down to see option buttons)</h1>\n" +
                "<h1>First step: Select a data plugin.</h1>\n" +
                "<h1>Second step: Wait until processing complete.</h1>\n" +
                "<h1>Thrid step: Select a visualization plugin.</h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        try {
            FileWriter blankHtml = new FileWriter(filename);
            blankHtml.write(content);
            blankHtml.close();
        } catch (IOException e) {
            System.out.println("An I/O error occurred.");
            e.printStackTrace();
        }
    }
}


