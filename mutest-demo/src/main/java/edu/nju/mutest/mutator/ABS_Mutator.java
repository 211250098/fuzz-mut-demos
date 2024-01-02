package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import edu.nju.mutest.visitor.collector.ExpressionCollector;
import edu.nju.mutest.visitor.collector.cond.NumericCond;

import java.util.List;

public class ABS_Mutator extends AbstractMutator{
    private List<Expression> mutPoints = null;

    private List<CompilationUnit> mutants = new NodeList<>();

    public ABS_Mutator(CompilationUnit cu) {super(cu);}

    @Override
    public void locateMutationPoints() {
        mutPoints = ExpressionCollector.collect(this.origCU);
    }

    @Override
    public List<CompilationUnit> mutate() {
        if (this.mutPoints == null)
            throw new RuntimeException("You must locate mutation points first!");

        for (Expression mp : mutPoints) {
            replaceNode(mp, new MethodCallExpr("Math.abs", mp.clone()));
            replaceNode(mp, new IntegerLiteralExpr("0"));
            replaceNode(mp, new UnaryExpr(new MethodCallExpr("Math.abs", mp.clone()), UnaryExpr.Operator.MINUS));
        }
        return mutants;
    }

    private void replaceNode(Expression oldExpr, Expression newExpr) {
        newExpr = new EnclosedExpr(newExpr);
        oldExpr.replace(newExpr);
        CompilationUnit mutCU = this.origCU.clone();
        newExpr.replace(oldExpr);
        mutants.add(mutCU);
    }
}
