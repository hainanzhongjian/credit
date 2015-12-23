package com.wm.service.manager;

import com.wm.service.dao.ComRegionDao;
import com.wm.service.model.ComRegion;
import com.wm.service.model.GeoAddressComponent;
import com.wm.service.model.Geocoding;
import com.wm.service.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @note manager层：将service层公用的方法拉倒manager层，防止循环调用
 * @auther wangmeng
 * @date 2015/10/22 11:15
 */
@Component
public class ComRegionManager {

    private static Logger logger = Logger.getLogger(ComRegionManager.class);

    @Autowired
    private ComRegionDao comRegionDao;

    /**
     * @note 验证Geocoding中的位置节点不存在
     * @param geocoding
     * @return
     */
    public boolean pointsIsNotExists(Geocoding geocoding){
        // 1：参数检验
        if(geocoding == null || geocoding.getAddressComponentObj() == null){
            logger.info("ComRegionManager-pointsIsExists:参数检验geocoding=null");
            return false;
        }
        GeoAddressComponent obj = geocoding.getAddressComponentObj();
        if (StringUtils.isEmpty(obj.getProvince()) || StringUtils.isEmpty(obj.getCity()) || StringUtils.isEmpty(obj.getDistrict())){
            logger.info("ComRegionManager-pointsIsExists:参数检验province、city、district=null");
            return false;
        }
        // 2:获取根节点
        ComRegion region = comRegionDao.selectRootPoints();
        // 3:验证省节点是否存在06
        region = comRegionDao.selectNextByPointsLayerName(region.getLeft(), region.getRight(), region.getLayer(), obj.getProvince());
        if (region == null ){
            logger.info("ComRegionManager-pointsIsExists:验证省节点不存在");
            return true;
        }
        // 4:验证市节点是否存在
        region = comRegionDao.selectNextByPointsLayerName(region.getLeft(), region.getRight(), region.getLayer(), obj.getCity());
        if (region == null ){
            logger.info("ComRegionManager-pointsIsExists:验证市节点不存在");
            return true;
        }
        // 4:验证县节点是否存在
        region = comRegionDao.selectNextByPointsLayerName(region.getLeft(), region.getRight(), region.getLayer(), obj.getDistrict());
        if (region == null ){
            logger.info("ComRegionManager-pointsIsExists:验证县节点不存在");
            return true;
        }
        return false;
    }

    /**
     * @note 插入省节点
     * @param record
     * @return
     */
    public int insertProvincePoints(ComRegion record){
        //1：验证省节点是否存在
        return 0;
    }

    public int insert(ComRegion record){
        return comRegionDao.insert(record);
    }

    public ComRegion selectByPrimaryKey(Long id){
        return comRegionDao.selectByPrimaryKey(id);
    }

    public List<ComRegion> selectByNameLayer(String name, int layer){
        return comRegionDao.selectByNameLayer(name, layer);
    }

    public List<ComRegion> selectNextByPointsLayer(int left, int right , int layer){
        return comRegionDao.selectNextByPointsLayer(left,right,layer);
    }

    public List<ComRegion> selectAll(){
        return comRegionDao.selectAll();
    }

    public int updateByPrimaryKey(ComRegion record){
        return comRegionDao.updateByPrimaryKey(record);
    }
}