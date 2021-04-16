package com.iot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.OsProcess;
import com.jezhumble.javasysmon.ProcessInfo;

class ProcessLogger extends Logger {
    int pid;
    int  delay;
    
    public ProcessLogger (String filename, String separator, Process p, int delay) throws IOException {
        super(filename, separator);
        this.delay = delay;
        this.pid = (int) p.pid();
        header(new String[] {"resident_bytes", "total_bytes", "system_millis", "user_millis"});
    }
    
    public void run () {
        try {
            while (true) {
                // TODO: make this more efficient
                OsProcess tree = OsProcess.createTree(new JavaSysMon().processTable());
                OsProcess node = tree.find(pid);
                if (node==null) break;
                ProcessInfo pinfo = tree.find(pid).processInfo();
                
                // http://jezhumble.github.io/javasysmon/com/jezhumble/javasysmon/ProcessInfo.html
                long resident_bytes = pinfo.getResidentBytes(); // main memory usage
                long total_bytes    = pinfo.getTotalBytes();    // total memory usage
                long system_millis  = pinfo.getSystemMillis();  // system time
                long user_millis    = pinfo.getUserMillis();    // user time

                log(resident_bytes+sep+total_bytes+sep+system_millis+sep+user_millis);
                
                Thread.sleep(delay);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("ProcessLogger("+filename+") caught exception: "+e);
        }
        try {
            finalize();
        } catch (IOException e) {
            System.out.println("ProcessLogger("+filename+") caught finalize exception: "+e);
        }
    }
}
