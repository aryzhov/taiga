package taiga.sablecc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class FixIt {

//	public static void main(String[] args) throws IOException {
//		System.out.println("Hello");
//		// generated sources location
//		File f = new File(args[0]);
//		fix(f, "lexer", "lexer.dat", "Lexer.java", "LexerDataInputStream");
//		fix(f, "parser", "parser.dat", "Parser.java", "ParserDataInputStream");
//	}

	public FixIt(File dir, String pkg, String dataFile, String srcFile, String newClass) throws IOException {
		File pkgDir = new File(dir, pkg);
		File data = new File(pkgDir, dataFile);
		FileInputStream is = new FileInputStream(data); 
		try {
			PrintWriter pw = new PrintWriter(new File(pkgDir, newClass+".java"), "US-ASCII");
			try {
				pw.println("package taiga.parser."+pkg+";");
				pw.println();
				pw.println("import taiga.parser.DataInputStream;");
				pw.println();
				pw.println("class " + newClass + " extends DataInputStream {");
				pw.println("    private static final byte[] data = new byte[] {");
				boolean first = true;
				int n = 0;
				for(int b = is.read(); b != -1; b = is.read()) {
					if(first) 
						first = false;
					else {
						if (++n % 40 == 0) {
							pw.println();
							pw.print("            ");
						}
						pw.print(",");
					}	
					pw.print(Integer.toString((byte)b));
				}
				pw.println("};");

				pw.println("    public " + newClass + "() {");
				pw.print("        super(data);");
				pw.println("    }");
				pw.println("}");
			} finally {
				pw.close();
			}
		} finally {
			is.close();
		}
	}

	
	public static String readAll(Reader r) throws IOException {
		try {
			StringBuilder sb = new StringBuilder();
			for(int c = r.read(); c >= 0; c = r.read()) {
				sb.append((char)c);
			}
			return sb.toString();
		} finally {
			try {r.close();} catch (IOException e) {}
		}
	}
	
}
