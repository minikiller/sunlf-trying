package com.kalix.trying.news.biz;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Main {
    final private int timeOut = (int) TimeUnit.SECONDS.toMillis(1);
    public static  void main(String[] args){
        Main main=new Main();
        if (main.check()){
            System.out.println("ok");
        }else{
            System.out.println("no");
        }
    }

    public boolean check(){
        Socket sock = new Socket();
        try {
            sock.connect(new InetSocketAddress("localhost", 8181), timeOut);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (!sock.isClosed())
                    sock.close();
            } catch (IOException e) {
                System.out.println("can't close socket, must already been closed");
            }
        }
    }
}
