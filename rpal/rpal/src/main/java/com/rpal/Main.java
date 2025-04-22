package com.rpal;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java com.rpal.Main <sourcefile.rpal>");
            return;
        }
        File file = new File(args[0]);
        System.out.println("Interpreting file : "+file.getName());
    }
}