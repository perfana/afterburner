package nl.stokpop.afterburner.metrics;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.core.CfEnv;
import lombok.Value;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Value
public class PushConfig {
    String url;
    String user;
    String password;
    String basicAuth;
    int appIndex;
    String appId;

    public static PushConfig fromCfEnv() {
        CfEnv cfEnv = new CfEnv();
        CfCredentials credentials = cfEnv.findServiceByLabel("app-autoscaler").getCredentials();
        Map<String, Object> customMetrics = (Map<String, Object>) credentials.getMap().get("custom_metrics");
        String scalerUrl = ((String) customMetrics.getOrDefault("url", "no-url"));
        String scalerUser = (String) customMetrics.get("username");
        String scalerPassword = (String) customMetrics.get("password");
        String appId = cfEnv.getApp().getApplicationId();
        int appIndex = cfEnv.getApp().getInstanceIndex();
        PushConfig pushConfig = new PushConfig(scalerUrl, scalerUser, scalerPassword, encodeBasicAuth(scalerUser, scalerPassword), appIndex, appId);
        System.out.println("***" + pushConfig);
        return pushConfig;
    }

    public static String encodeBasicAuth(String user, String password) {
        return Base64.getEncoder().encodeToString((user + ":" + password).getBytes(StandardCharsets.UTF_8));
    }
}
