package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.VariationDao;
import edu.nju.mutest.http.Pojo.Variation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariationService {

    @Autowired
    private VariationDao variationDao;

    public List<Variation> getVariationList(){
        return variationDao.getVariationList();
    }
}
