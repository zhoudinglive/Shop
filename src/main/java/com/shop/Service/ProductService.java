package com.shop.Service;

import com.shop.Dao.ProductDao;
import com.shop.Pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    private String makeTitle(String title) {
        String res = "%";
        String[] temp = title.split(" ");
        for (int i = 0; i < temp.length; i++) {
            res += temp[i] + "%";
        }
        System.gc();
        return res;
    }

    public boolean insert(Product product) {
        boolean flag = false;
        try {
            flag = productDao.insert(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean delete(int id) {
        boolean flag = false;
        try {
            flag = productDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public int getProductCountByTitle(String title) {
        int res = 0;
        try {
            res = productDao.getProductCountByTitle(makeTitle(title));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Product> getProductByTitle_id(String title, int start, int num) {
        List<Product> res = null;
        try {
            res = productDao.getProductsByTitle_id(makeTitle(title), start, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Product> getProductByTitle_saleDesc(String title, int start, int num) {
        List<Product> res = null;
        try {
            res = productDao.getProductsByTitle_saleDesc(makeTitle(title), start, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Product> getProductByTitle_saleAsc(String title, int start, int num) {
        List<Product> res = null;
        try {
            res = productDao.getProductsByTitle_saleAsc(makeTitle(title), start, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Product> getProductByTitle_scoreDesc(String title, int start, int num) {
        List<Product> res = null;
        try {
            res = productDao.getProductsByTitle_scoreDesc(makeTitle(title), start, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Product> getProductByTitle_scoreAsc(String title, int start, int num) {
        List<Product> res = null;
        try {
            res = productDao.getProductsByTitle_scoreAsc(makeTitle(title), start, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Product> getProductByTitle_priceDesc(String title, int start, int num) {
        List<Product> res = null;
        try {
            res = productDao.getProductsByTitle_priceDesc(makeTitle(title), start, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Product> getProductByTitle_priceAsc(String title, int start, int num) {
        List<Product> res = null;
        try {
            res = productDao.getProductsByTitle_priceAsc(makeTitle(title), start, num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public Product getProductById(int id) {
        Product res = null;
        try {
            res = productDao.getProductByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean update(Product product) {
        boolean flag = false;
        try {
            flag = productDao.update(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


}
