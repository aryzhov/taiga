package taiga.interfaces;


public class Position implements Comparable<Position> {
	public String fileName;
	public int lineNum; // 1-based
	public int linePos; // 1-based

	public Position(String fileName, int lineNum, int linePos) {
		this.fileName = fileName;
		this.lineNum = lineNum;
		this.linePos = linePos;
	}

	public Position(String fileName) {
		this(fileName, -1, -1);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Position))
			return false;
		Position p = (Position)obj;
		return StringUtils.equals(fileName, p.fileName, false) && lineNum == p.lineNum && linePos == p.linePos;
	}
	
	@Override
	public int hashCode() {
		return fileName.hashCode() ^ (lineNum << 6) ^ linePos;
	}
	
	@Override
	public int compareTo(Position p) {
		if(lineNum != p.lineNum)
			return lineNum - p.lineNum;
		else
			return linePos - p.linePos;
	}
	
	@Override
	public String toString() {
		if(fileName == null)
			return "(location unknown)";
		else
			return "file '"+ fileName + "'" + (lineNum < 0 ? "": (", line "+lineNum+"."+linePos));
	}

	public boolean isUndefined() {
		return lineNum < 0;
	}
	
	public static final Position UNDEFINED = new Position(null, -1, -1);
}
