package compile.il.ast;

import compile.il.analyzer.ObjectState;
import compile.il.analyzer.Types;
import compile.il.behaviour.State;

import java.util.HashMap;

/**
 * Representación de cadenas de caracteres
 */
public class Str extends Exp {
    public final String string;

    public Str(String string) {
        this.string = string;
    }

    @Override
    public String unparse() {
        return string;
    }

    @Override
    public String toString() {
        return "Str(" + string + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Str str = (Str) o;

        return string != null ? string.equals(str.string) : str.string == null;
    }

    @Override
    public int hashCode() {
        return string != null ? string.hashCode() : 0;
    }

    public Object evaluate(HashMap<String, Object> state) {
        return this.string;
    }

    public Object check(State state) {
        return new ObjectState(Types.STRING, false);
    }
}
