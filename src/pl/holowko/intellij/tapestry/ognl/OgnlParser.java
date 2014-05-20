package pl.holowko.intellij.tapestry.ognl;

import com.google.common.collect.ImmutableList;
import ognl.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class OgnlParser {

    private static final String OGNL_PREFIX = "ognl:";
    private static final String LISTENER_PREFIX = "listener:";
    public static final String ACTION_PREFIX = "action:";

    private String expression;

    public OgnlParser(String expression) {
        checkState(isOgnlExpression(expression), "Expression %s is not valid OGNL expression", expression);
        this.expression = expression.replace(OGNL_PREFIX, "").replace(LISTENER_PREFIX, "").replace(ACTION_PREFIX, "");
    }

    public List<OgnlNode> findNodes() {
        ImmutableList.Builder<OgnlNode> builder = ImmutableList.builder();
        try {
            Node node = (Node) Ognl.parseExpression(expression);
            builder.addAll(createNodes(node));
        } catch (OgnlException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }
    
    public static boolean isOgnlExpression(String text) {
        return text.startsWith(OGNL_PREFIX) || text.startsWith(LISTENER_PREFIX) || text.startsWith(ACTION_PREFIX);
    }

    private List<OgnlNode> createNodes(Node node) {
        ImmutableList.Builder<OgnlNode> builder = ImmutableList.builder();
        List<Node> leafNodes = getLeafNodes(node);
        for (Node leafNode : leafNodes) {
            if (leafNode instanceof ASTConst) {
                builder.add(new OgnlConstNode(((ASTConst) leafNode)));
            } else if (leafNode instanceof ASTMethod) {
                builder.add(new OgnlMethodNode((ASTMethod) leafNode));
            }
        }
        return builder.build();
    }
    
    private List<Node> getLeafNodes(Node node) {
        ImmutableList.Builder<Node> builder = ImmutableList.builder();
        if (node instanceof ASTChain) {
            //get first node
            builder.addAll(getLeafNodes(node.jjtGetChild(0)));
        } else {
            if (hasChildren(node)) {
                int numChildren = node.jjtGetNumChildren();
                for (int i = 0; i < numChildren; i++) {
                    Node child = node.jjtGetChild(i);
                    List<Node> leafNodes = getLeafNodes(child);
                    builder.addAll(leafNodes);
                }
            } else {
                builder.add(node);
            }
        }
        return builder.build();
    }
    
    private boolean hasChildren(Node node) {
        return node.jjtGetNumChildren() > 0;
    }
}
