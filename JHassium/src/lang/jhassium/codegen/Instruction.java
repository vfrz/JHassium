package lang.jhassium.codegen;

import lang.jhassium.SourceLocation;

/**
 * File : Instruction.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 13:46
 */
public class Instruction {

    private InstructionType instructionType;
    private double argument;
    private SourceLocation sourceLocation;

    public Instruction(InstructionType instructionType, double argument, SourceLocation location) {
        this.instructionType = instructionType;
        this.argument = argument;
        this.sourceLocation = location;
    }

    public InstructionType getInstructionType() {
        return instructionType;
    }

    public double getArgument() {
        return argument;
    }

    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }
}
