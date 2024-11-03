package com.seb.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * <p>RecursiveFile class.</p>
 *
 * @author xXTheSebXx
 * @version 1.1
 */
public class RecursiveFile {
    /**
     * <p>moveDir.</p>
     *
     * @param target a {@link java.io.File} object
     * @param dir a {@link java.io.File} object
     * @throws java.io.IOException if any.
     */
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
