package sk.upjs.ics.kopr2016.cviko05.priklad1;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Searcher implements Runnable {

	public static final File POISON_PILL = new File("poison.pill");
	
	private Queue<File> queue;
	private BlockingQueue<File> dirsToSearch;
	private AtomicInteger unprocessedDirs;

	public Searcher(File rootDir, Queue<File> queue) {
		this.dirsToSearch = new LinkedBlockingQueue<>();
		unprocessedDirs = new AtomicInteger(1);
		dirsToSearch.offer(rootDir);
		this.queue = queue;
	}

	public void run() {
		try {
			while(true) {
				File myDir = dirsToSearch.take();
				if (myDir == POISON_PILL) {
					return;
				}
				search(myDir.listFiles());
				int count = unprocessedDirs.decrementAndGet();
				if (count == 0) {
					for (int i = 0; i <  WordCounter.NUMBER_OF_FILE_ANALYZERS; i++)
						queue.offer(POISON_PILL);
					for (int i = 0; i <  WordCounter.NUMBER_OF_SEARCHERS; i++)
						dirsToSearch.offer(POISON_PILL);
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
				dirsToSearch.offer(dir[i]);
			}
			else {
				if (dir[i].getName().endsWith(".java")) {
					queue.offer(dir[i]);
				}
			}
		}
	}
}
