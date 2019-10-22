package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;

public class Generator {

    /**
     * This method will generate a string from the AST.
     *
     * @param ast The AST you want to generate, this AST needs to be valid.
     * @return A string that is formatted like a official CSS file.
     */
    public String generate(AST ast) {
        StringBuilder builder = new StringBuilder("/* This file is generated by the generator from Geert Braakman. */\n\n");
        generate(ast.root, builder);
        return builder.toString();
    }

    /**
     * This method will generate a String from a Stylesheet
     *
     * @param stylesheet The stylesheet you want to be generate.
     * @param builder    The builder that is going to be filled with the generated CSS.
     */
    private void generate(Stylesheet stylesheet, StringBuilder builder) {
        for (ASTNode child : stylesheet.getChildren()) {
            if (child instanceof Stylerule) {
                generate((Stylerule) child, builder);
            }
        }
    }

    /**
     * This methode will generate a String from a Stylerule.
     *
     * @param stylerule The Stylerule you want to be generated.
     * @param builder   The builder that is going to be filled with the generated CSS
     */
    private void generate(Stylerule stylerule, StringBuilder builder) {
        for (int i = 0; i < stylerule.selectors.size(); i++) {
            builder.append(stylerule.selectors.get(i));
            if (i < stylerule.selectors.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(" {\n");
        for (ASTNode node : stylerule.body) {
            if (node instanceof Declaration) {
                Declaration declaration = (Declaration) node;
                builder.append("\t").append(declaration.property.name).append(": ").append(declaration.expression).append(";\n");
            }
        }
        builder.append("}\n\n");
    }
}
