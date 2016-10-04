package examples.compil2015ut4;

import java.util.*;
import java.io.*;
import examples.compil2015ut4.ast.*;
import examples.compil2015ut4.parser.*;
import examples.compil2015ut4.behaviour.*;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			StringBuilder source = new StringBuilder();
			for (String line; (line = in.readLine()) != null && line.length() > 0 ;) {
				source.append(line).append("\n");
			}
			
			State state = new State();
			try {
				Stmt prog = (Stmt)(Parser.parse(source.toString()).value);
				System.out.println("// source = \n//\t"+ 
					source.toString().replace("\n", "\n//\t"));
				state = prog.evaluate(state);
				System.out.println("// evaluation = "+ state);
				System.out.println(CompilationContextIL.compileIL(prog));
			} catch (Exception err) {
				System.err.print(err);
				err.printStackTrace();
			}
	}
}