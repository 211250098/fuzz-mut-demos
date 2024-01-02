package edu.nju.mutest.visitor.collector;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.nju.mutest.visitor.collector.cond.NumericCond;

import java.util.List;
import java.util.stream.Collectors;

public class ExpressionCollector extends VoidVisitorAdapter<List<Expression>> {
    @Override
    public void visit(BooleanLiteralExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(CharLiteralExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(IntegerLiteralExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(LongLiteralExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(DoubleLiteralExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(NameExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(UnaryExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(BinaryExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(ConditionalExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(AssignExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    @Override
    public void visit(MethodCallExpr n, List<Expression> collector) {
        super.visit(n, collector);
        collector.add(n);
    }

    public static List<Expression> collect(CompilationUnit cu) {
        ExpressionCollector collector = new ExpressionCollector();
        List<Expression> ExprList = new NodeList<>();
        collector.visit(cu, ExprList);
        return ExprList.stream().filter(new NumericCond()::willCollect).collect(Collectors.toList());
    }
}
