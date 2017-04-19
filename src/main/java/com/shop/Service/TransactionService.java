package com.shop.Service;

import com.shop.Dao.TransactionDao;
import com.shop.Pojo.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Carpenter on 2017/3/7.
 */
@Service
public class TransactionService {
    @Autowired
    private TransactionDao transactionDao;

    public boolean insert(Transaction transaction){
        boolean flag = false;
        try  {
            flag = transactionDao.insert(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean delete(int id){
        boolean flag = false;
        try  {
            flag = transactionDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public Transaction selectLocation(int id){
        Transaction res = null;
        try {
            res = transactionDao.getLocation(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Transaction> selectAll(int user_id){
        List<Transaction> res = null;
        try {
            res = transactionDao.getTransactionByUser(user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
