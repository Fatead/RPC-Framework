package factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例工厂
 */
public class SingletonFactory {
    //单例模式的单例容器，每个类对应一个对象实例
    private static Map<Class,Object> objectMap = new HashMap<>();

    private SingletonFactory(){

    }

    public static <T> T getInstance(Class<T> clazz){
        Object instance = objectMap.get(clazz);
        synchronized (clazz){
            if(instance == null){
                try {
                    instance = clazz.newInstance();
                    objectMap.put(clazz,instance);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        //cast方法将此Object强制转换为类或者接口
        return clazz.cast(instance);
    }

}
