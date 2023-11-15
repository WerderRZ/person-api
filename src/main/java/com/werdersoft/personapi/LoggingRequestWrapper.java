package com.werdersoft.personapi;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class LoggingRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public LoggingRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            InputStream requestInputStream = request.getInputStream();
            body = StreamUtils.copyToByteArray(requestInputStream);
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamWrapper(new ByteArrayInputStream(body));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(body));
        return new BufferedReader(isr);
    }

    private static class ServletInputStreamWrapper extends ServletInputStream {

        private final InputStream stream;

        public ServletInputStreamWrapper(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            try {
                return stream.available() == 0;
            } catch (IOException e) {
                return true;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // No-op
        }
    }

}
