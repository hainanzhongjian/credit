package com.wm.service.dao;

import java.util.List;

import com.wm.service.base.BaseDao;
import com.wm.service.model.Cdistrict;

public interface CdistrictDao extends BaseDao {

    int deleteByPrimaryKey(Long id);

    int insert(Cdistrict record);

    Cdistrict selectByPrimaryKey(Long id);

    List<Cdistrict> selectAll();

    int updateByPrimaryKey(Cdistrict record);

}