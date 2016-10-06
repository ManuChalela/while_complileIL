package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.Random;
import java.util.Set;

/**
 * Representación de conjunciones booleanas (AND).
 */
public class Conjunction extends BExp {
    public final BExp left;
    public final BExp right;

    public Conjunction(BExp left, BExp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String unparse() {
        return "(" + left.unparse() + " and " + right.unparse() + ")";
    }

    @Override
    public Boolean evaluate(State state) {
        return left.evaluate(state) && right.evaluate(state);
    }

    @Override
    public Set<String> freeVariables(Set<String> vars) {
        return right.freeVariables(left.freeVariables(vars));
    }

    @Override
    public int maxStackIL() {
        return Math.max(left.maxStackIL(), right.maxStackIL() + 1);
    }

    @Override
    public CompilationContextIL compileIL(CompilationContextIL ctx) {
        left.compileIL(ctx);
        String labelTrue = ctx.newLabel();
        String labelFalse = ctx.newLabel();
        ctx.codeIL.append("brfalse " + labelFalse + "\n");
        right.compileIL(ctx);
        ctx.codeIL.append("br" + labelTrue + "\n");
        ctx.codeIL.append(labelFalse + ": ldc.i4.0 \n");
        ctx.codeIL.append(labelTrue + ": nop \n");
        return ctx;
    }

    @Override
    public String toString() {
        return "Conjunction(" + left + ", " + right + ")";
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + (this.left == null ? 0 : this.left.hashCode());
        result = result * 31 + (this.right == null ? 0 : this.right.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Conjunction other = (Conjunction) obj;
        return (this.left == null ? other.left == null : this.left.equals(other.left))
                && (this.right == null ? other.right == null : this.right.equals(other.right));
    }

    public static Conjunction generate(Random random, int min, int max) {
        BExp left;
        BExp right;
        left = BExp.generate(random, min - 1, max - 1);
        right = BExp.generate(random, min - 1, max - 1);
        return new Conjunction(left, right);
    }
}
