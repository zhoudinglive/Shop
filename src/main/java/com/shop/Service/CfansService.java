package com.shop.Service;

import com.shop.Dao.CfansDao;
import com.shop.Pojo.Cfans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Carpenter on 2017/3/13.
 */
@Service
public class CfansService {
    @Autowired
    private CfansDao cfansDao;

    public boolean insert(Cfans cfans){
        try{
            return cfansDao.insert(cfans);
        }catch (Exception e){
            return false;
        }
    }

    public boolean delete(){
        try{
            if(cfansDao.getCount() == 0){
                return true;
            }
            return cfansDao.delete();
        }catch (Exception e){
            return false;
        }
    }

    public List<Cfans> getAllRecommendByUid(int uid){
        try{
            return cfansDao.getAllRecommendByUid(uid);
        }catch (Exception e){
            return null;
        }
    }
}
