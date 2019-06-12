package com.needto.simulate.service;

import com.needto.simulate.entity.Request;
import com.needto.simulate.entity.Response;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class SimulateService {

    @Autowired
    private BrowserService browserService;

    @Autowired
    private HttpClientService httpClientService;

    /**
     * 同步模拟
     * @param request
     * @return
     */
    public Response simulate(Request request){
        Assert.validateNull(request.getUrlInfo());
        if(request.isUseBrowser()){
            return browserService.syncExecute(request);
        }else{
            return httpClientService.execute(request);
        }
    }

    /**
     * 异步模拟
     * @param request
     */
    public void asyncSimulate(Request request){
        Assert.validateNull(request.getUrlInfo());
        if(request.isUseBrowser()){
            browserService.asyncExecute(request);
        }else{
            httpClientService.asyncExecute(request);
        }
    }
}
