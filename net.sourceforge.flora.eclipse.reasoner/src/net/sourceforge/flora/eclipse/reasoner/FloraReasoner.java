/* File:      FloraReasoner.java
 **
 ** Author(s): Daniel Winkler
 ** Contact:   flora-users@lists.sourceforge.net
 **
 ** Copyright (C) 2007 Digital Enterprise Research Insitute (DERI) Innsbruck
 **
 ** FLORA-2 Visualizer is free software; you can redistribute it and/or
 ** modify it under the terms of the GNU Lesser General Public License
 ** as published by the Free Software Foundation; either version 2
 ** of the License, or (at your option) any later version.
 ** This program is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 ** GNU Lesser General Public License for more details.
 ** You should have received a copy of the GNU General Public License
 ** along with this program; if not, write to the Free Software
 ** Foundation, Inc., 51 Franklin Street, 5th Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.flora.eclipse.reasoner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

import net.sourceforge.flora.eclipse.FloraKeywords;
import net.sourceforge.flora.eclipse.console.FloraConsole;
import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.reasoner.util.ConsoleReader;
import net.sourceforge.flora.eclipse.reasoner.util.EngineOutputListener;
import net.sourceforge.flora.eclipse.reasoner.util.FloraPropertySetter;
import net.sourceforge.flora.javaAPI.src.FloraObject;
import net.sourceforge.flora.javaAPI.src.FloraSession;
import net.sourceforge.flora.javaAPI.util.FlrException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import com.declarativa.interprolog.PrologEngine;
import com.declarativa.interprolog.SubprocessEngine;

/**
 * The Flora-2 Reasoner
 * <p>
 * Opens a Flora-2 Session ({@link net.sourceforge.flora.javaAPI.src.FloraSession})
 * and supports operations for this session
 * 
 * @author Daniel Winkler
 */
public class FloraReasoner extends Observable {

	private FloraSession fSession = null;

	private boolean fSessionLoaded = false;

	private static String DEFAULT_MODULE = "main";

	private static String QUERY_START = "?-";

	public static String VISUALIZER_MODULE = "";

	private FloraConsole fConsole = null;

	private ConsoleReader fConsoleReader;

	private File fLogFile = null;

	private BufferedWriter fLog = null;

	/**
	 * returns the Singleton Instance of this reasoner
	 * 
	 * @return the Singleton Instance of this reasoner
	 */
	public static FloraReasoner getInstance() {
		return InstanceHolder.getReasoner();
	}

	/**
	 * holds the Singleton Instance of {@link FloraReasoner}
	 */
	private static class InstanceHolder {
		private static FloraReasoner fReasoner;
		
		private static FloraReasoner getReasoner() {
			if (InstanceHolder.fReasoner == null) {
				InstanceHolder.fReasoner = new FloraReasoner();
			}
			
			return InstanceHolder.fReasoner;
		}
	}

	/**
	 * the Constructor
	 * <p>
	 * starts a new {@link FloraSession} and initializes a new
	 * {@link ConsoleReader} to read Commands from the {@link FloraConsole}
	 */
	private FloraReasoner() {
		fConsole = FloraConsole.getInstance();

		startSession();

		fConsoleReader = new ConsoleReader(fConsole.getInputStream());
		fConsoleReader.start();

		try {
			fLogFile = File.createTempFile("Log", ".flr");
			fLog = new BufferedWriter(new FileWriter(fLogFile));
		} catch (Exception e) {
			fConsole.printlnError("Error with Logfile"
					+ fLogFile.getAbsolutePath() + "\nSession won't be logged");
			fConsole.printFloraPrompt();
		}
	}

