package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.*;

public class EvalExpressions implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;

    @Override
    public void apply(AST ast) {
        variableValues = new LinkedList<>();
        evalExpressions(ast.root);
    }

    private void evalExpressions(ASTNode root) {
        HashMap<String, Literal> map = new HashMap<>();
        List<ASTNode> toRemove = new ArrayList<>();
        for (ASTNode child : root.getChildren()) {
            if (child instanceof VariableAssignment) {
                VariableAssignment variableAssignment = (VariableAssignment) child;
                VariableReference variableReference = variableAssignment.name;
                map.put(variableReference.name, getLiteral(variableAssignment.expression));
                toRemove.add(child);
            }

            if (child instanceof Expression) {
                toRemove.add(child);
                root.addChild(getLiteral((Expression) child));
            }

            variableValues.addLast(map);
            evalExpressions(child);
            variableValues.removeLast();
        }

        for (ASTNode node : toRemove) {
            root.removeChild(node);
        }

    }

    private Literal getLiteral(Expression expression) {
        if (expression instanceof Literal) {
            return (Literal) expression;
        } else if (expression instanceof VariableReference) {
            VariableReference variableReference = (VariableReference) expression;
            for (int i = variableValues.size() - 1; i >= 0; i--) {
                Map<String, Literal> map = variableValues.get(i);
                if (map.containsKey(variableReference.name)) {
                    return map.get(variableReference.name);
                }
            }
        } else if (expression instanceof Operation) {
            Operation operation = (Operation) expression;
            return doOperation(operation);
        }
        return null;
    }

    private Literal doOperation(Operation operation) {
        int valueLeft = getValue(getLiteral(operation.lhs));
        int valueRight = getValue(getLiteral(operation.rhs));

        switch (operation.getExpressionType()) {
            case PIXEL:
                return new PixelLiteral(doOperation(operation, valueLeft, valueRight));
            case PERCENTAGE:
                return new PercentageLiteral(doOperation(operation, valueLeft, valueRight));
            case SCALAR:
                return new ScalarLiteral(doOperation(operation, valueLeft, valueRight));
            default:
                return null;
        }
    }

    private int doOperation(Operation operation, int left, int right) {
        if (operation instanceof AddOperation) {
            return left + right;
        } else if (operation instanceof SubtractOperation) {
            return left - right;
        } else if (operation instanceof MultiplyOperation) {
            return left * right;
        }
        return 0;
    }

    private int getValue(Literal literal) {
        if (literal instanceof PixelLiteral) {
            return ((PixelLiteral) literal).value;
        } else if (literal instanceof PercentageLiteral) {
            return ((PercentageLiteral) literal).value;
        } else if (literal instanceof ScalarLiteral) {
            return ((ScalarLiteral) literal).value;
        }
        return 0;
    }

}
