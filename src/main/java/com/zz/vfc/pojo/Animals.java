package com.zz.vfc.pojo;

/**
 * Created by Francis.zz on 2017/8/1.
 */
public interface Animals {
    String getName();
    byte[] prepareExternalAuthAPDU(int offset);

    short processExternalAuthRes(byte[] origin, int offset);
}
