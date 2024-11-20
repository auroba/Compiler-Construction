package org.example;

import java.util.ArrayList;
import java.util.List;
public class AbstractSyntaxTree {
    NodeProgram root; // root node of the AST

    public NodeProgram getRoot() {
        return root;
    }

    public void setRoot(NodeProgram root) {
        this.root = root;
    }

    /**
     * abstract class NodeBase
     */
    abstract class NodeBase {
        public abstract void display(); // method to display node information
    }

    /**
     * abstract class NodeExpr
     */
    abstract class NodeExpr extends NodeBase {
    }

    /**
     * abstract class NodeStmt
     */
    abstract class NodeStmt extends NodeBase {
    }

    /**
     * Node representing an id
     */
    class NodeId extends NodeExpr {
        String name;

        public NodeId(String name) {
            this.name = name;
        }

        @Override
        public void display() {
            System.out.println("AST id " + name);
        }
    }

    /**
     * Node representing an intliteral
     */
    class NodeIntLiteral extends NodeExpr {
        int intLiteral;

        public NodeIntLiteral(int intLiteral) {
            this.intLiteral = intLiteral;
        }

        @Override
        public void display() {
            System.out.println("AST int literal " + intLiteral);
        }
    }

    /**
     * Node representing an addition expression
     */
    class NodePlus extends NodeExpr {
        NodeExpr lhs;
        NodeExpr rhs;

        public NodePlus(NodeExpr lhs, NodeExpr rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public void display() {
            System.out.println("AST sum");
            System.out.print("LHS: ");
            lhs.display();
            System.out.print("RHS: ");
            rhs.display();
        }
    }

    /**
     * Node representing a print statement
     */
    class NodePrint extends NodeStmt {
        NodeId id;

        public NodePrint(NodeId id) {
            this.id = id;
        }

        @Override
        public void display() {
            System.out.println("AST print");
            id.display();
        }
    }

    /**
     * Node representing a set statement that assigns an integer literal to a variable
     */
    class NodeSet extends NodeStmt {
        NodeId id;
        NodeIntLiteral intLiteral;

        public NodeSet(NodeId id, NodeIntLiteral intLiteral) {
            this.id = id;
            this.intLiteral = intLiteral;
        }

        @Override
        public void display() {
            System.out.println("AST set");
            id.display();
            intLiteral.display();
        }
    }

    /**
     * Node representing a calc statement that calculates an expression and assigns it to a variable
     */
    class NodeCalc extends NodeStmt {
        NodeId id;
        NodeExpr expr;

        public NodeCalc(NodeId id, NodeExpr expr) {
            this.id = id;
            this.expr = expr;
        }

        @Override
        public void display() {
            System.out.println("AST calc");
            id.display();
            expr.display();
        }
    }

    /**
     * Node representing a sequence of statements
     */
    class NodeStmts extends NodeBase {
        List<NodeStmt> stmts = new ArrayList<>();

        public void addStmt(NodeStmt stmt) {
            stmts.add(stmt);
        }

        @Override
        public void display() {
            System.out.println("AST Statements");
            for (NodeStmt stmt : stmts) {
                stmt.display();
            }
        }
    }

    /**
     * Node representing an if statement with two conditions and a sequence of statements
     */
    class NodeIf extends NodeStmt {
        NodeId lhs;
        NodeId rhs;
        NodeStmts stmts;

        public NodeIf(NodeId lhs, NodeId rhs, NodeStmts stmts) {
            this.lhs = lhs;
            this.rhs = rhs;
            this.stmts = stmts;
        }

        @Override
        public void display() {
            System.out.println("AST if");
            System.out.print("LHS: ");
            lhs.display();
            System.out.print("RHS: ");
            rhs.display();

            for (NodeStmt stmt : stmts.stmts) {
                stmt.display();
            }
            System.out.println("AST endif");
        }
    }

    /**
     * Node representing a sequence of declarations
     */
    class NodeDecls extends NodeBase {
        List<NodeId> decls = new ArrayList<>();

        public void addDecl(NodeId id) {
            decls.add(id);
        }

        @Override
        public void display() {
            System.out.println("AST Declarations");
            for (NodeId id : decls) {
                id.display();
            }
        }
    }

    /**
     * Root node representing the entire program with declarations and statements
     */
    class NodeProgram extends NodeBase {
        NodeDecls decls;
        NodeStmts stmts;

        public NodeProgram(NodeDecls decls, NodeStmts stmts) {
            this.decls = decls;
            this.stmts = stmts;
        }

        @Override
        public void display() {
            decls.display();
            stmts.display();
        }
    }
}