package pl.holowko.intellij.tapestry.partner;

import ch.mjava.intellij.PluginHelper;
import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiFile;
import pl.holowko.intellij.tapestry.util.PsiFileUtils;

import java.util.List;

public abstract class PartnerClassFinder {

    protected PsiFile file;

    protected PartnerClassFinder(PsiFile file) {
        this.file = file;
    }
    
    public static PartnerClassFinder forFile(PsiFile file) {
        if (PsiFileUtils.isJavaFile(file)) {
            return new JavaPartnerClassFinder(file);
        } else {
            return new HtmlPartnerClassFinder(file);
        }
    }
    
    public List<PsiFile> find() {
        String partnerFileName = getPartnerFileName();
        List<PsiFile> files = PluginHelper.searchFiles(partnerFileName, file.getProject());
        return ImmutableList.copyOf(files);
    }
    
    protected abstract String getPartnerFileName();

}
