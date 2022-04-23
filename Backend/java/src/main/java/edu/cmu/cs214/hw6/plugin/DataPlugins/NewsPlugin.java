package edu.cmu.cs214.hw6.plugin.DataPlugins;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import edu.cmu.cs214.hw6.framework.core.ContentClassifyFramework;
import edu.cmu.cs214.hw6.framework.core.DataPlugin;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsPlugin implements DataPlugin {
    private static final String DATA_NAME = "| CNN News Description | ";
    private static final String API_KEY = "be8cf4696fdd4b359fd48b2e2e161e75";
    private static final int MAX_RESULT = 50;
    private String inputSource = "CNN";
    private boolean processState = false;
    private final NewsApiClient newsApiClient;
    private ContentClassifyFramework framework;
    private ArrayList<String> results = new ArrayList<>();

    public NewsPlugin() {
        this.newsApiClient = new NewsApiClient(API_KEY);
        System.out.println("News plugin called.");
    }

    @Override
    public String getDataName() {
        return DATA_NAME;
    }

    @Override
    public void onRegister(ContentClassifyFramework framework) {
        this.framework = framework;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void processData() {
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .sources(inputSource)
                        .pageSize(MAX_RESULT)
                        .sortBy("popularity")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        for (int i = 0; i < MAX_RESULT; i++) {
                            if (i == MAX_RESULT - 1) {
                                processState = true;
                            }
                            String description = response.getArticles().get(i).getDescription();
                            if (description.split("\\s+").length < MIN_LENGTH) {
                                continue;
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append(description);
                            sb.append(SPLIT_REGEX);
                            sb.append(response.getArticles().get(i).getPublishedAt());
                            results.add(sb.toString());
                            System.out.println(results.size());
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }

    @Override
    public List<String> getProcessedData() throws IOException {
        while (!processState) {
            System.out.println("Processing...");
        }
        System.out.println("Process complete");
        return results;
    }

    private void writeBlankHtml(String content) throws IOException {
        String filename = "../../Frontend/hw6-frontend-app/public/visualization.html";

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
