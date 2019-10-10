package nl.han.ica.icss.parser;

import java.util.Stack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
	private Stack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		currentContainer = new Stack<>();
	}
    public AST getAST() {
        return ast;
    }

	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		currentContainer.push(new Stylesheet());
		System.out.println("Creating stylesheet");
	}

	@Override
	public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
		TagSelector selector = new TagSelector(ctx.getText());
		currentContainer.peek().addChild(selector);
		System.out.println("Creating tag selector: " + ctx.getText() + " by " + currentContainer.peek().getNodeLabel());
	}

	@Override
	public void enterDecleration(ICSSParser.DeclerationContext ctx) {
		Declaration declaration = new Declaration(ctx.getText());
		currentContainer.peek().addChild(declaration);
		System.out.println("Creating Decleration: " + ctx.getText() + " by " + currentContainer.peek().getNodeLabel());
		currentContainer.push(declaration);
	}

	@Override
	public void exitDecleration(ICSSParser.DeclerationContext ctx) {
		currentContainer.pop();
		System.out.println("Removing Decleration " + ctx.getText() + " from the stack");

	}

	@Override
	public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
		ColorLiteral colorLiteral = new ColorLiteral(ctx.getText());
		currentContainer.peek().addChild(colorLiteral);
		System.out.println("Creating colorLiteral: " + ctx.getText() + " by " + currentContainer.peek().getNodeLabel());

	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		ast.root = (Stylesheet) currentContainer.pop();
	}

}
