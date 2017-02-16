package com.tydic.guava.service;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import com.tydic.base.util.StringUtil;

import sun.misc.Signal;
import sun.misc.SignalHandler;

@SuppressWarnings("all")
public class KillTest {

    private static FileLock lock = null;

   
	public static void main(String[] args) throws Exception {
        if (args.length == 0 || "start".equalsIgnoreCase(args[0])) {
            lock = lockOnly();
            if (lock == null) return;
            try {

                //注册捕获kill信号
                ProcessSignal processSignal = new ProcessSignal();
                Signal.handle(new Signal("TERM"), processSignal);//kill命令
                Signal.handle(new Signal("INT"), processSignal);//ctrl+c 命令
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                    	System.out.println("killed11111111");
                    }
                });
            } catch (Throwable ex) {
            }
            lock.release();
            lock.close();
            lock = null;
        }
        
        while (true) {
        	Thread.currentThread().sleep(10000000000L);
		}
        
    }

    //获取进程号
    public static String getCurrentProcessId() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    /**
     * 文件锁，保证程序只被启动一次
     *
     * @return
     */
    public static FileLock lockOnly() throws Exception {
        File f = new File("_dpp.pid");
        if (!f.exists()) {
            f.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        FileChannel fc = raf.getChannel();
        MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, 16);
        FileLock lock = fc.tryLock(8, 8, false);//尝试获取文件后半截独占锁
        long pid = 0;
        if (lock != null) {
            //没有其他程序独占
            pid = Long.valueOf(getCurrentProcessId());
            out.putLong(pid);
            out.force();
            f.deleteOnExit();//文件退出后，删除文件
            System.out.println("进程号=>" + pid);
        } else {
            //有其他程序独占
            out = fc.map(FileChannel.MapMode.READ_ONLY, 0, 8);//读文件的前半截
            pid = out.getLong();
            fc.close();
            raf.close();
            System.out.println("程序已被另一进程启动,进程号=>" + pid);
        }
        return lock;
    }

    //监控程序终止信号
    static class ProcessSignal implements SignalHandler {

        ProcessSignal() {
        }

        public void handle(Signal signal) {
            String nm = signal.getName();
            if (nm.equals("TERM") || nm.equals("INT") || nm.equals("KILL")) {
                System.out.println("接收到kill信号……");
            } else {
                System.out.println("非法信号:" + nm);
            }
        }
    }


}
