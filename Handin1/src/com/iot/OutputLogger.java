package com.iot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class OutputLogger extends Logger {
    BufferedReader input;
    
    public OutputLogger (String filename, String separator, Process p) throws IOException {
        super(filename, separator);
        input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        header(new String[] {"line"});
    }
    
    public void run () {
        try {
            String line;
            while ((line = input.readLine()) != null) {
                log(line);
            }
        } catch (IOException e) {
            System.out.println("OutputLogger("+filename+") caught exception: "+e);
        }
        try {
            finalize();
        } catch (IOException e) {
            System.out.println("OutputLogger("+filename+") caught finalize exception: "+e);
        }
    }
}
