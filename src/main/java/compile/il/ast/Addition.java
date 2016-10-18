package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.Random;
import java.util.Set;

/**
 * Representación de sumas.
 */
public class Addition extends AExp {
    public final AExp left;
    public final AExp right;

    public Addition(AExp left, AExp right) {
        this.left = left;
        this.right = right;
    }

    public static Addition generate(Random random, int min, int max) {
        AExp left;
        AExp right;
        left = AExp.generate(random, min - 1, max - 1);
        right = AExp.generate(random, min - 1, max - 1);
        return new Addition(left, right);
    }

    @Override
    public String unparse() {
        return "(" + left.unparse() + " + " + right.unparse() + ")";
    }

    @Override
    public Double evaluate(State state) {
        return left.evaluate(state) + right.evaluate(state);
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
        right.compileIL(ctx);
        ctx.codeIL.append("add" + "\n");
        return ctx;
    }

    @Override
    public AExp optimization(State state) {
        AExp izq = this.left.optimization(state);
        AExp der = this.right.optimization(state);
        if (izq instanceof Numeral && der instanceof Numeral) {
            if (((Numeral)izq).number == 0) {
                return new Numeral(((Numeral)der).number);
            } else if (((Numeral)der).number == 0) {
                return new Numeral(((Numeral)izq).number);
            } else {
                return new Numeral(((Numeral)izq).number + ((Numeral)der).number);
            }
//            return new Numeral(((Numeral) izq).number + ((Numeral) der).number);
        } else {
            return new Addition(izq, der);
        }
    }

    @Override
    public String toString() {
        return "Addition(" + left + ", " + right + ")";
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
        Addition other = (Addition) obj;
        return (this.left == null ? other.left == null : this.left.equals(other.left))
                && (this.right == null ? other.right == null : this.right.equals(other.right));
    }
}
