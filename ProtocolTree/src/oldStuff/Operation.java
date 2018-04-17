package oldStuff;
//package otology;
//
//import resources.ResourceFindingDummyClass;
//
//import javax.swing.tree.TreeNode;
//import java.net.URL;
//import java.util.Enumeration;
//
//public class XXXOperation implements TreeNode {
//    public String name;
//    public URL url;
//
//    public XXXOperation(String book, String filename) {
//        name = book;
//        url = ResourceFindingDummyClass.getResource(filename);
//        System.out.println("Instantiating Operation class");
//        if (url == null) {
//            System.err.println("Couldn't find file: " + filename);
//        }
//    }
//
//    public String toString() {
//        return name;
//    }
//
//    @Override
//    public TreeNode getChildAt(int childIndex) {
//        return null;
//    }
//
//    @Override
//    public int getChildCount() {
//        return 0;
//    }
//
//    @Override
//    public TreeNode getParent() {
//        return null;
//    }
//
//    @Override
//    public int getIndex(TreeNode node) {
//        return 0;
//    }
//
//    @Override
//    public boolean getAllowsChildren() {
//        return false;
//    }
//
//    @Override
//    public boolean isLeaf() {
//        return false;
//    }
//
//    @Override
//    public Enumeration children() {
//        return null;
//    }
//}