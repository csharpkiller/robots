package gui.Ser;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SaveFramePosition {

    public static Map<String, String> savePosition(Component component) {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("X", Integer.toString(component.getX()));
        hashMap.put("Y", Integer.toString(component.getY()));
        hashMap.put("Width", Integer.toString(component.getWidth()));
        hashMap.put("Height", Integer.toString(component.getHeight()));

        if(component instanceof JInternalFrame){
            var frameComponent = (JInternalFrame)component;
            hashMap.put("IsIcon", Boolean.toString(frameComponent.isIcon()));
            hashMap.put("IsSelected", Boolean.toString(frameComponent.isSelected()));
            hashMap.put("IsMaximum", Boolean.toString(frameComponent.isMaximum()));
        }
        return hashMap;
    }
}
