import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class HttpClient {
    private final String credentials;
    private final String url;
    private final boolean sslVerifyDisabled;

    HttpClient(Properties properties) {
        this.credentials = Base64.getEncoder().encodeToString(String.join(":", properties.getUserName(), properties.getApiToken()).getBytes(StandardCharsets.UTF_8));
        this.url = properties.getJenkinsUrl();
        this.sslVerifyDisabled = properties.isSslVerifyDisabled();
    }

    public String getCredentials() {
        return credentials;
    }

    public String getUrl() {
        return url;
    }

    public boolean isSslVerifyDisabled() {
        return sslVerifyDisabled;
    }

    public String getResponse(String fileData) throws IOException {
        OkHttpClient client = isSslVerifyDisabled() ? getUnsafeOkHttpClient() : new OkHttpClient().newBuilder().build();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("jenkinsfile", fileData)
                .build();

        Request request = new Request.Builder()
                .url(String.join("/", getUrl(), "pipeline-model-converter/validate"))
                .method("POST", body)
                .addHeader("Authorization", String.join(" ", "Basic", getCredentials()))
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            return responseBody.string();
        }
        return "No response from jenkins server";
    }

    // Reference : https://stackoverflow.com/a/50961201/10155936
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
