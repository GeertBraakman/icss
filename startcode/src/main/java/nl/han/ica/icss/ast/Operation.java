package nl.han.ica.icss.ast;

import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;

public abstract class Operation extends Expression {

    public Expression lhs;
    public Expression rhs;

    private ExpressionType expressionType;

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        if(lhs != null)
            children.add(lhs);
        if(rhs != null)
            children.add(rhs);
        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if(lhs == null) {
            lhs = (Expression) child;
        } else if(rhs == null) {
            rhs = (Expression) child;
        }
        return this;
    }

    public void setExpressionType(ExpressionType type){
        this.expressionType = type;
    }

    @Override
    public ExpressionType getExpressionType() {
        if(expressionType == null) {
            expressionType = ExpressionType.UNDEFINED;
        }
        return expressionType;
    }
}
