package resources;

import javax.swing.*;
import java.net.URL;

public class ResourceFindingDummyClass {
    private static ResourceFindingDummyClass resourceFindingDummyClassSingleton = new ResourceFindingDummyClass();
    public static URL getResource(String path) {
                return resourceFindingDummyClassSingleton.getClass().getResource(path);
    }


    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    public static ImageIcon createImageIcon(String path) {
        URL imgURL = ResourceFindingDummyClass.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
