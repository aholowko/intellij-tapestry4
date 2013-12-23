package ch.mjava.intellij.tapestry;

import ch.mjava.intellij.FieldChoiceDialog;
import ch.mjava.intellij.PluginHelper;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author knm
 */
public class OgnlResolver extends AnAction
{
    @Override
    public void actionPerformed(AnActionEvent e)
    {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        FileType fileType = psiFile.getFileType();
        if(fileType.getDefaultExtension().equals("html"))
        {
            String name = psiFile.getName();
            Editor editor = e.getData(PlatformDataKeys.EDITOR);
            if (editor == null) {
                PluginHelper.showErrorBalloonWith(e, "no internal editor found for " + name);
                return;
            }
            int offset = editor.getCaretModel().getOffset();
            PsiElement elementAt = psiFile.findElementAt(offset);
            String text = elementAt.getText();
            System.out.println("text = " + text); // = listener:addShippingAddress

            if(text.contains("ognl:") || text.contains("listener:"))
            {
                List<PsiFile> javaCandidates = new TapestrySwitcher().getPartnerFiles(e);
                List<PsiMethod> allFields = new ArrayList<PsiMethod>();
                String fieldName = text.replace("ognl:", "").replace("listener:", "");
                Project project = e.getProject();
                for(PsiFile javaCandidate : javaCandidates)
                {

                    PsiClass[] classes = PsiShortNamesCache.getInstance(project).getClassesByName(javaCandidate.getVirtualFile().getNameWithoutExtension(), javaCandidate.getResolveScope());

                    if(classes != null && classes.length > 0)
                    {
                        PsiClass clazz = classes[0];
                        PsiMethod[] methods = clazz.getAllMethods();
                        for(PsiMethod method : methods)
                        {
                            if(method.getName().toLowerCase().contains(fieldName.toLowerCase()))
                            {
                                allFields.add(method);
                            }
                        }
                    }
                }
                FieldChoiceDialog dlg = new FieldChoiceDialog(project, allFields.toArray(new PsiMethod[allFields.size()]));
                dlg.show();
                if (dlg.isOK()) {
                    List<PsiMethod> methods = dlg.getFields();
                    for(PsiMethod method : methods)
                    {
                        NavigationUtil.activateFileWithPsiElement(method);
                    }
                }
            }


        }
    }
}
