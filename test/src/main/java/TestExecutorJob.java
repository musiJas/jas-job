/**
 * @program: job-boot
 * @description:
 * @author: chen_mx
 * @create: 2019-09-05 10:56
 **/
public class TestExecutorJob {

    public static void main(String[] args) {
        System.out.println("模拟执行开始......");
        for(int  i=0;i<1000;i++){
            System.out.println("执行开始数据:"+i);
            try {
                String  time=args[0];
                Thread.sleep(Long.parseLong(time));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("执行结束");
    }
}
