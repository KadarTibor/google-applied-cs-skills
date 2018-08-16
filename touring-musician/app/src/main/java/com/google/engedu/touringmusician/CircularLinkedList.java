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

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }

    Node head;

    public void insertBeginning(Point p) {
       Node passedInPoint = new Node();
       passedInPoint.point = p;

       if(head == null) {
           head = passedInPoint;
           head.next = head;
           head.prev = head;
       } else {
           passedInPoint.next = head;
           passedInPoint.prev = head.prev;
           head.prev.next = passedInPoint;
           head = passedInPoint;
       }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        Iterator<Point> iterator = iterator();
        Point p;
        if(iterator.hasNext()){
            p = iterator.next();
        } else {
            return 0.0f;
        }
        while(iterator.hasNext()){
            Point to = iterator.next();
            total += distanceBetween(p, to);
            p = to;
        }
        return total;
    }

    public void insertNearest(Point p) {
        Node passedInPoint = new Node();
        passedInPoint.point = p;
        if(head == null) {
            head = passedInPoint;
            head.next = head;
            head.prev = head;
        } else {
            float minDistance = distanceBetween(head.point, p);
            Node closestPoint = head;
            Node parser = head.next;
            while(parser != head){
                float distance = distanceBetween(parser.point, p);
                if(minDistance > distance){
                    closestPoint = parser;
                }
                parser = parser.next;
            }
            passedInPoint.next = closestPoint.next;
            passedInPoint.prev = closestPoint;
            closestPoint.next = passedInPoint;
        }
    }

    public void insertSmallest(Point p) {
        Node passedInPoint = new Node();
        passedInPoint.point = p;
        if(head == null) {
            head = passedInPoint;
            head.next = head;
            head.prev = head;
        } else if(head.next == head){
            head.next = passedInPoint;
            passedInPoint.prev = head;
            passedInPoint.next = head;
        } else {
            float baseDistance = totalDistance();
            Node insertionPoint = head;
            float potentialShortest = baseDistance -
                                      distanceBetween(head.point, head.next.point) +
                                      distanceBetween(head.point, p) + distanceBetween(p, head.next.point);
            Node parser = head.next.next;
            while(parser != head){
                float originalDistance = distanceBetween(parser.prev.point, parser.point);
                float newDistance = distanceBetween(parser.prev.point, p) + distanceBetween(p, parser.point);
                float totalDistance = baseDistance - originalDistance + newDistance;
                if(totalDistance < potentialShortest){
                    insertionPoint = parser;
                    potentialShortest = baseDistance - originalDistance + newDistance;
                }
                parser = parser.next;
            }
            passedInPoint.next = insertionPoint.next;
            passedInPoint.prev = insertionPoint;
            insertionPoint.next = passedInPoint;
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
