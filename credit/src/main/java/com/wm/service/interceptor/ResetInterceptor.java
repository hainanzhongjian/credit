package com.wm.service.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wm.service.constants.CommConstants;
import com.wm.service.model.CommParams;
import com.wm.service.utils.ServeltUtils;

/**
 * @note 请求拦截器
 * @author wangmeng
 * @date 2015年6月14日 下午8:26:37
 */
@Component
public class ResetInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1:获取参数信息
        CommParams<Map<String, Object>> appParams = ServeltUtils.getRequestParams(request);
        // 2:值传递 添加IP地址
        appParams.getCommData().setIp(ServeltUtils.getIpAddr(request));
        request.setAttribute(CommConstants.APP_PARAMS, appParams);
        return true;
    }
}
