package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.Random;
import java.util.Set;

/**
 * Representación de constantes numéricas o numerales.
 */
public class Numeral extends AExp {
    public final Double number;

    public Numeral(Double number) {
        this.number = number;
    }

    @Override
    public String unparse() {
        return number.toString();
    }

    @Override
    public Double evaluate(State state) {
        return number;
    }

    @Override
    public Set<String> freeVariables(Set<String> vars) {
        return vars;
    }

    @Override
    public int maxStackIL() {
        return 1;
    }

    @Override
    public CompilationContextIL compileIL(CompilationContextIL ctx) {
        ctx.codeIL.append("ldc.r8 " + number + "\n");
        return ctx;
    }

    @Override
    public String toString() {
        return "Numeral(" + number + ")";
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + (this.number == null ? 0 : this.number.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Numeral other = (Numeral) obj;
        return (this.number == null ? other.number == null : this.number.equals(other.number));
    }

    public static Numeral generate(Random random, int min, int max) {
        Double number;
        number = Math.round(random.nextDouble() * 1000) / 100.0;
        return new Numeral(number);
    }
}