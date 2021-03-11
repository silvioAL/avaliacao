package br.com.agibank.avaliacao.service;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gradle.internal.impldep.org.apache.commons.io.FilenameUtils;
import org.gradle.internal.impldep.org.apache.commons.io.IOCase;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Startup
@ApplicationScoped
@AllArgsConstructor
public class InputFileWatchService {

    private static final String PATH_PROPERTY = "user.home";
    private static final String RELATIVE_PATH = "/data/in";
    private final FileStreamingService fileStreamer;

    public void execute(@Observes StartupEvent ev) throws IOException, InterruptedException {
        log.info("STARTED");


        final WatchService watchService
                = FileSystems.getDefault().newWatchService();

        final Path path = Paths.get(System.getProperty(PATH_PROPERTY) + RELATIVE_PATH);
        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {

                final Path changedFilePath = (Path) event.context();
                if (IOCase.SENSITIVE.checkEndsWith(changedFilePath.toString(), ".dat")) {
                    System.out.println("File has changed");
                    log.info(
                            "Event kind:" + event.kind()
                                    + ". File affected: " + event.context() + ".");
                    String filePath = FilenameUtils.concat(path.toString(), changedFilePath.toString());
                    fileStreamer.readFileAsynchronously(filePath);
                }
            }
            key.reset();
        }
    }
}
