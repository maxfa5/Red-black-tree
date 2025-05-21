package org.RedBlackTree;



import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);

        tree.insert(123);
        tree.insert(125);
        tree.insert(130);
        tree.insert(200);
        tree.insert(250);
        tree.insert(300);
        tree.remove(125);
        tree.remove(200);
        tree.remove(5);
//        tree.remove(250);
//        tree.remove(123);
//        System.out.println("has tree 123:" + tree.contains(123));
////        System.out.println("has tree 200:" + tree.contains(200));
//        Iterator<Integer> iter =  tree.iterator();
//        while(iter.hasNext()) {
//            System.out.println(iter.next());
//        }
        
        tree.printTree();
        System.out.println(tree.verifyInvariants());
    }


}

