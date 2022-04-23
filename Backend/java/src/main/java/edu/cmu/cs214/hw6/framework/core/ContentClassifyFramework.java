package edu.cmu.cs214.hw6.framework.core;

import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.ClassificationCategory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static edu.cmu.cs214.hw6.framework.core.DataPlugin.MIN_LENGTH;
import static edu.cmu.cs214.hw6.framework.core.DataPlugin.SPLIT_REGEX;

public class ContentClassifyFramework {
    private static String NO_DATA_NAME = "A Text Content Classify framework";
    private DataPlugin currentDataPlugin;
    private VisualPlugin currentVisualPlugin;
    private List<String> inputData;
    private List<DataPlugin> registeredDataPlugins;
    private List<VisualPlugin> registeredVisualPlugins;
    private List<String> textContent;
    private List<String> otherInfo;
    private HashMap<String, List<String>> resultMap;
    private HashMap<String, Integer> topicFreqMap;
    private String msg = "--------";
    private static boolean changedData = false;

    public ContentClassifyFramework() {
        registeredDataPlugins = new ArrayList<>();
        registeredVisualPlugins = new ArrayList<>();
        resultMap = new HashMap<>();
        topicFreqMap = new HashMap<>();
        textContent = new ArrayList<>();
        otherInfo = new ArrayList<>();
    }

    public ContentClassifyFramework(List<String> input) {
        registeredDataPlugins = new ArrayList<>();
        registeredVisualPlugins = new ArrayList<>();
        resultMap = new HashMap<>();
        topicFreqMap = new HashMap<>();
        textContent = new ArrayList<>();
        otherInfo = new ArrayList<>();
        this.inputData = input;
    }

    /**
     * Registers a new {@link DataPlugin} with the framework
     * @param plugin The data plugin to register
     */
    public void registerDataPlugin(DataPlugin plugin) {
        for (DataPlugin dp : registeredDataPlugins) {
            if (dp.getDataName().equals(plugin.getDataName())) return;
        }

        plugin.onRegister(this);
        registeredDataPlugins.add(plugin);
    }

    /**
     * Registers a new {@link VisualPlugin} with the framework
     * @param plugin The visualization plugin to register
     */
    public void registerVisualPlugin(VisualPlugin plugin) {
        for (VisualPlugin vp : registeredVisualPlugins) {
            if (vp.getVisualName().equals(plugin.getVisualName())) return;
        }

        plugin.onRegister(this);
        registeredVisualPlugins.add(plugin);
    }

    /**
     * Starts a new data for the provided {@link DataPlugin}
     * @param plugin The data plugin to start
     */
    public void startWithNewData(DataPlugin plugin) throws IOException {
        if (currentDataPlugin != plugin) {
            if (currentDataPlugin != null) {
                inputData.clear();
                resultMap.clear();
                topicFreqMap.clear();
                textContent.clear();
                otherInfo.clear();
                currentDataPlugin.onClosed();
                writeBlankHtml();

            }

            currentDataPlugin = plugin;
            inputData = plugin.getProcessedData();
            processFromInput();
            classifyContent();
            processTopicFreq();
            changedData = true;
        }
    }

    /**
     * Starts a new visualization for the provided {@link VisualPlugin}
     * @param plugin The visualization plugin to start
     */
    public void startWithNewVisual(VisualPlugin plugin) {
        if (currentVisualPlugin != plugin  || changedData) {
            if (currentVisualPlugin != null) {
                currentVisualPlugin.onClosed();
            }
            currentVisualPlugin = plugin;
            plugin.visualizeData();

            changedData = false;
        }
    }

    /**
     * Process the input data by splitting the text content and other information.
     */
    public void processFromInput() {
        if (inputData.isEmpty()) {
            System.out.println("Empty Data");
            return;
        }
        for (String s : inputData) {
            if (s.split(SPLIT_REGEX)[0].split("\\s+").length < MIN_LENGTH) {
                continue;
            }
            textContent.add(s.split(SPLIT_REGEX)[0]);
            otherInfo.add(s.split(SPLIT_REGEX)[1]);
        }
    }

    private void processTopicFreq() {
        for (String key : resultMap.keySet()) {
            topicFreqMap.put(key, resultMap.get(key).size());
        }
    }

    private void classifyContent() {
        for (int i = 0; i < textContent.size(); i++) {
            try (LanguageServiceClient language = LanguageServiceClient.create()) {
                // set content to the text string
                Document doc = Document.newBuilder().setContent(textContent.get(i)).setType(Document.Type.PLAIN_TEXT).build();
                ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(doc).build();
                // detect categories in the given text
                ClassifyTextResponse response = language.classifyText(request);

                for (ClassificationCategory category : response.getCategoriesList()) {
                    String[] topics = category.getName().split("/");
                    System.out.printf(
                            "Category name : %s, Confidence : %.3f\n",
                            topics[1], category.getConfidence());
                    List<String> temp;
                    if (!resultMap.containsKey(topics[1])) {
                        temp = new ArrayList<>();
                    } else {
                        temp = resultMap.get(topics[1]);
                    }
                    temp.add(inputData.get(i));
                    resultMap.put(topics[1], temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Getters for visualization plugin to use*/
    public List<String> getTextContent() {
        return textContent;
    }

    public List<String> getOtherInfo() {
        return otherInfo;
    }

    public Map<String, List<String>> getResultMap() {
        return resultMap;
    }

    public Map<String, Integer> getTopicFreqMap() {
        return topicFreqMap;
    }

    /* Getters for Gui purposes*/
    public String getDataName() {
        if (currentDataPlugin != null) {
            return currentDataPlugin.getDataName();
        } else {
            return NO_DATA_NAME;
        }
    }

    public String getMsg() {
        return this.msg;
    }


    public void setMsg(String str) {
        this.msg = str;
    }

    public String getVisualName() {
        if (currentVisualPlugin != null) {
            return currentVisualPlugin.getVisualName();
        } else {
            return NO_DATA_NAME;
        }
    }

    public List<String> getRegisteredDataPluginName(){
        return registeredDataPlugins.stream().map(DataPlugin::getDataName).collect(Collectors.toList());
    }

    public List<String> getRegisteredVisualPluginName(){
        return registeredVisualPlugins.stream().map(VisualPlugin::getVisualName).collect(Collectors.toList());
    }

    public boolean hasData() {
        return currentDataPlugin != null;
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
