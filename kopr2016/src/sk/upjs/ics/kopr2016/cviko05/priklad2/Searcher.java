package sk.upjs.ics.kopr2016.cviko05.priklad2;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Searcher implements Runnable {

	public static final File POISON_PILL = new File("poison.pill");
	
	private int myPosition;
	private BlockingDeque<File> myDeque;
	
	private Queue<File> queue;
	private List<BlockingDeque<File>> deques;

	private static AtomicInteger unprocessedDirs = new AtomicInteger(1);
	private static Semaphore numberOfFilesInDequesSemaphor = new Semaphore(1); 

	public Searcher(List<BlockingDeque<File>> deques, int myPosition, Queue<File> queue) {
		this.deques = deques;
		this.myPosition = myPosition;
		this.myDeque = deques.get(myPosition);
		this.queue = queue;
	}

	public void run() {
		try {
			while(true) {
				numberOfFilesInDequesSemaphor.acquire();
				File myDir = myDeque.pollFirst();
				if (myDir == null) {
					int pos = (int) (Math.random() * WordCounter.NUMBER_OF_SEARCHERS);
					do {
						myDir = deques.get(pos).pollLast();
						pos = (pos + 1) % WordCounter.NUMBER_OF_SEARCHERS;
					} while(myDir == null);
				}
				if (myDir == POISON_PILL) {
					return;
				}
				search(myDir.listFiles());
				int count = unprocessedDirs.decrementAndGet();
				if (count == 0) {
					for (int i = 0; i <  WordCounter.NUMBER_OF_FILE_ANALYZERS; i++)
						queue.offer(POISON_PILL);
					for (int i = 0; i <  WordCounter.NUMBER_OF_SEARCHERS; i++) {
						myDeque.offerFirst(POISON_PILL);
						numberOfFilesInDequesSemaphor.release();
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void search(File[] dir) {
		for (int i = 0;  i < dir.length; i++) {
			if (dir[i].isDirectory()) {
				//search(dir[i].listFiles());
				unprocessedDirs.incrementAndGet();
				myDeque.offerFirst(dir[i]);
				numberOfFilesInDequesSemaphor.release();
			}
			else {
				if (dir[i].getName().endsWith(".java")) {
					queue.offer(dir[i]);
				}
			}
		}
	}
}
