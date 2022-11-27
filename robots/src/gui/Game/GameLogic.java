package gui.Game;

import java.awt.*;
import java.util.Observable;


public class GameLogic extends Observable{
    protected volatile double m_robotPositionX = 100;
    protected volatile double m_robotPositionY = 100;
    protected volatile double m_robotDirection = 0;

    protected volatile int m_targetPositionX = 250;
    protected volatile int m_targetPositionY = 200; // 100

    protected static final double maxVelocity = 0.1; // 0.1
    protected static final double maxAngularVelocity = 0.001; //0.001

    // костыль1
    private static boolean isFirstTime = true;

    public double[] getRobotPosition(){
        return new double[]{m_robotPositionX, m_robotPositionY};
    }

    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        double polarSystemCoord;

        // костыль1
        if(isFirstTime){
            polarSystemCoord=0.0;
            isFirstTime = false;
        }
        else {
            polarSystemCoord = (Math.atan2(diffY, diffX)); // полярная система коорд.
        }
        //polarSystemCoord = (Math.atan2(diffY, diffX)); // полярная система коорд.
        return asNormalizedRadians(polarSystemCoord);
    }

    protected void onModelUpdateEvent()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0; // скорость поворота
        if (angleToTarget > m_robotDirection) // m_robotDirection == 0
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(velocity, angularVelocity, 10);
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity); // скорость

        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity); // скорость поворота

        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        //notifyObserver();

        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        double[] toNotyf = new double[2];
        toNotyf[0] = newX;
        toNotyf[1] = newY;
        setChanged();
        notifyObservers(toNotyf);
        m_robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle) // angle = polsystcord
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }
}
