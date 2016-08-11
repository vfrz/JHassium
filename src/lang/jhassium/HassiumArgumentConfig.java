package lang.jhassium;

import lang.jhassium.utils.HassiumIO;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * File : HassiumArgumentConfig.java
 * Description : None
 * Author : FRITZ Valentin
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:05
 */
public class HassiumArgumentConfig {

    public boolean CreatePackage;
    public String PackageFile;
    public String SourceFile;
    public List<String> HassiumArgs;

    public static void executeConfig(HassiumArgumentConfig config) {
        if (config.CreatePackage) {
            HassiumIO.writeAllText("manifest.conf", config.SourceFile);
            HassiumIO.createZip(HassiumIO.getCurrentDirectory().toString(), HassiumIO.getCurrentDirectory().getParent().toString() + "/" + config.PackageFile);
        } else {
            if (config.SourceFile.endsWith(".pkg")) {
                String path = HassiumIO.getCurrentDirectory().toString() + "/.pkg";
                if (Files.isDirectory(Paths.get(path)))
                    HassiumIO.deleteFolder(path);
                HassiumIO.createFolder(path);
                HassiumIO.unzip(config.SourceFile, path);
                HassiumExecuter.fromFilePath(HassiumIO.readAllText("manifest.conf"), config.HassiumArgs);
                HassiumIO.deleteFolder(path);
            } else
                HassiumExecuter.fromFilePath(config.SourceFile, config.HassiumArgs);
        }
    }
}
