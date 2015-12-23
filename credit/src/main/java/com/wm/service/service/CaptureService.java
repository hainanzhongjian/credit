package com.wm.service.service;

import com.wm.service.manager.ComRegionManager;
import com.wm.service.model.Geocoding;
import com.wm.service.model.LonLat;
import com.wm.service.utils.BaiduUtils;
import com.wm.service.utils.CircleUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @note 抓取service层
 * @author wangmeng
 * @date 2015年7月29日下午8:34:14
 */
@Service
public class CaptureService {

    private static Logger logger = Logger.getLogger(CaptureService.class);

    @Autowired
    private ComRegionManager comRegionManager;

    /**
     * @note 抓取百度行政区
     * @author wangmeng
     * @date 2015年8月1日下午5:26:41
     */
    public void captureBaiduDistrict() {
        for (int i = 0; i < 100; i++) {
            // 1:获取一个圆的经纬度列表
            List<LonLat> lonLatList = CircleUtils.getOneCircleLonLatList(i);
            if ((lonLatList == null) || (lonLatList.size() == 1)) {
                logger.info("CaptureService-captureBaiduDistrict:圆的经纬度列表为null");
                return ;
            }
            // 2:调用百度接口获取位置信息，保存入库
            for (LonLat lonLat : lonLatList){
                // 2.1:调用百度接口获取位置信息
                Geocoding geocoding = BaiduUtils.parseLnyLat(lonLat.getLon() + "," + lonLat.getLat());
                // 2.2:验证省市县是否存在
                boolean boo = comRegionManager.pointsIsNotExists(geocoding);
                if(boo){
                    logger.info("CaptureService-captureBaiduDistrict:位置已经存在");
                }

            }
        }
    }
}

