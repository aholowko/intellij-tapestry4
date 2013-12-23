package ch.mjava.intellij.tapestry;

import ch.mjava.intellij.PluginHelper;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author knm
 */
public class TapestrySwitcher extends AnAction
{
    public void actionPerformed(AnActionEvent e)
    {
        List<PsiFile> files = getPartnerFiles(e);

        if(files.isEmpty())
        {
            PluginHelper.showErrorBalloonWith(e, "No partner file found");
        }
        else
        {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(e.getProject());
            for(PsiFile file : files)
            {
                fileEditorManager.openFile(file.getVirtualFile(), true, true);
            }
        }
    }

    public List<PsiFile> getPartnerFiles(AnActionEvent e)
    {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        String name = psiFile.getName();
        String partnerFile;
        if(name.endsWith("java"))
            partnerFile = name.replace(".java", ".html");
        else
            partnerFile = name.replace(".html", ".java");

        ArrayList<PsiFile> files = PluginHelper.searchFiles(e, partnerFile);
        ArrayList<PsiFile> result = new ArrayList<PsiFile>();


        if(files.isEmpty() && name.endsWith("html"))
        {
            result.addAll(getCandidatesFromJWCFiles(e, name));
        }
        else
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

    private List<PsiFile> getCandidatesFromJWCFiles(AnActionEvent e, String name)
    {
        String jwcFileName = name.replace(".html", ".jwc");
        ArrayList<PsiFile> psiFiles = PluginHelper.searchFiles(e, jwcFileName);
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
                    ArrayList<PsiFile> javaCandidates = PluginHelper.searchFiles(e, javaName + ".java");
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

    private boolean isAPartnerCandidate(PsiFile psiFile, PsiFile file)
    {
        return file.getParent().getName().equals(psiFile.getParent().getName());
    }


}
