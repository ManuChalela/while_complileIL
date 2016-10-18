package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.Random;
import java.util.Set;

/**
 * Representación de usos de variable en expresiones.
 */
public class Variable extends AExp {
    public final String id;

    public Variable(String id) {
        this.id = id;
    }

    public static Variable generate(Random random, int min, int max) {
        String id;
        id = "" + "abcdefghijklmnopqrstuvwxyz".charAt(random.nextInt(26));
        return new Variable(id);
    }

    @Override
    public String unparse() {
        return id;
    }

    @Override
    public Double evaluate(State state) {
        return state.get(id);
    }

    @Override
    public Set<String> freeVariables(Set<String> vars) {
        vars.add(id);
        return vars;
    }

    @Override
    public int maxStackIL() {
        return 1;
    }

    @Override
    public CompilationContextIL compileIL(CompilationContextIL ctx) {
        if (id instanceof String) {
            ctx.codeIL.append("ldc." + "\n");
        }
        return ctx;
    }

    @Override
    public String toString() {
        return "Variable(" + id + ")";
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Variable other = (Variable) obj;
        return (this.id == null ? other.id == null : this.id.equals(other.id));
    }

    @Override
    public AExp optimization(State state) {
        if (state.variables.containsKey(this.id)) {
            return new Numeral(state.get(id));
        }
        return super.optimization(state);
    }
}
