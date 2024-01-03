package edu.nju.mutest.http.Dao;

import edu.nju.mutest.http.Pojo.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParamDao {

    @Autowired
    private List<Param> paramList;

    public boolean addParam(Param param){
        paramList.add(param);
        return true;
    }
}
