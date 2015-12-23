package com.wm.service.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.wm.service.constants.CommConstants;
import com.wm.service.model.CommData;
import com.wm.service.model.CommParams;
import com.wm.service.model.TokenData;

/**
 * @note 基础管理层
 * @author wangmeng
 * @date 2015年6月14日 下午7:33:52
 */
public class BaseController {

    /**
     * 请求参数 { "comm": { "pid": "123456", "type": "3", "version": "1.0", "us":
     * "0" }, "body": { "loanId":"21" } }
     **/
    /** @ModelAttribute: 被此注解注释的方法会在此controller的每个方法前执行 **/
    /** 请求中获取所有参数对象，并将参数放入model中 **/
    @SuppressWarnings("unchecked")
    @ModelAttribute
    public void initReqParam(HttpServletRequest request, Model model) {
        CommParams<Map<String, Object>> appParams = (CommParams<Map<String, Object>>) request
                .getAttribute(CommConstants.APP_PARAMS);
        if (appParams != null) {
            model.addAttribute(CommConstants.APP_PARAMS, appParams);
        }
    }

    /** model中取出参数对象 **/
    @SuppressWarnings("unchecked")
    public CommParams<Map<String, Object>> getAppParams(Model model) {
        return (CommParams<Map<String, Object>>) model.asMap().get(CommConstants.APP_PARAMS);
    }

    /** 获取body中数据 中封装ip **/
    public Map<String, Object> getBodyData(Model model) {
        Map<String, Object> bodyMap = getAppParams(model).getBody();
        return bodyMap;
    }

    /** 获取comm中数据 **/
    public CommData getCommData(Model model) {
        return getAppParams(model).getCommData();
    }

    /** 获取tocken中数据 **/
    public TokenData getTokenData(Model model) {
        return getAppParams(model).getTokenData();
    }

    /** 获取tocken中userid **/
    public Long getUserId(Model model) {
        return getAppParams(model).getTokenData().getUserId();
    }

}
