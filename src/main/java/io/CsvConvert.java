package io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CsvConvert {
	private final List<String[]> csv = new ArrayList<>();

	public CsvConvert(final List<String[]> csv) {
		this.csv.addAll(csv);
	}


	public List<String[]> format() {
		return csv
				.stream()
				.map(CsvConvert::formatLine)
				.collect(Collectors.toList());
	}

	public static String[] formatLine(final String[] line) {
		return Arrays.stream(line)
				.map(CsvConvert::formatElement)
				.toArray(String[]::new);
	}

	public static String formatElement(final String element) {
		if(isDoubleQuoted(element)) {
			return element.substring(1, element.length()-1);
		} else {
			return element;
		}
	}

	public static boolean isDoubleQuoted(final String element) {
		return element.startsWith("\"") && element.endsWith("\"");
	}

	public void print() {
		csv.forEach(strary -> System.out.println(String.join(", ", strary)));
	}

}
