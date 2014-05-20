package pl.holowko.intellij.tapestry.ognl;

import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableSet;
import com.intellij.psi.PsiMethod;
import ognl.ASTConst;

import java.util.Set;

public class OgnlConstNode implements OgnlNode {

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    
    final private ASTConst property;
    final private Set<String> possibleMethodNames;
    
    /*package*/ OgnlConstNode(ASTConst property) {
        this.property = property;
        this.possibleMethodNames = ImmutableSet.of(name(), getterName(), booleanGetterName());
    }

    @Override
    public boolean matches(PsiMethod method) {
        String methodName = method.getName();
        return possibleMethodNames.contains(methodName);
    }
    
    private String name() {
        return property.toString().replaceAll("\"", "");
    }
    
    private String getterName() {
        return GET_PREFIX + firstCharToUpper(name());
    }
    
    private String booleanGetterName() {
        return IS_PREFIX + firstCharToUpper(name());
    }
    
    private static String firstCharToUpper(String word) {
    return (word.isEmpty())
        ? word
        : new StringBuilder(word.length())
            .append(Ascii.toUpperCase(word.charAt(0)))
            .append(word.substring(1))
            .toString();
  }
}
