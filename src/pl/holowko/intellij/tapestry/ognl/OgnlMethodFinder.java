package pl.holowko.intellij.tapestry.ognl;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.PsiShortNamesCache;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Iterables.any;

public class OgnlMethodFinder {
    
    final private PsiFile file;
    final private OgnlParser ognlParser;
    final private List<OgnlNode> ognlNodes;

    public OgnlMethodFinder(PsiFile file, String expression) {
        checkState(file.getFileType().equals(StdFileTypes.JAVA));
        
        this.file = file;
        this.ognlParser = new OgnlParser(expression);
        this.ognlNodes = ognlParser.findNodes();
    }
    
    public List<PsiMethod> find() {
        ImmutableList.Builder<PsiMethod> builder = ImmutableList.builder();

        PsiShortNamesCache psiShortNamesCache = PsiShortNamesCache.getInstance(file.getProject());
        PsiClass[] classes = psiShortNamesCache.getClassesByName(file.getVirtualFile().getNameWithoutExtension(), file.getResolveScope());

        for (PsiClass aClass : classes) {
            PsiMethod[] methods = aClass.getAllMethods();
            for (final PsiMethod method : methods) {
                boolean anyMatches = any(ognlNodes, new Predicate<OgnlNode>() {
                    @Override
                    public boolean apply(OgnlNode ognlNode) {
                        return ognlNode.matches(method);
                    }
                });
                
                if (anyMatches) {
                    builder.add(method);
                }
            }
        }
        
        return builder.build();
    }
    
}
