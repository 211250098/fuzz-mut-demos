package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import edu.nju.mutest.visitor.collector.UOIExpressionCollector;
import edu.nju.mutest.visitor.collector.cond.BooleanCond;
import edu.nju.mutest.visitor.collector.cond.IntegerCond;
import edu.nju.mutest.visitor.collector.cond.NumericCond;
import edu.nju.mutest.visitor.collector.cond.VariableCond;

import java.util.List;

public class UOI_Mutator extends AbstractMutator{
    private List<Expression> mutPoints = null;

    private List<CompilationUnit> mutants = new NodeList<>();

    public UOI_Mutator(CompilationUnit cu) {
        super(cu);
    }

    @Override
    public void locateMutationPoints() {
        mutPoints = null;
    }

    @Override
    public List<CompilationUnit> mutate() {
        if (mutPoints == null) {
            mutPoints = UOIExpressionCollector.collect(this.origCU);
        }

        for (Expression mp : mutPoints) {
            if (new IntegerCond().willCollect(mp)) {
                replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.BITWISE_COMPLEMENT));
            }
            if (new BooleanCond().willCollect(mp)) {
                replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.LOGICAL_COMPLEMENT));
            }
            if (new VariableCond().willCollect(mp)) {
                if (new NumericCond().willCollect(mp)) {
                    replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.PREFIX_INCREMENT));
                    replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.PREFIX_DECREMENT));
                    replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.POSTFIX_INCREMENT));
                    replaceNode(mp, new UnaryExpr(mp.clone(), UnaryExpr.Operator.POSTFIX_DECREMENT));
                }
                replaceNode(mp, new UnaryExpr(new EnclosedExpr(mp.clone()), UnaryExpr.Operator.MINUS));
            }
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
