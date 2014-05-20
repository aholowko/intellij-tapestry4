package pl.holowko.intellij.tapestry.partner;

import com.intellij.psi.PsiFile;

import static pl.holowko.intellij.tapestry.util.PsiFileUtils.HTML_EXT;
import static pl.holowko.intellij.tapestry.util.PsiFileUtils.JAVA_EXT;

public class JavaPartnerClassFinder extends PartnerClassFinder {

    /*package*/ JavaPartnerClassFinder(PsiFile file) {
        super(file);
    }

    @Override
    protected String getPartnerFileName() {
        return file.getName().replace(JAVA_EXT, HTML_EXT);
    }
}
