package br.com.agibank.avaliacao.service;

import br.com.agibank.avaliacao.handler.InputFileHandler;
import br.com.agibank.avaliacao.handler.OutputFileHandler;
import br.com.agibank.avaliacao.model.Attachment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class FileStreamingService {

    private final InputFileHandler inputFileHandler;

    private final OutputFileHandler outputFileHandler;

    public void readFileAsynchronously(final String filePath) {

        try {
            final AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(Paths.get(filePath), READ);
            final int fileSize = (int) asynchronousFileChannel.size();
            final ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);

            final Attachment attachment = getAttachment(filePath, asynchronousFileChannel, byteBuffer);
            asynchronousFileChannel.read(byteBuffer, 0, attachment, inputFileHandler);

        } catch (Exception exception) {
            log.info("Error reading file at path: " + filePath + " message: " + exception.getMessage());
        }
    }

    public void writeFileAsynchronously(final String filePath, final String fileContent) {

        try {
            final AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(Paths.get(filePath), WRITE, CREATE);

            final byte[] fileContentBytes = fileContent.getBytes();
            final ByteBuffer byteBuffer = ByteBuffer.wrap(fileContentBytes);

            final Attachment attachment = getAttachment(filePath, asynchronousFileChannel, byteBuffer);

            asynchronousFileChannel.write(byteBuffer, 0, attachment, outputFileHandler);

        } catch (Exception exception) {
            log.info("Error reading file at path: " + filePath + " message: " + exception.getMessage());
        }
    }

    private Attachment getAttachment(final String filePath, final AsynchronousFileChannel asynchronousFileChannel, final ByteBuffer byteBuffer) {
        return Attachment.builder()
                .asyncChannel(asynchronousFileChannel)
                .buffer(byteBuffer)
                .path(filePath)
                .build();
    }
}
