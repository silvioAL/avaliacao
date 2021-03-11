package br.com.agibank.avaliacao.handler;

import br.com.agibank.avaliacao.model.Attachment;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@ApplicationScoped
@AllArgsConstructor
public class OutputFileHandler implements CompletionHandler<Integer, Attachment> {

    @Override
    public void completed(Integer result, Attachment attach) {
        final Path path = Paths.get(attach.getPath()).toAbsolutePath();

        if (result.equals(-1)) {
            log.error("No content written to " + path);
            return;
        }

        log.info("bytes written to " + path);

        try {
            attach.getAsyncChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(final Throwable e, final Attachment attach) {
        try {
            attach.getAsyncChannel().close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
