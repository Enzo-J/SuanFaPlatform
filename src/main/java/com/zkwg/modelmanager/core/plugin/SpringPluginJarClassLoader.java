//package com.zkwg.modelmanager.core.plugin;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.loader.archive.JarFileArchive;
//import org.springframework.boot.loader.jar.Handler;
//import sun.misc.URLClassPath;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.JarURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.security.AccessController;
//import java.security.PrivilegedAction;
//import java.security.PrivilegedActionException;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.jar.JarFile;
//
//public class SpringPluginJarClassLoader extends AbstractPluginJarClassLoader {
//
//    private static Logger logger = LoggerFactory.getLogger(SpringPluginJarClassLoader.class);
//
//    private JarURLConnection cachedJarFile = null;
//
//    private static final int BUFFER_SIZE = 4096;
//
//    private final boolean exploded;
//
//    public SpringPluginJarClassLoader(URL[] urls, ClassLoader parent, boolean exploded, boolean exploded1) {
//        this(false, urls, parent);
//    }
//
//    public SpringPluginJarClassLoader(boolean exploded, URL[] urls, ClassLoader parent) {
//        super(urls, parent);
//        this.exploded = exploded;
//    }
//
//    public SpringPluginJarClassLoader(boolean exploded) {
//        super(new URL[] {}, findParentClassLoader());
//        this.exploded = exploded;
//    }
//
//
//    /**
//     * 将指定的文件url添加到类加载器的classpath中去，并缓存jar connection，方便以后卸载jar
//     * 一个可想类加载器的classpath中添加的文件url
//     * @param
//     */
//    public void addURLFile(URL file) {
//        try {
//            // 打开并缓存文件url连接
//            URLConnection uc = file.openConnection();
//            if (uc instanceof JarURLConnection) {
//                uc.setUseCaches(true);
//                ((JarURLConnection) uc).getManifest();
//                cachedJarFile = (JarURLConnection) uc;
//            }
//            File f = new File(file.toString());
////                new JarFileArchive(f);
//            // BOOT-INF/classes/
//            // BOOT-INF/lib/
////            this.ucp = new URLClassPath(urls, acc);
//        } catch (Exception e) {
//            logger.error("Failed to cache plugin JAR file: " + file.toExternalForm());
//            throw new RuntimeException("Failed to cache plugin JAR file", e);
//        }
//
//    }
//
//    private static List<URL> findDependencyJar(File file) throws MalformedURLException {
//        List<URL> list = new ArrayList<>();
//        File parentFile = file.getParentFile();
//        File libFile = new File(file.getParent() + File.separator + "BOOT-INF/lib/");
//        if (libFile.exists() && parentFile.isDirectory()) {
//            for (File jar : libFile.listFiles()) {
//                if (jar.isFile()
//                        && jar.getName().toLowerCase().endsWith(".jar")
//                ) {
//                    list.add(jar.toURI().toURL());
//                }
//            }
//        }
//        list.add(file.toURI().toURL());
//        return list;
//    }
//
//
//    public void unloadJarFile(String url){
//
//        try {
//            if(cachedJarFile == null){
//                return;
//            }
//            JarURLConnection jarURLConnection = cachedJarFile;
//            logger.debug("Unloading plugin JAR file " + jarURLConnection.getJarFile().getName());
//            jarURLConnection.getJarFile().close();
//            jarURLConnection = null;
////            System.gc();
//        } catch (Exception e) {
//            logger.error("Failed to unload JAR file\n"+e);
//        }
//    }
//
//
//    public URL findResource(String name) {
//        if (this.exploded) {
//            return super.findResource(name);
//        } else {
//            Handler.setUseFastConnectionExceptions(true);
//
//            URL var2;
//            try {
//                var2 = super.findResource(name);
//            } finally {
//                Handler.setUseFastConnectionExceptions(false);
//            }
//
//            return var2;
//        }
//    }
//
//    public Enumeration<URL> findResources(String name) throws IOException {
//        if (this.exploded) {
//            return super.findResources(name);
//        } else {
//            Handler.setUseFastConnectionExceptions(true);
//
//            SpringPluginJarClassLoader.UseFastConnectionExceptionsEnumeration var2;
//            try {
//                var2 = new SpringPluginJarClassLoader.UseFastConnectionExceptionsEnumeration(super.findResources(name));
//            } finally {
//                Handler.setUseFastConnectionExceptions(false);
//            }
//
//            return var2;
//        }
//    }
//
//    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        Class result;
//        if (name.startsWith("org.springframework.boot.loader.jarmode.")) {
//            try {
//                result = this.loadClassInLaunchedClassLoader(name);
//                if (resolve) {
//                    this.resolveClass(result);
//                }
//
//                return result;
//            } catch (ClassNotFoundException var10) {
//            }
//        }
//
//        if (this.exploded) {
//            return super.loadClass(name, resolve);
//        } else {
//            Handler.setUseFastConnectionExceptions(true);
//
//            try {
//                try {
//                    this.definePackageIfNecessary(name);
//                } catch (IllegalArgumentException var8) {
//                    if (this.getPackage(name) == null) {
//                        throw new AssertionError("Package " + name + " has already been defined but it could not be found");
//                    }
//                }
//
//                result = super.loadClass(name, resolve);
//            } finally {
//                Handler.setUseFastConnectionExceptions(false);
//            }
//
//            return result;
//        }
//    }
//
//    private Class<?> loadClassInLaunchedClassLoader(String name) throws ClassNotFoundException {
//        String internalName = name.replace('.', '/') + ".class";
//        InputStream inputStream = this.getParent().getResourceAsStream(internalName);
//        if (inputStream == null) {
//            throw new ClassNotFoundException(name);
//        } else {
//            try {
//                try {
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    byte[] buffer = new byte[4096];
//                    boolean var6 = true;
//
//                    int bytesRead;
//                    while((bytesRead = inputStream.read(buffer)) != -1) {
//                        outputStream.write(buffer, 0, bytesRead);
//                    }
//
//                    inputStream.close();
//                    byte[] bytes = outputStream.toByteArray();
//                    Class<?> definedClass = this.defineClass(name, bytes, 0, bytes.length);
//                    this.definePackageIfNecessary(name);
//                    Class var9 = definedClass;
//                    return var9;
//                } finally {
//                    inputStream.close();
//                }
//            } catch (IOException var14) {
//                throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", var14);
//            }
//        }
//    }
//
//    private void definePackageIfNecessary(String className) {
//        int lastDot = className.lastIndexOf(46);
//        if (lastDot >= 0) {
//            String packageName = className.substring(0, lastDot);
//            if (this.getPackage(packageName) == null) {
//                try {
//                    this.definePackage(className, packageName);
//                } catch (IllegalArgumentException var5) {
//                    if (this.getPackage(packageName) == null) {
//                        throw new AssertionError("Package " + packageName + " has already been defined but it could not be found");
//                    }
//                }
//            }
//        }
//
//    }
//
//    private void definePackage(String className, String packageName) {
//        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
//            String packageEntryName = packageName.replace('.', '/') + "/";
//            String classEntryName = className.replace('.', '/') + ".class";
//            URL[] var5 = this.getURLs();
//            int var6 = var5.length;
//
//            for(int var7 = 0; var7 < var6; ++var7) {
//                URL url = var5[var7];
//
//                try {
//                    URLConnection connection = url.openConnection();
//                    if (connection instanceof JarURLConnection) {
//                        JarFile jarFile = ((JarURLConnection)connection).getJarFile();
//                        if (jarFile.getEntry(classEntryName) != null && jarFile.getEntry(packageEntryName) != null && jarFile.getManifest() != null) {
//                            this.definePackage(packageName, jarFile.getManifest(), url);
//                            return null;
//                        }
//                    }
//                } catch (IOException var11) {
//                }
//            }
//
//            return null;
//        }, AccessController.getContext());
//
//    }
//
//    public void clearCache() {
//        if (!this.exploded) {
//            URL[] var1 = this.getURLs();
//            int var2 = var1.length;
//
//            for(int var3 = 0; var3 < var2; ++var3) {
//                URL url = var1[var3];
//
//                try {
//                    URLConnection connection = url.openConnection();
//                    if (connection instanceof JarURLConnection) {
//                        this.clearCache(connection);
//                    }
//                } catch (IOException var6) {
//                }
//            }
//
//        }
//    }
//
//    private void clearCache(URLConnection connection) throws IOException {
//        Object jarFile = ((JarURLConnection)connection).getJarFile();
//        if (jarFile instanceof org.springframework.boot.loader.jar.JarFile) {
//            ((org.springframework.boot.loader.jar.JarFile)jarFile).clearCache();
//        }
//
//    }
//
//    static {
//        ClassLoader.registerAsParallelCapable();
//    }
//
//    private static class UseFastConnectionExceptionsEnumeration implements Enumeration<URL> {
//        private final Enumeration<URL> delegate;
//
//        UseFastConnectionExceptionsEnumeration(Enumeration<URL> delegate) {
//            this.delegate = delegate;
//        }
//
//        public boolean hasMoreElements() {
//            Handler.setUseFastConnectionExceptions(true);
//
//            boolean var1;
//            try {
//                var1 = this.delegate.hasMoreElements();
//            } finally {
//                Handler.setUseFastConnectionExceptions(false);
//            }
//
//            return var1;
//        }
//
//        public URL nextElement() {
//            Handler.setUseFastConnectionExceptions(true);
//
//            URL var1;
//            try {
//                var1 = (URL)this.delegate.nextElement();
//            } finally {
//                Handler.setUseFastConnectionExceptions(false);
//            }
//
//            return var1;
//        }
//    }
//
//
//}
