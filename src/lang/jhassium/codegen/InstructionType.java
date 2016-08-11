package lang.jhassium.codegen;

/**
 * File : InstructionType.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 11/08/2016 13:46
 */
public enum InstructionType {
    Binary_Operation,
    Build_Closure,
    Call,
    Create_Dict,
    Create_List,
    Create_Tuple,
    Dup,
    Iter,
    Enumerable_Full,
    Enumerable_Next,
    Enumerable_Reset,
    Jump,
    Jump_If_True,
    Jump_If_False,
    Key_Value_Pair,
    Label,
    Load_Attribute,
    Load_Global,
    Load_Global_Variable,
    Load_List_Element,
    Load_Local,
    Pop,
    Pop_Handler,
    Push,
    Push_Bool,
    Push_Handler,
    Push_Object,
    Raise,
    Return,
    Self_Reference,
    Skip,
    Slice,
    Store_Attribute,
    Store_Global_Variable,
    Store_Local,
    Store_List_Element,
    Unary_Operation
}
