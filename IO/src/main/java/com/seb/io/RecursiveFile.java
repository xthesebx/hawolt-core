package com.seb.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RecursiveFile {
    public static void moveDir(File target, File dir) throws IOException {
        for (File f : dir.listFiles()) {
            System.err.println(f.getName());
            if (f.isFile()) {
                File f2 = new File(target + "\\" + f.getName());
                target.mkdirs();
                Files.move(f.toPath(), f2.toPath());
            } else if (f.isDirectory()) {
                moveDir(dir, f);
                f.delete();
            }
        }
        dir.delete();
    }
}
