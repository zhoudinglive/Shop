package com.shop.Service;

import com.shop.Dao.RecommendDao;
import com.shop.Pojo.Cfans;
import com.shop.Pojo.Recommend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCommand;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by Carpenter on 2017/3/7.
 */
@Service
public class RecommendService {
    @Autowired
    private RecommendDao recommendDao;

    @Autowired
    private CfansService cfansService;

    @Autowired
    private DataSource dataSource;

    final static int NEIGHBORHOOD_NUM = 2;
    final static int RECOMMENDER_NUM = 5;

    public boolean insert(Recommend recommend) {
        try {
            return recommendDao.insert(recommend);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(int user_id, int product_id) {
        try {
            return recommendDao.delete(user_id, product_id);
        } catch (Exception e) {
            return false;
        }
    }

    public List<Recommend> getRecommendByUserID(int user_id) {
        try {
            return recommendDao.getRecommendByUserId(user_id);
        } catch (Exception e) {
            return null;
        }
    }

    public int getMark(int user_id, int product_id) {
        int res = -1;
        Recommend recommend = null;
        try {
            recommend = recommendDao.getMark(user_id, product_id);
            res = recommend.getMark();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean updateMark(Recommend recommend) {
        try {
            return recommendDao.updateMark(recommend);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean itemCF(){
        try{
            DataModel dataModel = new MySQLJDBCDataModel(dataSource,
                    "recommend", "user_id", "product_id", "mark", null);
            ItemSimilarity itemSimilarity = new EuclideanDistanceSimilarity(dataModel);
            Recommender r = new GenericItemBasedRecommender(dataModel, itemSimilarity);
            LongPrimitiveIterator iter = dataModel.getUserIDs();

            if(cfansService.delete()){
                Cfans cfans = new Cfans();
                while (iter.hasNext()) {
                    int uid = (int) iter.nextLong();
                    List<RecommendedItem> list = r.recommend(uid, RECOMMENDER_NUM);
                    for (RecommendedItem ritem : list) {
                        cfans.setUid(uid);
                        cfans.setPid((int) ritem.getItemID());
                        cfansService.insert(cfans);
                    }
                }
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            return false;
        }
    }

    public boolean userCF() {
        try{
            DataModel dataModel = new MySQLJDBCDataModel(dataSource,
                    "recommend", "user_id", "product_id", "mark", null);
            UserSimilarity user = new EuclideanDistanceSimilarity(dataModel);
            NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, user, dataModel);
            Recommender r = new GenericUserBasedRecommender(dataModel, neighbor, user);
            LongPrimitiveIterator iter = dataModel.getUserIDs();

            if(cfansService.delete()){
                Cfans cfans = new Cfans();
                while (iter.hasNext()) {
                    int uid = (int) iter.nextLong();
                    List<RecommendedItem> list = r.recommend(uid, RECOMMENDER_NUM);
                    for (RecommendedItem ritem : list) {
                        cfans.setUid(uid);
                        cfans.setPid((int) ritem.getItemID());
                        cfansService.insert(cfans);
                    }
                }
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
