# Compiler Construction Project

## Overview
This project is part of a Compiler Construction and Design course. The aim is to build a compiler in stages over several assignments. The compiler processes a custom programming language and includes:
- **Scanner:** Tokenizes the input source code.
- **Parser:** Implements recursive descent parsing to validate the language's grammar.
- **Abstract Syntax Tree (AST):** Represents the syntactic structure of the program in a tree format.

Future assignments will extend this project with additional features such as semantic analysis and code generation.

## Features
1. **Scanner:**
   - Recognizes tokens such as `DECLARE`, `SET`, `PRINT`, `IF`, and `CALC`.
   - Handles integer literals, identifiers, and reserved words.
2. **Parser:**
   - Implements recursive descent parsing for program structure validation.
   - Constructs meaningful error messages for invalid syntax.
3. **Abstract Syntax Tree (AST):**
   - Represents declarations, statements, and expressions in a tree structure.
   - Outputs a depth-first traversal display for debugging and analysis.
