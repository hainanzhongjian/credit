package com.wm.service.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.wm.service.utils.ServeltUtils;

/**
 * @note 异常处理层
 * @author wangmeng
 * @date 2015年6月14日 下午10:05:36
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    /*
     * 第4个参数表示对哪种类型的异常进行处理，如果想同时对多种异常进行处理，可以把它换成一个异常数组
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        // 1：定义：默认的错误页面
        ModelAndView mav = new ModelAndView("/tip/intro");
        try {
            if (!(handler instanceof HandlerMethod)) {
                return mav;
            }
            // 2：方法中是否有注解ResponseBody 有此注解 返回code msg
            HandlerMethod method = (HandlerMethod) handler;
            ResponseBody resbody = method.getMethodAnnotation(ResponseBody.class);
            if (resbody != null) {
                // 级别下也能看到error信息
                ServeltUtils.sendResponse(response, ex.getMessage());
                return null;
            }
            // 3：无注解ResponseBody 返回错误页面
        } catch (IOException e) {
            return mav;
        }
        return mav;
    }
}
