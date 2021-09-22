import java.util.Timer;
import java.util.TimerTask;

public class VariableChange {

    private int x = 0;
    private int y = 0;
    private int z = 0;
    private static VariableChange instance;

    public VariableChange(){
        instance = this;
        count();
    }

    public static VariableChange getInstance(){
        return instance;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getZ() {
        return z;
    }

    public void count(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                VariableChange instance = VariableChange.getInstance();
                instance.setX(instance.getX() + 1);
                instance.setY(instance.getY() + 1);
                instance.setZ(instance.getZ() + 1);
            }
        }, 0, 500);//wait 0 ms before doing the action and do it evry 500ms (0.5 second)
    }
}
