package compile.il.ast;

import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

/**
 * Representación de las secuencias de sentencias.
 */
public class Sequence extends Stmt {
    public final Stmt[] statements;

    public Sequence(Stmt[] statements) {
        this.statements = statements;
    }

    @Override
    public String unparse() {
        String result = "{ ";
        for (Stmt statement : statements) result += statement.unparse() + " ";
        return result + "}";
    }

    @Override
    public State evaluate(State state) {
        for (Stmt statement : statements) state = statement.evaluate(state);
        return state;
    }

    @Override
    public Set<String> freeVariables(Set<String> vars) {
        for (Stmt statement : statements) vars = statement.freeVariables(vars);
        return vars;
    }

    @Override
    public int maxStackIL() {
        int result = 0;
        for (Stmt statement : statements) result = Math.max(result, statement.maxStackIL());
        return result;
    }

    @Override
    public CompilationContextIL compileIL(CompilationContextIL ctx) {
        ctx.codeIL.append("nop \n");
        for (Stmt s : statements) {
            ctx = s.compileIL(ctx);
        }
        return ctx;
    }

    @Override
    public String toString() {
        return "Sequence(" + Arrays.toString(statements) + ")";
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = result * 31 + Arrays.hashCode(this.statements);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sequence other = (Sequence) obj;
        return Arrays.equals(this.statements, other.statements);
    }

    public static Sequence generate(Random random, int min, int max) {
        Stmt[] statements;
        statements = new Stmt[random.nextInt(Math.max(0, max) + 1)];
        for (int i = 0; i < statements.length; i++) {
            statements[i] = Stmt.generate(random, min - 1, max - 1);
        }
        return new Sequence(statements);
    }

    @Override
    public Stmt optimization(State state) {
        ArrayList<Stmt> listStmt = new ArrayList<Stmt>();
        for (int i = 0; i < statements.length; i++) {
            Stmt s = statements[i].optimization(state);
            if (!(s instanceof Sequence && (((Sequence) s).statements.length == 0))) {
                listStmt.add(s);
            }
        }

        Stmt[] sec = new Stmt [listStmt.size()];
        for (int i = 0; i<sec.length;i++){
            sec[i] = listStmt.get(i);
        }
        return new Sequence(sec);

    }
}
