package pl.holowko.intellij.tapestry.codeInsight;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nullable;
import pl.holowko.intellij.tapestry.ognl.OgnlMethodFinder;
import pl.holowko.intellij.tapestry.ognl.OgnlParser;
import pl.holowko.intellij.tapestry.partner.PartnerClassFinder;

import java.util.List;


public class OgnlGoToDeclarationHandler implements GotoDeclarationHandler {
    
    @Override
    public PsiElement[] getGotoDeclarationTargets(PsiElement psiElement, int i, Editor editor) {
        PsiFile containingFile = psiElement.getContainingFile();
        
        if (containingFile.getFileType() == StdFileTypes.HTML) {
            String text = psiElement.getText();
            
            if (OgnlParser.isOgnlExpression(text)) {
                
                PartnerClassFinder partnerClassFinder = PartnerClassFinder.forFile(containingFile);
                List<PsiFile> partnerFiles = partnerClassFinder.find();
                
                List<PsiMethod> methods = Lists.newArrayList();
                for (PsiFile partnerFile : partnerFiles) {
                    OgnlMethodFinder ognlMethodFinder = new OgnlMethodFinder(partnerFile, text);
                    methods.addAll(ognlMethodFinder.find());
                }
                return methods.toArray(new PsiElement[methods.size()]);
            }
        }

        return new PsiElement[0];
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext) {
        return null;
    }
    
}
