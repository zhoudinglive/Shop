package com.shop;

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
import org.apache.mahout.common.RandomUtils;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class UserBased {

    final static int NEIGHBORHOOD_NUM = 2;
    final static int RECOMMENDER_NUM = 2;

    /*
    public static void main(String[] args) throws IOException, TasteException {
        String file = "src/data/testCF.csv";
        DataModel model = new FileDataModel(new File(file));
        
        UserSimilarity user = new EuclideanDistanceSimilarity(model);
        NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, user, model);
        Recommender r = new GenericUserBasedRecommender(model, neighbor, user);
        LongPrimitiveIterator iter = model.getUserIDs();

        while (iter.hasNext()) {
            long uid = iter.nextLong();
            List<RecommendedItem> list = r.recommend(uid, RECOMMENDER_NUM);
            System.out.print("uid:" + uid);
            for (RecommendedItem ritem : list) {
                System.out.print("(" + ritem.getItemID() + ", " + ritem.getValue() + ")");
            }
            System.out.println();
        }
    }
    */

    public static void main(String[] args) throws TasteException, IOException {
        RandomUtils.useTestSeed();
        String file = "src/testCF.csv";
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("115.159.90.204");
        dataSource.setUser("web_user");
        dataSource.setPassword("2014211xxx");
        dataSource.setDatabaseName("shop");
        //DataModel dataModel = RecommendFactory.buildDataModel(file);
        JDBCDataModel jdbcDataModel;
        DataModel dataModel = new MySQLJDBCDataModel(dataSource, "recommend", "user_id", "product_id", "mark", null);
        //DataModel dataModel = new FileDataModel(new File(file));
        System.out.println(dataModel);
        userCF(dataModel);
    }


    public static void itemCF(DataModel dataModel) throws TasteException {
        ItemSimilarity itemSimilarity = new EuclideanDistanceSimilarity(dataModel);
        Recommender r = new GenericItemBasedRecommender(dataModel, itemSimilarity);
        LongPrimitiveIterator iter = dataModel.getUserIDs();

        while (iter.hasNext()) {
            long uid = iter.nextLong();
            List<RecommendedItem> list = r.recommend(uid, RECOMMENDER_NUM);
            System.out.print("uid:" + uid);
            for (RecommendedItem ritem : list) {
                System.out.print("(" + ritem.getItemID() + ", " + ritem.getValue() + ")");
            }
            System.out.println();
        }
    }

    public static void userCF(DataModel dataModel) throws TasteException {
        UserSimilarity user = new EuclideanDistanceSimilarity(dataModel);
        NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, user, dataModel);
        Recommender r = new GenericUserBasedRecommender(dataModel, neighbor, user);
        LongPrimitiveIterator iter = dataModel.getUserIDs();

        while (iter.hasNext()) {
            long uid = iter.nextLong();
            List<RecommendedItem> list = r.recommend(uid, RECOMMENDER_NUM);
            System.out.print("uid:" + uid);
            for (RecommendedItem ritem : list) {
                System.out.print("(" + ritem.getItemID() + ", " + ritem.getValue() + ")");
            }
            System.out.println();
        }
    }


} 