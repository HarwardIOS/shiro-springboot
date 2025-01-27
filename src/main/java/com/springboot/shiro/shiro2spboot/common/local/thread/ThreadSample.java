package com.springboot.shiro.shiro2spboot.common.local.thread;

import com.springboot.shiro.shiro2spboot.common.util.DateTimeUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 抽卡模拟
 * 将抽卡简化成随机取一个1000的样本中的数，取到指定的算抽中
 * 在取到需要的时，会将与其同样的从期望中一并移除
 * <p>
 * 使用多线程时，有时需关注其他线程的完成情况
 * 采用线程的方式  Thread
 */
//TODO 使用volatile关键字修饰的变量不适用于使用n++，此时需使用synchronized关键字
//TODO 经核实，数据丢失随着线程数的增加而增加，推测是代码中其他部分的问题
//TODO 将线程创建方式改为线程池之后，数据丢失问题有所好转(只有极少量数据丢失)，该方法使用synchronized(变量名)较synchronized(this)修饰同步代码块，效果更好
//@SpringBootTest
public class ThreadSample {

    private Logger logger4j = LoggerFactory.getLogger(ThreadSample.class);
    private Integer s50 = 0;
    private Integer s100 = 0;
    private Integer s150 = 0;
    private Integer s200 = 0;
    private Integer s250 = 0;
    private Integer s300 = 0;
    private Integer s350 = 0;
    private Integer s400 = 0;
    private Integer s450 = 0;
    private Integer s500 = 0;
    private Integer other = 0;
    //   开启模拟线程数
    private static Integer threadCount = 100;
    //   模拟次数
    private Integer simuCount = 1000000;
    private static final CountDownLatch latch = new CountDownLatch(threadCount);

    @Test
    public void startWork() {

        try {
//            记录开始时间
            long start = DateTimeUtil.getCurMilli();
//            开始模拟
//        创建随机数
//        池子集合
            List<List<Integer>> lists = new ArrayList<>();
//        池子1 2% 2% 2.5% 2.5% 2.5%
            List<Integer> list1 = Arrays.asList(20, 20, 25, 25, 25);

//        池子2 2% 1.8% 1.8% 2.5% 5%
            List<Integer> list2 = Arrays.asList(20, 18, 25, 50);
//        将池子放入总集
            lists.add(list1);
            lists.add(list2);
//            使用线程池创建线程，这里使用java8新增的newWorkStealingPool线程池，
            ExecutorService threadPool = Executors.newWorkStealingPool();
//        开始模拟
            for (int i = 0; i < threadCount; i++) {
                SimuThread simuThread = new SimuThread(lists);
                threadPool.execute(simuThread);
//                Thread thread = new Thread(simuThread);
//                thread.start();
            }
//            等待并关闭线程池
            threadPool.shutdown();
//            因为把结果输出放在主线程，所以需要设计主线程等待其他线程结束
            latch.await();
            System.out.println("输出结果");
            logger4j.info("50次以内：" + (double) s50 * 100 / simuCount + "%;");
            logger4j.info("100次以内：" + (double) s100 * 100 / simuCount + "%;");
            logger4j.info("150次以内：" + (double) s150 * 100 / simuCount + "%;");
            logger4j.info("200次以内：" + (double) s200 * 100 / simuCount + "%;");
            logger4j.info("250次以内：" + (double) s250 * 100 / simuCount + "%;");
            logger4j.info("300次以内：" + (double) s300 * 100 / simuCount + "%;");
            logger4j.info("350次以内：" + (double) s350 * 100 / simuCount + "%;");
            logger4j.info("400次以内：" + (double) s400 * 100 / simuCount + "%;");
            logger4j.info("450次以内：" + (double) s450 * 100 / simuCount + "%;");
            logger4j.info("500次以内：" + (double) s500 * 100 / simuCount + "%;");
            logger4j.info("500次以上：" + (double) other * 100 / simuCount + "%;");
            System.out.println("总计模拟:" + (s50 + s100 + s150 + s200 + s250 + s300 + s350 + s400 + s450 + s500 + other) + "次");
//            记录结束时间
            long end = DateTimeUtil.getCurMilli();
//            输出模拟用时
            System.out.println(end - start);
        } catch (InterruptedException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    class SimuThread implements Runnable {

        private List<List<Integer>> lists;

        private Random random = SecureRandom.getInstanceStrong();

        SimuThread(List<List<Integer>> lists) throws NoSuchAlgorithmException {
            this.lists = lists;
        }

        /**
         * @Description: 进行模拟，并分析统计结果
         * @Param: []
         * @return: void
         * @Author: Hongyan Wang
         * @Date: 2019/9/24 11:11
         */
        @Override
        public void run() {
            int singleSimuCount = simuCount / threadCount;
            for (int j = 0; j < singleSimuCount; j++) {
//            开始模拟
                int count = simulate(random, lists);
//            记录结果
                if (count <= 50) synchronized (s50) {
                    s50++;
                }
                else if (count <= 100) synchronized (s100) {
                    s100++;
                }
                else if (count <= 150) synchronized (s150) {
                    s150++;
                }
                else if (count <= 200) synchronized (s200) {
                    s200++;
                }
                else if (count <= 250) synchronized (s250) {
                    s250++;
                }
                else if (count <= 300) synchronized (s300) {
                    s300++;
                }
                else if (count <= 350) synchronized (s350) {
                    s350++;
                }
                else if (count <= 400) synchronized (s400) {
                    s400++;
                }
                else if (count <= 450) synchronized (s450) {
                    s450++;
                }
                else if (count <= 500) synchronized (s500) {
                    s500++;
                }
                else synchronized (other) {
                        other++;
                    }
            }
            synchronized (this) {
                System.out.printf("运行结束,总计模拟%d次%n", (s50 + s100 + s150 + s200 + s250 + s300 + s350 + s400 + s450 + s500 + other));
            }
            latch.countDown();
        }

        /**
         * @Description: 抽卡模拟，核心代码区
         * @Param: [random, lists]
         * @return: int
         * @Author: Hongyan Wang
         * @Date: 2019/9/24 9:59
         */
        private int simulate(Random random, List<List<Integer>> lists) {
            //        抽卡数
            int count = 0;
//              存放目标集合，内部数个子集合
            List<List<Integer>> mblist = new ArrayList<>();
//            存放目标值的集合
            List<Integer> dblist = new ArrayList<>();
            for (List<Integer> list : lists) {
//            生成目标数值的开始值
                int start = 1;
                for (Integer integer : list) {
//                单个子集合
                    List<Integer> zlist = new ArrayList<>();
                    for (int i = 0; i < integer; i++) {
                        zlist.add(start);
                        start++;
                    }
                    mblist.add(zlist);
                    dblist.addAll(zlist);
                }
//            当目标值不为空时进行抽卡

                if (!dblist.isEmpty()) do {
//            开始抽卡
                    int result = random.nextInt(1000) + 1;
//                判断是否抽到目标卡
                    //                    当抽到目标卡时，遍历目标子集合
                    if (dblist.contains(result)) //                        判断目标是否在子集合中
                        for (List<Integer> integerList : mblist)
                            //                            当目标在子集合中时，从目标集合中移除对应子集合内容
                            if (integerList.contains(result)) dblist.removeAll(integerList);
//                抽卡次数+1
                    count++;
                } while (!dblist.isEmpty());
//            抽完一池，置空子集合
                mblist.clear();
            }
            return count;
        }
    }
}
