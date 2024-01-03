package edu.nju.mutest.http.Dao;

import org.springframework.stereotype.Repository;

@Repository
public class DataDao {
    public static String op;

    public static String[][] res;

    public boolean add(String operator,String param){
        op = operator;
        return true;
    }

    public String getOperator() {
        return op;
    }

    public void setOperator(String opr) {
        op = opr;
    }

    public void setRes(String[][] r) {
        res = r;
    }

    public String[][] getRes() {
        return res;
    }
}
