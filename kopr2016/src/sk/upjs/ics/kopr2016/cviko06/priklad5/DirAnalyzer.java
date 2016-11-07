package sk.upjs.ics.kopr2016.cviko06.priklad5;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class DirAnalyzer extends RecursiveTask<DirSize> {

	private static final long serialVersionUID = 6189390750070833507L;
	private File myDir;
	
	public DirAnalyzer(File myDir) {
		super();
		this.myDir = myDir;
	}

	@Override
	protected DirSize compute() {
		DirSize dirSize = new DirSize(myDir,analyzeDir(myDir));
		return dirSize;
	}

//	@Override
//	public DirSize call() throws Exception {
//		DirSize dirSize = new DirSize(myDir,analyzeDir(myDir));
//		return dirSize;
//	}

	private static long analyzeDir(File dir) {
		long sumSize = 0;
		File[] files = dir.listFiles();
		List<DirAnalyzer> podulohy = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
				sumSize += files[i].length();
			if (files[i].isDirectory()) {
				DirAnalyzer poduloha = new DirAnalyzer(files[i]);
				poduloha.fork();
				podulohy.add(poduloha);
//				sumSize += analyzeDir(files[i]);
			}
		}
		for (DirAnalyzer uloha : podulohy) {
			sumSize += uloha.join().getSize();
		}
		
		return sumSize;
	}

}