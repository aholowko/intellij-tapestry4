package ch.mjava.intellij.tapestry.codeInsight;

import ch.mjava.intellij.PluginHelper;
import ch.mjava.intellij.tapestry.Constants;
import ch.mjava.intellij.tapestry.OgnlResolver;
import ch.mjava.intellij.tapestry.TapestrySwitcher;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.daemon.impl.GutterIconTooltipHelper;
import com.intellij.codeInsight.daemon.impl.PsiElementListNavigator;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
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
public class TapestryMarkupLineMarkerProvider implements LineMarkerProvider
{
    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element)
    {
        if(element instanceof XmlAttribute && ((XmlAttribute) element).getValue().startsWith("ognl"))
        {
            XmlAttribute attribute = (XmlAttribute) element;
            String methodName = OgnlResolver.cleanOgnlExpression(attribute.getValue());
            List<PsiMethod> methods = OgnlResolver.getMethodsCandidatesFrom(element.getContainingFile(), methodName);
            List<NavigatablePsiElement> linksTo = new ArrayList<NavigatablePsiElement>();
            for(PsiMethod method : methods)
            {
                linksTo.add(method);
            }

            return createNavigableLineMarker(element, linksTo);
        }
        return null;
    }

    private LineMarkerInfo createNavigableLineMarker(PsiElement element, List<NavigatablePsiElement> targets)
    {
        final NavigatablePsiElement[] links = targets.toArray(new NavigatablePsiElement[targets.size()]);
        return new LineMarkerInfo(element, element.getTextRange(), Constants.ICON_TAPESTRY, Pass.UPDATE_ALL,
                new Function<PsiElement, String>()
                {
                    @Override
                    public String fun(PsiElement o)
                    {
                        return GutterIconTooltipHelper.composeText(links, "Go to ognl reference", "");
                    }
                },
                new GutterIconNavigationHandler()
                {

                    @Override
                    public void navigate(MouseEvent mouseEvent, PsiElement psiElement)
                    {
                        if(links.length < 1)
                        {
                            DataContext tc = SimpleDataContext.getProjectContext(psiElement.getProject());
                            PluginHelper.showErrorBalloonWith("Could not found associated code. " +
                                    "Ognl expression was too hard for me", tc);
                        }
                        PsiElementListNavigator.openTargets(mouseEvent, links, "Select your target", "",
                                new DefaultPsiElementCellRenderer());
                    }
                },
                GutterIconRenderer.Alignment.LEFT
        );
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> psiElements, @NotNull Collection<LineMarkerInfo> lineMarkerInfos)
    {
    }

}
