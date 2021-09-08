/* File:      ConsoleReader.java
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

package net.sourceforge.flora.eclipse.reasoner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;

import org.eclipse.swt.widgets.Display;

/**
 * A Thread which reads from a <code>InputStream</code> and passes the read String to {@link FloraReasoner#parseCommand(String)}
 * 
 * @author Daniel Winkler
 */
public class ConsoleReader extends Thread {

    private InputStream fStream;

    private BufferedReader fReader;

    private StringBuffer fQuery;

    private boolean inText;

    private boolean inString;

    private boolean inCharSequence;

    /**
     * the Constructor
     * 
     * @param stream the <code>InputStream</code> to read from
     */
    public ConsoleReader(InputStream stream) {
	fStream = stream;
	fReader = new BufferedReader(new InputStreamReader(fStream));

	fQuery = new StringBuffer();
	inText = false;
	inString = false;
	inCharSequence = false;
    }

    /** reads from the <code>InputStream</code> and passes every line
     * to {@link #parseLine(String)}
     * 
     * @see java.lang.Thread#run()
     * @see #parseLine(String)
     * @see BufferedReader#readLine()
     */
    public void run() {
	String line;

	try {
	    while ((line = fReader.readLine()) != null) {
		parseLine(line);
	    }
	} catch (IOException e) {
	    // stream closed, shut down
	}
    }

    /**
     * parses a Flora-2 command and passes it to the {@link FloraReasoner}
     * if the end of command was detected
     * 
     * @param command
     */
    public void parseLine(String command) {
	int sign = 0;

	StringReader parser = new StringReader(command);

	try {
	    while ((sign = parser.read()) >= 0) {
//	    	char thechar = (char) sign;
		inText = inString || inCharSequence;

		fQuery.append((char) sign);

		if (!inText && ((char) sign == '.')) {
		    parser.mark(1);
		    int temp = parser.read();
		    parser.reset();

		    if (Character.isWhitespace(temp) || (temp < 0)) {
			Display.getDefault().syncExec(new Runnable() {
			    public void run() {
		    		FloraReasoner.getInstance().addString("?- " + fQuery.toString());
			    }
			});

			fQuery = new StringBuffer();
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
	    if (fQuery.length() > 0) // fquery is not a new empty StringBuffer
	    	fQuery.append('\n');
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }
}
