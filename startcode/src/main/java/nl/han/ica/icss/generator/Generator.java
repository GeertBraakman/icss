package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

public class Generator {

	public String generate(AST ast) {
        return generate(ast.root, new StringBuilder()).toString();
	}

	private StringBuilder generate(ASTNode node, StringBuilder builder) {
		for(ASTNode child: node.getChildren()) {
			if(child instanceof Stylerule) {
				generate((Stylerule) child, builder);
			} else {
				StringBuilder result = generate(child, builder);
				if(result.length() != 0){
					builder.append(result);
				}
			}
		}

		return builder;
	}

	private void generate(Stylerule stylerule, StringBuilder builder) {
		for(Selector selector: stylerule.selectors) {
			builder.append(getSelectorTag(selector)).append(" ");
		}
		builder.append("{\n");
		for(ASTNode node: stylerule.body) {
			if(node instanceof Declaration) {
				Declaration declaration = (Declaration) node;
				builder.append("\t").append(declaration.property.name).append(": ").append(getValue((Literal) declaration.expression));
				builder.append(";\n");
			}
		}
		builder.append("}\n\n");
	}

	private String getValue(Literal literal){
		if(literal instanceof PixelLiteral) {
			return String.valueOf(((PixelLiteral) literal).value);
		} else if (literal instanceof PercentageLiteral) {
			return String.valueOf(((PercentageLiteral) literal).value);
		} else if (literal instanceof ColorLiteral) {
			return String.valueOf(((ColorLiteral) literal).value);
		}
		return "null";
	}

	private String getSelectorTag(Selector selector) {
		if(selector instanceof TagSelector) {
			return ((TagSelector) selector).tag;
		} else if (selector instanceof ClassSelector) {
			return ((ClassSelector) selector).cls;
		} else if (selector instanceof IdSelector) {
			return ((IdSelector) selector).id;
		} else {
			return "null";
		}
	}

}
