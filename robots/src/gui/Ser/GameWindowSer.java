package gui.Ser;

import java.io.Serializable;

public class GameWindowSer implements Serializable {
    private static final long serialVersionUID = 3L;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    int x;
    int y;
    int width;
    int height;

    public boolean isIcon() {
        return isIcon;
    }

    public void setIcon(boolean icon) {
        isIcon = icon;
    }

    boolean isIcon;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isSelected;

    public GameWindowSer(int x, int y, int width, int height, boolean isIcon, boolean isSelected) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isIcon = isIcon;
        this.isSelected = isSelected;
    }
}
