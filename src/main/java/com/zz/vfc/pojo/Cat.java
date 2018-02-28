package com.zz.vfc.pojo;

/**
 * Created by Francis.zz on 2017/8/1.
 */
public class Cat implements Animals {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public byte[] prepareExternalAuthAPDU(int offset) {
        return new byte[0];
    }

    @Override
    public short processExternalAuthRes(byte[] origin, int offset) {
        return 0;
    }
}
