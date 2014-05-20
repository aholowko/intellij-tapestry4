package pl.holowko.intellij.tapestry.codeInsight;

import ch.mjava.intellij.PluginHelper;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;
import pl.holowko.intellij.tapestry.util.JavaPsiUtil;
import pl.holowko.intellij.tapestry.util.PsiElementFunctions;
import pl.holowko.intellij.tapestry.util.PsiElementPredicates;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static pl.holowko.intellij.tapestry.util.PsiElementFunctions.classesFromFile;
import static pl.holowko.intellij.tapestry.util.PsiElementPredicates.instanceOfTapestryComponent;
import static pl.holowko.intellij.tapestry.util.PsiFileUtils.JAVA_EXT;
import static pl.holowko.intellij.tapestry.util.PsiFileUtils.isHtmlFile;

public class JwcidGoToComponentDeclarationHandler implements GotoDeclarationHandler {

    public static final String AT = "@";

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
                .transformAndConcat(classesFromFile())
                .filter(instanceOfTapestryComponent())
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
}
