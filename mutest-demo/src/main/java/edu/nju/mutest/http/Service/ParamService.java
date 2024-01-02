package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.ParamDao;
import edu.nju.mutest.http.Pojo.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParamService {

    @Autowired
    private ParamDao paramDao;

    public boolean addParam(Param param){
        paramDao.addParam(param);
        return true;
    }
}
