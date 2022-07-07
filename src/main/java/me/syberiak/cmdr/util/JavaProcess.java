package me.syberiak.cmdr.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

public final class JavaProcess {

    private JavaProcess() {}        

    public static void exec(Class<?> klass, List<String> args) throws IOException, InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = klass.getName();

        List<String> command = new LinkedList<String>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        if (args != null) {
            command.addAll(args);
        }

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.inheritIO().start();
    }
}