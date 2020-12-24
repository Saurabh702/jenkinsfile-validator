import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class SettingsDialog extends DialogWrapper {

    private JTextField userNameTextField;
    private JTextField apiTokenTextField;
    private JTextField jenkinsUrlTextField;
    private JCheckBox sslVerify;
    private final String userName;
    private final String apiToken;
    private final String jenkinsUrl;
    private final boolean sslVerifyDisabled;

    public SettingsDialog(Properties properties) {
        super(false);
        this.userName = properties.getUserName();
        this.apiToken = properties.getApiToken();
        this.jenkinsUrl = properties.getJenkinsUrl();
        this.sslVerifyDisabled = properties.isSslVerifyDisabled();
        init();
        setTitle("Jenkinsfile Validator Settings");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        JPanel labelPane = new JPanel(new GridLayout(0, 1));
        JPanel fieldPane = new JPanel(new GridLayout(0, 1));

        JLabel userNameLabel = new JLabel("Username ");
        JLabel tokenLabel = new JLabel("API token ");
        JLabel urlLabel = new JLabel("Server URL ");

        userNameTextField = new JTextField(20);
        apiTokenTextField = new JTextField(20);
        jenkinsUrlTextField = new JTextField(20);

        sslVerify = new JCheckBox("Disable SSL verification");

        userNameTextField.setToolTipText("Your Jenkins Username");
        apiTokenTextField.setToolTipText("Get API token from here : &lt;your-jenkins-server-url&gt;/user/&lt;your-username&gt;/configure");
        jenkinsUrlTextField.setToolTipText("Your Jenkins Server URL<br>Example : http://localhost:8080");
        sslVerify.setToolTipText("Check this box, if you are facing issues with validating over https server url<br>Note : This is unsafe and not recommended");

        userNameLabel.setLabelFor(userNameTextField);
        tokenLabel.setLabelFor(apiTokenTextField);
        urlLabel.setLabelFor(jenkinsUrlTextField);

        userNameTextField.setText(userName);
        apiTokenTextField.setText(apiToken);
        jenkinsUrlTextField.setText(jenkinsUrl);

        sslVerify.setSelected(sslVerifyDisabled);

        labelPane.add(userNameLabel);
        labelPane.add(tokenLabel);
        labelPane.add(urlLabel);

        fieldPane.add(userNameTextField);
        fieldPane.add(apiTokenTextField);
        fieldPane.add(jenkinsUrlTextField);

        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialogPanel.add(labelPane, BorderLayout.CENTER);
        dialogPanel.add(fieldPane, BorderLayout.LINE_END);
        dialogPanel.add(sslVerify, BorderLayout.AFTER_LAST_LINE);

        return dialogPanel;
    }

    public Properties getProperties() {
        return new Properties(userNameTextField.getText(), apiTokenTextField.getText(), jenkinsUrlTextField.getText(), sslVerify.isSelected());
    }
}