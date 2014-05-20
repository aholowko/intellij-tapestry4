package pl.holowko.intellij.tapestry.partner;

import ch.mjava.intellij.PluginHelper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import pl.holowko.intellij.tapestry.util.PsiElementFunctions;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Predicates.or;
import static pl.holowko.intellij.tapestry.util.PsiElementFunctions.classesFromFile;
import static pl.holowko.intellij.tapestry.util.PsiElementPredicates.instanceOfTapestryComponent;
import static pl.holowko.intellij.tapestry.util.PsiElementPredicates.instanceOfTapestryPage;
import static pl.holowko.intellij.tapestry.util.PsiFileUtils.HTML_EXT;
import static pl.holowko.intellij.tapestry.util.PsiFileUtils.JAVA_EXT;
import static pl.holowko.intellij.tapestry.util.PsiFileUtils.JWC_EXT;

public class HtmlPartnerElementFinder extends PartnerElementFinder {

    /*package*/ HtmlPartnerElementFinder(PsiFile file) {
        super(file);
    }

    @Override
    protected String getPartnerFileName() {
        return file.getName().replace(HTML_EXT, JAVA_EXT);
    }

    @Override
    public List<? extends PsiElement> findPartnerElements() {
        List<PsiFile> partnerFiles = getPartnerFilesByName();
        if (partnerFiles.isEmpty()) {
            List<PsiFile> partnerFilesFromJwcFiles = findFromJwcFiles();
            return ImmutableList.copyOf(partnerFilesFromJwcFiles);
        } else {
            return FluentIterable.from(partnerFiles)
                    .transformAndConcat(classesFromFile())
                    .filter(or(instanceOfTapestryComponent(), instanceOfTapestryPage()))
                    .toList();
        }
    }

    @Override
    public List<PsiFile> findPartnerFiles() {
        List<? extends PsiElement> partnerElements = findPartnerElements();
        return FluentIterable.from(partnerElements)
                .transform(PsiElementFunctions.fileForElement())
                .toList();
    }

    private List<PsiFile> findFromJwcFiles() {
        String jwcFileName = file.getName().replace(HTML_EXT, JWC_EXT);
        Project project = file.getProject();
        List<PsiFile> psiFiles = PluginHelper.searchFiles(jwcFileName, project);
        if (!psiFiles.isEmpty()) {
            // looking for files inside jwc files
            PsiFile jwcCandidate = psiFiles.get(0);
            VirtualFile jwcFile = jwcCandidate.getVirtualFile();
            try {
                String content = new String(jwcFile.contentsToByteArray(), "UTF-8");
                Pattern pattern = Pattern.compile(".*class=\"(.*?)\"");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String fqClassName = matcher.group(1);
                    String javaName = fqClassName.substring(fqClassName.lastIndexOf(".") + 1);
                    return PluginHelper.searchFiles(javaName + JAVA_EXT, project);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return Collections.emptyList();
    }
}
