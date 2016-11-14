package sk.upjs.ics.kopr2016.cviko07.priklad1;

import java.io.File;
import java.util.concurrent.Callable;

public class DirAnalyzer implements Callable<DirSize> {

	private File myDir;
	
	public DirAnalyzer(File myDir) {
		super();
		this.myDir = myDir;
	}

	@Override
	public DirSize call() throws Exception {
		DirSize dirSize = new DirSize(myDir,analyzeDir(myDir));
		return dirSize;
	}

	private static long analyzeDir(File dir) throws DirectoryForbiddenException{
		long sumSize = 0;
		File[] files = dir.listFiles();
		if (files == null) 
			throw new DirectoryForbiddenException(new DirSize(dir, 0L));
		try {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile())
					sumSize += files[i].length();
				if (files[i].isDirectory())
					sumSize += analyzeDir(files[i]);
			}
		} catch (DirectoryForbiddenException e) {
			throw new DirectoryForbiddenException(
					new DirSize(dir, e.getDirSize().getSize() + sumSize));
		}
		return sumSize;
	}
}