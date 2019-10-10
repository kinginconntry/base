package com.needto.web.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ErrorPageProducer {

    String DEFAULT_ERROR_PATH_PREFIX = "/error";

    default String path(Integer status, HttpServletRequest request, HttpServletResponse response, Exception e){ return DEFAULT_ERROR_PATH_PREFIX + "/" + status; }
}
