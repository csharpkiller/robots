package gui.Ser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class SerFramePosition implements Serializable {
    private static final long serialVersionUID = 5L;
    ArrayList<Map> mapArrayList = new ArrayList<>();

    public SerFramePosition(){}

    public void addSavePosition(Map map){
        mapArrayList.add(map);
    }

    public ArrayList<Map> getMapArrayList() {
        return mapArrayList;
    }
}
