package net.sourceforge.flora.eclipse.reasoner.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FloraPropertySetter {
	public static final int windowsOperatingSystem = 1 << 0;
	public static final int unixOperatingSystem = 1 << 1;
	
	public static boolean setEnvironmentProperties(String floraDir, String engine, int operatingSystem) {
		String fPrologDir = "";
		String fFloraDir = "";
		
		String floraSettingsPath = floraDir + File.separator + "java" + File.separator + "flora_settings";
		
		if ((operatingSystem & windowsOperatingSystem) > 0)//ask on preference page
			floraSettingsPath += ".bat";
		else if ((operatingSystem & unixOperatingSystem) > 0)
			floraSettingsPath += ".sh";
		else
			return false;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(floraSettingsPath)));
			
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("FLORADIR="))
					fFloraDir = line.substring(line.indexOf("FLORADIR=") + "FLORADIR=".length()).trim();
				if (line.contains("PROLOGDIR="))
					fPrologDir = line.substring(line.indexOf("PROLOGDIR=") + "PROLOGDIR=".length()).trim();
			}
			
			System.setProperty("FLORADIR", fFloraDir);
			System.setProperty("PROLOGDIR", fPrologDir);
			System.setProperty("java.library.path", fPrologDir);
			
			System.setProperty("ENGINE", engine);
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
}
