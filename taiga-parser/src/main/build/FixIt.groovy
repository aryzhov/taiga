import java.io.BufferedReader;
import java.io.FileReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

File f = new File(args[0])
String pkg = args[1]

fix(f, pkg, 'lexer', 'lexer.dat', 'Lexer.java', 'LexerDataInputStream');
fix(f, pkg, 'parser', 'parser.dat', 'Parser.java', 'ParserDataInputStream');

def fix(File dir, String pkg, String subpkg, String dataFileName, String srcFileName, String newClass) throws IOException {
	File pkgDir = new File(new File(dir, pkg.replaceAll('\\.', '/')), subpkg)
	File dataFile = new File(pkgDir, dataFileName);
	FileInputStream is = new FileInputStream(dataFile); 
	try {
		PrintWriter pw = new PrintWriter(new File(pkgDir, newClass+'.java'), 'US-ASCII');
		try {
			pw.println('package '+pkg+'.'+subpkg+';');
			pw.println();
			pw.println('import '+pkg+'.DataInputStream;');
			pw.println();
			pw.println('class ' + newClass + ' extends DataInputStream {');
			pw.println();
			pw.println('    private static final byte[] data = {');
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
					pw.print(',');
				}	
				if (n++ % 20 == 0) {
					pw.println();
					pw.print('            ');
				}
				pw.print(Integer.toString(zeros));
				pw.print(',');
				pw.print(Integer.toString((byte)b));
				zeros = 0;
			}
			pw.println('};');

			pw.println('    public ' + newClass + '() {');
			pw.println('        super('+len+', data);');
			pw.println('    }');
			pw.println('}');
		} finally {
			pw.close();
		}
	} finally {
		is.close();
	}
	
	File srcFile = new File(pkgDir, srcFileName);
	String src = readFile(srcFile);
	src = src.replaceAll('\\QDataInputStream s = new DataInputStream(\\E', newClass + ' s = new '+ newClass + '();');
	src = src.replaceAll('\\Qnew BufferedInputStream(\\E', '');
	src = src.replaceAll('\\w+\\Q.class.getResourceAsStream(\\E.*;', '');
	src = src.replaceAll('\\Qimport java.io.DataInputStream;\\E', '');
	src = src.replaceAll('\\Qimport java.io.BufferedInputStream;\\E', '');
	writeFile(srcFile, src);
	
	dataFile.delete();
}

def readFile(File f) throws IOException {
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

def writeFile(File f, String s) throws IOException {
	PrintWriter pw = new PrintWriter(f);
	try {
		pw.write(s);
	} finally {
		pw.close();
	}
}


