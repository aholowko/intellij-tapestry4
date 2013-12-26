package ch.mjava.intellij.tapestry;

import ch.mjava.intellij.PluginHelper;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class TapestrySwitcher extends AnAction
{
    public void actionPerformed(AnActionEvent e)
    {
        List<PsiFile> files = getPartnerFiles(e.getData(LangDataKeys.PSI_FILE));

        if(files.isEmpty())
        {
            PluginHelper.showErrorBalloonWith("No partner file found", e.getDataContext());
        } else
        {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(e.getProject());
            for(PsiFile file : files)
            {
                fileEditorManager.openFile(file.getVirtualFile(), true, true);
            }
        }
    }

    public static List<PsiFile> getPartnerFiles(PsiFile psiFile)
    {
        String name = psiFile.getName();
        String partnerFile;
        if(name.endsWith("java"))
            partnerFile = name.replace(".java", ".html");
        else
            partnerFile = name.replace(".html", ".java");

        ArrayList<PsiFile> files = PluginHelper.searchFiles(partnerFile, psiFile.getProject());
        ArrayList<PsiFile> result = new ArrayList<PsiFile>();


        if(files.isEmpty() && name.endsWith("html"))
        {
            result.addAll(getCandidatesFromJWCFiles(name, psiFile.getProject()));
        } else
        {
            for(PsiFile file : files)
            {
                if(isAPartnerCandidate(psiFile, file))
                {
                    result.add(file);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    private static List<PsiFile> getCandidatesFromJWCFiles(String name, Project project)
    {
        String jwcFileName = name.replace(".html", ".jwc");
        ArrayList<PsiFile> psiFiles = PluginHelper.searchFiles(jwcFileName, project);
        if(!psiFiles.isEmpty())
        {
            // looking for files inside jwc files
            PsiFile jwcCandidate = psiFiles.get(0);
            VirtualFile jwcFile = jwcCandidate.getVirtualFile();
            try
            {
                String content = new String(jwcFile.contentsToByteArray(), "UTF-8");
                Pattern pattern = Pattern.compile(".*class=\"(.*?)\"");
                Matcher matcher = pattern.matcher(content);
                if(matcher.find())
                {
                    String fqClassName = matcher.group(1);
                    String javaName = fqClassName.substring(fqClassName.lastIndexOf(".") + 1);
                    ArrayList<PsiFile> javaCandidates = PluginHelper.searchFiles(javaName + ".java", project);
                    return javaCandidates;
                }
            }
            catch(IOException e1)
            {
                e1.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    private static boolean isAPartnerCandidate(PsiFile psiFile, PsiFile file)
    {
        return file.getParent().getName().equals(psiFile.getParent().getName());
    }


}
