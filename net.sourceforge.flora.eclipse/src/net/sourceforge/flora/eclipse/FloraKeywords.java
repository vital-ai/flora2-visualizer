/* File:      FloraKeywords.java
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

package net.sourceforge.flora.eclipse;

import java.util.ArrayList;

/**
 * @author Daniel Winkler
 * 
 */
public class FloraKeywords {

	// Delimiters
	public static String SQUARE_BRACKET_OPEN = "[";

	public static String SQUARE_BRACKET_CLOSE = "]";

	public static String CURLY_BRACKET_OPEN = "{";

	public static String CURLY_BRACKET_CLOSE = "}";

	public static String PARENTHESIS_OPEN = "(";

	public static String PARENTHESIS_CLOSE = ")";

	public static String RULE_DEFINITION = ":-";

	public static String QUERY_DEFINITION = "?-";

	public static String DOT = ".";

	public static String COMMA = ",";

	public static String SEMICOLON = ";";

	public static String VERTICAL_LINE = "|";

	public static String PERCENT = "%";

	public static String HASH = "#";

	public static String UNDERSCORE_HASH = "_#";

	public static String AT = "@";

	public static String ARROW = "->";

	public static String STAR_ARROW = "*->";

	public static String ARROW2 = "=>";

	public static String STAR_ARROW2 = "*=>";

	public static String PLUS_ARROW_ARROW = "+>>";

	public static String STAR_PLUS_ARROW_ARROW = "*+>>";

	public static String DOUBLE_ARROW = "->->";

	public static String STAR_DOUBLE_ARROW = "*->->";

	public static String COLON = ":";

	public static String DOUBLE_COLON = "::";

	public static String EQUALITY = ":=:";

	public static String NOT_EQUAL = "\\=";

	// Flora Quote signs
	public static String SINGLE_QUOTE = "'";

	public static String DOUBLE_QUOTE = "\"";

	// Flora Keywords
	public static String FL_LOAD = "_load";

	public static String FL_DEMO = "_demo";

	public static String FL_END = "_end";

	public static String FL_ADD = "_add";

	public static String FL_ONE = "_one";

	public static String FL_ALL = "_all";

	public static String FL_HELP = "_help";

	public static String FL_COMPILE = "_compile";

	public static String FL_RESET = "_reset(";

	public static String FL_MAXERR = "_maxerr";

	public static String FL_TRACE = "_trace";

	public static String FL_NO_TRACE = "_noTrace";

	public static String FL_CHATTER = "_chatter";

	public static String FL_NO_CHATTER = "_noChatter";

	public static String FL_HALT = "_halt";

	public static String ABOLISH_ALL_TABLES = "abolish_all_tables";

	public static String OP = "op";

	// Flora-2 System Modules
	// Flora IO Keywords
	public static String IO_SEE = "see";

	public static String IO_SEEING = "seeing";

	public static String IO_SEEN = "seen";

	public static String IO_TELL = "tell";

	public static String IO_TELLING = "telling";

	public static String IO_TOLD = "told";

	public static String IO_WRITE = "write";

	public static String IO_STREAM = "Stream";

	public static String IO_WRITELN = "writeln";

	public static String IO_NL = "nl";

	public static String IO_READ = "read";

	public static String IO_FILENAME = "Filename";

	public static String IO_PORT = "Port";

	public static String IO_STDREAD = "stdread";

	public static String IO_STDWRITE = "stdwrite";

	public static String IO_FMT_WRITE = "fmt_write";

	public static String IO_FMT_WRITE_STRING = "fmt_write_string";

	public static String IO_FMT_READ = "fmt_read";

	public static String IO_WRITE_CANONICAL = "write_canonical";

	public static String IO_READ_CANONICAL = "read_canonical";

	public static String IO_READLINE = "readline";

	// Storage Control
	public static String COMMIT = "commit";

	public static String PURGEDB = "purgedb";

	// System Control
	public static String LIBPATH = "libpath";

	public static String TABLES = "tables";

	public static String ABORT = "abort";

	public static String CATCH = "catch";

	public static String INCLUDE = "include";

	public static String WARNING = "warning";

	public static String MESSAGE = "message";

	// Cardinality Constraint Checking
	public static String CHECK_CARDINALITY = "CheckCardinality";

	// Pretty Printing
	public static String PP_CLASS = "pp_class";

	public static String PP_SELF = "pp_self";

	public static String PP_ISA = "pp_isa";

	/**
	 * returns all Flora-Keywords
	 * 
	 * @return an ArrayList containing all Flora-Keywords
	 */
	public static ArrayList<String> getFloraKeywords() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(FL_LOAD);
		result.add(FL_DEMO);
		result.add(FL_END);
		result.add(FL_ADD);
		result.add(FL_ONE);
		result.add(FL_ALL);
		result.add(FL_HELP);
		result.add(FL_COMPILE);
		result.add(FL_RESET);
		result.add(FL_MAXERR);
		result.add(FL_TRACE);
		result.add(FL_NO_TRACE);
		result.add(FL_CHATTER);
		result.add(FL_NO_CHATTER);
		result.add(FL_HALT);
		result.add(ABOLISH_ALL_TABLES);
		result.add(OP);

