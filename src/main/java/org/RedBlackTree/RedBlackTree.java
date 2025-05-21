package org.RedBlackTree;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

enum COLOR {
    RED,
    BLACK
}

public class RedBlackTree implements Iterable<Integer> {
    private final Node EMPTY = new Node(0);
    private Node root;
    
    
    public int calculateDepth() {
        if  (root==EMPTY){
            return 0;
        }
        return calculateDepth(root);
    }
    
    private int calculateDepth(Node node) {
        if (node==EMPTY){
            return 1;
        }
        int left = 1 + calculateDepth(node.left);
        int right =1 + calculateDepth(node.right);
        return Math.max(left, right);
    }
    static class Node {
        Integer element;
        Node left;
        Node right;
        COLOR color;
        Node parent;

        Node(Integer data) {
            this.element = data;
            this.color = COLOR.BLACK; // По умолчанию чёрный
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        Node(Integer item, Node left, Node right) {
            this(item);
            this.left = left;
            this.right = right;
        }
        boolean hasRedChild() {
            return (left != null && left.color == COLOR.RED) ||
                (right != null && right.color == COLOR.RED);
        }
        Node sibling() {
            if (parent == null) return null;
            return isOnLeft() ? parent.right : parent.left;
        }
        // check if node is left child of parent
        boolean isOnLeft() {
            return this == parent.left;
        }
    }

    public RedBlackTree() {
        EMPTY.color = COLOR.BLACK;
        EMPTY.left = EMPTY;
        EMPTY.right = EMPTY;
        root = EMPTY;
    }

