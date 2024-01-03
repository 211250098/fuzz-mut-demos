package edu.nju.mutest.mutator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import edu.nju.mutest.visitor.collector.BinaryExprCollector;

import java.util.List;

import static com.github.javaparser.ast.expr.BinaryExpr.Operator.*;

public class AOR_Mutator extends AbstractMutator{
    private List<BinaryExpr> mutPoints = null;
    private List<CompilationUnit> mutants = new NodeList<>();


    private final List<BinaryExpr.Operator> targetOps = List.of(
            PLUS, MINUS, MULTIPLY, DIVIDE, REMAINDER
    );

    public AOR_Mutator(CompilationUnit cu) {
        super(cu);
    }

    @Override
    public void locateMutationPoints() {
        mutPoints = null;
    }

    @Override
    public List<CompilationUnit> mutate() {
        if (this.mutPoints == null)
            mutPoints = BinaryExprCollector.collect(this.origCU);

        for (BinaryExpr mp : mutPoints) {
            if (targetOps.contains(mp.getOperator())) {
                for (BinaryExpr.Operator targetOp : targetOps) {
                    if (mp.getOperator() != targetOp) {
                        BinaryExpr.Operator oldOp = mp.getOperator();
                        mp.setOperator(targetOp);
                        CompilationUnit mutCU = this.origCU.clone();
                        mp.setOperator(oldOp);
                        mutants.add(mutCU);
                    }
                }
                replaceNode(mp, mp.getLeft());
                replaceNode(mp, mp.getRight());
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
