package taiga;

import java.io.IOException;
import java.io.Reader;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;

public class IOUtils {

	public static String readAll(Reader r) throws ModelException {
		try {
			StringBuilder sb = new StringBuilder();
			for(int c = r.read(); c >= 0; c = r.read()) {
				sb.append((char)c);
			}
			return sb.toString();
		} catch (IOException ex) {
			throw new ModelException(ex, ErrorCode.IO_EXCEPTION);
		} finally {
			try {r.close();} catch (IOException e) {}
		}
	}

}
