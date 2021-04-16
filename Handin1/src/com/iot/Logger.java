package com.iot;

import java.io.FileWriter;
import java.io.IOException;

abstract class Logger extends Thread {
    String filename;
    FileWriter writer;
    String sep;
    
    protected Logger (String filename, String separator) throws IOException {
        this.filename  = filename;
        this.sep       = separator;
        this.writer = new FileWriter(filename);
    }
    
    protected void header (String[] entries) throws IOException {
        writer.write("# timestamp"+sep+String.join(sep, entries)+System.lineSeparator());
    }
    
    protected void log (String line) throws IOException {
        writer.write(System.currentTimeMillis()+sep+line+System.lineSeparator());
    }
    
    protected void finalize () throws IOException {
        writer.close();
    }
}
