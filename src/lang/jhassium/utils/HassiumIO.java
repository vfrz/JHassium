package lang.jhassium.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * File : HassiumIO.java
 * Description : None
 * Authors : FRITZ Valentin, Jacob Misirian & zdimension
 * Website : https://github.com/vfrz/JHassium
 * Date : 10/08/2016 20:08
 */
public class HassiumIO {

    public static String readAllText(String path) {
        try {
            return FileUtils.readFileToString(new File(path), Charset.defaultCharset());
        } catch (IOException e) {
            HassiumLogger.error("Error while reading file : " + path);
        }
        return null;
    }

    public static void writeAllText(String path, String content) {
        try {
            FileUtils.writeStringToFile(new File(path), content, Charset.defaultCharset());
        } catch (IOException e) {
            HassiumLogger.error("Error while writing file : " + path);
        }
    }

    public static void createZip(String sourceDirPath, String zipFilePath) {
        ZipOutputStream zipFile = null;
        try {
            zipFile = new ZipOutputStream(new FileOutputStream(zipFilePath));
            privateCreateZip(sourceDirPath, sourceDirPath, zipFile);
        } catch (Exception e) {
            HassiumLogger.error("Error while creating zip file : " + zipFilePath);
        }
        IOUtils.closeQuietly(zipFile);
    }

    private static void privateCreateZip(String rootDir, String sourceDir, ZipOutputStream out) throws IOException {
        for (File file : new File(sourceDir).listFiles()) {
            if (file.isDirectory()) {
                privateCreateZip(rootDir, sourceDir + file.getName() + File.separator, out);
            } else {
                ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + file.getName());
                out.putNextEntry(entry);
                FileInputStream in = new FileInputStream(sourceDir + file.getName());
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
            }
        }
    }

    public static void unzip(String archive, String outputDir) {
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, new File(outputDir));
            }
        } catch (Exception e) {
            HassiumLogger.error("Error while extracting file " + archive);
        }
    }

    private static void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {
        if (entry.isDirectory()) {
            createFolder(outputDir.getAbsolutePath());
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            createFolder(outputFile.getParentFile().getAbsolutePath());
        }

        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }

    public static void createFolder(String folder) {
        if (!new File(folder).mkdirs()) HassiumLogger.error("Error while creating folder : " + folder);
    }

    public static void deleteFolder(String folder) {
        try {
            FileUtils.deleteDirectory(new File(folder));
        } catch (IOException e) {
            HassiumLogger.error("Error while deleting folder : " + folder);
        }
    }

    public static File getCurrentDirectory() {
        return new File(Paths.get(".").toAbsolutePath().normalize().toString());
    }


}
