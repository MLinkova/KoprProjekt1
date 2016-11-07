package sk.upjs.ics.kopr2016.cviko06.priklad4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

	private static long analyzeDir(File dir) throws InterruptedException, ExecutionException {
		long sumSize = 0;
		File[] files = dir.listFiles();
		List<Future<DirSize>> podulohy = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
				sumSize += files[i].length();
			if (files[i].isDirectory()) {
				DirAnalyzer poduloha = new DirAnalyzer(files[i]);
				Future<DirSize> vysledok = SizeSummarizer.EXECUTOR.submit(poduloha);
				podulohy.add(vysledok);
//				sumSize += analyzeDir(files[i]);
			}
		}
		for (Future<DirSize> uloha : podulohy) {
			sumSize += uloha.get().getSize();
		}
		
		return sumSize;
	}
}