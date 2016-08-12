package lang.jhassium.runtime.standardlibrary.io;

import lang.jhassium.runtime.VirtualMachine;
import lang.jhassium.runtime.standardlibrary.HassiumObject;
import lang.jhassium.runtime.standardlibrary.types.*;
import lang.jhassium.utils.HassiumLogger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * File : HassiumFile.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 12/08/2016 12:00
 */
public class HassiumFile extends HassiumObject {

    public HassiumFile() {
        try {
            Attributes.put("createDirectory", new HassiumFunction(this.getClass().getDeclaredMethod("createDirectory", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("createFile", new HassiumFunction(this.getClass().getDeclaredMethod("createFile", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("currentDirectory", new HassiumProperty(this.getClass().getDeclaredMethod("get_CurrentDirectory", VirtualMachine.class, HassiumObject[].class),
                    this.getClass().getDeclaredMethod("set_CurrentDirectory", VirtualMachine.class, HassiumObject[].class), this));
            Attributes.put("delete", new HassiumFunction(this.getClass().getDeclaredMethod("delete", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("deleteDirectory", new HassiumFunction(this.getClass().getDeclaredMethod("deleteDirectory", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("deleteFile", new HassiumFunction(this.getClass().getDeclaredMethod("deleteFile", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("directoryExists", new HassiumFunction(this.getClass().getDeclaredMethod("directoryExists", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("exists", new HassiumFunction(this.getClass().getDeclaredMethod("exists", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("fileExists", new HassiumFunction(this.getClass().getDeclaredMethod("fileExists", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("getDirectories", new HassiumFunction(this.getClass().getDeclaredMethod("getDirectories", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("getFiles", new HassiumFunction(this.getClass().getDeclaredMethod("getFiles", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("readBytes", new HassiumFunction(this.getClass().getDeclaredMethod("readBytes", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("readLines", new HassiumFunction(this.getClass().getDeclaredMethod("readLines", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("readText", new HassiumFunction(this.getClass().getDeclaredMethod("readText", VirtualMachine.class, HassiumObject[].class), this, 1));
            Attributes.put("writeBytes", new HassiumFunction(this.getClass().getDeclaredMethod("writeBytes", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put("writeLines", new HassiumFunction(this.getClass().getDeclaredMethod("writeLines", VirtualMachine.class, HassiumObject[].class), this, 2));
            Attributes.put("writeText", new HassiumFunction(this.getClass().getDeclaredMethod("writeText", VirtualMachine.class, HassiumObject[].class), this, 2));
        } catch (NoSuchMethodException e) {
            HassiumLogger.error("Internal error HassiumBool : " + e.getMessage());
        }
    }

    public HassiumString createDirectory(VirtualMachine vm, HassiumObject[] args) {
        new File(args[0].toString(vm)).mkdir();
        return HassiumString.create(args[0]);
    }

    public HassiumString createFile(VirtualMachine vm, HassiumObject[] args) {
        try {
            new File(args[0].toString(vm)).createNewFile();
        } catch (IOException e) {
            HassiumLogger.error("Error while creating file : " + args[0].toString(vm));
        }
        return HassiumString.create(args[0]);
    }

    public HassiumString get_CurrentDirectory(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumString(FileUtils.getFile("").getAbsolutePath());
    }

    public HassiumNull set_CurrentDirectory(VirtualMachine vm, HassiumObject[] args) {
        System.setProperty("user.dir", HassiumString.create(args[0]).getValue());
        return HassiumObject.Null;
    }

    public HassiumString delete(VirtualMachine vm, HassiumObject[] args) {
        File file = FileUtils.getFile(args[0].toString(vm));
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.delete())
                    HassiumLogger.error("Error while deleting file : " + file.getPath());
            } else if (file.isDirectory()) {
                try {
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    HassiumLogger.error("Error while deleting folder : " + file.getPath());
                }
            }
        } else {
            HassiumLogger.error("File / directory does not exist: " + file.getPath());
        }
        return HassiumString.create(args[0]);
    }

    public HassiumString deleteDirectory(VirtualMachine vm, HassiumObject[] args) {
        return delete(vm, args);
    }

    public HassiumString deleteFile(VirtualMachine vm, HassiumObject[] args) {
        return delete(vm, args);
    }

    public HassiumBool directoryExists(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(new File(args[0].toString(vm)).isDirectory());
    }

    public HassiumBool exists(VirtualMachine vm, HassiumObject[] args) {
        File file = FileUtils.getFile(args[0].toString(vm));
        return new HassiumBool(file.isFile() || file.isDirectory());
    }

    public HassiumBool fileExists(VirtualMachine vm, HassiumObject[] args) {
        return new HassiumBool(new File(args[0].toString(vm)).isFile());
    }

    public HassiumList getDirectories(VirtualMachine vm, HassiumObject[] args) {
        File file = FileUtils.getFile(HassiumString.create(args[0]).getValue()).getAbsoluteFile();
        String[] dirs = file.list((current, name) -> new File(current, name).isDirectory());
        HassiumString[] elements = new HassiumString[dirs.length];
        for (int i = 0; i < elements.length; i++)
            elements[i] = new HassiumString(dirs[i]);
        return new HassiumList(elements);
    }

    public HassiumList getFiles(VirtualMachine vm, HassiumObject[] args) {
        File file = FileUtils.getFile(HassiumString.create(args[0]).getValue()).getAbsoluteFile();
        String[] files = file.list((current, name) -> new File(current, name).isFile());
        HassiumString[] elements = new HassiumString[files.length];
        for (int i = 0; i < elements.length; i++)
            elements[i] = new HassiumString(files[i]);
        return new HassiumList(elements);
    }

    public HassiumList readBytes(VirtualMachine vm, HassiumObject[] args) {
        String text = null;
        try {
            text = FileUtils.readFileToString(FileUtils.getFile(HassiumString.create(args[0]).getValue()), Charset.defaultCharset());
        } catch (IOException e) {
            HassiumLogger.error("Error while reading file : " + HassiumString.create(args[0]).getValue());
        }
        byte[] bytes = text.getBytes();
        HassiumChar[] chars = new HassiumChar[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = new HassiumChar((char) bytes[i]);
        }
        return new HassiumList(chars);
    }

    public HassiumList readLines(VirtualMachine vm, HassiumObject[] args) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(FileUtils.getFile(HassiumString.create(args[0]).getValue()).toPath());
        } catch (IOException e) {
            HassiumLogger.error("Error while reading file : " + HassiumString.create(args[0]).getValue());
        }
        HassiumString[] strings = new HassiumString[lines.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = new HassiumString(lines.get(i));
        }
        return new HassiumList(strings);
    }

    public HassiumString readText(VirtualMachine vm, HassiumObject[] args) {
        String text = null;
        try {
            text = FileUtils.readFileToString(FileUtils.getFile(HassiumString.create(args[0]).getValue()), Charset.defaultCharset());
        } catch (IOException e) {
            HassiumLogger.error("Error while reading file : " + HassiumString.create(args[0]).getValue());
        }
        return new HassiumString(text);
    }

    public HassiumNull writeBytes(VirtualMachine vm, HassiumObject[] args) {

        HassiumList chars = HassiumList.create(args[1]);
        byte[] bytes = new byte[chars.getValue().size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) HassiumChar.create(chars.getValue().get(i)).getValue().charValue();
        }
        try {
            FileUtils.writeByteArrayToFile(new File(HassiumString.create(args[0]).getValue()), bytes);
        } catch (IOException e) {
            HassiumLogger.error("Error while writing bytes to file : " + HassiumString.create(args[0]).getValue());
        }
        return HassiumObject.Null;
    }

    public HassiumNull writeLines(VirtualMachine vm, HassiumObject[] args) {

        HassiumList strings = HassiumList.create(args[1]);
        StringBuilder stringBuilder = new StringBuilder();
        for (HassiumObject object : strings.getValue()) {
            stringBuilder.append(HassiumString.create(object).getValue() + "\n");
        }
        return writeText(vm, new HassiumObject[]{new HassiumString(HassiumString.create(args[0]).getValue()), new HassiumString(stringBuilder.toString())});
    }

    public HassiumNull writeText(VirtualMachine vm, HassiumObject[] args) {
        try {
            FileUtils.write(FileUtils.getFile(HassiumString.create(args[0]).getValue()), HassiumString.create(args[1]).getValue(), Charset.defaultCharset());
        } catch (IOException e) {
            HassiumLogger.error("Error while writing text to file : " + HassiumString.create(args[0]).getValue());
        }
        return HassiumObject.Null;
    }

}
