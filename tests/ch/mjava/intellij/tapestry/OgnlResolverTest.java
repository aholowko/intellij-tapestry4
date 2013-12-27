package ch.mjava.intellij.tapestry;

import com.intellij.psi.PsiMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations.*;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author knm
 */
@RunWith(MockitoJUnitRunner.class)
public class OgnlResolverTest
{
    @Mock
    private PsiMethod psiMethod;

    @Test
    public void testCleanOgnlExpression() throws Exception
    {
        // given

        // when

        // then

    }

    @Test
    public void testMethodNameMatchesForSimpleGetterAndSetter() throws Exception
    {
        // given
        String methodName = "getItem";
        when(psiMethod.getName()).thenReturn(methodName);
        String ognlExpression = "item";

        // when
        boolean matches = OgnlResolver.methodNameMatches(ognlExpression, psiMethod);

        // then
        assertThat(matches, is(equalTo(true)));

        //-----------------------------
        methodName = "setItem";
        when(psiMethod.getName()).thenReturn(methodName);

        // when
        matches = OgnlResolver.methodNameMatches(ognlExpression, psiMethod);

        // then
        assertThat(matches, is(equalTo(true)));

        //-----------------------------
        methodName = "getItemDecorator";
        when(psiMethod.getName()).thenReturn(methodName);

        // when
        matches = OgnlResolver.methodNameMatches(ognlExpression, psiMethod);

        // then
        assertThat(matches, is(equalTo(true)));

        //-----------------------------
        methodName = "setItemDecorator";
        when(psiMethod.getName()).thenReturn(methodName);

        // when
        matches = OgnlResolver.methodNameMatches(ognlExpression, psiMethod);

        // then
        assertThat(matches, is(equalTo(true)));

        //-----------------------------
        methodName = "getItenfoo";
        when(psiMethod.getName()).thenReturn(methodName);

        // when
        matches = OgnlResolver.methodNameMatches(ognlExpression, psiMethod);

        // then
        assertThat(matches, is(equalTo(false)));

    }

    @Test
    public void testMethodNameMatchesForFullOgnlPath() throws Exception
    {
        // given
        String methodName = "getItem";
        when(psiMethod.getName()).thenReturn(methodName);
        String ognlExpression = "getItem()";

        // when
        boolean matches = OgnlResolver.methodNameMatches(ognlExpression, psiMethod);

        // then
        assertThat(matches, is(equalTo(true)));

        //-----------------------------
        methodName = "setItem";
        when(psiMethod.getName()).thenReturn(methodName);

        // when
        matches = OgnlResolver.methodNameMatches(ognlExpression, psiMethod);

        // then
        assertThat(matches, is(equalTo(false)));

    }
    @Test
    public void testMethodNameMatchesForBooleanInverse() throws Exception
    {
        // given
        String methodName = "getEditable";
        when(psiMethod.getName()).thenReturn(methodName);
        String ognlExpression = "!editable";

        // when
        boolean matches = OgnlResolver.methodNameMatches(ognlExpression, psiMethod);

        // then
        assertThat(matches, is(equalTo(true)));
    }
}
