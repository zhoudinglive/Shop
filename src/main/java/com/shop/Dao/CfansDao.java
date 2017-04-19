package com.shop.Dao;

import com.shop.Pojo.Cfans;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Carpenter on 2017/3/13.
 */
public interface CfansDao {
    @Insert("INSERT INTO cfans(uid, pid) VALUES(#{uid}, #{pid})")
    public boolean insert(Cfans cfans);

    @Delete("DELETE From cfans")
    public boolean delete();

    @Select("SELECT * FROM cfans WHERE uid = #{uid}")
    public List<Cfans> getAllRecommendByUid(int uid);

    @Select("SELECT COUNT(*) FROM cfans")
    public int getCount();
}
