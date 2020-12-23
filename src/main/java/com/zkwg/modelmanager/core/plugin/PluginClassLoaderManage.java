package com.zkwg.modelmanager.core.plugin;

import com.zkwg.modelmanager.entity.Plugin;
import com.zkwg.modelmanager.utils.FilePathUtil;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PluginClassLoaderManage {

    private AbstractPluginJarClassLoader abstractPluginJarClassLoader;

    private final static ConcurrentHashMap<String, AbstractPluginJarClassLoader> LOADER_CACHE = new ConcurrentHashMap<>();

    public PluginClassLoaderManage() {
    }

    public PluginClassLoaderManage(AbstractPluginJarClassLoader abstractPluginJarClassLoader) {
        this.abstractPluginJarClassLoader = abstractPluginJarClassLoader;
    }

    public void loadJar(String path, String jarName) throws MalformedURLException {
        AbstractPluginJarClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
        if(urlClassLoader != null){
            return;
        }
        urlClassLoader = this.abstractPluginJarClassLoader;
        URL jarUrl = new URL("jar:file:" + FilePathUtil.getRealFilePath(path) + "!/");
        urlClassLoader.addURLFile(jarUrl);

        LOADER_CACHE.put(jarName,urlClassLoader);
    }

    public void loadJar(String jarName) throws MalformedURLException {
        AbstractPluginJarClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
        if(urlClassLoader != null){
            return;
        }
        urlClassLoader =  this.abstractPluginJarClassLoader;
        String path = PluginClassLoaderManage.class.getClassLoader().getResource("static/pluginLib/"+ jarName + ".jar").getPath();
//        String path = "E:\\project\\github\\ai-auto-learning-platform\\src\\main\\resources\\static\\pluginLib";
        URL jarUrl = new URL("jar:file:" + FilePathUtil.getRealFilePath(path) + "!/");
        urlClassLoader.addURLFile(jarUrl);

        LOADER_CACHE.put(jarName,urlClassLoader);
    }

    public Plugin parsePluginJson(String jarName) throws ClassNotFoundException {
        AbstractPluginJarClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
        Assert.notNull(urlClassLoader,jarName + "插件未加载 ！！！");
        InputStream inputStream = urlClassLoader.getResourceAsStream("plugin.json");
        Assert.notNull(inputStream,"无法读取 plugin.json");
        Plugin plugin = Serialization.unmarshal(inputStream, Plugin.class);
        plugin.setName(jarName);
        return plugin;
    }

    public Class loadClass(String jarName,String name) throws ClassNotFoundException {
        AbstractPluginJarClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
        Assert.notNull(urlClassLoader,jarName + "插件未加载 ！！！");
        return urlClassLoader.loadClass(name);
    }

    public void unloadJarFile(String jarName) throws MalformedURLException {
        AbstractPluginJarClassLoader urlClassLoader = LOADER_CACHE.get(jarName);
        if(urlClassLoader == null){
            return;
        }
        String path = PluginClassLoaderManage.class.getClassLoader().getResource("static/pluginLib/"+ jarName + ".jar").getPath();
        String jarStr = "jar:file:" + FilePathUtil.getRealFilePath(path) + "!/";
        urlClassLoader.unloadJarFile(jarStr);
        urlClassLoader = null;
        LOADER_CACHE.remove(jarName);
    }

    public AbstractPluginJarClassLoader getAbstractPluginJarClassLoader() {
        return abstractPluginJarClassLoader;
    }

    public void setAbstractPluginJarClassLoader(AbstractPluginJarClassLoader abstractPluginJarClassLoader) {
        this.abstractPluginJarClassLoader = abstractPluginJarClassLoader;
    }
}
