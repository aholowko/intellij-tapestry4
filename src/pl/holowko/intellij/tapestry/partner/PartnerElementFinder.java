package pl.holowko.intellij.tapestry.partner;

import ch.mjava.intellij.PluginHelper;
import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import pl.holowko.intellij.tapestry.util.PsiFileUtils;

import java.util.List;

public abstract class PartnerElementFinder {

    protected PsiFile file;

    protected PartnerElementFinder(PsiFile file) {
        this.file = file;
    }
    
    public static PartnerElementFinder forFile(PsiFile file) {
        if (PsiFileUtils.isJavaFile(file)) {
            return new JavaPartnerElementFinder(file);
        } else {
            return new HtmlPartnerElementFinder(file);
        }
    }
    
    public List<? extends PsiElement> findPartnerElements() {
        return findPartnerFiles();
    }
    
    public List<PsiFile> findPartnerFiles() {
        return getPartnerFilesByName();
    }

    protected List<PsiFile> getPartnerFilesByName() {
        String partnerFileName = getPartnerFileName();
        List<PsiFile> files = PluginHelper.searchFiles(partnerFileName, file.getProject());
        return ImmutableList.copyOf(files);
    }

    protected abstract String getPartnerFileName();

}
