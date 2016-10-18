package sk.upjs.ics.kopr2016.cviko04.priklad1;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import sk.upjs.ics.kopr2016.cviko04.priklad2.Searcher;

public class FileAnalyzer implements Runnable {

	private final BlockingQueue<File> filesToAnalyze;
	private final Map<String, Integer> words;
	
	public FileAnalyzer(BlockingQueue<File> filesToAnalyze, Map<String, Integer> words) {
		this.filesToAnalyze = filesToAnalyze;
		this.words = words;
	}

	public void run() {
		try {
			File file = filesToAnalyze.take();
			while(file != Searcher.POISON_PILL) {
				try (Scanner scanner = new Scanner(file)){
					while(scanner.hasNext()) {
						String word = scanner.next();
						Integer count = words.get(word);
						if (count == null) {
							count = 1;
						}
						else {
							count++;
						}
						words.put(word, count);
					}
				} catch (Exception consumed) {}
				file = filesToAnalyze.take();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
