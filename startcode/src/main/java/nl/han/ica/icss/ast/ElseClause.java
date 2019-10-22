package nl.han.ica.icss.ast;

import java.util.ArrayList;
import java.util.Objects;

public class ElseClause extends ASTNode {

    public ArrayList<ASTNode> body = new ArrayList<>();

    public ElseClause() { }

    public ElseClause(Expression conditionalExpression, ArrayList<ASTNode> body) {
        this.body = body;
    }

    @Override
    public String getNodeLabel() {
        return "Else_Clause";
    }
    @Override
    public ArrayList<ASTNode> getChildren() {
        return new ArrayList<>(body);
    }

    @Override
    public ASTNode addChild(ASTNode child) {
            body.add(child);
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ElseClause elseClause = (ElseClause) o;
        return Objects.equals(body, elseClause.body);
    }

    @Override
    public ASTNode removeChild(ASTNode child) {
        body.remove(child);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }

}
