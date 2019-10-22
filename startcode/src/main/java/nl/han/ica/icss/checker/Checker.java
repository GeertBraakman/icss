package nl.han.ica.icss.checker;

import java.util.HashMap;
import java.util.LinkedList;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.types.*;

public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        check(ast.root);
    }

    private void check(ASTNode root) {
        checkRuleUndefinedVariable(root);
        checkRuleOperations(root);
        checkRuleValidDeclarations(root);
        checkRuleValidIfDeclaration(root);

        HashMap<String, ExpressionType> map = new HashMap<>();
        for (ASTNode child : root.getChildren()) {
            if (child instanceof VariableAssignment) {
                VariableAssignment variableAssignment = (VariableAssignment) child;
                VariableReference variableReference = variableAssignment.name;
                variableReference.setExpressionType(getExpressionType(variableAssignment.expression));
                map.put(variableReference.name, variableReference.getExpressionType());
            }
            variableTypes.addLast(map);
            check(child);
            variableTypes.removeLast();
        }
    }

    /**
     * Checks if a variable is found in the scope where it's used.
     *
     * @param node the node that you want to check.
     */
    private void checkRuleUndefinedVariable(ASTNode node) {
        if (node instanceof VariableReference) {
            VariableReference variableReference = (VariableReference) node;

            if (findExpressionType(variableReference) == null) {
                node.setError("Variable reference '" + variableReference.name + "' not found.");
            }
        }
    }

    /**
     * Checks if the operation is correct.
     * - COLOR is not allowed to be used in operations.
     * - BOOL is not allowed to be used in operations.
     * - Multiply can only be done with at least 1 SCALAR.
     * - Subtract or Add can only be used on the same Expression Types.
     *
     * @param node the node that you want to check.
     */
    private void checkRuleOperations(ASTNode node) {
        if (node instanceof Operation) {
            Operation operation = (Operation) node;
            if (operation.getExpressionType() == ExpressionType.UNDEFINED) {
                getOperationType(operation);
            }
        }
    }

    /**
     * Checks if a declaration is correct.
     * - color & background-color can only use a COLOR,
     * - width & height can only use PIXEL or PERCENTAGE.
     * - a declaration can only be for color, background-color, width or height.
     *
     * @param node The node you want to check.
     */
    private void checkRuleValidDeclarations(ASTNode node) {
        if (node instanceof Declaration) {
            Declaration declaration = (Declaration) node;
            ExpressionType expressionType = declaration.expression.getExpressionType();

            if (expressionType == ExpressionType.UNDEFINED) {
                expressionType = getExpressionType(declaration.expression);
            }

            String propertyName = declaration.property.name;


            if (propertyName.equals("color") || propertyName.equals("background-color")) {
                if (!(expressionType == ExpressionType.COLOR)) {
                    declaration.expression.setError("Declarations for '" + propertyName + "' can only use COLOR Literals.");
                }
            } else if (propertyName.equals("width") || propertyName.equals("height")) {
                if (expressionType == ExpressionType.COLOR || expressionType == ExpressionType.SCALAR) {
                    declaration.expression.setError("Declarations for '" + propertyName + "' can only use PIXEL or PERCENTAGE Literals.");
                }
            } else {
                declaration.property.setError("Property '" + propertyName + "' is not a valid property.");
            }
        }
    }

    /**
     * Check if an if declaration is correct.
     * - You can only use a BOOLEAN literal or a variable with the ExpresionType BOOLEAN
     *
     * @param node
     */
    private void checkRuleValidIfDeclaration(ASTNode node) {
        if (node instanceof IfClause) {
            IfClause ifClause = (IfClause) node;
            ExpressionType expressionType = ifClause.conditionalExpression.getExpressionType();
            if (expressionType == ExpressionType.UNDEFINED) {
                expressionType = getExpressionType(ifClause.conditionalExpression);
            }

            if (expressionType != ExpressionType.BOOL) {
                ifClause.conditionalExpression.setError("You can only use a BOOLEAN expression to define an ifClause.");
            }
        }
    }

    private ExpressionType getExpressionType(Expression expression) {
        ExpressionType type = expression.getExpressionType();

        if (type == ExpressionType.UNDEFINED || type == null) {
            if (expression instanceof VariableReference) {
                VariableReference variableReference = (VariableReference) expression;
                type = findExpressionType(variableReference);
                variableReference.setExpressionType(type);
                return type;
            }
            if (expression instanceof Operation) {
                Operation operation = (Operation) expression;
                type = getOperationType(operation);
                operation.setExpressionType(type);
                return type;
            }
        }

        return type;
    }

    private ExpressionType findExpressionType(VariableReference variableReference) {
        for (HashMap<String, ExpressionType> map : variableTypes) {
            if (map.containsKey(variableReference.name)) {
                return map.get(variableReference.name);
            }
        }
        return null;
    }

    private ExpressionType getOperationType(Operation operation) {
        ExpressionType type = operation.getExpressionType();
        if (type != ExpressionType.UNDEFINED) {
            return type;
        }

        ExpressionType lhsType = operation.lhs.getExpressionType();

        if (lhsType == ExpressionType.UNDEFINED) {
            lhsType = getExpressionType(operation.lhs);
        }

        ExpressionType rhsType = operation.rhs.getExpressionType();

        if (rhsType == ExpressionType.UNDEFINED) {
            rhsType = getExpressionType(operation.rhs);
        }


        // Checks if the operation uses Colors.
        if (rhsType == ExpressionType.COLOR || lhsType == ExpressionType.COLOR) {
            operation.setError("Calculation with colors is not allowed.");
            return ExpressionType.COLOR;
        }

        if (rhsType == ExpressionType.BOOL || lhsType == ExpressionType.BOOL) {
            operation.setError("Calculation with colors is not allowed");
            return ExpressionType.BOOL;
        }

        // Checks if a multiply operation is done with a scalar.
        if (operation instanceof MultiplyOperation) {
            if (lhsType == ExpressionType.SCALAR) {
                operation.setExpressionType(rhsType);
                return rhsType;
            } else if (rhsType == ExpressionType.SCALAR) {
                operation.setExpressionType(lhsType);
                return lhsType;
            } else {
                operation.setError("A multiply operation should contain at least 1 SCALAR.");
                return ExpressionType.UNDEFINED;
            }
        }

        // Check if a Subtract or Add operation is done by the same type.
        if (rhsType.equals(lhsType)) {
            return rhsType;
        }

        operation.setError("You can only subtract or add the same literal type.");
        return type;
    }


}
