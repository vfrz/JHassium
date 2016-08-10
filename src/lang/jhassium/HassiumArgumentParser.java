package lang.jhassium;

import lang.jhassium.utils.HassiumLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * File : HassiumArgumentParser.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:43
 */
public class HassiumArgumentParser {

    private String[] args;
    private int position;
    private List<String> hassiumArgs = new ArrayList<>();

    public HassiumArgumentParser(String[] args) {
        this.args = args;
    }

    public HassiumArgumentConfig parse() {
        HassiumArgumentConfig result = new HassiumArgumentConfig();
        result.CreatePackage = false;

        for (position = 0; position < args.length; position++) {
            if (new File(args[position]).exists()) {
                if (result.SourceFile == null)
                    result.SourceFile = args[position];
                else
                    hassiumArgs.add(args[position]);
            } else
                switch (args[position]) {
                    case "-p":
                    case "--package":
                        result.CreatePackage = true;
                        result.PackageFile = expectData("package file");
                        break;
                    default:
                        hassiumArgs.add(args[position]);
                        break;
                }
        }

        result.HassiumArgs = hassiumArgs;

        return result;
    }

    private String expectData(String type) {
        if (args[++position].startsWith("-")) {
            HassiumLogger.error(String.format("Expected %1s instead of flag %2s !", type, args[position]));
        }
        return args[position];
    }
}
