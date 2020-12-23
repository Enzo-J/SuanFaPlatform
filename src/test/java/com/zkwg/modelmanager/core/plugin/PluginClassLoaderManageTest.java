package com.zkwg.modelmanager.core.plugin;

import com.zkwg.modelmanager.entity.Plugin;

public class PluginClassLoaderManageTest {

    public static void main(String[] args) throws Exception {

        PluginClassLoaderManage classLoader = new PluginClassLoaderManage(new GeneralPluginJarClassLoader());

        try {

//            classLoader.unloadJarFile("dubbo-consumer-0.0.1-SNAPSHOT");

            classLoader.loadJar("ann");

//            classLoader.loadJar("dubbo-consumer-0.0.1-SNAPSHOT");

            Plugin plugin = classLoader.parsePluginJson("ann");
//
            Class serverClazz1 = classLoader.loadClass("ann","com.zkwg.modelmanager.core.plugin.impl.CNNSpeechServerManager");

//            Class serverClazz = classLoader.loadClass("dubbo-consumer-0.0.1-SNAPSHOT","com.demo.dubboconsumer.plugin.MyServerPlugin");

//            Class serverClazz = classLoader.loadClass("ann",plugin.getClassName());
//            Class parameterClazz = classLoader.loadClass("ann",plugin.getParameterClassName());
//            Class resultClazz = classLoader.loadClass("ann",plugin.getResultClassName());

//            Object parameter = parameterClazz.newInstance();
//            Object result = resultClazz.newInstance();
//            AbstractServerPlugin serverManager = (AbstractServerPlugin) serverClazz.newInstance();

//            serverManager.server(parameter);

            classLoader.unloadJarFile("ann");

        } catch (Exception e) {
            e.printStackTrace();
            classLoader.unloadJarFile("ann");
        }

    }
}
