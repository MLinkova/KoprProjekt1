package sk.upjs.ics.kopr2016.cviko07.priklad1;

public class DirectoryForbiddenException extends RuntimeException{

	private DirSize dirSize;
	
	public DirectoryForbiddenException(DirSize dirSize) {
		super();
		this.dirSize = dirSize;
	}

	public DirSize getDirSize() {
		return dirSize;
	}
}
