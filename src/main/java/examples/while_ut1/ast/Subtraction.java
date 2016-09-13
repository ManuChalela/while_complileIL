package examples.while_ut1.ast;

import examples.while_ut1.Logger;
import examples.while_ut1.analyzer.CheckState;
import examples.while_ut1.analyzer.ObjectState;

import java.util.*;

/**
 * Representación de restas.
 */
public class Subtraction extends Exp {
    public final Exp left;
    public final Exp right;

    public Subtraction(Exp left, Exp right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String unparse() {
        return "(" + left.unparse() + " - " + right.unparse() + ")";
    }

    @Override
    public String toString() {
        return "Subtraction(" + left + ", " + right + ")";
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
        Subtraction other = (Subtraction) obj;
        return (this.left == null ? other.left == null : this.left.equals(other.left))
                && (this.right == null ? other.right == null : this.right.equals(other.right));
    }

    @Override
    public Object evaluate(HashMap<String, Object> state) {
        return (Double) left.evaluate(state) - (Double) right.evaluate(state);
    }

    public Object check(CheckState state) {
        Object leftO = this.left.check(state);
        Object rightO = this.right.check(state);

        if (leftO == null || rightO == null) {
            Logger.log(this.getClass().getName(), "El compilador no se puede recuperar!");
        }

        switch ((ObjectState.Types) leftO) {
            case NUMERIC:
                if (rightO == ObjectState.Types.NUMERIC) {
                    return ObjectState.Types.NUMERIC;
                }
                Logger.log(this.getClass().getName(), "No se puede restar numeros con otros tipos de variables");
                return ObjectState.Types.NUMERIC;
            case STRING:
                if (rightO == ObjectState.Types.STRING) {
                    return ObjectState.Types.STRING;
                }
                Logger.log(this.getClass().getName(), "No se puede restar string con otros tipos de variables");
                return ObjectState.Types.NUMERIC;
            case BOOLEAN:
                Logger.log(this.getClass().getName(), "No se puede restar boolenaos");
                return ObjectState.Types.NUMERIC;
            default:
                Logger.log(this.getClass().getName(), "Tipo desconocido en resta");
                return ObjectState.Types.NUMERIC;
        }
    }
//
//	public static Subtraction generate(Random random, int min, int max) {
//		AExp left; AExp right;
//		left = AExp.generate(random, min-1, max-1);
//		right = AExp.generate(random, min-1, max-1);
//		return new Subtraction(left, right);
//	}
}
