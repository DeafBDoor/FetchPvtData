package com.example.richard.fetchpvtdata.com.example.richard.fetchpvtdata.utils;

import java.io.File;

/**
 * Created by richard on 04/11/17.
 */

public class SuCmdNative extends SuCmd {
    @Override
    public boolean suCopyFile() {
        return false;
    }

    public SuCmdNative(File pvtFile, File targetFile) {
        super(pvtFile, targetFile);
    }
}
