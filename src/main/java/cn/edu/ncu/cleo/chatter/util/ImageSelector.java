package cn.edu.ncu.cleo.chatter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @description 图像选择器
 */
public class ImageSelector {
    private Random random;

    private List<Integer> used; // 已使用
    private List<Integer> unused; // 未使用

    public ImageSelector(int numOfImages) {
        random = new Random();
        used = new ArrayList<Integer>();
        unused = new ArrayList<Integer>();
        for (int i = 0; i < numOfImages; i++) {
            unused.add(i);
        }
    }

    /**
     * 随机选取一张未使用的图片
     * @return
     */
    public synchronized int select() {
        int index = random.nextInt(unused.size());
        int image = unused.get(index);
        unused.remove(image);
        used.add(image);
        return image;
    }

    /**
     * 撤销图片的使用
     * @param image
     */
    public synchronized void retreat(int image) {
        unused.add(image);
        used.remove(image);
    }
}
