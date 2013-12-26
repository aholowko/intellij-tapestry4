package ch.mjava.intellij;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
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
public class FieldChoiceDialog extends DialogWrapper
{
    private CollectionListModel<PsiMethod> myFields;
    private final LabeledComponent<JPanel> myComponent;
    private final JList fieldList;

    public FieldChoiceDialog(Project project, PsiMethod[] allFields)
    {
        super(project);
        setTitle("Select Fields");

        myFields = new CollectionListModel<PsiMethod>(allFields);
        fieldList = new JList(myFields);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        fieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        decorator.disableUpDownActions();
        decorator.disableRemoveAction();

        JPanel panel = decorator.createPanel();
        myComponent = LabeledComponent.create(panel, "Candidates");

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel()
    {
        return myComponent;
    }

    public List<PsiMethod> getFields()
    {
        Object[] selectedValues = fieldList.getSelectedValues();
        if(selectedValues.length > 0)
        {
            PsiMethod selectedValue = (PsiMethod) selectedValues[0];
            return Arrays.asList(selectedValue);
        }
        return myFields.getItems();
    }
}