import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        SettingsDialog dialog = new SettingsDialog(PluginProperties.getInstance().getValues());
        if (dialog.showAndGet()) {
            PluginProperties.getInstance().setValues(dialog.getProperties());
        }
    }
}
