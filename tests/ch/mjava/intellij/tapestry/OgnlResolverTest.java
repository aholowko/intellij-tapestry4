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

    @Test
    public void testIsOgnlExpressionForAction() throws Exception
    {
        // given
        String withAction = "action:document";

        // when
        boolean isHit = OgnlResolver.isOgnlExpression(withAction);

        // then
        assertThat(isHit, is(equalTo(true)));
    }

    @Test
    public void testIsOgnlExpressionForListener() throws Exception
    {
        // given
        String withListener = "listener:document";

        // when
        boolean isHit = OgnlResolver.isOgnlExpression(withListener);

        // then
        assertThat(isHit, is(equalTo(true)));
    }

    @Test
    public void testIsOgnlExpressionForOgnl() throws Exception
    {
        // given
        String withOgnl = "ognl:document";

        // when
        boolean isHit = OgnlResolver.isOgnlExpression(withOgnl);

        // then
        assertThat(isHit, is(equalTo(true)));
    }

    @Test
    public void testCleanReturnPrefixAndRest() throws Exception
    {
        // given
        String expression = "ognl:document";

        // when
        String[] splitted = OgnlResolver.separateOgnlExpression(expression);

        // then
        assertThat(splitted.length, is(equalTo(2)));
        assertThat(splitted[0], is(equalTo("ognl:")));
        assertThat(splitted[1], is(equalTo("document")));
    }

    @Test
    public void testCleanReturnPrefixAndRestForAction() throws Exception
    {
        // given
        String expression = "action:document";

        // when
        String[] splitted = OgnlResolver.separateOgnlExpression(expression);

        // then
        assertThat(splitted.length, is(equalTo(2)));
        assertThat(splitted[0], is(equalTo("action:")));
        assertThat(splitted[1], is(equalTo("document")));
    }

    @Test
    public void testCleanReturnPrefixAndRestForListener() throws Exception
    {
        // given
        String expression = "listener:document";

        // when
        String[] splitted = OgnlResolver.separateOgnlExpression(expression);

        // then
        assertThat(splitted.length, is(equalTo(2)));
        assertThat(splitted[0], is(equalTo("listener:")));
        assertThat(splitted[1], is(equalTo("document")));
    }

    @Test
    public void testCleanReturnPrefixAndRestHandlesNoOgnl() throws Exception
    {
        // given
        String expression = "document";

        // when
        String[] splitted = OgnlResolver.separateOgnlExpression(expression);

        // then
        assertThat(splitted.length, is(equalTo(1)));
        assertThat(splitted[0], is(equalTo("document")));
    }

    @Test
    public void testCleanReturnPrefixAndrestHandlesEmptyOgnle() throws Exception
    {
        // given
        String expression = "ognl:";

        // when
        String[] splitted = OgnlResolver.separateOgnlExpression(expression);

        // then
        assertThat(splitted.length, is(equalTo(2)));
        assertThat(splitted[0], is(equalTo("ognl:")));
        assertThat(splitted[1], is(equalTo("")));
    }
}
