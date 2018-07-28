package physics;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;

import model.AirWall;
import model.Player;
import model.Position;
import model.Sprite;
import model.Vector;
import physics.shape.CircleShape;
import physics.shape.RectangleShape;
import utils.MMath;

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
            while (true) {
                handleCollide();
                for (PhysicsBody body : mComponents) {
                    if (body.isFixed())
                        continue;

                    Vector speed = body.getSpeed();
                    Position pos = body.getPosition();
                    pos.x = (int) (pos.x + speed.x);
                    if (body.isGravityEnable() && body.getWeight() > 0.0) {
                        speed.y += mGravity * 0.016;
                    }

                    pos.x += speed.x;
                    pos.y += speed.y;
                }

                try {
                    sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
                if ((mComponents.get(i).getCollideCode() & mComponents.get(j).getCollideCode()) == 0) {
                    continue;
                }


                if (isCollide(mComponents.get(i), mComponents.get(j))) {
                    boolean ha = mComponents.get(i).getParentSprite().onCollide(mComponents.get(j).getParentSprite());
                    boolean hb = mComponents.get(j).getParentSprite().onCollide(mComponents.get(i).getParentSprite());

                    if (!ha | !hb) {
                        Vector[] res = elasticCollision(mComponents.get(i).getWeight(),
                                mComponents.get(i).getSpeed(),
                                mComponents.get(j).getWeight(),
                                mComponents.get(j).getSpeed());
                        if (!ha)
                            mComponents.get(i).setSpeed(res[0]);

                        if (!hb)
                            mComponents.get(j).setSpeed(res[1]);
                    }
                }
            }
        }
    }

    /**
     * 完全弹性碰撞计算
     * @param ma 物体a质量
     * @param va 物体a初速度
     * @param mb 物体b质量
     * @param vb 物体b初速度
     * @return 碰撞后a的速度和b的速度
     */
    private Vector[] elasticCollision(Double ma, Vector va, Double mb, Vector vb) {
        Vector[] res = new Vector[2];
        if (ma == Double.MAX_VALUE) {
            res[0] = new Vector(va);
            res[1] = new Vector(-vb.x, -vb.y);
            return res;
        }
        if (mb == Double.MAX_VALUE) {
            res[0] = new Vector(-va.x, -va.y);
            res[1] = new Vector(vb);
            return res;
        }

        System.out.println("" + ma + " " + mb);

        System.out.println("swap speed");
        res[0] = new Vector(vb);
        res[1] = new Vector(va);
        return res;
    }

    private boolean isCollide(PhysicsBody a, PhysicsBody b) {

        boolean cc = false, cr = false, rr = false;

        Position ap = a.getPosition(), bp = b.getPosition();

        if (a.getShape() instanceof CircleShape) {
            if (b.getShape() instanceof CircleShape) {
                cc = true;
            } else cr = true;
        } else if (b.getShape() instanceof CircleShape) {
            cr = true;
        } else rr = true;

        if (cc) {
            int d = MMath.triangle(ap.x - bp.x, ap.y - bp.y);
            return d <= ((CircleShape) a.getShape()).getRadius()
                    + ((CircleShape) b.getShape()).getRadius();
        }

        if (cr) {
            // TODO: 圆形与矩形碰撞检测
            rr = true;
        }

        if (rr) {
            int maxx = (int) MMath.max(ap.x,
                    bp.x,
                    ap.x + a.getShape().getWidth(),
                    bp.x + b.getShape().getWidth());
            int minx = (int) MMath.min(ap.x,
                    bp.x,
                    ap.x + a.getShape().getWidth(),
                    bp.x + b.getShape().getWidth());

            int maxy = (int) MMath.max(ap.y,
                    bp.y,
                    ap.y + a.getShape().getHeight(),
                    bp.y + b.getShape().getHeight());
            int miny = (int) MMath.min(ap.y,
                    bp.y,
                    ap.y + a.getShape().getHeight(),
                    bp.y + b.getShape().getHeight());

            return maxx - minx <= a.getShape().getWidth() + b.getShape().getWidth() &&
                    maxy - miny <= a.getShape().getHeight() + b.getShape().getHeight();
        }

        return false;

    }

    public double getGravity() {
        return mGravity;
    }

    public void setGravity(double gravity) {
        mGravity = gravity;
    }
}
