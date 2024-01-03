package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.CaseDao;
import edu.nju.mutest.http.Pojo.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseService {

    @Autowired
    private CaseDao caseDao;

    public List<Case> getCaseList(){
        return caseDao.getCaseList();
    }

    public Case getCaseById(String id){
        return caseDao.getCaseById(id);
    }

    public boolean addCase(byte[] file){
        caseDao.addCase(file);
        return true;
    }

    public boolean updateCase(String id, byte[] file){
        return caseDao.updateCase(id,file);
    }

    public boolean removeCase(String id){
        caseDao.removeCase(id);
        return true;
    }
}
