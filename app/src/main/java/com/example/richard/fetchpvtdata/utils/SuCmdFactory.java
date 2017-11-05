package com.example.richard.fetchpvtdata.utils;

import java.io.File;

/**
 * Created by richard on 04/11/17.
 */

public class SuCmdFactory {
    public enum CMD {JAVA, NATIVE}

    static public SuCmd makeSuCmd(CMD id, File pvtFile, File targetFile) {
        if (id == CMD.JAVA)
            return new SuCmdJava(pvtFile, targetFile);
        else
            return new SuCmdNative(pvtFile, targetFile);
    }
}
