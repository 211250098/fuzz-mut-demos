package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.OperatorAndParamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorAndParamService {

    @Autowired
    private OperatorAndParamDao operatorAndParamDao;

    public boolean add(String operator,String param){
        operatorAndParamDao.add(operator,param);
        return true;
    }
}
