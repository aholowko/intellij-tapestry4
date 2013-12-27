package ch.mjava.intellij.tapestry;

import ch.mjava.intellij.FieldChoiceDialog;
import ch.mjava.intellij.PluginHelper;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiShortNamesCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2013 http://www.mjava.ch
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
            if(editor == null)
            {
                PluginHelper.showErrorBalloonWith("no internal editor found for " + name, e.getDataContext());
                return;
            }
            int offset = editor.getCaretModel().getOffset();
            PsiElement elementAt = psiFile.findElementAt(offset);
            String text = elementAt.getText();

            if(text.contains("ognl:") || text.contains("listener:"))
            {
                String ognlExpression = text.replace("ognl:", "").replace("listener:", "");
                List<PsiMethod> allFields = getMethodsCandidatesFrom(psiFile, ognlExpression);
                FieldChoiceDialog dlg = new FieldChoiceDialog(psiFile.getProject(), allFields.toArray(new PsiMethod[allFields.size()]));
                dlg.show();
                if(dlg.isOK())
                {
                    List<PsiMethod> methods = dlg.getFields();
                    for(PsiMethod method : methods)
                    {
                        NavigationUtil.activateFileWithPsiElement(method);
                    }
                }
            }


        }
    }

    public static String cleanOgnlExpression(String ognlExpression)
    {
        return ognlExpression.replace("ognl:", "").replace("listener:", "")
                .replace("(", "")
                .replace(")", "")
                .replace("!", "");
    }

    public static List<PsiMethod> getMethodsCandidatesFrom(PsiFile psiFile, String ognlExpression)
    {
        List<PsiFile> javaCandidates = TapestrySwitcher.getPartnerFiles(psiFile);
        List<PsiMethod> allFields = new ArrayList<PsiMethod>();
        Project project = psiFile.getProject();
        PsiShortNamesCache psiShortNamesCache = PsiShortNamesCache.getInstance(project);
        for(PsiFile javaCandidate : javaCandidates)
        {
            PsiClass[] classes = psiShortNamesCache.getClassesByName(javaCandidate.getVirtualFile().getNameWithoutExtension(), javaCandidate.getResolveScope());

            if(classes.length > 0)
            {
                for(PsiClass aClass : classes)
                {
                    PsiMethod[] methods = aClass.getAllMethods();
                    for(PsiMethod method : methods)
                    {
                        if(methodNameMatches(ognlExpression, method))
                        {
                            allFields.add(method);
                        }
                    }
                }
            }
        }
        return allFields;
    }

    public static boolean methodNameMatches(String fieldName, PsiMethod method)
    {
        // TODO: we could use a proper ognl parsing here!
        String lowerFieldName = fieldName.toLowerCase()
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("!", "");
        String methodName = method.getName().toLowerCase();
        return methodName.contains(lowerFieldName);
    }
}
