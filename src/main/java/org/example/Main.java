package org.example;


public class Main {
    public static void main(String[] args) {
        MyParser parser = new MyParser();
        boolean parsed = parser.parse("declare w\n" +
                "declare x\n" +
                "declare y\n" +
                "set w = 5\n" +
                "set x = 10\n" +
                "set y = 15\n" +
                "calc w = x + y + 4\n" +
                "if x = y then\n" +
                "print w\n" +
                "print x\n" +
                "endif");

        if (parsed) {
            System.out.println("Abstract Syntax Tree:");
            parser.getAst().getRoot().display(); // display AST
        } else {
            System.out.println("Parsing failed."); // parsing failed
        }
    }
}