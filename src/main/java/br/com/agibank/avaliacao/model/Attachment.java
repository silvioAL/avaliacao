package br.com.agibank.avaliacao.model;

import lombok.*;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Attachment {

    private String path;
    private ByteBuffer buffer;
    private AsynchronousFileChannel asyncChannel;

}