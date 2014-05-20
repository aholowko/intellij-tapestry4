package pl.holowko.intellij.tapestry;

import ch.mjava.intellij.PluginHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.psi.PsiFile;
import pl.holowko.intellij.tapestry.partner.PartnerClassFinder;

import java.util.List;

public class TapestrySwitcher extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        PartnerClassFinder partnerClassFinder = PartnerClassFinder.forFile(e.getData(LangDataKeys.PSI_FILE));
        List<PsiFile> partnerFiles = partnerClassFinder.find();

        if (partnerFiles.isEmpty()) {
            PluginHelper.showErrorBalloonWith("No partner file found", e.getDataContext());
        } else {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(e.getProject());
            for (PsiFile file : partnerFiles) {
                fileEditorManager.openFile(file.getVirtualFile(), true, true);
            }
        }
    }
}
