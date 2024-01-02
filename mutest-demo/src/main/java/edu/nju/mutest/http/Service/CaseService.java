package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.CaseDao;
import edu.nju.mutest.http.Pojo.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseService {

    @Autowired
    private CaseDao caseDao;

    public boolean addCase(Case c){
        caseDao.addCase(c);
        return true;
    }

    public boolean updateCase(Long id, Case c){
        return caseDao.updateCase(id,c);
    }

    public boolean removeCase(Case c){
        caseDao.removeCase(c);
        return true;
    }
}
