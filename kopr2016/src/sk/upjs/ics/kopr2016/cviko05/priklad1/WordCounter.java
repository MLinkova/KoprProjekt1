package sk.upjs.ics.kopr2016.cviko05.priklad1;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WordCounter {

	public static final String START_DIR = "c:\\Users\\pc12\\git";
	public static final int NUMBER_OF_SEARCHERS = 7;
	public static final int NUMBER_OF_FILE_ANALYZERS = 7;
	
	public static void main(String[] args) {
		File dir = new File(START_DIR);
		BlockingQueue<File> filesToAnalyze = new LinkedBlockingQueue<>();
		ConcurrentMap<String,AtomicInteger> words = new ConcurrentHashMap<>();
		long start = System.nanoTime();
		Searcher searcher = new Searcher(dir, filesToAnalyze);
		for(int i = 0; i < NUMBER_OF_SEARCHERS; i++) {
			Thread searcherThread = new Thread(searcher);
			searcherThread.start();
		}
		CountDownLatch gate = new CountDownLatch(NUMBER_OF_FILE_ANALYZERS);
		FileAnalyzer a = new FileAnalyzer(filesToAnalyze, words, gate);
		for (int i = 0; i < NUMBER_OF_FILE_ANALYZERS; i++) {
			Thread analyzerThread = new Thread(a);
			analyzerThread.start();
		}
		try {
			gate.await();
			System.out.println("Running time: "+ (System.nanoTime()-start)/1000000.0 +" ms");
			printTop20Words(words);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printTop20Words(Map<String,AtomicInteger> words) {
		PriorityQueue<Map.Entry<String, AtomicInteger>> sortedWords = 
				new PriorityQueue<Map.Entry<String,AtomicInteger>>(
						words.size(), new Comparator<Map.Entry<String, AtomicInteger>>() {

			public int compare(Entry<String, AtomicInteger> o1, Entry<String, AtomicInteger> o2) {
				return new Integer(o2.getValue().get()).compareTo(o1.getValue().get());
			}
		});
		
		for (Entry<String, AtomicInteger> entry : words.entrySet()) {
			sortedWords.add(entry);
		}
		int min = Math.min(20, sortedWords.size()); 
		
		for (int i = 0; i < min; i++) {
			System.out.print(i+": "+ sortedWords.poll()+", ");
		}		
		System.out.println(); 
	}
	

}
