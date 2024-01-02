package edu.nju.mutest.visitor.collector.cond;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;

public class VariableCond implements CollectionCond<Expression>{
    @Override
    public boolean willCollect(Expression expr) {
        return expr instanceof NameExpr;
    }
}
