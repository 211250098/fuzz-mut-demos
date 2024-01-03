package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.ResultDao;
import edu.nju.mutest.http.Pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    @Autowired
    private ResultDao resultDao;

    public Result getResult(String id){
        return resultDao.getResult(id);
    }
}