	/**
	 * starts a new FloraSession
	 */
	private void startSession() {
		IPreferenceStore store = FloraReasonerPlugin.getDefault()
				.getPreferenceStore();
		String engineType = store
				.getString(FloraPreferenceConstants.REASONER_ENGINE);
		String floraDirectory = store
				.getString(FloraPreferenceConstants.REASONER_FLORA_DIRECTORY);
		VISUALIZER_MODULE = store
				.getString(FloraPreferenceConstants.REASONER_VISUALIZER_MODULE_NAME);

		String osName = System.getProperty("os.name");
		int operatingSystem = 0;
		if (osName.toUpperCase().contains("win".toUpperCase())) {
			// windows detected
			// test for cygwin
			try {
				Runtime.getRuntime().exec("uname");
				// no exception -> maybe cygwin ? ask user
				MessageBox msgBox = new MessageBox(Display.getCurrent()
						.getActiveShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				msgBox.setMessage("Did you install Flora-2 with cygwin?");
				if (msgBox.open() == SWT.YES) {
					operatingSystem = FloraPropertySetter.unixOperatingSystem;
				} else {
					operatingSystem = FloraPropertySetter.windowsOperatingSystem;
				}
			} catch (IOException e) {
				// no cygwin
				operatingSystem = FloraPropertySetter.windowsOperatingSystem;
			}
		} else { // unix operating system
			operatingSystem = FloraPropertySetter.unixOperatingSystem;
		}

		boolean setProperties = FloraPropertySetter.setEnvironmentProperties(
				floraDirectory, engineType, operatingSystem);
		if (!setProperties) {
			fConsole
					.printlnError("FLORA-2 Visualizer did not find a configured version of FLORA-2 at a default location.");
			fConsole
					.printlnError("Please specify the location of a configured FLORA-2 system using the following menu:\n\tWindow -> Preferences -> Flora Reasoner");
			return;
		}

		// String prologDirectory = "";
		// BufferedReader reader = null;
		//
		// String osName = System.getProperty("os.name");
		//
		// if (osName.toUpperCase().contains("linux".toUpperCase())) {
		// String prologPathHolder = floraDirectory + File.separatorChar
		// + ".prolog_path";
		// File prologPathHolderFile = new File(prologPathHolder);
		// try {
		// reader = new BufferedReader(
		// new FileReader(prologPathHolderFile));
		// prologDirectory = reader.readLine().trim();
		// reader.close();
		// } catch (FileNotFoundException e) {
		// fConsole
		// .printlnError("FLORA-2 Visualizer did not find a configured version
		// of FLORA-2 at a default location.");
		// fConsole
		// .printlnError("Please specify the location of a configured FLORA-2
		// system using the following menu: Window -> Preferences -> Flora
		// Reasoner");
		// return;
		// } catch (IOException e) {
		// fConsole.printlnError("Java IOException");
		// fConsole
		// .printlnError("Please reinstall Flora-2 or contact the Flora-2
		// Team");
		// return;
		// }
		// } else if (osName.toUpperCase().contains("cygwin".toUpperCase())) {
		// String prologPathHolder = floraDirectory + File.separatorChar
		// + ".prolog_path_cygwin";
		// File prologPathHolderFile = new File(prologPathHolder);
		// try {
		// reader = new BufferedReader(
		// new FileReader(prologPathHolderFile));
		// prologDirectory = reader.readLine().trim();
		// reader.close();
		// } catch (FileNotFoundException e) {
		// fConsole
		// .printlnError("FLORA-2 Visualizer did not find a configured version
		// of FLORA-2 at a default location.");
		// fConsole
		// .printlnError("Please specify the location of a configured FLORA-2
		// system using the following menu: Window -> Preferences -> Flora
		// Reasoner");
		// return;
		// } catch (IOException e) {
		// fConsole.printlnError("Java IOException");
		// fConsole
		// .printlnError("Please reinstall Flora-2 or contact the Flora-2
		// Team");
		// return;
		// }
		// } else if (osName.toUpperCase().contains("win".toUpperCase())) {
		// String prologPathHolder = floraDirectory + File.separatorChar
		// + ".prolog_path_wind";
		// File prologPathHolderFile = new File(prologPathHolder);
		// if (prologPathHolderFile.exists()) {
		// try {
		// reader = new BufferedReader(new FileReader(
		// prologPathHolderFile));
		// prologDirectory = reader.readLine().trim();
		// if (prologDirectory.contains("PROLOG=")) {
		// prologDirectory = prologDirectory
		// .substring(prologDirectory.indexOf("PROLOG=")
		// + new String("PROLOG=").length());
		// prologDirectory = prologDirectory.substring(0,
		// prologDirectory.indexOf("\\xsb"));
		// }
		// reader.close();
		// } catch (FileNotFoundException e) {
		// fConsole
		// .printlnError("FLORA-2 Visualizer did not find a configured version
		// of FLORA-2 at a default location.");
		// fConsole
		// .printlnError("Please specify the location of a configured FLORA-2
		// system using the following menu: Window -> Preferences -> Flora
		// Reasoner");
		// return;
		// } catch (IOException e) {
		// fConsole.printlnError("Java IOException");
		// fConsole
		// .printlnError("Please reinstall Flora-2 or contact the Flora-2
		// Team");
		// return;
		// }
		// } else {
		// prologPathHolder = floraDirectory + File.separatorChar
		// + ".prolog_path_cygwin";
		// prologPathHolderFile = new File(prologPathHolder);
		// try {
		// reader = new BufferedReader(new FileReader(
		// prologPathHolderFile));
		// prologDirectory = reader.readLine().trim();
		// reader.close();
		// } catch (FileNotFoundException e) {
		// fConsole
		// .printlnError("FLORA-2 Visualizer did not find a configured version
		// of FLORA-2 at a default location.");
		// fConsole
		// .printlnError("Please specify the location of a configured FLORA-2
		// system using the following menu: Window -> Preferences -> Flora
		// Reasoner");
		// return;
		// } catch (IOException e) {
		// fConsole.printlnError("Java IOException");
		// fConsole
		// .printlnError("Please reinstall Flora-2 or contact the Flora-2
		// Team");
		// return;
		// }
		// }
		//
		// }

		try {
			fSession = new FloraSession();

			fConsole.showConsole();
			fConsole.println("FloraSession initialized");
			fConsole.printFloraPrompt();

			fSession.ExecuteCommand("[visualizer>>" + VISUALIZER_MODULE + "].");

			fSessionLoaded = true;
			
			PrologEngine engine = fSession.getEngine();
			if (engine instanceof SubprocessEngine) {
				SubprocessEngine spEngine = (SubprocessEngine) engine;
				spEngine.addPrologOutputListener(new EngineOutputListener());
			} else {
				fConsole.printlnError("Console isn't able to show output from the engine");
				fConsole.printlnError("To display the output use subprocess-engine");
			}
		} catch (FlrException e) {
			fSession = null;
			fSessionLoaded = false;
			fConsole.printlnError(e.getMessage());
			fConsole.printFloraPrompt();
		}
	}

	/**
	 * executes a query at the reasoner and returns an Iterator containing the
	 * resulting <code>HashMap(s)</code>
	 * 
	 * @param query
	 *            the query to execute
	 * @param vars
	 *            a Vector containing the variables in the query
	 * @param printQuery
	 *            prints the query to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @param printResponse
	 *            prints the response to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @return Iterator containing the resulting <code>HashMap(s)</code> each
	 *         HashMap uses the variable as key and returns a
	 *         {@link FloraObject} of the variable as value
	 */
	public Iterator<HashMap<String, FloraObject>> executeQuery(String query,
			Vector<String> vars, boolean printQuery, boolean printResponse) {
		String tempQuery = query.trim();
		if (tempQuery.startsWith(QUERY_START)) {
			tempQuery = tempQuery.substring(2);
		}

		Iterator<HashMap<String, FloraObject>> returnValue = null;

		if (printQuery)
			fConsole.println(tempQuery);
		else if (printResponse)
			fConsole.println("");

		try {
			returnValue = fSession.ExecuteQuery(tempQuery, vars);
			if (printResponse) {
				HashMap<String, FloraObject> hashMap = null;
				FloraObject object = null;
				while (returnValue.hasNext()) {
					hashMap = returnValue.next();

					for (String var : vars) {
						object = hashMap.get(var);
						fConsole.printlnResponse(var + " = "
								+ object.toString());
					}
					fConsole.printlnResponse("");
				}
			}
			if (printQuery || printResponse)
				fConsole.printFloraPrompt();
		} catch (FlrException e) {
			fConsole.printlnError(e.getMessage());
			fConsole.printFloraPrompt();
		}

		return returnValue;
	}

	/**
	 * executes a query at the reasoner and returns an Iterator containing the
	 * resulting <code>FloraObject(s)</code>
	 * 
	 * @param query
	 *            the query to execute
	 * @param printQuery
	 *            prints the query to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @param printResponse
	 *            prints the response to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @return an Iterator for the resulting {@link FloraObject}s
	 */
	public Iterator<FloraObject> executeQuery(String query, boolean printQuery,
			boolean printResponse) {
		String tempQuery = query.trim();
		if (tempQuery.startsWith(QUERY_START)) {
			tempQuery = tempQuery.substring(2);
		}

		Iterator<FloraObject> returnValue = null;

		if (printQuery)
			fConsole.println(tempQuery);
		else if (printResponse)
			fConsole.println("");

		try {
			returnValue = fSession.ExecuteQuery(tempQuery);
			if (printResponse) {
				FloraObject object = null;

				while (returnValue.hasNext()) {
					object = returnValue.next();
					fConsole.printlnResponse("?Var = " + object.toString());
				}
			}
			if (printQuery || printResponse)
				fConsole.printFloraPrompt();
		} catch (FlrException e) {
			fConsole.printlnError(e.getMessage());
			fConsole.printFloraPrompt();
		}

		return returnValue;
	}

	// /**
	// * executes a query at the reasoner and returns an ArrayList containing
	// the resulting <code>HashMap(s)</code>
	// *
	// * @param query
	// * the query to execute
	// * @param vars
	// * a Vector containing the variables in the query
	// * @return ArrayList containing the resulting <code>HashMap(s)</code> each
	// HashMap uses the variable as key and returns a
	// * {@link FloraObject} of the variable as value
	// * @see #executeAndPrintQuery(String, Vector)
	// */
	// public ArrayList<HashMap<String, FloraObject>> executeQueryL(String
	// query, Vector<String> vars) {
	//
	// Iterator<HashMap<String, FloraObject>> temp = executeQuery(query, vars);
	//
	// ArrayList<HashMap<String, FloraObject>> list = new
	// ArrayList<HashMap<String, FloraObject>>();
	//
	// while (temp.hasNext())
	// list.add(temp.next());
	//
	// return list;
	// }
	//
	// /**
	// * executes a query at the reasoner and returns an ArrayList containing
	// the resulting <code>FloraObject(s)</code>
	// *
	// * @param query
	// * the query to execute
	// * @return an ArrayList for the resulting {@link FloraObject}s
	// * @see #executeAndPrintQuery(String)
	// */
	// public ArrayList<FloraObject> executeQueryL(String query) {
	//
	// Iterator<FloraObject> temp = executeQuery(query);
	//
	// ArrayList<FloraObject> list = new ArrayList<FloraObject>();
	//
	// while (temp.hasNext())
	// list.add(temp.next());
	//
	// return list;
	// }

	/**
	 * executes a Command and notifies the Observers of this Reasoner since a
	 * command could change the state of this reasoner
	 * 
	 * @param command
	 *            the command to execute
	 * @param printCommand
	 *            prints the command to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @param printResponse
	 *            prints the response to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @param notifyObservers
	 *            <code>true</code> to notify Observers
	 * @return <code>true</code> if the command executed successful
	 */
	public boolean executeCommand(String command, boolean printCommand,
			boolean printResponse, boolean notifyObservers) {
		logCommand(command);

		if (printCommand)
			fConsole.println(command);
		else if (printResponse)
			fConsole.println("");

		boolean returnValue = false;

		try {
			returnValue = fSession.ExecuteCommand(command);
		} catch (FlrException e) {
			fConsole.printlnError(e.getMessage());
			fConsole.printFloraPrompt();
		} catch (Exception e) {
			//fConsole.printlnError(e.getMessage());
			fConsole.printFloraPrompt();
		}

		setChanged();
		if (notifyObservers) {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					notifyObservers();
					clearChanged();
				}
			});
		}

		if (printResponse)
			fConsole.printlnResponse(returnValue ? "Yes" : "No");

		if (printCommand || printResponse)
			fConsole.printFloraPrompt();

		return returnValue;
	}

	/**
	 * executes a Command and notifies the Observers of this Reasoner since a
	 * command could change the state of this reasoner
	 * 
	 * @param command
	 *            the command to execute
	 * @param printCommand
	 *            prints the command to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @param printResponse
	 *            prints the response to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @return <code>true</code> if the command executed successful
	 */
	public boolean executeCommand(String command, boolean printCommand,
			boolean printResponse) {
		return executeCommand(command, printCommand, printResponse, true);
	}

	/**
	 * loads a String to the default Flora-2 module
	 * 
	 * @param string
	 *            the Text to load
	 */
	public void loadString(String string) {
		loadString(string, DEFAULT_MODULE);
	}

	/**
	 * loads a String to a specified module
	 * 
	 * @param string
	 *            the string to load
	 * @param module
	 *            the module in which the string should be loaded
	 */
	public void loadString(String string, String module) {
		final File tempFile = stringToFile(string);

		loadFile(tempFile, module, false);
	}

	/**
	 * adds a String to the default Flora-2 module.
	 * 
	 * @param string
	 *            the Text to add
	 */
	public void addString(String string) {
		addString(string, DEFAULT_MODULE);
	}

	/**
	 * adds a String to a specified module
	 * 
	 * @param string
	 *            the string to add
	 * @param module
	 *            the module in which the string should be added
	 */
	public void addString(String string, String module) {
		final File tempFile = stringToFile(string);

		addFile(tempFile, module);
	}

	/**
	 * loads a file to the default module
	 * 
	 * @param file
	 *            the <code>File</code> to load
	 * @param eraseRegistered
	 *            <code>true</code> to erase the registered Objects from
	 *            Visualizer
	 */
	public void loadFile(File file, boolean eraseRegistered) {
		loadFile(file, DEFAULT_MODULE, eraseRegistered);
	}

	/**
	 * loads a <code>File</code> to a given module and cleans the registered
	 * objects of the module
	 * 
	 * @param file
	 *            the <code>File</code> to load
	 * @param module
	 *            the module where the <code>File</code> should be loaded
	 * @param eraseRegistered
	 *            <code>true</code> to erase the registered Objects from
	 *            Visualizer
	 */
	public void loadFile(File file, String module, boolean eraseRegistered) {
		String command = "_load('" + file.getAbsolutePath().replace('\\', '/')
				+ "'>>" + module + ").";

		if (eraseRegistered) {
			ArrayList<String> registeredObjects = new ArrayList<String>();
			for (FloraObject registeredObject : getProgrammaticallyRegisteredObjects(module))
				registeredObjects.add(registeredObject.toString());
			if (registeredObjects.size() > 0)
				unregister(module, registeredObjects, false);
			for (FloraObject registeredObject : getManuallyRegisteredObjects(module))
				registeredObjects.add(registeredObject.toString());
			if (registeredObjects.size() > 0)
				unregisterManually(module, registeredObjects, false);
		}

		logCommand(command);
		fConsole.println(command);
		fConsole.printFloraPrompt();

		fSession.loadFile(file.getAbsolutePath().replace('\\', '/'), module);

		setChanged();
		notifyObservers(module);
		clearChanged();
	}

	/**
	 * adds a file to the default module
	 * 
	 * @param file
	 *            the <code>File</code> to add
	 */
	public void addFile(File file) {
		addFile(file, DEFAULT_MODULE);
	}

	/**
	 * adds a <code>File</code> to a given module
	 * 
	 * @param file
	 *            the <code>File</code> to add
	 * @param module
	 *            the module where the <code>File</code> should be added
	 */
	public void addFile(File file, String module) {
		String command = "_add('" + file.getAbsolutePath().replace('\\', '/')
				+ "'>>" + module + ").";

		logCommand(command);
		fConsole.println(command);
		fConsole.printFloraPrompt();

		fSession.addFile(file.getAbsolutePath().replace('\\', '/'), module);

		setChanged();
		notifyObservers(module);
		clearChanged();
	}

	/**
	 * parses a command and either executes it as query or command, depending on
	 * occurence of variables (<code>?VAR</code>)
	 * 
	 * @param query
	 *            the query to parse
	 * @param printCommand
	 *            prints the command to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @param printResponse
	 *            prints the response to the <code>FloraConsole</code> if
	 *            <code>true</code>
	 * @see #executeCommand(String)
	 * @see #executeQuery(String)
	 * @see #executeQuery(String, Vector)
	 */
	public void parseCommand(String query, boolean printCommand,
			boolean printResponse) {
		boolean inText = false;
		boolean inString = false;
		boolean inCharSequence = false;
		boolean inVariableDeclaration = false;

		StringBuffer fQuery = new StringBuffer();

		int sign = 0;
		StringBuffer var = new StringBuffer();
		final Vector<String> vars = new Vector<String>();

		StringReader parser = new StringReader(query);

		try {
			while ((sign = parser.read()) >= 0) {
				inText = inString || inCharSequence;

				if (!inText && Character.isWhitespace((char) sign)) {
					fQuery.append(' ');
				} else {
					fQuery.append((char) sign);
				}

				if (inVariableDeclaration) {
					if ((Character.isWhitespace((char) sign))
							|| FloraKeywords.isDelimiterStart((char) sign)) {
						if (!vars.contains(var.toString()))
							vars.add(var.toString());
						var = new StringBuffer();
						inVariableDeclaration = !inVariableDeclaration;
					} else {
						var.append((char) sign);
					}

				}

				if (!inText && ((char) sign == '?')) {
					inVariableDeclaration = true;
					var.append("?");
				}

				if (!inText && ((char) sign == '.')) {
					parser.mark(1);
					int temp = parser.read();
					parser.reset();

					if (Character.isWhitespace(temp) || (temp < 0)) {
						switch (vars.size()) {
						case 0:
							executeCommand(fQuery.toString(), printCommand,
									printResponse);
							break;

						default:
							executeQuery(fQuery.toString(), vars, printCommand,
									printResponse);
							break;
						}
						fQuery = new StringBuffer();
						var = new StringBuffer();
						vars.clear();
						continue;
					}
				}

				if ((!inCharSequence) && ((char) sign == '\"')) {
					inString = !inString;
					continue;
				}

				if ((!inString) && ((char) sign == '\'')) {
					inCharSequence = !inCharSequence;
					continue;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * returns a temporary <code>File</code> containing the given
	 * <code>String</code>
	 * 
	 * @param content
	 *            the content of the new File
	 * @return a temporary <code>File</code> containing the given
	 *         <code>String</code>
	 */
	public static File stringToFile(String content) {

		File tempFile = null;

		try {
			tempFile = File.createTempFile("flora", ".flr");
			tempFile.createNewFile();
			if (tempFile.canWrite()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						tempFile));
				writer.write(content);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tempFile;
	}

	/**
	 * closes the current Session
	 */
	protected void close() {
		fConsoleReader.interrupt();
		try {
			fLog.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isSessionLoaded())
			fSession.close();
	}

	/**
	 * restarts the {@link FloraSession}
	 */
	public void restart() {
		startSession();
		setChanged();
		notifyObservers();
		clearChanged();
	}

	/**
	 * returns <code>true</code> if the {@link FloraSession} is loaded
	 * (without errors)
	 * 
	 * @return <code>true</code> if the <code>FloraSession</code> is loaded
	 *         (without errors)
	 */
	public boolean isSessionLoaded() {
		return fSessionLoaded;
	}

	/**
	 * logs commands to a temporary log File
	 * 
	 * @param command
	 *            he command to log
	 */
	private void logCommand(String command) {
		if (fLog != null) {
			try {
				fLog.write("?- ");
				fLog.write(command);
				fLog.write('\n');
				fLog.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * returns the log <code>File</code>
	 * 
	 * @return the log <code>File</code>
	 */
	public File getLogFile() {
		return fLogFile;
	}

	/**
	 * Registers the objects of the given list for the Flora-2 Visualizer
	 * 
	 * @param module
	 *            the module who's elements shall be registered
	 * @param list
	 *            the elements to register
	 */
	public void register(String module, ArrayList<String> list) {
		StringBuffer command = new StringBuffer();
		command.append(module);
		command.append("[%registerManually([");

		for (int i = 0; i < list.size(); ++i) {
			command.append(list.get(i));
			if (i < list.size() - 1) {
				command.append(",");
			}
		}
		command.append("])]@");
		command.append(VISUALIZER_MODULE);
		command.append(".");

		executeCommand(command.toString(), true, true);
	}

	/**
	 * Unregisters the objects of the given list
	 * 
	 * @param module
	 *            the module who's elements shall be unregistered
	 * @param list
	 *            the elements to unregister
	 * @return true if unregistered successfully
	 */
	public boolean unregister(String module, ArrayList<String> list) {
		return unregister(module, list, true);
	}

	/**
	 * Unregisters the objects of the given list
	 * 
	 * @param module
	 *            the module who's elements shall be unregistered
	 * @param list
	 *            the elements to unregister
	 * @param notifyObservers
	 *            <code>true</code> to notify Observers
	 * @return true if unregistered successfully
	 */
	public boolean unregister(String module, ArrayList<String> list,
			boolean notifyObservers) {
		if (list.size() <= 0)
			return false;
		StringBuffer command = new StringBuffer();
		command.append(module);
		command.append("[%unregister([");

		for (int i = 0; i < list.size(); ++i) {
			command.append(list.get(i));
			if (i < list.size() - 1) {
				command.append(",");
			}
		}
		command.append("])]@");
		command.append(VISUALIZER_MODULE);
		command.append(".");

		return executeCommand(command.toString(), true, true, notifyObservers);
	}

	/**
	 * Unregisters the objects of the given list
	 * 
	 * @param module
	 *            the module who's elements shall be unregistered
	 * @param list
	 *            the (manually registered) elements to unregister
	 * @param notifyObservers
	 *            <code>true</code> to notify Observers
	 * @return true if unregistered successfully
	 */
	public boolean unregisterManually(String module, ArrayList<String> list) {
		return unregisterManually(module, list, true);
	}

	/**
	 * Unregisters the objects of the given list
	 * 
	 * @param module
	 *            the module who's elements shall be unregistered
	 * @param list
	 *            the (manually registered) elements to unregister
	 * @param notifyObservers
	 *            <code>true</code> to notify Observers
	 * @return true if unregistered successfully
	 */
	public boolean unregisterManually(String module, ArrayList<String> list,
			boolean notifyObservers) {
		if (list.size() <= 0)
			return false;
		StringBuffer command = new StringBuffer();
		command.append(module);
		command.append("[%unregisterManually([");

		for (int i = 0; i < list.size(); ++i) {
			command.append(list.get(i));
			if (i < list.size() - 1) {
				command.append(",");
			}
		}
		command.append("])]@");
		command.append(VISUALIZER_MODULE);
		command.append(".");

		return executeCommand(command.toString(), true, true, notifyObservers);
	}

	public ArrayList<FloraObject> getRegisteredObjects(String module) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(module);
		buffer.append("[registeredObject->?X]@");
		buffer.append(VISUALIZER_MODULE);
		buffer.append(".");

		Iterator<FloraObject> result = executeQuery(buffer.toString(), false,
				false);

		ArrayList<FloraObject> resultList = new ArrayList<FloraObject>();

		while (result.hasNext())
			resultList.add(result.next());

		return resultList;
	}

	public ArrayList<FloraObject> getManuallyRegisteredObjects(String module) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(module);
		buffer.append("[registeredManually->?X]@");
		buffer.append(VISUALIZER_MODULE);
		buffer.append(".");

		Iterator<FloraObject> result = executeQuery(buffer.toString(), false,
				false);

		ArrayList<FloraObject> resultList = new ArrayList<FloraObject>();

		while (result.hasNext())
			resultList.add(result.next());

		return resultList;
	}

	public ArrayList<FloraObject> getProgrammaticallyRegisteredObjects(
			String module) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(module);
		buffer.append("[registeredProgrammatically->?X]@");
		buffer.append(VISUALIZER_MODULE);
		buffer.append(".");

		Iterator<FloraObject> result = executeQuery(buffer.toString(), false,
				false);

		ArrayList<FloraObject> resultList = new ArrayList<FloraObject>();

		while (result.hasNext())
			resultList.add(result.next());

		return resultList;
	}
}
