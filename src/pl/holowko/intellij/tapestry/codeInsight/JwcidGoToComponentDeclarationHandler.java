package pl.holowko.intellij.tapestry.codeInsight;

import ch.mjava.intellij.PluginHelper;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;
import org.jetbrains.annotations.Nullable;
import pl.holowko.intellij.tapestry.util.JavaPsiUtil;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static pl.holowko.intellij.tapestry.util.PsiFileUtils.JAVA_EXT;
import static pl.holowko.intellij.tapestry.util.PsiFileUtils.isHtmlFile;

public class JwcidGoToComponentDeclarationHandler implements GotoDeclarationHandler {

    public static final String AT = "@";
    public static final String TAPESTRY_COMPONENT_CLASS_NAME = "org.apache.tapestry.internal.Component";

    @Override
    public PsiElement[] getGotoDeclarationTargets(PsiElement psiElement, int i, Editor editor) {
        PsiFile containingFile = psiElement.getContainingFile();
        if (isHtmlFile(containingFile) && isJwcid(psiElement)) {
            String componentName = getComponentName(psiElement.getText());
            List<PsiClass> psiClassList = findComponentClass(componentName, psiElement.getProject());
            return psiClassList.toArray(new PsiElement[psiClassList.size()]);
        }
        return new PsiElement[0];
    }

    private List<PsiClass> findComponentClass(String componentName, Project project) {
        List<PsiFile> psiFiles = PluginHelper.searchFiles(componentName, project);
        return FluentIterable.from(psiFiles)
                .transformAndConcat(PsiClassesFromPsiFileFunction.INSTANCE).
                filter(TapestryComponentClassesPredicate.INSTANCE)
                .toList();
    }

    private String getComponentName(String jwcid) {
        int atIndex = jwcid.indexOf(AT);
        return jwcid.substring(atIndex + 1) + JAVA_EXT;
    }

    private boolean isJwcid(PsiElement psiElement) {
        String text = psiElement.getText();
        return text.contains(AT);
    }
    
    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        return null;
    }
    
    private enum PsiClassesFromPsiFileFunction implements Function<PsiFile, Iterable<PsiClass>> {
        INSTANCE;

        @Override
        public Iterable<PsiClass> apply(PsiFile file) {
            PsiShortNamesCache psiShortNamesCache = PsiShortNamesCache.getInstance(file.getProject());
                PsiClass[] classes = psiShortNamesCache.getClassesByName(file.getVirtualFile().getNameWithoutExtension(), file.getResolveScope());
                return newArrayList(classes);
        }

    }
    
    private enum TapestryComponentClassesPredicate implements Predicate<PsiClass> {
        INSTANCE;

        @Override
        public boolean apply(PsiClass psiClass) {
            return JavaPsiUtil.isInstanceOf(psiClass, TAPESTRY_COMPONENT_CLASS_NAME);
        }

    }
}
