package edu.cmu.cs214.hw6.plugin.DataPlugins;

import edu.cmu.cs214.hw6.framework.core.ContentClassifyFramework;
import edu.cmu.cs214.hw6.framework.core.DataPlugin;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.signature.TwitterCredentials;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitterPlugin implements DataPlugin {
    private static final String DATA_NAME = "| CMU Twitter Description | ";
    private static final String PATHNAME = "twitter key/key.json";
    private static final int MAX_RESULT = 100;
    private String userName = "CarnegieMellon";
    private String inputSource = userName;
    private ContentClassifyFramework framework;
    private boolean processState = false;
    private List<String> results = new ArrayList<>();

    public TwitterPlugin() {
        System.out.println("Twitter plugin called.");
    }

    private void twitterProcess() throws IOException {
        TwitterClient twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                .readValue(new File(PATHNAME), TwitterCredentials.class));
        twitterClient.setAutomaticRetry(false);
        User user = twitterClient.getUserFromUserName(userName);
        String userID = user.getId();
        int count = 0;

        String curUserId = userID;
        System.out.println(curUserId);
        TweetList tweetLst = twitterClient.getUserTimeline(curUserId, AdditionalParameters.builder().recursiveCall(false).maxResults(MAX_RESULT).build());
        List<TweetV2.TweetData> tweetDataLst = tweetLst.getData();

        if (tweetDataLst == null) return;
        for (TweetV2.TweetData tweetData : tweetDataLst) {
            count++;
            if (count == MAX_RESULT - 1) {
                processState = true;
                break;
            }
            String tmpStr;

            tmpStr = processString(tweetData.getText());
            if (tmpStr.split("\\s+").length < MIN_LENGTH) {
                continue;
            }
            System.out.println(tmpStr);
            results.add(tmpStr + SPLIT_REGEX + "0");
        }
    }

    private String processString(String str) {
        int atPosition = -1;
        int httpPosition = -1;
        int strLen = str.length();
        int whiteSpacePosition = -1;
        try {

            for (int i = 0; i < strLen; i++) {
                if (str.charAt(i) == '@') {
                    atPosition = i;
                    break;
                }
            }
            if (atPosition >= 0) {  // there exists '@'
                for (int i = atPosition; i < strLen; i++) {
                    if (Character.isWhitespace(str.charAt(i))) {
                        whiteSpacePosition = i;
                        break;
                    }
                    if (i == strLen - 1) {  // till the end of the string
                        return str.substring(0, atPosition);
                    }
                }
                return processString(str.substring(0, atPosition) + str.substring(whiteSpacePosition + 1));
            }

            if (str.contains("https://")) {
                httpPosition = str.indexOf("https://");
            }
            if (httpPosition >= 0) {  // there exists 'https://'
                for (int i = httpPosition; i < strLen; i++) {
                    if (Character.isWhitespace(str.charAt(i))) {
                        whiteSpacePosition = i;
                        break;
                    }
                    if (i == strLen - 1) {  // till the end of the string
                        return str.substring(0, httpPosition);
                    }
                }
                return processString(str.substring(0, httpPosition) + str.substring(whiteSpacePosition + 1));
            }
            return str;
        } catch (Exception e) {
            return str;
        }
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
    public void processData() throws IOException {
        twitterProcess();
    }

    @Override
    public List<String> getProcessedData() {
        while (!processState) {
            System.out.println("Processing...");
        }
        System.out.println("Process complete");
        return results;
    }
}
