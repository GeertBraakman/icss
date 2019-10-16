package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.ArrayList;
import java.util.List;

public class RemoveIf implements Transform {

    @Override
    public void apply(AST ast) {
        removeIf(ast.root);
    }

    private void removeIf(ASTNode root) {
//        List<ASTNode> toAdd = new ArrayList<>();
        for (ASTNode child : root.getChildren()) {
            if (child instanceof IfClause) {
//                IfClause ifClause = (IfClause) child;
//                if (getValue(ifClause.conditionalExpression)) {
//                    toAdd.addAll(ifClause.body);
//                }
                root.removeChild(child);
            }
            removeIf(child);
        }
//        for (ASTNode node: toAdd) {
//            System.out.println("adding: " + node);
//            root.addChild(node);
//        }
    }

    private boolean getValue(Expression expression) {
        if (expression instanceof BoolLiteral) {
            return ((BoolLiteral) expression).value;
        } else {
            return false;
        }
    }

}
