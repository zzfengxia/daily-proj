package com.zz.dailytest;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis.zz on 2015/12/10.
 * 描述：截取文件内容
 */
public class TestSubFile {
    private File file;
    private long rowCount;  // 行数标记初始化为1
    private RandomAccessFile accessFile;
    private FileChannel channel;
    private Charset charset;
    private Map<Long, String> rowInfos;  // 每行对应的在文件中的位置信息

    /**
     * 构造方法
     * @param f 操作的文件
     * @param charEncoding 设置字符编码
     */
    public TestSubFile(File f, String charEncoding) {
        this.file = f;
        charset = Charset.forName(charEncoding);
        init();
    }

    /**
     * 将文件中每一行的起始和结尾位置存入Map中
     */
    private void init() {
        BufferedReader bfReader = null;
        try {
            accessFile = new RandomAccessFile(file, "r");
            channel = accessFile.getChannel();
            rowInfos = new HashMap<Long, String>();
            bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            String tempStr = null;
            // 每行开始位置
            long begPosition = 0l;
            while((tempStr = bfReader.readLine()) != null) {
                rowCount++;
                // 因为RandomFileAccess的seek方法使在设置的文件位置的下一处开始读取， 所有这里设置指针-1；换行符占2个字节
                long endPosition = begPosition + tempStr.getBytes(charset).length - 1 + 2;
                rowInfos.put(rowCount, begPosition + "~" + endPosition);
                begPosition = endPosition + 1;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bfReader != null) {
                try {
                    bfReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 截取文件内容(包含起始行，不包含结束行)
     * @param begRow 起始行
     * @param endRow 结束行
     * @return
     */
    public String subFileInfo(long begRow, long endRow) {
        // TODO 自定义异常 抛出相关异常

        String out = null;
        String begLineInfo = null;
        String endLineInfo = null;
        endRow--;
        try {
            begLineInfo = rowInfos.get(begRow);
            endLineInfo = rowInfos.get(endRow);
            accessFile.seek(Long.parseLong(begLineInfo.split("~")[0]));
            // 需要截取的字节大小
            int byteCounts = (int) (Long.parseLong(endLineInfo.split("~")[1]) -
                    Long.parseLong(begLineInfo.split("~")[0]) + 1);
            // 创建需要截取大小的字节缓冲区
            ByteBuffer byteBf = ByteBuffer.allocate(byteCounts);
            channel.read(byteBf);
            if (null != byteBf) {
                // 反转缓冲区，使用设置的字符编码解码此字节数据
                byteBf.flip();
                out = charset.decode(byteBf).toString();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            destory();
        }
        return out;
    }

    /**
     * 截取文件内容(从指定开始行到文件结尾)
     * @param begLine 开始行
     * @return
     */
    public String subFileInfo(long begLine) {
        if(begLine > rowCount || begLine < 1) {
            // TODO 自定义异常 抛出相关异常

        }
        return subFileInfo(begLine, begLine + rowCount);
    }
    /**
     * 截取文件内容(从指定开始行截取指定的行数)
     * @param begLine 开始行
     * @param rows 行数
     * @return
     */
    public String subFileInfo(long begLine, int rows) {
        if(begLine > rowCount || begLine < 1) {
            // TODO 自定义异常 抛出相关异常

        }
        return subFileInfo(begLine, begLine + rows);
    }
    /**
     * 关闭相应的资源、流
     */
    private void destory() {
        try {
            if(null != accessFile) {
                accessFile.close();
            }
            if(null != channel) {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TestSubFile sub = new TestSubFile(new File("d:\\test.txt"), "utf-8");

        String outStr = sub.subFileInfo(2, 5);
        System.out.println(outStr);
    }
}
