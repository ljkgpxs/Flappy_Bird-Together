package physics;

import java.util.ArrayList;
import java.util.List;

public class World {
    private double mGravity;
    private List<PhysicsBody> mComponents;

    private Thread mEmulatorThread;

    /**
     * 创建世界, 默认重力为10.0
     */
    public World() {
        this(10.0);
    }

    /**
     * 创建指定重力世界
     * @param gravity 重力G
     */
    public World(double gravity) {
        mComponents = new ArrayList<>();
        mGravity = gravity;
    }

    /**
     * 为世界添加组件
     * @param body 物理组件
     */
    public void addComponent(PhysicsBody body) {
        mComponents.add(body);
    }

    /**
     * 开始对世界进行模拟
     */
    public void run() {
        mEmulatorThread = new Thread(() -> {

        });

        mEmulatorThread.start();
    }

    public void stopEmulator() {
        mEmulatorThread.interrupt();
    }

    private void handleCollide() {
        for (int i = 0; i < mComponents.size(); i++) {
            for (int j = i + 1; j < mComponents.size(); j++) {
                // TODO:

            }
        }
    }

    public double getGravity() {
        return mGravity;
    }

    public void setGravity(double gravity) {
        mGravity = gravity;
    }
}
