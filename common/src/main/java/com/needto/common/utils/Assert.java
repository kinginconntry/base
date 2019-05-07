package com.needto.common.utils;

import com.needto.common.exception.ValidateException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author Administrator
 * 参数检查
 */
public class Assert {


    /**
     * 判断条件，如果条件为真，抛异常，如果条件为假，则无操作
     * @param flag
     * @param code
     * @param msg
     */
    public static void validateCondition(boolean flag, String code, String msg){
        if(flag){
            throw new ValidateException(code, msg);
        }
    }

    public static void validateCondition(boolean flag, String msg){
        if(flag){
            throw new ValidateException("", msg);
        }
    }

    public static void validateCondition(boolean flag){
        if(flag){
            throw new ValidateException("", "");
        }
    }

    /**
     * 验证null
     * @param object
     * @param code
     * @param msg
     */
    public static void validateNull(Object object, String code, String msg){
        if(object == null){
            throw new ValidateException(code, msg);
        }
    }

    public static void validateNull(Object object, String msg){
        validateNull(object, "", msg);
    }

    public static void validateNull(Object object){
        validateNull(object, "param can not be null");
    }

    /**
     * 验证空字符串
     * @param object
     * @param code
     * @param msg
     */
    public static void validateStringEmpty(Object object, String code, String msg){
        if(StringUtils.isEmpty(object)){
            throw new ValidateException(code, msg);
        }
    }

    public static void validateStringEmpty(Object object, String msg){
        validateStringEmpty(object, "", msg);
    }

    public static void validateStringEmpty(Object object){
        validateStringEmpty(object, "param can not be empty");
    }

    /**
     * 校验是否为空集合
     * @param co
     * @param code
     * @param msg
     */
    public static <T> void validateCollectionEmpty(Collection<T> co, String code, String msg){
        if(CollectionUtils.isEmpty(co)){
            throw new ValidateException(code, msg);
        }
    }

    public static <T> void validateCollectionEmpty(Collection<T> co, String msg){
        validateCollectionEmpty(co, "", msg);
    }

    public static <T> void validateCollectionEmpty(Collection<T> co){
        validateCollectionEmpty(co, "collection param can not be empty");
    }

    public static void validateNumScope(int num, int min, int max){
        if(num < min && num > max){
            throw new ValidateException("", "num can not less than " + min + " and num can not more than " + max);
        }
    }

    public static void validateNumScope(long num, long min, long max){
        if(num < min && num > max){
            throw new ValidateException("", "num can not less than " + min + " and num can not more than " + max);
        }
    }

    public static void validateNumScope(float num, float min, float max){
        if(num < min && num > max){
            throw new ValidateException("", "num can not less than " + min + " and num can not more than " + max);
        }
    }

    public static void validateNumScope(double num, double min, double max){
        if(num < min && num > max){
            throw new ValidateException("", "num can not less than " + min + " and num can not more than " + max);
        }
    }

    public static void validateLessThan(int num, int limit){
        if(num < limit){
            throw new ValidateException("", "num can not less than " + limit);
        }
    }

    public static void validateMoreThan(int num, int limit){
        if(num > limit){
            throw new ValidateException("", "num can not more than " + limit);
        }
    }
}
