package br.com.agibank.avaliacao.handler;

import br.com.agibank.avaliacao.model.Attachment;
import br.com.agibank.avaliacao.service.InputContentService;
import br.com.agibank.avaliacao.service.OutputContentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class InputFileHandler implements CompletionHandler<Integer, Attachment> {

    private final InputContentService inputContentService;

    private final OutputContentService outputDataContentService;

    @Override
    public void completed(final Integer result, final Attachment attach) {

        log.info("%s bytes read from: " + result.toString() + " " + attach.getPath());
        final byte[] byteData = attach.getBuffer().array();

        try {
            final String fileIdentifier = UUID.randomUUID().toString();
            final String data = new String(byteData);
            final String[] splittedLines = data.split("\n");

            CompletableFuture.runAsync(() -> saveContent(splittedLines, fileIdentifier))
                    .thenRun(() -> generateReport(fileIdentifier));

            log.info("Read data is: " + data);
            attach.getAsyncChannel().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(final Throwable exc, final Attachment attachment) {
        log.info("Failed to read file: " + exc.getLocalizedMessage() + " ,at Path: " + attachment.getPath());
    }

    private void saveContent(final String[] lines, final String fileIdentifier) {
        Arrays.stream(lines).forEach(
                line -> inputContentService.saveLineContent(line, fileIdentifier));
    }

    private void generateReport(final String fileName) {
        outputDataContentService.generateReport(fileName);
    }

}
