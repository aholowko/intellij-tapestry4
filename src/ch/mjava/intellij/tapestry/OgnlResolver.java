package ch.mjava.intellij.tapestry;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.PsiShortNamesCache;
import pl.holowko.intellij.tapestry.partner.PartnerClassFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2013 http://www.mjava.ch
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author knm
 */
public class OgnlResolver {


    public static boolean isOgnlExpression(String text) {
        return text.contains("ognl:") || text.contains("listener:") || text.contains("action:");
    }

    public static List<PsiMethod> getMethodsCandidatesFrom(PsiFile psiFile, String ognlExpression) {
        List<PsiFile> javaCandidates = PartnerClassFinder.forFile(psiFile).find();
        List<PsiMethod> allFields = new ArrayList<PsiMethod>();
        Project project = psiFile.getProject();
        PsiShortNamesCache psiShortNamesCache = PsiShortNamesCache.getInstance(project);
        for (PsiFile javaCandidate : javaCandidates) {
            PsiClass[] classes = psiShortNamesCache.getClassesByName(javaCandidate.getVirtualFile().getNameWithoutExtension(), javaCandidate.getResolveScope());

            if (classes.length > 0) {
                for (PsiClass aClass : classes) {
                    PsiMethod[] methods = aClass.getAllMethods();
                    for (PsiMethod method : methods) {
                        if (methodNameMatches(ognlExpression, method)) {
                            allFields.add(method);
                        }
                    }
                }
            }
        }
        return allFields;
    }

    public static boolean methodNameMatches(String fieldName, PsiMethod method) {
        // TODO: we could use a proper ognl parsing here!
        String lowerFieldName = fieldName.toLowerCase()
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("!", "");
        String methodName = method.getName().toLowerCase();
        return methodName.contains(lowerFieldName);
    }

    /**
     * could use a model object instead of string[] here but waiting for proper ognl parsing
     */
    public static String[] separateOgnlExpression(String expression) {
        if (!isOgnlExpression(expression)) {
            return new String[]{expression};
        }

        String[] keeper = expression.split(":");
        String tail = keeper.length < 2 ? "" : keeper[1];
        return new String[]{keeper[0] + ":", tail};
    }
}
