package com.example.richard.fetchpvtdata.utils;

import java.io.File;

/**
 * Wrapper class to access native method for copying.
 */
public class SuCmdNative extends SuCmd {
    @Override
    public boolean suCopyFile() {
        return nativeSuCopyFile(pvtFile.getAbsolutePath(), targetFile.getAbsolutePath());
    }

    protected SuCmdNative(File pvtFile, File targetFile) {
        super(pvtFile, targetFile);
    }

    private native boolean nativeSuCopyFile(String pvtPath, String targetPath);
}
