import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.extensions.PluginId;

public class PluginProperties {
    private static final PluginProperties instance = new PluginProperties();

    private final PropertiesComponent applicationProperties;
    private final String pluginId;
    private Properties cachedUserProperties;

    private PluginProperties() {
        applicationProperties = PropertiesComponent.getInstance();
        PluginId id = PluginManagerCore.getPluginByClassName(this.getClass().getName());
        pluginId = (id != null) ? id.getIdString() : "";
    }

    public static PluginProperties getInstance() {
        return instance;
    }

    public void setValues(Properties properties) {
        applicationProperties.setValue(String.join(".", pluginId, "userName"), properties.getUserName());
        applicationProperties.setValue(String.join(".", pluginId, "apiToken"), properties.getApiToken());
        applicationProperties.setValue(String.join(".", pluginId, "jenkinsUrl"), properties.getJenkinsUrl());
        applicationProperties.setValue(String.join(".", pluginId, "sslVerifyDisabled"), properties.isSslVerifyDisabled());
        cachedUserProperties = properties;
    }

    public Properties getValues() {
        if (cachedUserProperties == null) {
            cachedUserProperties = new Properties();
            cachedUserProperties.setUserName(applicationProperties.getValue(String.join(".", pluginId, "userName"), ""));
            cachedUserProperties.setApiToken(applicationProperties.getValue(String.join(".", pluginId, "apiToken"), ""));
            cachedUserProperties.setJenkinsUrl(applicationProperties.getValue(String.join(".", pluginId, "jenkinsUrl"), ""));
            cachedUserProperties.setSslVerifyDisabled(applicationProperties.getBoolean(String.join(".", pluginId, "sslVerifyDisabled"), false));
        }
        return cachedUserProperties;
    }
}