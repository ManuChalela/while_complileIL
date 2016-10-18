package compile.il;

import compile.il.ast.Stmt;
import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;
import compile.il.parser.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder source = new StringBuilder();
        for (String line; (line = in.readLine()) != null && line.length() > 0; ) {
            source.append(line).append("\n");
        }

        State state = new State();
        try {
            Stmt prog = (Stmt) (Parser.parse(source.toString()).value);

            System.out.println("// source = \n//\t" +
                    source.toString().replace("\n", "\n//\t"));
            state = prog.evaluate(state);
            System.out.println("// evaluation = " + state);
            System.out.println("Código sin optimizar");
            System.out.println(CompilationContextIL.compileIL(prog));
            System.out.println("Código optimizado");
            System.out.println(CompilationContextIL.compileIL(prog, state));
        } catch (Exception err) {
            System.err.print(err);
            err.printStackTrace();
        }
    }
}