/* File:      FloraConsole.java
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

package net.sourceforge.flora.eclipse.console;

import java.io.IOException;

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;

/**
 *  A console for textual communication with the Flora-2 reasoner
 * 
 * @author Daniel Winkler
 */
public class FloraConsole {

    private IOConsole fConsole = null;

    protected static final int MSG_DEFAULT = 1 << 0;
    protected static final int MSG_RESPONSE = 1 << 1;
    protected static final int MSG_ERROR = 1 << 2;

    private RGB defaultColor;
    private RGB responseColor;
    private RGB errorColor;

    /**
     * returns the Singleton instance of this console
     * 
     * @return the Singleton instance of this console
     */
    public static FloraConsole getInstance() {
	return FloraConsoleHolder.fConsole;
    }

    /**
     * holds a singleton instance of {@link FloraConsole}
     * 
     * @author Daniel Winkler
     */
    private static class FloraConsoleHolder {
	private static FloraConsole fConsole = new FloraConsole();
    }

    /**
     * default constructor
     */
    private FloraConsole() {
	fConsole = new IOConsole("Flora-2 Console", null);

	ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { fConsole });
	showConsole();

	IPreferenceStore store = FloraConsolePlugin.getDefault().getPreferenceStore();
	defaultColor = PreferenceConverter.getColor(store, FloraPreferenceConstants.CONSOLE_COLOR_DEFAULT);
	responseColor = PreferenceConverter.getColor(store, FloraPreferenceConstants.CONSOLE_COLOR_RESPONSE);
	errorColor = PreferenceConverter.getColor(store, FloraPreferenceConstants.CONSOLE_COLOR_ERROR);
    }

    /**
     * shows the actual Flora-2 console
     * 
     * @see IConsoleManager#showConsoleView(IConsole)
     */
    public void showConsole() {
	ConsolePlugin.getDefault().getConsoleManager().showConsoleView(fConsole);
    }

    /**
     * returns the input stream of the console
     * 
     * @return the input stream of the console
     */
    public IOConsoleInputStream getInputStream() {
	return fConsole.getInputStream();
    }

    /**
     * prints a message to the Console
     * 
     * @param message the message
     * @param messageKind type of the message.
     * Should be <code>MSG_DEFAULT</code> or <code>MSG_RESPONSE</code> of <code>MSG_ERROR</code>
     */
    protected void print(String message, int messageKind) {
	try {
	    getOutputStream(messageKind).write(message);
	} catch (IOException e) {
//	    e.printStackTrace();
	}
    }

    /**
     * prints a message followed by a newline to the Console
     * 
     * @param message the message
     * @param messageKind type of the message.
     * Should be <code>MSG_DEFAULT</code> or <code>MSG_RESPONSE</code> of <code>MSG_ERROR</code>
     * 
     * @see #print(String, int)
     */
    protected void println(String message, int messageKind) {
	print(message, messageKind);
	println(messageKind);
    }

    /**
     * prints the Flora-2 prompt to the Console (<code>'flora2 ?-'<code>)
     * 
     * @see #print(String, int)
     */
    public void printFloraPrompt() {
    	// no more needed (prologoutputlistener works)
    	// will be kept in code (maybe we'll switch back to parsing console) 
	//print("flora2 ?- ", MSG_DEFAULT);
    }

    /**
     * prints a newline to the Console
     * 
     * @param messageKind type of the message.
     * Should be <code>MSG_DEFAULT</code> or <code>MSG_RESPONSE</code> of <code>MSG_ERROR</code>
     */
    protected void println(int messageKind) {
	print("\n", messageKind);
    }
    
    /**
     * prints a message to the Console <p>
     * uses {@link #println(String, int)} with <code>messageKind</code>
     * set to <code>MSG_ERROR</code>
     * 
     * @param message the message
     * @see #println(String, int)
     */
    public void printError(String message) {
	println(message, MSG_ERROR);
    }
    
    /**
     * prints a message followed by a newline to the Console <p>
     * uses {@link #println(String, int)} with <code>messageKind</code>
     * set to <code>MSG_ERROR</code>
     * 
     * @param message the message
     * @see #println(String, int)
     */
    public void printlnError(String message) {
	println(message, MSG_ERROR);
    }
    
    /**
     * prints a message to the Console <p>
     * uses {@link #println(String, int)} with <code>messageKind</code>
     * set to <code>MSG_RESPONSE</code>
     * 
     * @param message the message
     * @see #println(String, int)
     */
    public void printResponse(String message) {
	print(message, MSG_RESPONSE);
    }
    

    /**
     * prints a message followed by a newline to the Console <p>
     * uses {@link #println(String, int)} with <code>messageKind</code>
     * set to <code>MSG_RESPONSE</code>
     * 
     * @param message the message
     * @see #println(String, int)
     */
    public void printlnResponse(String message) {
	println(message, MSG_RESPONSE);
    }
    

    /**
     * prints a message to the Console <p>
     * uses {@link #print(String, int)} with <code>messageKind</code>
     * set to <code>MSG_DEFAULT</code>
     * 
     * @param message the message
     * @see #println(String, int)
     */
    public void print(String message) {
	print(message, MSG_DEFAULT);
    }

    /**
     * prints a message followed by a newline to the Console <p>
     * uses {@link #println(String, int)} with <code>messageKind</code>
     * set to <code>MSG_DEFAULT</code>
     * 
     * @param message the message
     * @see #println(String, int)
     */
    public void println(String message) {
	println(message, MSG_DEFAULT);
    }

    /**
     * returns a OutputStream depending on messageKind.<p>
     * the OutputStreams differ in the text color. The Colors are defined
     * on the Preference Page
     * 
     * @param messageKind type of the message.
     * Should be <code>MSG_DEFAULT</code> or <code>MSG_RESPONSE</code> of <code>MSG_ERROR</code>
     * @return a OutputStream
     * 
     * @see IOConsoleOutputStream
     * @see IOConsoleOutputStream#setColor(Color)
     */
    protected IOConsoleOutputStream getOutputStream(int messageKind) {
	final RGB streamRGB;
	switch (messageKind) {
	case MSG_DEFAULT:
	    streamRGB = defaultColor;
	    break;
	case MSG_RESPONSE:
	    streamRGB = responseColor;
	    break;
	case MSG_ERROR:
	    streamRGB = errorColor;
	    break;
	default:
	    streamRGB = defaultColor;
	    break;
	}

	final IOConsoleOutputStream outputStream = fConsole.newOutputStream();
	Display.getDefault().syncExec(new Runnable(){
		public void run() {
			outputStream.setColor(ConsoleColorManager.getColor(streamRGB));
		}});

	return outputStream;
    }
}
