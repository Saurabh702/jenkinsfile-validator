import java.util.stream.Stream;

public class Properties {
    private String userName;
    private String apiToken;
    private String jenkinsUrl;
    private boolean sslVerifyDisabled;

    public Properties() {
    }

    public Properties(String userName, String apiToken, String jenkinsUrl, boolean sslVerifyDisabled) {
        this.userName = userName;
        this.apiToken = apiToken;
        this.jenkinsUrl = jenkinsUrl;
        this.sslVerifyDisabled = sslVerifyDisabled;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public void setJenkinsUrl(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    public boolean isSslVerifyDisabled() {
        return sslVerifyDisabled;
    }

    public void setSslVerifyDisabled(boolean sslVerifyDisabled) {
        this.sslVerifyDisabled = sslVerifyDisabled;
    }

    public boolean isEmpty() {
        return Stream.of(this.userName, this.apiToken, this.jenkinsUrl).anyMatch(String::isBlank);
    }
}
