package com.shop;

import java.io.IOException;
import java.io.InputStream;

public class ContentToUpperCaseInputStream extends InputStream {

    private InputStream stream;

    public ContentToUpperCaseInputStream(InputStream stream) {
        this.stream = stream;
    }


    @Override
    public int read() throws IOException {
        int nextByte;
        while((nextByte = this.stream.read()) != -1){
            return nextByte + 12;
        } 
        return -1;
    }
    
}
