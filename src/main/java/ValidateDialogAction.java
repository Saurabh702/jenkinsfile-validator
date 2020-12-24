import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;

public class ValidateDialogAction extends AnAction {

    public ValidateDialogAction() {
        super();
    }

    public ValidateDialogAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project currentProject = event.getProject();
        String message = "";

        Properties properties = PluginProperties.getInstance().getValues();
        if (properties.isEmpty()) {
            new SettingsAction().actionPerformed(event);
        } else {
            HttpClient httpClient = new HttpClient(properties);

            VirtualFile currentFile = getCurrentFile(currentProject);
            String fileName;
            if (currentFile != null) {
                fileName = currentFile.getName();
                if (fileName.toLowerCase().contains("jenkinsfile")) {
                    try {
                        Document document = FileDocumentManager.getInstance().getDocument(currentFile);
                        if (document != null) {
                            message = httpClient.getResponse(document.getText());
                        } else {
                            message = "Unable to get contents of the file";
                        }
                    } catch (Exception e) {
                        message = "Validation failed";
                        e.printStackTrace();
                    }
                }
            }
            Messages.showMessageDialog(currentProject, message, "Jenkinsfile Validation", Messages.getInformationIcon());
        }
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        VirtualFile currentFile = getCurrentFile(e.getProject());
        if (currentFile != null) {
            String fileName = currentFile.getName();
            e.getPresentation().setEnabledAndVisible(fileName.toLowerCase().contains("jenkinsfile"));
        }
        // ~/.config/options/other.xml path where properties are stored
    }

    public VirtualFile getCurrentFile(Project project) {
        if (project != null) {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
            Editor editor = fileEditorManager.getSelectedTextEditor();
            if (editor != null) {
                Document currentDoc = editor.getDocument();
                return FileDocumentManager.getInstance().getFile(currentDoc);
            }
        }
        return null;
    }
}