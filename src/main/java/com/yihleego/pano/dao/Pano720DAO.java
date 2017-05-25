package com.yihleego.pano.dao;

import com.yihleego.pano.pojo.DO.Pano720DO;
import org.springframework.stereotype.Repository;

/**
 * Created by YihLeego on 17-5-24.
 */
@Repository
public interface Pano720DAO {
    int deleteByPrimaryKey(Integer id);

    int insert(Pano720DO record);

    int insertSelective(Pano720DO record);

    Pano720DO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Pano720DO record);

    int updateByPrimaryKey(Pano720DO record);
}
