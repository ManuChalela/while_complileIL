package compile.il.ast;

import compile.il.Logger;
import compile.il.analyzer.ObjectState;
import compile.il.analyzer.Types;
import compile.il.behaviour.State;
import java.util.Set;
import compile.il.behaviour.CompilationContextIL;

import java.util.HashMap;

/**
 * Representación de divisiones.
 */
public class Division extends AExp {
    public final AExp left;
    public final AExp right;

    public Division(AExp left, AExp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String unparse() {
        return "(" + left.unparse() + " / " + right.unparse() + ")";
    }

    @Override
    public String toString() {
        return "Division(" + left + ", " + right + ")";
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
        Division other = (Division) obj;
        return (this.left == null ? other.left == null : this.left.equals(other.left))
                && (this.right == null ? other.right == null : this.right.equals(other.right));
    }


    @Override
    public Double evaluate(State state) {
        return (Double) left.evaluate(state) / (Double) right.evaluate(state);
    }

    @Override
    public Set<String> freeVariables(Set<String> vars) {
        return right.freeVariables(left.freeVariables(vars));
    }

    /*
    @Override
    public Object check(State state) {
        Object leftO = this.left.check(state);
        Object rightO = this.right.check(state);

        if (leftO == null || rightO == null) {
            Logger.log(this.getClass().getName(), "El compilador no se puede recuperar!");
        }

        switch ((Types) leftO) {
            case NUMERIC:
                if (rightO == Types.NUMERIC) return new ObjectState(Types.NUMERIC, true);
                Logger.log(this.getClass().getName(), "No se puede dividir numeros con otros tipos de variables");
                return new ObjectState(Types.NUMERIC, true);
            case STRING:
                Logger.log(this.getClass().getName(), "No se puede dividir con un string.");
                return new ObjectState(Types.NUMERIC, true);
            case BOOLEAN:
                Logger.log(this.getClass().getName(), "No se puede dividir con un booleano.");
                return new ObjectState(Types.NUMERIC, true);
            default:
                Logger.log(this.getClass().getName(), "No se puede dividir.");
                return new ObjectState(Types.NUMERIC, true);
        }
    }
    */
    public AExp optimization(State state) {
        AExp izq = this.left.optimization(state);
        AExp der = this.right.optimization(state);
        if (((Numeral) izq).number == 0) {
            return new Numeral(0.0);
        }
        if (izq instanceof Numeral && der instanceof Numeral) {
            return new Numeral(((Numeral) izq).number / ((Numeral) der).number);
        } else {

            if (((Numeral) der).number == 1) {
                return izq;
            }
            return new Division(izq, der);
        }
    }

    @Override
    public CompilationContextIL compileIL(CompilationContextIL ctx) {
        left.compileIL(ctx);
        right.compileIL(ctx);
        ctx.codeIL.append("div" + "\n");
        return ctx;
    }

    @Override
    public int maxStackIL() {
        return Math.max(left.maxStackIL(), right.maxStackIL() + 1);
    }


    //	public static Division generate(Random random, int min, int max) {
//		AExp left; AExp right;
//		left = AExp.generate(random, min-1, max-1);
//		right = AExp.generate(random, min-1, max-1);
//		return new Division(left, right);
//	}
}
