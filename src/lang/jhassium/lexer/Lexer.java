package lang.jhassium.lexer;

import lang.jhassium.SourceLocation;
import lang.jhassium.utils.HassiumLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * File : Lexer.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 18:53
 */
public class Lexer {

    private List<Token> result;
    private int position;
    private String code;
    private SourceLocation location;

    public List<Token> Scan(String source) {
        result = new ArrayList<>();
        position = 0;
        code = source;
        location = new SourceLocation(1, 0);

        whiteSpace();
        while (position < code.length()) {
            scanToken();
        }

        return result;
    }

    private void scanToken() {
        char orig;
        if (Character.isLetter((char) peekChar()) || (char) peekChar() == '_')
            result.add(scanIdentifier());
        else if (Character.isDigit((char) peekChar()))
            result.add(scanNumber());
        else {
            switch ((char) peekChar()) {
                case '@':
                    if (peekChar() == '"') {
                        readChar();
                        result.add(scanString(true));
                    }
                    break;
                case '\"':
                    result.add(scanString(false));
                    break;
                case '\'':
                    readChar();
                    result.add(new Token(TokenType.Char, Character.toString((char) readChar()), location));
                    readChar();
                    break;
                case ';':
                    result.add(new Token(TokenType.Semicolon, ";", location));
                    readChar();
                    break;
                case ':':
                    readChar();
                    if ((char) peekChar() == ':') {
                        readChar();
                        result.add(new Token(TokenType.UnaryOperation, "::", location));
                    } else
                        result.add(new Token(TokenType.Colon, ":", location));
                    break;
                case ',':
                    result.add(new Token(TokenType.Comma, ",", location));
                    readChar();
                    break;
                case '(':
                    result.add(new Token(TokenType.LeftParentheses, "(", location));
                    readChar();
                    break;
                case ')':
                    result.add(new Token(TokenType.RightParentheses, ")", location));
                    readChar();
                    break;
                case '{':
                    result.add(new Token(TokenType.LeftBrace, "{", location));
                    readChar();
                    break;
                case '}':
                    result.add(new Token(TokenType.RightBrace, "}", location));
                    readChar();
                    break;
                case '.':
                    readChar();
                    if ((char) peekChar() == '.') {
                        result.add(new Token(TokenType.BinaryOperation, "..", location));
                        readChar();
                    } else
                        result.add(new Token(TokenType.BinaryOperation, ".", location));
                    break;
                case '?':
                    readChar();
                    if (peekChar() == '?') {
                        readChar();
                        result.add(new Token(TokenType.BinaryOperation, "??", location));
                    } else
                        result.add(new Token(TokenType.Question, "?", location));
                    break;
                case '+':
                case '-':
                    orig = (char) readChar();
                    if ((char) peekChar() == orig)
                        result.add(new Token(TokenType.UnaryOperation, Character.toString(orig) + (char) readChar(), location));
                    else if ((char) peekChar() == '=')
                        result.add(new Token(TokenType.Assignment, Character.toString(orig) + (char) readChar(), location));
                    else
                        result.add(new Token(TokenType.BinaryOperation, Character.toString(orig), location));
                    break;
                case '*':
                case '/':
                    orig = (char) readChar();
                    if ((char) peekChar() == orig)
                        result.add(new Token(TokenType.BinaryOperation, Character.toString(orig) + (char) readChar(), location));
                    else if ((char) peekChar() == '=')
                        result.add(new Token(TokenType.Assignment, Character.toString(orig) + (char) readChar(), location));
                    else
                        result.add(new Token(TokenType.BinaryOperation, Character.toString(orig), location));
                    break;
                case '%':
                case '^':
                    orig = (char) readChar();
                    if ((char) peekChar() == '=')
                        result.add(new Token(TokenType.Assignment, Character.toString(orig) + (char) readChar(), location));
                    else
                        result.add(new Token(TokenType.BinaryOperation, Character.toString(orig), location));
                    break;
                case '|':
                    readChar();
                    if ((char) peekChar() == '|') {
                        readChar();
                        result.add(new Token(TokenType.BinaryOperation, "||", location));
                    } else if ((char) peekChar() == '=') {
                        readChar();
                        result.add(new Token(TokenType.Assignment, "|=", location));
                    } else
                        result.add(new Token(TokenType.BinaryOperation, "|", location));
                    break;
                case '&':
                    readChar();
                    if ((char) peekChar() == '&') {
                        readChar();
                        result.add(new Token(TokenType.BinaryOperation, "&&", location));
                    } else if ((char) peekChar() == '=') {
                        readChar();
                        result.add(new Token(TokenType.Assignment, "&=", location));
                    } else
                        result.add(new Token(TokenType.BinaryOperation, "&", location));
                    break;
                case '=':
                    readChar();
                    if ((char) peekChar() == '=') {
                        readChar();
                        result.add(new Token(TokenType.Comparison, "==", location));
                    } else
                        result.add(new Token(TokenType.Assignment, "=", location));
                    break;
                case '!':
                    readChar();
                    if ((char) peekChar() == '=') {
                        readChar();
                        result.add(new Token(TokenType.Comparison, "!=", location));
                    } else
                        result.add(new Token(TokenType.UnaryOperation, "!", location));
                    break;
                case '<':
                    readChar();
                    if (peekChar() == '<') {
                        readChar();
                        result.add(new Token(TokenType.BinaryOperation, "<<", location));
                    } else if ((char) peekChar() == '=') {
                        readChar();
                        result.add(new Token(TokenType.Comparison, "<=", location));
                    } else if ((char) peekChar() == '-' && (char) peekChar(1) == '>') {
                        readChar();
                        readChar();
                        result.add(new Token(TokenType.BinaryOperation, "<->", location));
                    } else
                        result.add(new Token(TokenType.Comparison, "<", location));
                    break;
                case '>':
                    readChar();
                    if (peekChar() == '>') {
                        readChar();
                        result.add(new Token(TokenType.BinaryOperation, ">>", location));
                    } else if ((char) peekChar() == '=') {
                        readChar();
                        result.add(new Token(TokenType.Comparison, ">=", location));
                    } else
                        result.add(new Token(TokenType.Comparison, ">", location));
                    break;
                case '[':
                    readChar();
                    result.add(new Token(TokenType.LeftSquare, "[", location));
                    break;
                case ']':
                    readChar();
                    result.add(new Token(TokenType.RightSquare, "]", location));
                    break;
                case '#':
                    scanSingleComment();
                    break;
                case '$':
                    scanMultilineComment();
                    break;
                case '~':
                    readChar();
                    result.add(new Token(TokenType.UnaryOperation, "~", location));
                    break;
                default:
                    HassiumLogger.error("Caught unknown char in lexer: " + readChar() + " at " + location);
            }
        }
        whiteSpace();
    }

