package io.read;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings {
	private String settings;

	public Settings() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("config.ini"));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			String ls = System.getProperty("line.separator");

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			reader.close();
			settings = stringBuilder.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String get(String s) {
		Pattern pattern = Pattern.compile("(?:" + s + "=)(.*)(?:\n?)");
		Matcher matcher = pattern.matcher(settings);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}
}
