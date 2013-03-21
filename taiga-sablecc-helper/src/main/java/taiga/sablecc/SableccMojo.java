package taiga.sablecc;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Says "Hi" to the user.
 */
@Mojo(name = "generate")
@Execute( goal = "generate", phase = LifecyclePhase.GENERATE_SOURCES )
public class SableccMojo extends AbstractMojo
{
	 /**
     * The greeting to display.
     */
    @Parameter(property = "package", defaultValue = "taiga.parser")
    private String packageName;	
    
    public void execute() throws MojoExecutionException
    {
    	try {
	    	File sourcesDir = new File("target/generated-sources/sablecc"); 
			if(!sourcesDir.exists())
				throw new MojoExecutionException("No generated sources found:"+sourcesDir.getCanonicalPath());
	    	File f = new File(sourcesDir, packageName.replace('.', '/'));
			fix(f, "lexer", "lexer.dat", "Lexer.java", "LexerDataInputStream");
			fix(f, "parser", "parser.dat", "Parser.java", "ParserDataInputStream");
    	} catch (IOException ex) {
    		throw new MojoExecutionException("Can't fix", ex);
    	}
        getLog().info( "The generted parser is now GWT compatible" );
    }
    
    
	private void fix(File dir, String pkg, String dataFileName, String srcFileName, String newClass) throws IOException {
		File pkgDir = new File(dir, pkg);
		File dataFile = new File(pkgDir, dataFileName);
		FileInputStream is = new FileInputStream(dataFile); 
		try {
			PrintWriter pw = new PrintWriter(new File(pkgDir, newClass+".java"), "US-ASCII");
			try {
				pw.println("package taiga.parser."+pkg+";");
				pw.println();
				pw.println("import taiga.parser.DataInputStream;");
				pw.println();
				pw.println("class " + newClass + " extends DataInputStream {");
				pw.println();
				pw.println("    private static final byte[] data = {");
				boolean first = true;
				int n = 0;
				int zeros = 0;
				int len = 0;
				for(int b = is.read(); b != -1; b = is.read()) {
					len++;
					if(b == 0 && zeros < 127) {
						zeros++;
						continue;
					}
					if(first) 
						first = false;
					else {
						pw.print(",");
					}	
					if (n++ % 20 == 0) {
						pw.println();
						pw.print("            ");
					}
					pw.print(Integer.toString(zeros));
					pw.print(",");
					pw.print(Integer.toString((byte)b));
					zeros = 0;
				}
				pw.println("};");

				pw.println("    public " + newClass + "() {");
				pw.println("        super("+len+", data);");
				pw.println("    }");
				pw.println("}");
			} finally {
				pw.close();
			}
		} finally {
			is.close();
		}
		
		File srcFile = new File(pkgDir, srcFileName);
		String src = readFile(srcFile);
		src = src.replaceAll("\\QDataInputStream s = new DataInputStream(\\E", newClass + " s = new "+ newClass + "();");
		src = src.replaceAll("\\Qnew BufferedInputStream(\\E", "");
		src = src.replaceAll("\\w+\\Q.class.getResourceAsStream(\\E.*;", "");
		src = src.replaceAll("\\Qimport java.io.DataInputStream;\\E", "");
		src = src.replaceAll("\\Qimport java.io.BufferedInputStream;\\E", "");
		writeFile(srcFile, src);
	}

	public static String readFile(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f)); 
		try {
			StringBuilder sb = new StringBuilder();
			for(int c = br.read(); c >= 0; c = br.read()) {
				sb.append((char)c);
			}
			return sb.toString();
		} finally {
			try {br.close();} catch (IOException e) {}
		}
	}

	public void writeFile(File f, String s) throws IOException {
		PrintWriter pw = new PrintWriter(f);
		try {
			pw.write(s);
		} finally {
			pw.close();
		}
	}
	
}
