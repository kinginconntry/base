package com.needto.quartz.service;

import com.needto.dao.common.CommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class ScheduleService {

    @Autowired
    private CommonDao commonDao;
}
