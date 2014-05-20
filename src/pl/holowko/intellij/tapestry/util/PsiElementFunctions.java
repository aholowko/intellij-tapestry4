package pl.holowko.intellij.tapestry.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.PsiShortNamesCache;

import static com.google.common.collect.Lists.newArrayList;

public final class PsiElementFunctions {

    private PsiElementFunctions() {
    }

    public static Function<PsiElement, PsiFile> fileForElement() {
        return PsiFileFromPsiElement.INSTANCE;
    }
    
    public static Function<PsiFile, Iterable<PsiClass>> classesFromFile() {
        return PsiClassesFromPsiFileFunction.INSTANCE;
    }

    private enum PsiFileFromPsiElement implements Function<PsiElement, PsiFile> {
        INSTANCE;

        @Override
        public PsiFile apply(PsiElement element) {
            return element.getContainingFile();
        }


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
}
