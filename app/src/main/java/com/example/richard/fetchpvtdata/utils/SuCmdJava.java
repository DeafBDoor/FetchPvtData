package com.example.richard.fetchpvtdata.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by richard on 04/11/17.
 */

public class SuCmdJava extends SuCmd {
    private final static int BUFSIZE = 4096;

    protected SuCmdJava(File pvtFile, File targetFile) {
        super(pvtFile, targetFile);
    }

    /**
     * Basic function to copy data from an inputstream to an output stream.
     * Fixed buffer size.
     *
     * @param input
     * @param output
     * @throws IOException
     */
    private static void copyStream(InputStream input, OutputStream output)
            throws IOException
    {
        byte[] buffer = new byte[BUFSIZE]; // Copy chunks of 4k
        int bytesRead;

        while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
        }

    }

    /**
     * A simple routine to copy a private file to a file represented by targetFile.
     * This routine uses cat to retrieve the private file data, instead of cp, so
     * I can avoid a second su command to change the ownership of the newly created file.
     *
     * @return true, if copy was successful. Otherwise, false.
     */
    public boolean suCopyFile() {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "/system/xbin/su", "-c", "cat",
                pvtFile.getAbsolutePath());

        FileOutputStream fos;
        // Uses process builder to execute cat through supersu. Fetch its output and copy
        // to the target file
        try {
            fos = new FileOutputStream(targetFile);
            Process p = processBuilder.start();
            InputStream is = p.getInputStream();

            copyStream(is, fos);

            int exitVal = p.waitFor();
            fos.close();
            Log.d(Constants.TAG, "Return value from process: " + exitVal);

            return exitVal == 0;
        } catch (Exception e) {
            Log.e(Constants.TAG, e.getMessage() );
            return false;
        }
    }
}
