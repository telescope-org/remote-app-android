package org.bert.carehelper.annotation;

import android.util.Log;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationUtils {

    private static final String EXT = "class";

    /**
     * 根据包名获取包的URL
     * @param pkgName com.demo.controller
     * @return
     */
    public static String getPkgPath(String pkgName){
        String pkgDirName = pkgName.replace('.', File.separatorChar);
        URL url = Thread.currentThread().getContextClassLoader().getResource(pkgDirName);

        return url == null ? null : url.getFile();
    }

    /**
     * 获取指定包下所有类对象的集合
     * @param pkgName 包名(com.demo.controller)
     * @param pkgPath 包路径(/Users/xxx/workspace/java/project/out/production/classes/com/demo/controller)
     * @param recursive 是否递归遍历子目录
     * @return 类集合
     */
    public static Set<Class<?>> scanClasses(String pkgName, String pkgPath, final boolean recursive){
        Set<Class<?>> classesSet = new HashSet<>();

        Collection<File> allClassFile = getAllClassFile(pkgPath, recursive);

        for (File curFile : allClassFile){
            try {
                classesSet.add(getClassObj(curFile, pkgPath, pkgName));
            } catch (ClassNotFoundException e) {
                Log.e("AnnotationUtil", "load class fail" + e);
            }
        }

        return classesSet;
    }

    /**
     * 获取指定包下包含指定注解的所有类对象的集合
     * @param pkgName 包名(com.demo.controller)
     * @param pkgPath 包路径(/Users/xxx/workspace/java/project/out/production/classes/com/demo/controller)
     * @param recursive 是否递归遍历子目录
     * @param targetAnnotations 指定注解
     * @return 以注解和对应类集合构成的键值对
     */
    public static Map<Class<? extends Annotation>, Set<Class<?>>> scanClassesByAnnotations(
            String pkgName, String pkgPath, final boolean recursive, List<Class<? extends Annotation>> targetAnnotations){
        Map<Class<? extends Annotation>, Set<Class<?>>> resultMap = new HashMap<>(16);

        Collection<File> allClassFile = getAllClassFile(pkgPath, recursive);

        for (File curFile : allClassFile){
            try {
                Class<?> curClass = getClassObj(curFile, pkgPath, pkgName);
                for (Class<? extends Annotation> annotation : targetAnnotations){
                    if (curClass.isAnnotationPresent(annotation)){
                        if (!resultMap.containsKey(annotation)){
                            resultMap.put(annotation, new HashSet<Class<?>>());
                        }
                        resultMap.get(annotation).add(curClass);
                    }
                }
            } catch (ClassNotFoundException e) {
                logger.error("load class fail", e);
            }
        }

        return resultMap;
    }

    /**
     * 加载类
     * @param file
     * @param pkgPath
     * @param pkgName
     * @return
     * @throws ClassNotFoundException
     */
    private static Class<?> getClassObj(File file, String pkgPath, String pkgName) throws ClassNotFoundException{
        // 考虑class文件在子目录中的情况
        String absPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - EXT.length() - 1);
        String className = absPath.substring(pkgPath.length()).replace(File.separatorChar, '.');
        className = className.startsWith(".") ? pkgName + className : pkgName + "." + className;

        return Thread.currentThread().getContextClassLoader().loadClass(className);
    }

    /**
     * 遍历指定目录下所有扩展名为class的文件
     * @param pkgPath 包目录
     * @param recursive 是否递归遍历子目录
     * @return
     */
    private static Collection<File> getAllClassFile(String pkgPath, boolean recursive){
        File fPkgDir = new File(pkgPath);

        if (!(fPkgDir.exists() && fPkgDir.isDirectory())){
            logger.error("the directory to package is empty: {}", pkgPath);

            return null;
        }

        return FileUtils.listFiles(fPkgDir, new String[]{EXT}, recursive);
    }

    /**
     * 查找指定注解的Method
     * @param classes 查找范围
     * @param targetAnnotations 指定的注解
     * @return 以注解和对应Method类集合构成的键值对
     */
    public static Map<Class<? extends Annotation>, Set<Method>> scanMethodsByAnnotations(Set<Class<?>> classes,
                                                                                         List<Class<? extends Annotation>> targetAnnotations){
        Map<Class<? extends Annotation>, Set<Method>> resultMap = new HashMap<>(16);

        for (Class<?> cls : classes){
            Method[] methods = cls.getMethods();

            for (Class<? extends Annotation> annotation : targetAnnotations){
                for (Method method : methods){
                    if (method.isAnnotationPresent(annotation)){
                        if (!resultMap.containsKey(annotation)){
                            resultMap.put(annotation, new HashSet<Method>());
                        }
                        resultMap.get(annotation).add(method);
                    }
                }
            }
        }

        return resultMap;
    }
}
