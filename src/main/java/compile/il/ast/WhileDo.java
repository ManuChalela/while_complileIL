package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.Random;
import java.util.Set;

/**
 * Representación de las iteraciones while-do.
 */
public class WhileDo extends Stmt {
    public final BExp condition;
    public final Stmt body;

    public WhileDo(BExp condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    public static WhileDo generate(Random random, int min, int max) {
        BExp condition;
        Stmt body;
        condition = BExp.generate(random, min - 1, max - 1);
        body = Stmt.generate(random, min - 1, max - 1);
        return new WhileDo(condition, body);
    }

    @Override
    public String unparse() {
        return "while " + condition.unparse() + " do { " + body.unparse() + " }";
    }

    @Override
    public State evaluate(State state) {
        while (condition.evaluate(state)) state = body.evaluate(state);
        return state;
    }

    @Override
    public Set<String> freeVariables(Set<String> vars) {
        vars = condition.freeVariables(vars);
        return body.freeVariables(vars);
    }

    @Override
    public int maxStackIL() {
        return Math.max(condition.maxStackIL(), body.maxStackIL());
    }

    @Override
    public CompilationContextIL compileIL(CompilationContextIL ctx) {
        String labelTrue = ctx.newLabel();
        String labelFalse = ctx.newLabel();
        ctx.codeIL.append(labelTrue + ": nop \n");
        condition.compileIL(ctx);
        ctx.codeIL.append("brfalse " + labelFalse + " \n");
        body.compileIL(ctx);
        ctx.codeIL.append("br " + labelTrue + " \n");
        ctx.codeIL.append(labelFalse + ": nop \n");
        return ctx;
    }

    @Override
    public String toString() {
        return "WhileDo(" + condition + ", " + body + ")";
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + (this.condition == null ? 0 : this.condition.hashCode());
        result = result * 31 + (this.body == null ? 0 : this.body.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WhileDo other = (WhileDo) obj;
        return (this.condition == null ? other.condition == null : this.condition.equals(other.condition))
                && (this.body == null ? other.body == null : this.body.equals(other.body));
    }

    @Override
    public Stmt optimization(State state) {
        state.variables.clear();
        BExp conditionOpt = condition.optimization(state);
        Stmt bodyOpt = body.optimization(state);
        state.variables.clear();
        return new WhileDo(conditionOpt, bodyOpt);
    }
}