    public void insert(Integer item) {
        Node newNode = new Node(item, EMPTY, EMPTY);
        newNode.color = COLOR.RED; // Новые узлы всегда красные

        if (root == EMPTY) {
            root = newNode;
            root.color = COLOR.BLACK;
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
        while (node != root && node.parent.color == COLOR.RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle.color == COLOR.RED) {
                    // Случай 1: дядя красный
                    node.parent.color = COLOR.BLACK;
                    uncle.color = COLOR.BLACK;
                    node.parent.parent.color = COLOR.RED;
                    node = node.parent.parent;
                } else {
                    // Случай 2: дядя чёрный
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    // Случай 3
                    node.parent.color = COLOR.BLACK;
                    node.parent.parent.color = COLOR.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                // Симметричный случай
                Node uncle = node.parent.parent.left;
                if (uncle.color == COLOR.RED) {
                    node.parent.color = COLOR.BLACK;
                    uncle.color = COLOR.BLACK;
                    node.parent.parent.color = COLOR.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = COLOR.BLACK;
                    node.parent.parent.color = COLOR.RED;
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.color = COLOR.BLACK;
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
            System.out.println("Tree is empty");
            return;
        }
        prIntegerTree(root, "", true);
    }

    private void prIntegerTree(Node node, String prefix, boolean isTail) {
        if (node == EMPTY) return;

        // Right child first (will appear above in the output)
        prIntegerTree(node.right, prefix + (isTail ? "    " : "│   "), false);

        // PrInt current node
        System.out.print(prefix);
        System.out.print(isTail ? "└── " : "├── ");

        // COLOR display (using ANSI codes for colored output)
        if (node.color == COLOR.RED) {
            System.out.print("\u001B[31mR\u001B[0m"); // Red
        } else {
            System.out.print("B"); // Black
        }
        System.out.println(node.element);

        // Left child last (will appear below in the output)
        prIntegerTree(node.left, prefix + (isTail ? "    " : "│   "), true);
    }
    // find node that do not have a left child
    // in the subtree of the given node
    Node successor(Node x) {
        Node temp = x;
        while (temp.left != EMPTY)
            temp = temp.left;
        return temp;
    }
    // find node that replaces a deleted node in BST
    Node BSTreplace(Node x) {
        if (x.left != EMPTY && x.right != EMPTY) {
            // Узел с двумя потомками - возвращаем преемника
            return getMin(x.right);
        }
        if (x.left == EMPTY && x.right == EMPTY) {
            // Лист
            return EMPTY;
        }
        // Узел с одним потомком
        return x.left != EMPTY ? x.left : x.right;
    }
    void deleteNode(Node v) {

        Node u = BSTreplace(v);
        
        // True when u and v are both black
        boolean uvBlack = ((u == EMPTY || u.color == COLOR.BLACK) && (v.color == COLOR.BLACK));
        Node parent = v.parent;
        
        if (u == EMPTY) {
            // u is NULL therefore v is leaf
            if (v == root)
                // v is root, making root null
                root = EMPTY;
            else {
                if (uvBlack)
                    // u and v both black
                    // v is leaf, fix double black at v
                    fixDoubleBlack(v);
                    
                    // u or v is red
                else if (v.sibling() != EMPTY)
                    // sibling is not null, make it red
                    v.sibling().color = COLOR.RED;
                
                // delete v from the tree
                if (v.isOnLeft())
                    parent.left = EMPTY;
                else
                    parent.right = EMPTY;
            }
            return;
        }
        
        if (v.left == EMPTY || v.right == EMPTY) {
            // v has 1 child
            if (v == root) {
                // v is root, assign the value of u to v, and delete u
                v.element = u.element;
                v.left = v.right = EMPTY;
                // delete u;
            } else {
                // Detach v from tree and move u up
                if (v.isOnLeft())
                    parent.left = u;
                else
                    parent.right = u;
                
                u.parent = parent;
                
                if (uvBlack)
                    // u and v both black, fix double black at u
                    fixDoubleBlack(u);
                else
                    // u or v red, color u black
                    u.color = COLOR.BLACK;
            }
            
            return;
        }
        
        // v has 2 children, swap values with successor and recurse
        swapValues(u, v);
        deleteNode(u);
    }
    void swapValues(Node u, Node v) {
        int temp = u.element;
        u.element = v.element;
        v.element = temp;
    }
    void fixDoubleBlack(Node x) {
        // Reached root
        if (x == root)
            return;
        
        Node sibling = x.sibling(), parent = x.parent;
        
        if (sibling == null)
            // No sibling, double black pushed up
            fixDoubleBlack(parent);
        else {
            if (sibling.color == COLOR.RED) {
                // sibling red
                parent.color = COLOR.RED;
                sibling.color = COLOR.BLACK;
                
                if (sibling.isOnLeft())
                    // right case
                    rotateRight(parent);
                else
                    // right case
                    rotateLeft(parent);
                
                fixDoubleBlack(x);
            } else {
                // Sibling black
                if (sibling.hasRedChild()) {
                    // at least 1 red children
                    if (sibling.left != null && sibling.left.color == COLOR.RED) {
                        if (sibling.isOnLeft()) {
                            // left left
                            sibling.left.color = sibling.color;
                            sibling.color = parent.color;
                            rotateRight(parent);
                        } else {
                            // right right
                            sibling.left.color = parent.color;
                            rotateRight(sibling);
                            rotateLeft(parent);
                        }
                    } else {
                        if (sibling.isOnLeft()) {
                            // left right
                            sibling.right.color = parent.color;
                            rotateLeft(sibling);
                            rotateRight(parent);
                        } else {
                            // right right
                            sibling.right.color = sibling.color;
                            sibling.color = parent.color;
                            rotateLeft(parent);
                        }
                    }
                    parent.color = COLOR.BLACK;
                } else {
                    // 2 black children
                    sibling.color = COLOR.RED;
                    if (parent.color == COLOR.BLACK)
                        fixDoubleBlack(parent);
                    else
                        parent.color = COLOR.BLACK;
                }
            }
        }
    }
    public boolean contains(Integer value) {
        Node current = root;
        while (current != null && current != EMPTY) {
            if (value == current.element) {
                return true;
            } else if (value < current.element) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    public Integer size() {
        return size(root);
    }

    private Integer size(Node node) { //обход в глубину
        if (node == null || node == EMPTY) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    public void clear() {
        // Явная очистка ссылок для помощи сборщику мусора
        if (root != null && root != EMPTY) {
            clearSubtree(root);
        }
        root = EMPTY;  // Устанавливаем корень в EMPTY
    }

    private void clearSubtree(Node node) {
        // Рекурсивно очищаем поддеревья
        if (node.left != null && node.left != EMPTY) {
            clearSubtree(node.left);
            node.left = null;
        }
        if (node.right != null && node.right != EMPTY) {
            clearSubtree(node.right);
            node.right = null;
        }
        // Обнуляем ссылку на родителя (если нужно)
        node.parent = null;
    }

    public Integer first() throws NoSuchElementException{
        if (root == null || root == EMPTY) {
            throw new NoSuchElementException("Tree is empty");
        }
        return getMin(root).element;
    }

    private Node getMin(Node node) {
        while (node.left != null && node.left != EMPTY) {
            node = node.left;
        }
        return node;
    }
    public Integer last() {
        if (root == null || root == EMPTY) {
            throw new NoSuchElementException("Tree is empty");
        }
        return getMax(root).element;
    }

    private Node getMax(Node node) {
        while (node.right != null && node.right != EMPTY) {
            node = node.right;
        }
        return node;
    }
    public void remove(int n) {
        if (root == EMPTY)
            return;
        
        Node v = search(n), u;
        if (v.element != n) {
            return;
        }
        
        deleteNode(v);
    }
    // searches for given value
    // if found returns the node (used for delete)
    // else returns the last node while traversing (used in insert)
    Node search(int n) {
        Node temp = root;
        while (temp != EMPTY) {
            if (n < temp.element) {
                if (temp.left == EMPTY)
                    break;
                else
                    temp = temp.left;
            } else if (n == temp.element) {
                break;
            } else {
                if (temp.right == EMPTY)
                    break;
                else
                    temp = temp.right;
            }
        }
        
        return temp;
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return new AscendingIterator();
    }

    public Iterator<Integer> descendingIterator() {
        return new DescendingIterator();
    }

    private class AscendingIterator implements Iterator<Integer> {
        private final Stack<Node> stack = new Stack<>();

        public AscendingIterator() {
            pushLeft(root);
        }

        private void pushLeft(Node node) {
            while (node != null && node != EMPTY) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node current = stack.pop();
            pushLeft(current.right);
            return current.element;
        }
    }

    private class DescendingIterator implements Iterator<Integer> {
        private final Stack<Node> stack = new Stack<>();

        public DescendingIterator() {
            pushRight(root);
        }

        private void pushRight(Node node) {
            while (node != null && node != EMPTY) {
                stack.push(node);
                node = node.right;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node current = stack.pop();
            pushRight(current.left);
            return current.element;
        }
    }
    public boolean verifyInvariants() {
        if (root == EMPTY) return true;
        return root.color == COLOR.BLACK
            && verifyNoTwoRedNodesInRow(root)
            && verifyBlackHeight(root) != -1;
    }
    
    private boolean verifyNoTwoRedNodesInRow(Node node) {
        if (node == EMPTY) return true;
        if (node.color == COLOR.RED) {
            if (node.left.color != COLOR.BLACK || node.right.color != COLOR.BLACK) {
                return false;
            }
        }
        return verifyNoTwoRedNodesInRow(node.left) && verifyNoTwoRedNodesInRow(node.right);
    }
    
    private int verifyBlackHeight(Node node) {
        if (node == EMPTY) return 1;
        int left = verifyBlackHeight(node.left);
        int right = verifyBlackHeight(node.right);
        return left == right && left != -1 ? left + (node.color == COLOR.BLACK ? 1 : 0) : -1;
    }
}