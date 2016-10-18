package sk.upjs.ics.kopr2016.cviko04.priklad1;

import java.io.File;
import java.util.Queue;

public class Searcher implements Runnable {

	public static final File POISON_PILL = new File("poison.pill");

	private File rootDir;
	private Queue<File> queue;

	public Searcher(File rootDir, Queue<File> queue) {
		this.rootDir = rootDir;
		this.queue = queue;
	}

	public void run() {
		search(rootDir.listFiles());
		queue.offer(POISON_PILL);
	}
	
	private void search(File[] dir) {
		for (int i = 0;  i < dir.length; i++) {
			if (dir[i].isDirectory()) {
				search(dir[i].listFiles());
			}
			else {
				if (dir[i].getName().endsWith(".java")) {
					queue.offer(dir[i]);
				}
			}
		}
	}
}