    private void whiteSpace() {
        while (Character.isWhitespace((char) peekChar()) && peekChar() != -1)
            readChar();
    }

    private Token scanIdentifier() {
        String str = "";
        while (Character.isLetterOrDigit((char) peekChar()) || (char) peekChar() == '_' && peekChar() != -1)
            str += (char) readChar();
        return new Token(str.equals("is") ? TokenType.BinaryOperation : TokenType.Identifier, str, location);
    }

    private Token scanNumber() {
        StringBuilder str = new StringBuilder();
        boolean sep = false;
        while (peekChar() != -1 &&
                (Character.isDigit((char) peekChar()) || "abcdefABCDEF".contains(Character.toString((char) peekChar())) || "xo-._".contains(Character.toString((char) peekChar())))) {
            char cchar = (char) readChar();
            if (cchar == '_') {
                if (sep) break;
                sep = true;
            } else {
                sep = false;
                str.append(cchar);
            }
        }
        String finalString = str.toString();
        String bname = "";
        int bsize = 0;
        if (finalString.startsWith("0x")) {
            bname = "hex";
            bsize = 16;
        } else if (finalString.startsWith("0b")) {
            bname = "binary";
            bsize = 2;
        } else if (finalString.startsWith("0o")) {
            bname = "octal";
            bsize = 8;
        }
        if (bname != "") {
            try {
                return new Token(TokenType.Int64, Integer.toString(Integer.parseInt(finalString.substring(2), bsize)), location);
            } catch (Exception ex) {
                HassiumLogger.error("Invalid " + bname + " number: " + finalString + " at " + location);
            }
        } else {
            try {
                return new Token(TokenType.Int64, Long.toString(Long.parseLong(finalString)), location);
            } catch (Exception ex) {
                try {
                    return new Token(TokenType.Double,
                            Double.toString(Double.parseDouble(finalString)), location);
                } catch (Exception ex1) {
                    HassiumLogger.error("Invalid number: " + finalString + " at " + location);
                }
            }
        }
        return null; // Not reachable but for compilation
    }

    private Token scanString(boolean isVerbatim) {
        StringBuilder str = new StringBuilder();
        readChar();
        while ((char) peekChar() != '\"' && peekChar() != -1) {
            char ch = (char) readChar();
            if (ch == '\\' && !isVerbatim)
                str.append(scanEscapeCode((char) readChar()));
            else if (ch == '#' && !isVerbatim && peekChar() == '{') {
                readChar();
                if (peekChar() == '}') {
                    readChar();
                    continue;
                }
                result.add(new Token(TokenType.String, str.toString(), location));
                str.setLength(0);
                result.add(new Token(TokenType.BinaryOperation, "+", location));
                result.add(new Token(TokenType.LeftParentheses, "(", location));
                while (peekChar() != -1 && peekChar() != '}')
                    scanToken();
                readChar();
                result.add(new Token(TokenType.RightParentheses, ")", location));
                result.add(new Token(TokenType.BinaryOperation, "+", location));
                continue;
            } else
                str.append(ch);
        }
        readChar();

        return new Token(TokenType.String, str.toString(), location);
    }

    private void scanSingleComment() {
        readChar();
        while (peekChar() != -1 && peekChar() != '\n')
            readChar();
    }

    private void scanMultilineComment() {
        readChar();
        while (peekChar() != -1 && peekChar() != '$')
            readChar();
        readChar();
    }

    private char scanEscapeCode(char escape) {
        switch (escape) {
            case '\\':
                return '\\';
            case '"':
                return '\"';
            case '\'':
                return '\'';
            case 'b':
                return '\b';
            case 'f':
                return '\f';
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            case '#':
                return '#';
            default:
                HassiumLogger.error("Unknown escape code \\" + escape + " at " + location);
                return 0; // Not reachable but for compilation
        }
    }

    private int peekChar() {
        return peekChar(0);
    }

    private int peekChar(int n) {
        return position + n < code.length() ? code.toCharArray()[position + n] : -1;
    }

    private int readChar() {
        if (position >= code.length())
            return -1;
        if (peekChar() == '\n')
            location = new SourceLocation(location.getLine() + 1, 0);
        else
            location = new SourceLocation(location.getLine(), location.getLetter() + 1);

        return code.toCharArray()[position++];
    }
}
