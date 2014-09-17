package filevisitor;
import static java.nio.file.FileVisitResult.*;
import impl.InstanceTranslator;
import io.InstanceWriter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import model.Instance;

public class TranslateFiles extends SimpleFileVisitor<Path> {
	
	private InstanceTranslator translator;
	
	private PathModifier modifier;
	
	public TranslateFiles(InstanceTranslator translator, PathModifier modifier) {
		super();
		this.translator = translator;
		this.modifier = modifier;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		if (attr.isRegularFile() && file.toString().endsWith(".vrp")) {
			String path = file.getParent().toString().replace("data/tsplib95_instance", "data/output");
			Path outputDirectory = Paths.get(path);
			outputDirectory.toFile().mkdirs();
			
			System.out.format("Started translation of %s.\n", file);
			Instance instance = translator.getInstance(file);
			String instanceName = instance.getInfo().getName();
			System.out.format("%s successfully translated.\n", file.getFileName());
			
			path = String.format(outputDirectory + "/%s.xml", instanceName);
			Path outputPath = Paths.get(path);
			InstanceWriter.writeXML(instance, outputPath);
		}
		return CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return CONTINUE;
	}
}
