package compile.il;

import compile.il.ast.Stmt;
import compile.il.behaviour.CompilationContextIL;
import compile.il.behaviour.State;
import compile.il.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {

        String input = read();
        while (!input.contains("exit")) {
            runCompiler(input);
            input = read();
        }
        System.exit(137);
    }

    private static String read() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder source = new StringBuilder();
        for (String line; (line = in.readLine()) != null && line.length() > 0; ) {
            source.append(line).append("\n");
        }

        return source.toString();
    }

    private static void runCompiler(String source) {
        State state = new State();
        try {
            Stmt prog = (Stmt) (Parser.parse(source).value);

            System.out.println("// source = \n//\t" +
                    source.replace("\n", "\n//\t"));
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