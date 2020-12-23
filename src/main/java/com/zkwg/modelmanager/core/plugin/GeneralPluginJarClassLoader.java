package com.zkwg.modelmanager.core.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

public class GeneralPluginJarClassLoader extends AbstractPluginJarClassLoader {

    private static Logger logger = LoggerFactory.getLogger(GeneralPluginJarClassLoader.class);

    private JarURLConnection cachedJarFile = null;

    public GeneralPluginJarClassLoader() {
        super(new URL[] {}, findParentClassLoader());
    }

    /**
     * 将指定的文件url添加到类加载器的classpath中去，并缓存jar connection，方便以后卸载jar
     * 一个可想类加载器的classpath中添加的文件url
     * @param
     */
    public void addURLFile(URL file) {
        try {
            // 打开并缓存文件url连接
            URLConnection uc = file.openConnection();
            if (uc instanceof JarURLConnection) {
                uc.setUseCaches(true);
//                uc.setUseCaches(false);
                ((JarURLConnection) uc).getManifest();
                cachedJarFile = (JarURLConnection) uc;
            }
        } catch (Exception e) {
            logger.error("Failed to cache plugin JAR file: " + file.toExternalForm());
            throw new RuntimeException("Failed to cache plugin JAR file", e);
        }
        addURL(file);
    }


    public void unloadJarFile(String url){

        try {
            if(cachedJarFile == null){
                return;
            }
            JarURLConnection jarURLConnection = cachedJarFile;
            logger.debug("Unloading plugin JAR file " + jarURLConnection.getJarFile().getName());
            jarURLConnection.getJarFile().close();
            jarURLConnection = null;
//            System.gc();
        } catch (Exception e) {
            logger.error("Failed to unload JAR file\n"+e);
        }
    }





}
