package com.iot;

import java.util.Arrays;
import java.io.IOException;

class TestHarness {
    public static void main (String[] args) throws IOException {
        // args
        if (args.length<2) {
            System.out.println(args);
            System.out.println(args.length);
            System.out.println("Syntax: java TestHarness LOG_PREFIX COMMAND");
            System.out.println("        java TestHarness run42 ls -l");
            System.exit(1);
        }
        String prefix = args[0];
        String command = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        System.out.println("Prefix: "+prefix);
        System.out.println("Command: "+command);
        
        // start process
        Process p = Runtime.getRuntime().exec(command);
        
        // start loggers
        Logger logger_output  = new OutputLogger(prefix+"_output.log", ",", p);
        Logger logger_process = new ProcessLogger(prefix+"_process.log", ",", p, 200);
        logger_output.start();
        logger_process.start();
    }
}
