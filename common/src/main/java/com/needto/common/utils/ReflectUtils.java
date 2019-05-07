package com.needto.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 * 
 * @Description
 */
public class ReflectUtils {
	private static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

	public static ConcurrentHashMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<String, BeanCopier>();

	

	public static void copyProperties(Object source, Object target) {
		// BeanUtils.copyProperties(source, target);
		copyPropertiesOfCglibBean(source, target);
		// copyPropertiesOfEasyMap(source, target);

	}
	private static String generateKey(Class<?>class1,Class<?>class2){
        return class1.toString() + class2.toString();
    }
	
	/**
	 * 通过cglib的方式来进行对象赋值，其性能最优，但不支持名字相同类型不同的情况
	 * @Description 
	 * @author Administrator
	 * @param source
	 * @param target
	 */
	public static  void copyPropertiesOfCglibBean(Object source,Object target){
		copyPropertiesOfCglibBean(source, target, null);
    } 
	
	/**
	 * 通过cglib的方式来进行对象赋值，其性能最优，但不支持名字相同类型不同的情况
	 * @Description 
	 * @author Administrator
	 * @param source
	 * @param target
	 */
	public static void copyPropertiesOfCglibBean(Object source,Object target, Converter converter){
        String beanKey = generateKey(source.getClass(),target.getClass());
        BeanCopier copier;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), null != converter);
            beanCopierMap.put(beanKey, copier);
        }else {
            copier = beanCopierMap.get(beanKey);
        }
        copier.copy(source, target, converter);
    } 
	
	
	   /** 
	 * 将对象装换为map 
	 * @param bean 
	 * @return 
	 */  
	public static <T> Map<String, Object> beanToMap(T bean) {  
	    Map<String, Object> map = Maps.newHashMap();  
	    if (bean != null) {  
	        BeanMap beanMap = BeanMap.create(bean);
	        for (Object key : beanMap.keySet()) {  
	            map.put(key+"", beanMap.get(key));  
	        }             
	    }  
	    return map;  
	}  
	  
	/** 
	 * 将map装换为javabean对象 
	 * @param map 
	 * @param bean 
	 * @return 
	 */  
	public static <T> T mapToBean(Map<String, Object> map,T bean) {  
	    BeanMap beanMap = BeanMap.create(bean);
	    for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
	        String key = it.next();
            try{
                beanMap.put(key, map.get(key));
            }catch(Exception e){
                //属性类型不一样时会出异常,默认只支持转换成string类型数据
                try{
                    beanMap.put(key, String.valueOf(map.get(key)));
                }catch(ClassCastException e1){
					logger.error("mapToBean异常"+e1.getMessage()+"堆栈信息:"+e1.getStackTrace());
                }
                
            }
           
        }
	    return bean;  
	}  
	
	/** 
	 * 将List<T>转换为List<Map<String, Object>> 
	 * @param objList 
	 * @return 
	 * @throws IOException
	 */  
	public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {  
	    List<Map<String, Object>> list = Lists.newArrayList();  
	    if (objList != null && objList.size() > 0) {  
	        Map<String, Object> map;
	        T bean;
	        for (int i = 0,size = objList.size(); i < size; i++) {  
	            bean = objList.get(i);  
	            map = beanToMap(bean);  
	            list.add(map);  
	        }  
	    }  
	    return list;  
	}  
	  




	/**
	 * 将List<Map<String,Object>>转换为List<T>
	 * 
	 * @param maps
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps,
			Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		List<T> list = Lists.newArrayList();
		if (maps != null && maps.size() > 0) {
			Map<String, Object> map;
			T bean;
			for (int i = 0, size = maps.size(); i < size; i++) {
				map = maps.get(i);
				bean = clazz.newInstance();
				mapToBean(map, bean);
				list.add(bean);
			}
		}
		return list;
	}

	/**
	 * map转对象
	 * 
	 * @Description
	 * @author Administrator
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	public static Object mapToObject(Map<String, Object> map,
			Class<?> beanClass) {
		if (map == null) {
            return null;
        }
		Object obj = null;
		try {
			obj = beanClass.newInstance();
			// BeanUtils.copyProperties(map, obj);
			org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		} catch (Exception e) {
			throw new RuntimeException("反射map到对象失败" + beanClass.getName());
		} finally {
			return obj;
		}
	}

	/**
	 * map转对象
	 * 
	 * @Description
	 * @author Administrator
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	public static <T> T mapToObj(Map<String, Object> map, Class<T> beanClass) {
		if (map == null) {
            return null;
        }
		T obj = null;
		try {
			obj = beanClass.newInstance();
			org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		} catch (Exception e) {
			throw new RuntimeException("反射map到对象失败" + beanClass.getName());
		} finally {
			return obj;
		}
	}

	/**
	 * map转对象
	 * 
	 * @Description
	 * @author Administrator
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static void mapToObject(Map<String, Object> map, Object target) {
		if (map == null) {
            return;
        }
		try {
			// BeanUtils.copyProperties(map, obj);
			org.apache.commons.beanutils.BeanUtils.populate(target, map);
		} catch (Exception e) {
			throw new RuntimeException(
					"反射map到对象失败" + target.getClass().getName());
		}
	}

	/**
	 * 对象转map
	 * 
	 * @Description
	 * @author zl
	 * @param obj
	 * @return
	 */
	public static Map<?, ?> objectToMap(Object obj) {
		if (obj == null) {
            return null;
        }
		return new org.apache.commons.beanutils.BeanMap(obj);
	}

	/**
	 * List[对象]转 List[MAP]
	 * 
	 * @Description
	 * @author Administrator
	 * @param list
	 * @return
	 */
	public static List<Map<?, ?>> listObjToListMap(List<?> list) {
		if (list==null||list.isEmpty()) {
			return Lists.newArrayList();
		}
		final List<Map<?, ?>> res = Lists.newArrayList();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			res.add(objectToMap(list.get(i)));
		}
		return res;
	}

	/**
	 * @Description
	 * @author Administrator
	 * @param listmap
	 * @param tClass
	 * @return
	 */
	public static <T> List<T> listMapToListObj(
			List<Map<String, Object>> listmap, Class<T> tClass) {
		if (listmap==null||listmap.isEmpty()) {
			return Lists.newArrayList();
		}
		List<T> list = Lists.newArrayList();
		int size = listmap.size();
		for (int i = 0; i < size; i++) {
			list.add(mapToObj(listmap.get(i), tClass));
		}
		return list;
	}


	/** 
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. 
     *  
     *@param clazz 
     *            clazz The class to introspect 
     * @param index 
     *            the Index of the generic ddeclaration,start from 0. 
     * @return the index generic declaration, or Object.class if cannot be 
     *         determined 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Class<Object> getSuperClassGenricType( final Class clazz, final int index) {  
          
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。  
        Type genType = clazz.getGenericSuperclass();  
  
        if (!(genType instanceof ParameterizedType)) {  
           return Object.class;  
        }  
        //返回表示此类型实际类型参数的 Type 对象的数组。  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
  
        if (index >= params.length || index < 0) {  
                     return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
              return Object.class;  
        }  
  
        return (Class) params[index];  
    }  
    

	/**
	 * Convenience method to instantiate a class using its no-arg constructor.
	 * 
	 * @param clazz
	 *            class to instantiate
	 * @return the new instance
	 * @throws BeanInstantiationException
	 *             if the bean cannot be instantiated
	 * @see Class#newInstance()
	 */
	@SuppressWarnings("unchecked")
	public static <T> T instantiate(Class<?> clazz)
			throws BeanInstantiationException {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz,
					"Specified class is an interface");
		}
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException ex) {
			throw new BeanInstantiationException(clazz,
					"Is it an abstract class?", ex);
		} catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(clazz,
					"Is the constructor accessible?", ex);
		}
	}
}
