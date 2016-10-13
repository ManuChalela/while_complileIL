package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.Random;
import java.util.Set;

/**
 * Representación de las comparaciones por menor o igual.
 */
public class CompareLessOrEqual extends BExp {
    public final AExp left;
    public final AExp right;

    public CompareLessOrEqual(AExp left, AExp right) {
        this.left = left;
        this.right = right;
    }

    public static CompareLessOrEqual generate(Random random, int min, int max) {
        AExp left;
        AExp right;
        left = AExp.generate(random, min - 1, max - 1);
        right = AExp.generate(random, min - 1, max - 1);
        return new CompareLessOrEqual(left, right);
    }

    @Override
    public String unparse() {
        return "(" + left.unparse() + " <= " + right.unparse() + ")";
    }

    @Override
    public Boolean evaluate(State state) {
        return left.evaluate(state) <= right.evaluate(state);
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
        right.compileIL(ctx);
        left.compileIL(ctx);
        ctx.codeIL.append("cgt\n");
        ctx.codeIL.append("ldc.i4.0\n");
        ctx.codeIL.append("ceq\n");
        return ctx;
    }

    @Override
    public String toString() {
        return "CompareLessOrEqual(" + left + ", " + right + ")";
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
        CompareLessOrEqual other = (CompareLessOrEqual) obj;
        return (this.left == null ? other.left == null : this.left.equals(other.left))
                && (this.right == null ? other.right == null : this.right.equals(other.right));
    }

    @Override
    public BExp optimization(State state) {
        AExp e1 = left.optimization(state);
        AExp e2 = right.optimization(state);
        if (e1 instanceof Numeral && e2 instanceof Numeral) {
            return new TruthValue(((Numeral) e1).number <= ((Numeral) e2).number);
        }
        return new CompareEqual(e1, e2);
    }
}
