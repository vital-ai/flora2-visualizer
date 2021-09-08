package net.sourceforge.flora.eclipse.reasoner.util;

import net.sourceforge.flora.eclipse.console.FloraConsole;

import com.declarativa.interprolog.PrologOutputListener;

public class EngineOutputListener implements PrologOutputListener {
	private FloraConsole fConsole = null;

	private StringBuffer printBuffer = new StringBuffer();

	public EngineOutputListener() {
		fConsole = FloraConsole.getInstance();

		new Thread(new Runnable() {

			public void run() {
				while (true) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (printBuffer.length() > 0) {
						String pB = "";
						synchronized (printBuffer) {
							pB = printBuffer.toString();
							printBuffer = new StringBuffer();
						}
						fConsole.printResponse(clean(pB));
					}
				}
			}

		}).start();

	}

	public void print(final String s) {
		synchronized (printBuffer) {
			printBuffer.append(s);
		}
	}

	public String clean(String stringToClear) {
		String temp = stringToClear.replaceAll("\\|\\s\\?-\\s*yes\\s*", "");
		return temp;
	}
}
