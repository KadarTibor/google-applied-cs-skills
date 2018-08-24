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
            insertNodeInTreeRecursive(root, value);
        }
    }

    private void insertNodeInTreeRecursive(TreeNode root, int value) {
        if (root.getValue() > value && root.left != null) {
            // go on the left branch of the tree
            insertNodeInTreeRecursive(root.left, value);
            root.setHeight(Math.max(root.left.getHeight(), root.right != null ? root.right.getHeight() : 0) + 1);
            return;
        } else if (root.getValue() < value && root.right != null) {
            // go on the right branch of the tree
            insertNodeInTreeRecursive(root.right, value);
            root.setHeight(Math.max(root.left != null ? root.left.getHeight() : 0, root.right.getHeight()) + 1);
            return;
        }
        if (root.getValue() > value) {
            // insert left
            root.left = new TreeNode(value);
        } else {
            // insert right
            root.right = new TreeNode(value);
        }
        root.setHeight(Math.max(root.left != null ? root.left.getHeight() : 0, root.right != null ? root.right.getHeight() : 0) + 1);
    }

    private void insertNodeInTreeIterative(int value){
        TreeNode parser = root;
        while(parser.left != null || parser.right != null){
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

    private TreeNode search(int value) throws Exception{
        TreeNode current = root;
        while(current != null){
            if(current.getValue() > value && current.left != null){
                current = current.left;
            } else if(current.getValue() < value){
                current = current.right;
            } else if(current.getValue() == value){
                //we found the node
                break;
            } else {
                throw new Exception("The value " + value + " is not present in the tree");
            }
        }
        return current;
    }

    public void invalidateNode(int targetValue) {
        try{
            TreeNode target = search(targetValue);
            target.invalidate();
        } catch (Exception ex) {
            // todo show some error message
        }
    }
}
