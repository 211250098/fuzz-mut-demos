package edu.nju.mutest.http.Dao;

import edu.nju.mutest.http.Pojo.Variation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VariationDao {

    @Autowired
    private List<Variation> variationList;

    public List<Variation> getVariationList(){
        variationList.removeIf(v -> v.id.isEmpty());
        return variationList;
    }
}
