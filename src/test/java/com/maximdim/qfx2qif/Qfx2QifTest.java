package com.maximdim.qfx2qif;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class Qfx2QifTest {

    @Test
    public void testParse1() throws IOException {
        InputStream is = getClass().getResourceAsStream("/accountactivity.qfx");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        new Qfx2Qif().parse(is, bos);
        String actual = bos.toString(StandardCharsets.UTF_8.name());
        String expected = convertInputStreamToString(getClass().getResourceAsStream("/accountactivity.qif"));

        Assert.assertEquals(expected, actual);
    }


    private static String convertInputStreamToString(InputStream is) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int size;
            while((size = is.read(buffer)) != -1){
                bos.write(buffer, 0, size);
            }
            return bos.toString(StandardCharsets.UTF_8.name());
        }
    }
}
