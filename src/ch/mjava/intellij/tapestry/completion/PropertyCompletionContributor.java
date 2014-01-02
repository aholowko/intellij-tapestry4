package ch.mjava.intellij.tapestry.completion;

import ch.mjava.intellij.tapestry.OgnlResolver;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlToken;
import org.apache.batik.util.gui.xmleditor.XMLToken;

import java.util.List;
import static ch.mjava.intellij.tapestry.OgnlResolver.isOgnlExpression;
import static ch.mjava.intellij.tapestry.OgnlResolver.separateOgnlExpression;

/**
 * @author knm
 */
public class PropertyCompletionContributor extends CompletionContributor
{
    @Override
    public void fillCompletionVariants(final CompletionParameters parameters, final CompletionResultSet result)
    {
        ApplicationManager.getApplication().runReadAction(new Runnable()
        {
            @Override
            public void run()
            {
                result.addElement(LookupElementBuilder.create("ognl:documentParXX"));
                PsiFile originalFile = parameters.getOriginalFile();
                if(originalFile.getFileType() == StdFileTypes.HTML)
                {
                    PsiElement position = parameters.getPosition();
                    if(position instanceof XmlToken)
                    {
                        IElementType tokenType = ((XmlToken) position).getTokenType();
                        if("XML_ATTRIBUTE_VALUE_TOKEN".equals(tokenType.toString()))
                        {
                            String attributeValue = (((XmlToken) position)).getText();
                            int indexOf = Math.max(0, attributeValue.indexOf(CompletionInitializationContext.DUMMY_IDENTIFIER));
                            String textBeforeCaret = attributeValue.substring(0, indexOf);

                            if(isOgnlExpression(textBeforeCaret))
                            {
                                String[] expressions = separateOgnlExpression(textBeforeCaret);
                                List<PsiMethod> methodCandidates = OgnlResolver.getMethodsCandidatesFrom(originalFile, expressions[1]);
                                for(PsiMethod candidate : methodCandidates)
                                {
                                    LookupElementBuilder element =
                                            LookupElementBuilder.create(expressions[0] + candidate.getName())
                                                    .withIcon(StdFileTypes.JAVA.getIcon())
                                                    .withTailText(" " + candidate.getContainingFile().getName(), true)
                                                    .withBoldness(true);
                                    result.addElement(element);
                                    result.stopHere();
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
