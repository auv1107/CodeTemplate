package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiDirectory;
import utils.FileUtils;

import javax.swing.*;

public class Gogogo extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PsiDirectory directory = e.getData(LangDataKeys.IDE_VIEW).getOrChooseDirectory();
        String path = directory.getVirtualFile().getPath();
        String packageName = getPackagePath(path);
        String prefix = JOptionPane.showInputDialog("输入前缀");

        readAndWrite("/mvp/Model.txt", "model", path, packageName, prefix + "Model.kt", prefix);
        readAndWrite("/mvp/Presenter.txt", "presenter", path, packageName, prefix + "Presenter.kt", prefix);
        readAndWrite("/mvp/View.txt", "view", path, packageName, prefix + "View.kt", prefix);

        refreshProject(e);
    }

    private String getPackagePath(String filePath) {
        return filePath.split("src/main/java/", 2)[1] // com/puke/template/test/A.java
                .replace(".java", "") // com/puke/template/test/A
                .replace('/', '.'); // com.puke.template.test.A
    }

    /**
     * 刷新项目
     *
     * @param e
     */
    private void refreshProject(AnActionEvent e) {
        e.getProject().getBaseDir().refresh(false, true);
    }

    private void readAndWrite(String templatePath, String subPath, String currentPath, String packageName, String fileName, String prefix) {
        String content = FileUtils.readFile(this, templatePath);
        content = content.replaceAll("\\$package\\$", packageName);
        content = content.replaceAll("\\$prefix\\$", prefix);
        FileUtils.writeToFile(content, currentPath + "/" + subPath, fileName);
    }
}
