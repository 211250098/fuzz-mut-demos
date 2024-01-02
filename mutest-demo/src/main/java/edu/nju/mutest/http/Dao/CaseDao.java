package edu.nju.mutest.http.Dao;

import edu.nju.mutest.http.Pojo.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CaseDao{

    @Autowired
    private List<Case> caseList;

    public boolean addCase(Case c){
        caseList.add(c);
        return true;
    }

    public List<Case> getCase(){
        return caseList;
    }

    public boolean removeCase(Case c){
        caseList.remove(c);
        return true;
    }

    public boolean updateCase(Long id,Case c){
        for(Case aCase : caseList){
            if(aCase.getId().equals(id)){
                caseList.remove(aCase);
                caseList.add(c);
                return true;
            }
        }
        return false;
    }
}
