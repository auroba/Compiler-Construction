package org.example;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class MyParser {
    enum TYPE {INTDATATYPE} // TYPE enum

    // SymbolTableItem class with two member variables
    private class SymbolTableItem {
        String name;
        TYPE type;

        // constructor
        public SymbolTableItem(String name, TYPE type) {
            this.name = name;
            this.type = type;
        }
    }
    private Map<String, SymbolTableItem> symbolTable = new HashMap<>(); // symbol table initialized with HashMap
    MyScanner scanner; // scanner member variable to read tokens
    MyScanner.TOKEN nextToken; // member variable for the next token
    private AbstractSyntaxTree ast = new AbstractSyntaxTree();


    public AbstractSyntaxTree getAst() {
        return ast;
    }

    /**
     * Method to get the next token
     */
    private void getNextToken () {
        nextToken = scanner.scan();
    }

    /**
     * Method to match tokens and return messages
     * @param expectedToken the token the input should match
     * @return true if the tokens match, false if they do not
     */
    private boolean match (MyScanner.TOKEN expectedToken) {
        if (nextToken == expectedToken) {
            System.out.println("The tokens matched! Token: " + nextToken + ", Buffer: " + scanner.getTokenBufferString());
            getNextToken();
            return true; // returns true if tokens match
        }
        System.out.println("The tokens do not match, expected: " + expectedToken + ", received: " + nextToken);
        return false; // returns false and displays error if tokens do not match
    }

    /**
     * Method to print out parse errors and exit the program
     * @param message error message that should be displayed
     */
    private void error(String message) {
        System.out.println("Parse Error: " + message);
        System.out.println("Received: " + nextToken + ", Buffer: " + scanner.getTokenBufferString());
        System.exit(0); // exit the program
    }

    /**
     * Method to parse the program
     * @param program passed in as a String
     * @return true if parsed successfully, false if not
     */
    public boolean parse (String program) {
        StringReader stringReader = new StringReader(program); // StringReader to read the program
        PushbackReader pbr = new PushbackReader(stringReader);
        scanner = new MyScanner(pbr); // initialize scanner with pbr
        getNextToken(); // get the token
        program(); // call method to start parsing
        if (match(MyScanner.TOKEN.SCANEOF)) {
            System.out.println("parsed successfully");
            return true; // return true and message if parsed successfully
        } else {
            System.out.println("parsing failed");
        }
        return false; // return false and message if parsing failed
    }

    /**
     * Method that parses the structure of the program
     * @return root node
     */
    private AbstractSyntaxTree.NodeProgram program() {
        AbstractSyntaxTree.NodeDecls decls = declarations(); // parse declarations and store in NodeDecls
        AbstractSyntaxTree.NodeStmts stmts = statements(); // parse statements and store in NodeStmts
        if (!match(MyScanner.TOKEN.SCANEOF)) {
            error("Expected: SCANEOF");
        }
        ast.setRoot(ast.new NodeProgram(decls, stmts));  // set the root of the AST
        return ast.root;
    }

    /**
     * Method to parse a declare statement
     * @return a NodeId representing the declared variable
     */
    private AbstractSyntaxTree.NodeId declare() {
        if (!match(MyScanner.TOKEN.DECLARE)) {
            error("Expected: DECLARE");
        }
        String varName = scanner.getTokenBufferString();
        if (!match(MyScanner.TOKEN.ID)) {
            error("Expected: ID");
        }
        if (!symbolTable.containsKey(varName)) {
            symbolTable.put(varName, new SymbolTableItem(varName, TYPE.INTDATATYPE)); // add variable to the symbol table
        }
        return ast.new NodeId(varName);
    }

    /**
     * Method to parse a sequence of declarations
     * @return a NodeDecls containing all parsed declarations
     */
    private AbstractSyntaxTree.NodeDecls declarations() {
        AbstractSyntaxTree.NodeDecls decls = ast.new NodeDecls();
        while (nextToken == MyScanner.TOKEN.DECLARE) {
            AbstractSyntaxTree.NodeId id = declare();
            decls.addDecl(id);
        }
        return decls;
    }

    /**
     * Method to parse a value
     * @return a NodeExpr representing the value
     */
    private AbstractSyntaxTree.NodeExpr value() {
        if (nextToken == MyScanner.TOKEN.ID) {
            String varName = scanner.getTokenBufferString();
            match(MyScanner.TOKEN.ID);
            return ast.new NodeId(varName); // return id
        } else if (nextToken == MyScanner.TOKEN.INTLITERAL) {
            int value = Integer.parseInt(scanner.getTokenBufferString());
            match(MyScanner.TOKEN.INTLITERAL);
            return ast.new NodeIntLiteral(value); // return intliteral
        } else {
            error("Expected: ID or INTLITERAL");
            return null; // else return null
        }
    }

    /**
     * Method to parse a single statement
     * @return a NodeStmt representing the parsed statement
     */
    private AbstractSyntaxTree.NodeStmt statement() {
        if (nextToken == MyScanner.TOKEN.PRINT) {
            match(MyScanner.TOKEN.PRINT);
            String varName = scanner.getTokenBufferString();
            if (!match(MyScanner.TOKEN.ID)) {
                error("Expected: ID after PRINT");
            }
            return ast.new NodePrint(ast.new NodeId(varName));
        }
        if (nextToken == MyScanner.TOKEN.SET) {
            match(MyScanner.TOKEN.SET);
            String varName = scanner.getTokenBufferString();
            if (!match(MyScanner.TOKEN.ID)) {
                error("Expected: ID after SET");
            }
            if (!match(MyScanner.TOKEN.EQUALS)) {
                error("Expected: EQUALS");
            }
            if (nextToken != MyScanner.TOKEN.INTLITERAL) {
                error("Expected: INTLITERAL after EQUALS");
            }
            int value = Integer.parseInt(scanner.getTokenBufferString());
            match(MyScanner.TOKEN.INTLITERAL);
            return ast.new NodeSet(ast.new NodeId(varName), ast.new NodeIntLiteral(value));
        }
        if (nextToken == MyScanner.TOKEN.IF) {
            match(MyScanner.TOKEN.IF);
            String lhsName = scanner.getTokenBufferString();
            if (!match(MyScanner.TOKEN.ID)) {
                error("Expected: ID after IF");
            }
            if (!match(MyScanner.TOKEN.EQUALS)) {
                error("Expected: EQUALS after ID in IF");
            }
            String rhsName = scanner.getTokenBufferString();
            if (!match(MyScanner.TOKEN.ID)) {
                error("Expected: ID after EQUALS in IF");
            }
            if (!match(MyScanner.TOKEN.THEN)) {
                error("Expected: THEN");
            }
            AbstractSyntaxTree.NodeStmts stmts = statements();
            if (!match(MyScanner.TOKEN.ENDIF)) {
                error("Expected: ENDIF");
            }
            return ast.new NodeIf(ast.new NodeId(lhsName), ast.new NodeId(rhsName), stmts);
        }
        if (nextToken == MyScanner.TOKEN.CALC) {
            match(MyScanner.TOKEN.CALC);
            String varName = scanner.getTokenBufferString();
            if (!match(MyScanner.TOKEN.ID)) {
                error("Expected: ID after CALC");
            }
            if (!match(MyScanner.TOKEN.EQUALS)) {
                error("Expected: EQUALS after ID in CALC");
            }
            AbstractSyntaxTree.NodeExpr expr = sum();
            return ast.new NodeCalc(ast.new NodeId(varName), expr);
        }
        error("Invalid statement");
        return null;
    }

    /**
     * Method to parse multiple statements in sequence
     * @return a NodeStmts containing all parsed statements
     */
    private AbstractSyntaxTree.NodeStmts statements() {
        AbstractSyntaxTree.NodeStmts stmts = ast.new NodeStmts();

        while (nextToken == MyScanner.TOKEN.PRINT || nextToken == MyScanner.TOKEN.SET ||
                nextToken == MyScanner.TOKEN.IF || nextToken == MyScanner.TOKEN.CALC) {
            AbstractSyntaxTree.NodeStmt stmt = statement(); // Parse a single statement
            stmts.addStmt(stmt); // Add the statement to NodeStmts
        }
        // Check if there’s an unexpected token after statements
        if (nextToken != MyScanner.TOKEN.ENDIF && nextToken != MyScanner.TOKEN.SCANEOF) {
            error("Expected: Statements or ENDIF or SCANEOF");
        }
        return stmts;
    }

    /**
     * Method to parse the remainder of a sum expression recursively
     * @param left the left operand of the expression
     * @return a NodeExpr representing the completed sum expression
     */
    private AbstractSyntaxTree.NodeExpr sumEnd(AbstractSyntaxTree.NodeExpr left) {
        if (nextToken == MyScanner.TOKEN.PLUS) {
            match(MyScanner.TOKEN.PLUS);
            AbstractSyntaxTree.NodeExpr right = value();
            right = sumEnd(right);
            return ast.new NodePlus(left, right);
        }
        return left;
    }

    /**
     * Method to parse a sum expression
     * @return a NodeExpr representing the parsed sum expression
     */
    private AbstractSyntaxTree.NodeExpr sum() {
        AbstractSyntaxTree.NodeExpr expr = value();
        return sumEnd(expr);
    }
} //end of MyParser


