package Compiller.Syntactic;
import java.util.List;
import Compiller.Lexic.Token;

public class Parser {
    
    private List<Token> tokens;
    private Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token getNextToken() {
        return tokens.size() > 0 ? tokens.remove(0) : null;
    }

    public void error(String rule, Token currentToken) {
        System.out.println("Error in rule " + rule + " at line " + currentToken.getLine() +
        " by lexeme " + currentToken.getLexeme() + " and type " + currentToken.getType());
    }

    // ifElse -> se '(' condition ')' '{' expression '}' cnao '{' expression '}'
    // condition -> ID operator (num | ID)
    // num -> FLOAT | INT
    // operator -> '<' | '>' | '==' | '<=' | '>=' | '!='
    // expression -> ID '=' E
    // E -> E + T | E - T | T
    // T -> T * F | T / F | F
    // F -> '(' E ')' | ID | num

    // eliminando recursão a esquerda e fatorando
    // ifElse -> se '(' condition ')' '{' expression '}' cnao '{' expression '}'
    // condition -> ID operator (num | ID)
    // num -> FLOAT | INT
    // operator -> '<' | '>' | '==' | '<=' | '>=' | '!='
    // expression -> ID '=' E
    // E -> T E'
    // E' -> + T E' | - T E' | ε
    // T -> F T'
    // T' -> * F T' | / F T' | ε
    // F -> '(' E ')' | ID | num

    public boolean ifElse() {
        if (
        matchLexeme("se") && matchLexeme("(") && condition() && matchLexeme(")")
        && matchLexeme("{") && expression() && matchLexeme("}") 
        && matchLexeme("cnao")
        && matchLexeme("{") && expression() && matchLexeme("}")) {
            return true;
        } 
        return false;
    }

    public boolean matchLexeme(String lexeme) {
        if (currentToken.getLexeme().equals(lexeme)) {
            currentToken = getNextToken();
            return true;
        }
        return false;
    }

    public boolean matchType(String type) {
        if (currentToken.getType().equals(type)) {
            currentToken = getNextToken();
            return true;
        }
        return false;
    }

    public boolean condition() {
        if ( (num() || matchType("ID")) && operator() && (num() || matchType("ID"))) {
            return true;
        }
        error("condition", currentToken);
        return false;
    }

    public boolean num() {
        if (matchType("FLOAT") || matchType("INT")) {
            return true;
        }
        return false;
    }

    public boolean operator() {
        if (matchLexeme("<") || matchLexeme(">") || matchLexeme("==") || matchLexeme("<=") || matchLexeme(">=") || matchLexeme("!=")) {
            return true;
        }
        error("operator", currentToken);
        return false;
    }

    public boolean expression() {
        if (matchType("ID") && matchLexeme("=") && (num() || matchType("ID"))) {
            return true;
        }
        error("expression", currentToken);
        return false;
    }

    public void analyze() {
        currentToken = getNextToken();
        if(ifElse()) {
            if (currentToken.getType().equals("EOF")) {
                System.out.println("Syntax is correct!");
            } else {
                error("EOF", currentToken);
            }
        }
        else {
            error("ifElse", currentToken);
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public Token getCurrentToken() {
        return currentToken;
    }
}