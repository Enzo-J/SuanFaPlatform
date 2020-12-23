package com.zkwg.modelmanager.utils;
 

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
//        testSSH();
        String jarName = "dubbo-consumer-0.0.1-SNAPSHOT";
        String path = Test.class.getClassLoader().getResource("static/pluginLib/"+ jarName + ".jar").getPath();
        URL jarUrl = new URL("jar:file:" + FilePathUtil.getRealFilePath(path) + "!/");
        File file = new File(jarUrl.toString());
        List<URL> list = findDependencyJar(file);
        System.out.println(list);
    }

    private static List<URL> findDependencyJar(File file) throws MalformedURLException {
        List<URL> list = new ArrayList<>();
        File parentFile = file.getParentFile();
        File libFile = new File( File.separator + "BOOT-INF/lib/");
        if (libFile.exists() && parentFile.isDirectory()) {
            for (File jar : libFile.listFiles()) {
                if (jar.isFile()
                        && jar.getName().toLowerCase().endsWith(".jar")
                ) {
                    list.add(jar.toURI().toURL());
                }
            }
        }
        list.add(file.toURI().toURL());
        return list;
    }

}