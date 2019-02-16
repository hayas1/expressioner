package io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Inputer {
	private final File file;

	public Inputer(final String filePath) {
		this.file = new File(filePath);
	}

	public List<String> readAsText() {
		try {
			return Files.readAllLines(file.toPath());
		} catch (final IOException e) {
			System.err.println("file not found");
			return null;
		}
	}

	public Stream<String> textLines() {
		try {
			return Files.lines(file.toPath());
		} catch (final IOException e) {
			System.err.println("file not found");
			return null;
		}
	}

	public List<String[]> readAsSimpleCsv() {
		return textLines()
				.map(str -> str.trim().split("\\s*,\\s*"))		// space* , space*
				.collect(Collectors.toList());
	}

	public List<String[]> readAsQuartedCsv() {
		return textLines()
				.map(str -> str.trim().split("\\s*,\\s*(?=(([^\"]*\"){2})*[^\"]*$)"))		//, exclude ","
				.map(CsvConvert::formatLine)
				.collect(Collectors.toList());
	}

}
