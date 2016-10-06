package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.Random;
import java.util.Set;

/**
 * Representación de las negaciones de expresiones booleanas.
 */
public class Negation extends BExp {
    public final BExp condition;

    public Negation(BExp condition) {
        this.condition = condition;
    }

    @Override
    public String unparse() {
        return "(!" + condition.unparse() + ")";
    }

    @Override
    public Boolean evaluate(State state) {
        return !condition.evaluate(state);
    }

    @Override
    public Set<String> freeVariables(Set<String> vars) {
        return condition.freeVariables(vars);
    }

    @Override
    public int maxStackIL() {
        return condition.maxStackIL();
    }

    @Override
    public CompilationContextIL compileIL(CompilationContextIL ctx) {
        condition.compileIL(ctx);
        ctx.codeIL.append("neg" + "\n");
        return ctx;
    }

    @Override
    public String toString() {
        return "Negation(" + condition + ")";
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + (this.condition == null ? 0 : this.condition.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Negation other = (Negation) obj;
        return (this.condition == null ? other.condition == null : this.condition.equals(other.condition));
    }

    public static Negation generate(Random random, int min, int max) {
        BExp condition;
        condition = BExp.generate(random, min - 1, max - 1);
        return new Negation(condition);
    }
}
