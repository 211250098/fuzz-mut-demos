package edu.nju.mutest.http.Dao;

import edu.nju.mutest.http.Pojo.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Repository
public class CaseDao{

    @Autowired
    private List<Case> caseList;

    public List<Case> getCaseList(){
        caseList.removeIf(c -> c.getId() == null);
        return caseList;
    }

    public Case getCaseById(String id){
        for(Case c : caseList){
            if(c.getId() == null)continue;
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }

    public boolean addCase(byte[] file){
        Random random = new Random();
        caseList.add(new Case(String.valueOf(Math.abs(random.nextLong())),file));
        return true;
    }

    public boolean removeCase(String id){
        for(Case aCase : caseList){
            if(aCase.getId().equals(id)){
                caseList.remove(aCase);
                return true;
            }
        }
        return true;
    }

    public boolean updateCase(String id,byte[] file){
        for(Case aCase : caseList){
            if(aCase.getId().equals(id)){
                aCase.setFile(file);
                return true;
            }
        }
        return false;
    }
}