		return result;
	}

	/**
	 * returns all System-Keywords
	 * 
	 * @return an ArrayList containing all System-Keywords
	 */
	public static ArrayList<String> getSystemKeywords() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(IO_SEE);
		result.add(IO_SEEING);
		result.add(IO_SEEN);
		result.add(IO_TELL);
		result.add(IO_TELLING);
		result.add(IO_TOLD);
		result.add(IO_WRITE);
		result.add(IO_STREAM);
		result.add(IO_WRITELN);
		result.add(IO_NL);
		result.add(IO_READ);
		result.add(IO_FILENAME);
		result.add(IO_PORT);
		result.add(IO_STDREAD);
		result.add(IO_STDWRITE);
		result.add(IO_FMT_WRITE);
		result.add(IO_FMT_WRITE_STRING);
		result.add(IO_FMT_READ);
		result.add(IO_WRITE_CANONICAL);
		result.add(IO_READ_CANONICAL);
		result.add(IO_READLINE);
		result.add(COMMIT);
		result.add(PURGEDB);
		result.add(LIBPATH);
		result.add(TABLES);
		result.add(ABORT);
		result.add(CATCH);
		result.add(INCLUDE);
		result.add(WARNING);
		result.add(MESSAGE);
		result.add(CHECK_CARDINALITY);
		result.add(PP_CLASS);
		result.add(PP_SELF);
		result.add(PP_ISA);

		return result;
	}

	/**
	 * returns all Keywords. The returned value conains all Keywords from {@link #getFloraKeywords()} and {@link #getSystemKeywords()}
	 * 
	 * @return an ArrayList containing all Keywords.
	 */
	public static ArrayList<String> getAllKeywords() {
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(getFloraKeywords());
		result.addAll(getSystemKeywords());
		return result;
	}

	/**
	 * returns all Delimiters
	 * 
	 * @return an ArrayList containing all Delimiters
	 */
	public static ArrayList<String> getDelimiters() {
		ArrayList<String> result = new ArrayList<String>();

		result.add(SQUARE_BRACKET_OPEN);
		result.add(SQUARE_BRACKET_CLOSE);
		result.add(CURLY_BRACKET_OPEN);
		result.add(CURLY_BRACKET_CLOSE);
		result.add(PARENTHESIS_OPEN);
		result.add(PARENTHESIS_CLOSE);
		result.add(RULE_DEFINITION);
		result.add(QUERY_DEFINITION);
		result.add(DOT);
		result.add(COMMA);
		result.add(SEMICOLON);
		result.add(COLON);
		result.add(DOUBLE_COLON);
		result.add(VERTICAL_LINE);
		result.addAll(getSigns());

		return result;
	}

	/**
	 * returns all Signs
	 * 
	 * @return an ArrayList containing all Signs
	 */
	public static ArrayList<String> getSigns() {
		ArrayList<String> result = new ArrayList<String>();

		result.add(PERCENT);
		result.add(HASH);
		result.add(UNDERSCORE_HASH);
		result.add(EQUALITY);
		result.add(NOT_EQUAL);
		result.add(AT);
		result.addAll(getValueReferenceConectives());

		return result;
	}

	/**
	 * returns all Value-Reference-Conectives
	 * 
	 * @return an ArrayList containing all Value-Reference-Conectives
	 */
	public static ArrayList<String> getValueReferenceConectives() {
		ArrayList<String> result = new ArrayList<String>();

		result.add(ARROW);
		result.add(STAR_ARROW);
		result.add(ARROW2);
		result.add(STAR_ARROW2);
		result.add(PLUS_ARROW_ARROW);
		result.add(STAR_PLUS_ARROW_ARROW);
		result.add(DOUBLE_ARROW);
		result.add(STAR_DOUBLE_ARROW);

		return result;
	}

	/**
	 * tests if the given String is a delimiter
	 * 
	 * @param delimiter
	 *            the String to test if it is a delimiter
	 * @return <code>true<code> if the given String is a delimiter
	 * 
	 * @see #getDelimiters()
	 */
	public static boolean isDelimiter(String delimiter) {
		return getDelimiters().contains(delimiter);
	}

	/**
	 * tests if the given String is part of a delimiter
	 * 
	 * @param delimiterPart
	 *            the String to test if it is part of a delimiter
	 * @return <code>true<code> if the given String is part of a delimiter
	 * 
	 * @see #getDelimiters()
	 */
	public static boolean isDelimiterPart(String delimiterPart) {
		for (String delimiter : getDelimiters())
			if (delimiter.contains(delimiterPart))
				return true;
		return false;
	}

	/**
	 * tests if the given <code>char</code> is the last Character of a Delimiter
	 * 
	 * @param delimiterPart
	 *            the <code>char</code> to test
	 * @return <code>true</code> if the given <code>char</code> is the last Character of a delimiter
	 */
	public static boolean isDelimiterEnd(Character delimiterPart) {
		for (String delimiter : getDelimiters())
			if (delimiter.endsWith(Character.toString(delimiterPart)))
				return true;
		return false;
	}

	/**
	 * tests if the given <code>char</code> is the first Character of a Delimiter
	 * 
	 * @param delimiterPart
	 *            the <code>char</code> to test
	 * @return <code>true</code> if the given <code>char</code> is the first Character of a delimiter
	 */
	public static boolean isDelimiterStart(Character delimiterPart) {
		for (String delimiter : getDelimiters())
			if (delimiter.startsWith(Character.toString(delimiterPart)))
				return true;
		return false;
	}

	/**
	 * tests if the given <code>String</code> is the beginning of a Delimiter
	 * 
	 * @param delimiterPart
	 *            the <code>String</code> to test
	 * @return <code>true</code> if the given <code>String</code> is the beginning of a delimiter
	 */
	public static boolean isDelimiterStart(String delimiterStart) {
		for (String delimiter : getDelimiters())
			if (delimiter.startsWith(delimiterStart))
				return true;
		return false;
	}
}
