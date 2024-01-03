package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.DataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorAndParamService {

    private DataDao DataDao = new DataDao();

    public boolean add(String operator,String param){
        DataDao.setOperator(operator);
        return true;
    }
}
