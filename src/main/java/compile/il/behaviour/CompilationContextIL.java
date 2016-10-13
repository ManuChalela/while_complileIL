package compile.il.behaviour;

import compile.il.ast.Stmt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class CompilationContextIL {
    public final List<String> variables = new ArrayList<String>();
    public final int maxStack;
    public final StringBuilder codeIL = new StringBuilder();

    private int currentLabel = 0;

    public CompilationContextIL(Stmt prog) {
        this(prog.freeVariables(new HashSet<String>()), prog.maxStackIL());
    }

    public CompilationContextIL(Collection<String> variables, int maxStack) {
        this.variables.addAll(variables);
        this.maxStack = maxStack;
    }

    public int indexVariables(String identificador) {
        return variables.indexOf(identificador);
    }

    /**
     * Usar este método para obtener un número único de etiqueta a la hora de
     * compilar construcciones que usan saltos.
     */
    public String newLabel() {
        return Integer.toString(currentLabel++, 16);
    }

    /**
     * Este método se utiliza para generar el código IL y obtener como un
     * String.
     */
    public static String compileIL(Stmt prog, State state) {
        prog = prog.optimization(state);

        CompilationContextIL ctx = new CompilationContextIL(prog);
        ctx.codeIL.append("// variables = " + ctx.variables + "\n");
        ctx.codeIL.append("// maxStack =  " + ctx.maxStack + "\n");

        prog.compileIL(ctx);
        return ctx.codeIL.toString();
    }

    /**
     * Este método se utiliza para generar el código IL y obtener como un
     * String.
     */
    public static String compileIL(Stmt prog) {
        CompilationContextIL ctx = new CompilationContextIL(prog);
        ctx.codeIL.append("// variables = " + ctx.variables + "\n");
        ctx.codeIL.append("// maxStack =  " + ctx.maxStack + "\n");

        prog.compileIL(ctx);
        return ctx.codeIL.toString();
    }
}
