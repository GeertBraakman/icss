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
        List<ASTNode> toAdd = new ArrayList<>();

        for (ASTNode child : root.getChildren()) {
            if (child instanceof IfClause) {
                IfClause ifClause = (IfClause) child;
                toAdd.addAll(checkIfClause(ifClause));
                root.removeChild(ifClause);
            } else {
                removeIf(child);
            }
        }
        for (ASTNode node : toAdd) {
            root.addChild(node);
        }
    }

    private ArrayList<ASTNode> checkIfClause(IfClause ifClause) {
        ArrayList<ASTNode> astNodes = new ArrayList<>();
        if (getValue(ifClause.conditionalExpression)) {

            for (ASTNode node : ifClause.body) {
                if (node instanceof IfClause) {
                    astNodes.addAll(checkIfClause((IfClause) node));
                } else {
                    astNodes.add(node);
                }
            }
        }
        return astNodes;
    }

    private boolean getValue(Expression expression) {
        if (expression instanceof BoolLiteral) {
            return ((BoolLiteral) expression).value;
        } else {
            return false;
        }
    }

}
