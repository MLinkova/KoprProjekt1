package sk.upjs.ics.kopr2016.cviko07.priklad3;

public class DirAnalyzerInteruptedException extends RuntimeException{
	private DirSize dirSize;

	public DirAnalyzerInteruptedException(DirSize dirSize) {
		super();
		this.dirSize = dirSize;
	}

	public DirSize getDirSize() {
		return dirSize;
	}
}
