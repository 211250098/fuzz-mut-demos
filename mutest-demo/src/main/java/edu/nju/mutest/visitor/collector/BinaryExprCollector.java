package edu.nju.mutest.visitor.collector;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Collect binary expressions
 * @see BinaryExpr
 */
public class BinaryExprCollector extends VoidVisitorAdapter<List<BinaryExpr>> {

    @Override
    public void visit(BinaryExpr n, List<BinaryExpr> arg) {
        super.visit(n, arg);
        arg.add(n);
    }

    public static List<BinaryExpr> collect(CompilationUnit cu) {
        BinaryExprCollector collector = new BinaryExprCollector();
        List<BinaryExpr> binaryExprList = new ArrayList<>();
        collector.visit(cu, binaryExprList);
        return new ArrayList<>(binaryExprList);
    }

}
