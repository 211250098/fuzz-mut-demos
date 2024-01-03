package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.OperatorAndParamDao;
import edu.nju.mutest.http.Pojo.OperatorAndParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorAndParamService {

    @Autowired
    private OperatorAndParamDao operatorAndParamDao;

    public boolean add(OperatorAndParam operatorAndParam){
        operatorAndParamDao.add(operatorAndParam);
        return true;
    }
}
