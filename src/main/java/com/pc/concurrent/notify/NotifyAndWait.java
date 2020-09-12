package com.pc.concurrent.notify;

/**
 * @description: wait()与notify的简单实用
 * @author:fangpengcheng
 * @date:2020/9/12 下午3:54
 */
public class NotifyAndWait {
    public static void main(String[] args) {
        Shop shop = new Shop();
        TestConsumer consumer = new TestConsumer(shop);
        TestProduct product = new TestProduct(shop);
        new Thread(product,"生产者C").start();
        //new Thread(product,"生产者D").start();
        //new Thread(consumer,"消费者A").start();
        new Thread(consumer,"消费者B").start();
    }

}

//商店
class Shop{
    private int product = 0;
    //进货,使用synchonized
    public synchronized void get(){
        while (product >= 10){
            System.out.println("货架已满");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + ":"+ ++product);
        this.notifyAll();
    }

    //卖货
    public synchronized void sale() {
        while (product<=0) {//为了避免虚假唤醒，应该使用在循环中才行
            System.out.println("缺货");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() +":"+ --product);
        this.notifyAll();
    }
}

class TestConsumer implements Runnable{
    private Shop shop;
    public TestConsumer(Shop shop) {
        this.shop = shop;
    }

    @Override
    public void run() {
        for (int i = 0;i<20;i++) {
            shop.sale();
        }
    }
}


//生产者
class TestProduct implements Runnable{
    private Shop shop;
    public TestProduct(Shop shop) {
        this.shop = shop;
    }

    @Override
    public void run() {
        for (int i = 0;i<20;i++) {
            shop.get();
        }
    }

}
