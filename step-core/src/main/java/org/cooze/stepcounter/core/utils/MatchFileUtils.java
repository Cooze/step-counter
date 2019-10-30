package org.cooze.stepcounter.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-02
 **/
public final class MatchFileUtils {

    private static final String XML_SUFFIX = ".xml";
    private static final String PATH_SEPARATER = ".xml";


    public static List<String> matchFile(String rootPath) {
        File rootFile = new File(rootPath);
        List<String> returnFiles = new ArrayList<>();
        listFileAndDir(rootFile, returnFiles);
        return returnFiles;
    }

    public static void listFileAndDir(File baseDir, List<String> returnFiles) {
        if (baseDir == null) {
            return;
        }
        File[] fList = baseDir.listFiles();
        if (fList == null) {
            return;
        }

        for (File file : fList) {
            if (file.isDirectory()) {
                listFileAndDir(file, returnFiles);

            } else if (file.isFile()) {
                if (file.getPath().toLowerCase().endsWith(XML_SUFFIX)) {
                    returnFiles.add(file.getPath());
                }
            }
        }
    }

    public static List<String> listJarResources(String filePath, String startWith) throws IOException {
        List<String> fileList = new ArrayList<>();
        JarFile jf = new JarFile(filePath);
        Enumeration<JarEntry> enume = jf.entries();
        while (enume.hasMoreElements()) {
            JarEntry element = enume.nextElement();
            String name = element.getName();
            if (name.startsWith(startWith) && name.toLowerCase().endsWith(XML_SUFFIX)) {
                fileList.add(PATH_SEPARATER + name);
            }
        }
        return fileList;
    }

}
