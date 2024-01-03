package edu.nju.mutest.http.Dao;

import edu.nju.mutest.http.Pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResultDao {

    @Autowired
    private List<Result> resultList;

    public Result getResult(Long id){
        for(Result result : resultList){
            if(result.getId() == null) continue;
            if(result.getId().equals(id)){
                return result;
            }
        }
        return null;
    }
}
