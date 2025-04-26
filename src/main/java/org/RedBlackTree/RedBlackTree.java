package org.RedBlackTree;

enum Color {
    RED,
    BLACK
}

public class RedBlackTree {
    private final Node EMPTY = new Node(0);
    private Node root;

    static class Node {
        int element;
        Node left;
        Node right;
        Color color;
        Node parent;

        Node(int data) {
            this.element = data;
            this.color = Color.BLACK; // По умолчанию чёрный
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        Node(int item, Node left, Node right) {
            this(item);
            this.left = left;
            this.right = right;
        }
    }

    public RedBlackTree() {
        EMPTY.color = Color.BLACK;
        EMPTY.left = EMPTY;
        EMPTY.right = EMPTY;
        root = EMPTY;
    }

    public void insert(int item) {
        Node newNode = new Node(item, EMPTY, EMPTY);
        newNode.color = Color.RED; // Новые узлы всегда красные

        if (root == EMPTY) {
            root = newNode;
            root.color = Color.BLACK;
            return;
        }

        Node current = root;
        Node parent = null;

        while (current != EMPTY) {
            parent = current;
            if (item < current.element) {
                current = current.left;
            } else if (item > current.element) {
                current = current.right;
            } else {
                return; // Дубликаты не допускаются
            }
        }

        newNode.parent = parent;
        if (item < parent.element) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixInsert(newNode);
    }

    private void fixInsert(Node node) {
        while (node != root && node.parent.color == Color.RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle.color == Color.RED) {
                    // Случай 1: дядя красный
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                } else {
                    // Случай 2: дядя чёрный
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Случай 3
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                // Симметричный случай
                Node uncle = node.parent.parent.left;
                if (uncle.color == Color.RED) {
                    node.parent.color = Color.BLACK;
                    uncle.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = Color.BLACK;
                    node.parent.parent.color = Color.RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = Color.BLACK;
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != EMPTY) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != EMPTY) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    public void printTree() {
        if (root == EMPTY) {
            System.out.println("Дерево пустое");
            return;
        }
        printTree(root, "", true);
    }

    private void printTree(Node node, String prefix, boolean isTail) {
        if (node == EMPTY) return;

        printTree(node.right, prefix + (isTail ? "    " : "│   "), false);
        System.out.print(prefix);
        System.out.print(isTail ? "└── " : "├── ");
        System.out.print(node.color == Color.BLACK ? "B" : "R");
        System.out.println(node.element);
        printTree(node.left, prefix + (isTail ? "    " : "│   "), true);
    }
}