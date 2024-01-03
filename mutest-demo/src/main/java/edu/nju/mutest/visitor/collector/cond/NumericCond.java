package edu.nju.mutest.visitor.collector.cond;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Set;

public class NumericCond implements CollectionCond<Expression>{
    private static final Set<String> Type = Set.of("short", "int", "long", "float", "double",
            "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double"
    );

    @Override
    public boolean willCollect(Expression expr) {
        ResolvedType resolvedType = expr instanceof MethodCallExpr ?
                ((MethodCallExpr) expr).resolve().getReturnType() : expr.calculateResolvedType();
        return Type.contains(resolvedType.describe());
    }
}
