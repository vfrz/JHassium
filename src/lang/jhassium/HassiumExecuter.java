package lang.jhassium;

import lang.jhassium.codegen.HassiumModule;
import lang.jhassium.lexer.Lexer;
import lang.jhassium.runtime.VirtualMachine;
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
            /*
            Parser.Parser parser = new Parser.Parser();
            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            HassiumCompiler compiler = new HassiumCompiler();
            var tokens = lexer.Scan(source);
            var ast = parser.Parse(tokens);
            var table = analyzer.Analyze(ast);
            module = compiler.Compile(ast, table, "MainModule");
            if (executeVM)
            {
                vm = new VirtualMachine();
                try
                {
                    vm.Execute(module, hassiumArgs);
                }
                catch (RuntimeException ex)
                {
                    Console.WriteLine("Hassium Runtime Exception! Message: {0} at {1}", ex.Message, ex.SourceLocation.ToString());
                    foreach (string str in vm.CallStack)
                    Console.WriteLine("At {0} -> ", str);
                }
            }*/
        return module;
    }
}
