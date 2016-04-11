package com.wm.service.dao;

import com.wm.service.model.SysBankInfo;

public interface SysBankInfoDao {

    public int insert(SysBankInfo record);

    public SysBankInfo selectByPrimaryKey(Long id);

    public int updateByPrimaryKey(SysBankInfo record);
}