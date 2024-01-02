package edu.nju.mutest.visitor.collector.cond;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Set;

public class BooleanCond implements CollectionCond<Expression>{
    private static final Set<String> validResolvedType = Set.of(
            "boolean",
            "java.lang.Boolean"
    );


    @Override
    public boolean willCollect(Expression expr) {
        ResolvedType resolvedType = expr instanceof MethodCallExpr ?
                ((MethodCallExpr) expr).resolve().getReturnType() : expr.calculateResolvedType();
        return validResolvedType.contains(resolvedType.describe());
    }
}
