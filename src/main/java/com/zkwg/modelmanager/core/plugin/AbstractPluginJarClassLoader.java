package com.zkwg.modelmanager.core.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLClassLoader;

public abstract class AbstractPluginJarClassLoader extends URLClassLoader {

    private static Logger logger = LoggerFactory.getLogger(AbstractPluginJarClassLoader.class);

    public AbstractPluginJarClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, findParentClassLoader());
    }

    public abstract void addURLFile(URL file);

    public abstract void unloadJarFile(String url);


    /**
     * 定位基于当前上下文的父类加载器
     * @return 返回可用的父类加载器.
     */
    public static ClassLoader findParentClassLoader() {
        ClassLoader parent = AbstractPluginJarClassLoader.class.getClassLoader();
        if (parent == null) {
            parent = AbstractPluginJarClassLoader.class.getClassLoader();
        }
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }
        return parent;
    }

}
