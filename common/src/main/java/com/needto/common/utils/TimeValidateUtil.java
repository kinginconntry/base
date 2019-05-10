package com.needto.common.utils;

import com.needto.common.entity.AbstractTime;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Administrator
 * 校验一堆时间是否能通过
 */
public class TimeValidateUtil {

    public boolean validate(List<AbstractTime> times){
        if(CollectionUtils.isEmpty(times)){
            return true;
        }
        for(AbstractTime abstractTime : times){
            if(!abstractTime.validate()){
                if(!abstractTime.coExist()){
                    return false;
                }
            }
        }
        return true;
    }
}
