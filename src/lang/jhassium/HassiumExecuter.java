package lang.jhassium;

import lang.jhassium.codegen.HassiumCompiler;
import lang.jhassium.codegen.HassiumModule;
import lang.jhassium.lexer.Lexer;
import lang.jhassium.lexer.Token;
import lang.jhassium.parser.AstNode;
import lang.jhassium.parser.Parser;
import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.semanticanalysis.SemanticAnalyzer;
import lang.jhassium.semanticanalysis.SymbolTable;
import lang.jhassium.utils.HassiumIO;

import java.util.List;

/**
 * File : HassiumExecuter.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:46
 */
public class HassiumExecuter {

    public static VirtualMachine vm;

    public static HassiumModule fromFilePath(String filePath, List<String> hassiumArgs) {
        return fromFilePath(filePath, hassiumArgs, true);
    }

    public static HassiumModule fromFilePath(String filePath, List<String> hassiumArgs, boolean executeVM) {
        return fromString(HassiumIO.readAllText(filePath), hassiumArgs, executeVM);
    }

    public static HassiumModule fromString(String source, List<String> hassiumArgs) {
        return fromString(source, hassiumArgs, true);
    }

    public static HassiumModule fromString(String source, List<String> hassiumArgs, boolean executeVM) {
        HassiumModule module = null;
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        HassiumCompiler compiler = new HassiumCompiler();
        List<Token> tokens = lexer.Scan(source);
        AstNode ast = parser.Parse(tokens);
        SymbolTable table = analyzer.Analyze(ast);
        module = compiler.Compile(ast, table, "MainModule");
        if (executeVM) {
            vm = new VirtualMachine();
            try {
                vm.execute(module, hassiumArgs);
            } catch (Exception ex) {
                System.out.println(String.format("Hassium Runtime Exception! Message: %1s", ex.getMessage()));
                for (String str : vm.getCallStack())
                    System.out.println(String.format("At %1s -> ", str));
            }
        }
        return module;
    }
}
