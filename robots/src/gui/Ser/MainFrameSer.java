package gui.Ser;

import java.io.Serializable;

public class MainFrameSer implements Serializable {
    private static final long serialVersionUID = 1L;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getState() {
        return state;
    }

    int x;
    int y;
    int width;
    int height;
    int state;

    public MainFrameSer(int x, int y, int width, int height, int state) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.state = state;
    }
}
