package sk.upjs.ics.kopr2016.cviko04.priklad2;

import java.io.File;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class FileAnalyzer implements Runnable {

	private final BlockingQueue<File> filesToAnalyze;
	private final ConcurrentMap<String,AtomicInteger> words;
	private final CountDownLatch gate;
	
	public FileAnalyzer(BlockingQueue<File> filesToAnalyze, 
			ConcurrentMap<String,AtomicInteger> words, CountDownLatch gate) {
		this.filesToAnalyze = filesToAnalyze;
		this.words = words;
		this.gate = gate;
	}

	public void run() {
		try {
			File file = filesToAnalyze.take();
			while(file != Searcher.POISON_PILL) {
				try (Scanner scanner = new Scanner(file)){
					while(scanner.hasNext()) {
						String word = scanner.next();
						words.putIfAbsent(word, new AtomicInteger());
						AtomicInteger count = words.get(word);
						count.incrementAndGet();
					}
				} catch (Exception consumed) {}
				file = filesToAnalyze.take();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			gate.countDown();
		}
	}
}
