package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.Random;
import java.util.Set;

/**
 * Representación de las asignaciones de valores a variables.
 */
public class Assignment extends Stmt {
    public final String id;
    public final AExp expression;

    public Assignment(String id, AExp expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public String unparse() {
        return id + " = " + expression.unparse() + "; ";
    }

    @Override
    public State evaluate(State state) {
        state.set(id, expression.evaluate(state));
        return state;
    }

    @Override
    public Set<String> freeVariables(Set<String> vars) {
        vars = expression.freeVariables(vars);
        vars.add(id);
        return vars;
    }

    @Override
    public int maxStackIL() {
        return expression.maxStackIL();
    }

    @Override
    public CompilationContextIL compileIL(CompilationContextIL ctx) {
        expression.compileIL(ctx);
        ctx.codeIL.append("stloc " + ctx.indexVariables(id) + "\n");
        return ctx;
    }

    @Override
    public String toString() {
        return "Assignment(" + id + ", " + expression + ")";
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + (this.id == null ? 0 : this.id.hashCode());
        result = result * 31 + (this.expression == null ? 0 : this.expression.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Assignment other = (Assignment) obj;
        return (this.id == null ? other.id == null : this.id.equals(other.id))
                && (this.expression == null ? other.expression == null : this.expression.equals(other.expression));
    }

    public static Assignment generate(Random random, int min, int max) {
        String id;
        AExp expression;
        id = "" + "abcdefghijklmnopqrstuvwxyz".charAt(random.nextInt(26));
        expression = AExp.generate(random, min - 1, max - 1);
        return new Assignment(id, expression);
    }
}
