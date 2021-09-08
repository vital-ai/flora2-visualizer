package net.sourceforge.flora.eclipse.preferences;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author danielwinkler
 *
 * the main preference page for the Flora-2 plugin
 */
public class PreferencePage extends org.eclipse.jface.preference.PreferencePage implements IWorkbenchPreferencePage {

	/**
	 * creates an empty preference page for the Flora-2 plugin
	 */
	public PreferencePage() {
		setDescription("Flora-2 Visualizer Preference Page");
	}

	protected Control createContents(Composite parent) {
		return null;
	}

	public void init(IWorkbench workbench) {
	}

}
