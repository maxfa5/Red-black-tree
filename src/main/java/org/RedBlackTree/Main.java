package org.RedBlackTree;
public class Main {
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        tree.insert(5);

        tree.insert(123);
        tree.insert(123);
        tree.insert(125);
        tree.insert(130);
        tree.insert(200);
        tree.insert(250);
        tree.insert(300);

        tree.printTree();
    }


}

