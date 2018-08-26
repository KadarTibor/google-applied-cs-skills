/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.bstguesser;

import android.graphics.Canvas;

public class BinarySearchTree {
    private TreeNode root = null;

    public BinarySearchTree() {
    }

    public void insert(int value) {
        if (root == null) {
            root = new TreeNode(value);
        } else {
            root = insertNodeInTreeRecursive(root, value);
        }
    }

    private TreeNode insertNodeInTreeRecursive(TreeNode root, int value) {
        if (root.getValue() > value && root.left != null) {
            // go on the left branch of the tree
            root.left = insertNodeInTreeRecursive(root.left, value);
            root.setHeight(Math.max(root.left.getHeight(), root.right != null ? root.right.getHeight() : 0) + 1);
            // check if the subtree rooted at the current tree is balanced
            int balance = root.left.getHeight() - (root.right != null ? root.right.getHeight() : 0);
            return balance(root, value, balance);
        } else if (root.getValue() < value && root.right != null) {
            // go on the right branch of the tree
            root.right = insertNodeInTreeRecursive(root.right, value);
            root.setHeight(Math.max(root.left != null ? root.left.getHeight() : 0, root.right.getHeight()) + 1);
            // check if the subtree rooted at the current tree is balanced
            int balance = (root.left != null ? root.left.getHeight() : 0) - root.right.getHeight();
            return balance(root, value, balance);
        }
        if (root.getValue() > value) {
            // insert left
            root.left = new TreeNode(value);
            //print tree
            printTree(this.root, 0);
        } else {
            // insert right
            root.right = new TreeNode(value);
            printTree(this.root, 0);

        }
        System.out.println("-----------------------Inserted new node with value " + value);
        root.setHeight(Math.max(root.left != null ? root.left.getHeight() : 0, root.right != null ? root.right.getHeight() : 0) + 1);
        return root;
    }

    private TreeNode balance(TreeNode root, int value, int balance) {
        //left branch is bigger, val
        if (balance > 1 && value < root.left.getValue()) {
            root = rightRotate(root);
        } else if (balance < -1 && value > root.right.getValue()) {
            root = leftRotate(root);
        } else if (balance > 1 && value > root.left.getValue()) {
            root.left = leftRotate(root.left);
            root = rightRotate(root);
        } else if (balance < -1 && value < root.right.getValue()) {
            root.right = rightRotate(root.right);
            root = leftRotate(root);
        }
        return root;
    }

    private TreeNode rightRotate(TreeNode a) {
        System.out.println("right rotate on " + a.getValue());
        TreeNode b = a.left;

        a.left = b.right;
        b.right = a;

        //update the heights
        a.setHeight(Math.max(a.right != null ? a.right.getHeight() : 0, a.left != null ? a.left.getHeight() : 0) + 1);
        b.setHeight(Math.max(b.right != null ? b.right.getHeight() : 0, b.left != null ? b.left.getHeight() : 0) + 1);
        return b;
    }

    private TreeNode leftRotate(TreeNode a) {
        System.out.println("Left rotate on " + a.getValue());
        TreeNode b = a.right;

        a.right = b.left;
        b.left = a;

        //update the heights
        a.setHeight(Math.max(a.right != null ? a.right.getHeight() : 0, a.left != null ? a.left.getHeight() : 0) + 1);
        b.setHeight(Math.max(b.right != null ? b.right.getHeight() : 0, b.left != null ? b.left.getHeight() : 0) + 1);
        return b;
    }

    private void insertNodeInTreeIterative(int value) {
        TreeNode parser = root;
        while (parser.left != null || parser.right != null) {
            if (parser.getValue() > value && parser.left != null) {
                // go on the left branch of the tree
                parser = parser.left;
            } else if (parser.getValue() < value && parser.right != null) {
                // go on the right branch of the tree
                parser = parser.right;
            } else
                break;
        }

        if (parser.getValue() > value) {
            // insert left
            parser.left = new TreeNode(value);
        } else {
            // insert right
            parser.right = new TreeNode(value);
        }
    }

    public void positionNodes(int width) {
        if (root != null)
            root.positionSelf(0, width, 0);
    }

    public void draw(Canvas c) {
        root.draw(c);
    }

    public int click(float x, float y, int target) {
        return root.click(x, y, target);
    }

    private TreeNode search(int value) throws Exception {
        TreeNode current = root;
        while (current != null) {
            if (current.getValue() > value && current.left != null) {
                current = current.left;
            } else if (current.getValue() < value) {
                current = current.right;
            } else if (current.getValue() == value) {
                //we found the node
                break;
            } else {
                throw new Exception("The value " + value + " is not present in the tree");
            }
        }
        return current;
    }

    public void invalidateNode(int targetValue) {
        try {
            TreeNode target = search(targetValue);
            target.invalidate();
        } catch (Exception ex) {
            // todo show some error message
        }
    }


    private void printTree(TreeNode node, int level) {

        if (node != null) {
            if (node.left != null)
                printTree(node.left, level + 1);
            for (int i = 0; i <= level; i++) {
                System.out.print(" ");
            }
            System.out.println(node.getValue());
            if (node.right != null)
                printTree(node.right, level + 1);
        }
    }
}
