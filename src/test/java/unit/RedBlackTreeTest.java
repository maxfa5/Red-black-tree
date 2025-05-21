package unit;
import org.RedBlackTree.RedBlackTree;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {
  private RedBlackTree tree = new RedBlackTree();
  
  @Test
  void testInsertAndContains() {
    assertFalse(tree.contains(5));
    tree.insert(5);
    assertTrue(tree.contains(5));
  }
  
  @Test
  void testEmptyTree() {
    assertEquals(0, (int) tree.size());
    assertEquals(0, tree.size());
  }
  @Test
  void testInsertionAndDeletion() {
    tree.insert(10);
    tree.insert(5);
    tree.insert(15);
    assertEquals(3, tree.size());
    
    tree.remove(5);
    assertEquals(2, tree.size());
    assertFalse(tree.contains(5));
    assertTrue(tree.verifyInvariants());
    
  }
  
  @Test
  void testTreeBalancing() {
    // Вставляем последовательно возрастающие значения
    for (int i = 1; i <= 100; i++) {
      tree.insert(i);
    }
    tree.printTree();
    // Проверяем, что глубина дерева логарифмическая
    int depth = tree.calculateDepth();
    System.out.println(depth);
    assertTrue(depth <= 2 * Math.log(tree.size()) / Math.log(2));
    assertTrue(tree.verifyInvariants());
  }
  @Test
  void stressTest() {
    Random random = new Random();
    Set<Integer> controlSet = new HashSet<>();
    
    // Тест на 10000 операций
    for (int i = 0; i < 10000; i++) {
      int value = random.nextInt(1000);
      if (random.nextBoolean()) {
        tree.insert(value);
        controlSet.add(value);
      } else {
        tree.remove(value);
        controlSet.remove(value);
      }
      // Периодическая проверка свойств
      if (i % 1000 == 0) {
        assertTrue(tree.verifyInvariants());
        assertEquals(controlSet.size(), tree.size());
      }
    }
  }
}