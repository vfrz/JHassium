package lang.jhassium;

/**
 * File : SourceLocation.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 18:47
 */
public class SourceLocation {

    private int line, letter;

    public SourceLocation(int line, int letter) {
        this.line = line;
        this.letter = letter;
    }

    @Override
    public String toString() {
        return String.format("[SourceLocation: Line=%1s, Letter=%2s]", line, letter);
    }

    public int getLine() {
        return line;
    }

    public int getLetter() {
        return letter;
    }

}
