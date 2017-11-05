package com.example.richard.fetchpvtdata.utils;

import java.io.File;

/**
 * Created by richard on 04/11/17.
 */

public abstract class SuCmd {
    public File getPvtFile() {
        return pvtFile;
    }

    public void setPvtFile(File pvtFile) {
        this.pvtFile = pvtFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    File pvtFile, targetFile;

    abstract public boolean suCopyFile();
    protected SuCmd(File pvtFile, File targetFile) {
        this.pvtFile = pvtFile;
        this.targetFile = targetFile;
    }
}
