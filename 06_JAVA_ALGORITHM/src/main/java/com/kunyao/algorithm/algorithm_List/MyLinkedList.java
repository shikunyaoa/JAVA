package com.kunyao.algorithm.algorithm_List;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @ClassName: MyLinkedList
 * @Author: kunyao
 * @Description: 基于链表实现的表
 * @Date: 2020/6/3 15:40
 * @Version: 1.0
 */
public class MyLinkedList<T> implements Iterable<T> {

    private static class Node<T>{

        public T data;
        public Node<T> prev;
        public Node<T> next;

        public Node(T data, Node<T> prev, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private int theSize;
    private int modCount = 0;
    private Node<T> beginMarker;
    private Node<T> endMarker;

    public void clear(){
        doClear();
    }

    private void doClear(){
        beginMarker = new Node<T>(null, null, null);
        endMarker = new Node<T>(null, beginMarker, null);
        beginMarker.next = endMarker;

        theSize = 0;
        modCount++;
    }

    public int size(){
        return theSize;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public boolean add(T x){
        add(size(), x);
        return true;
    }

    public void add(int idx, T x){
        addBefore(getNode(idx, 0, size()), x);
    }

    public T get(int idx){
        return getNode(idx).data;
    }

    public T set(int idx, T newVal){
        Node<T> p = getNode(idx);
        T oldVal = p.data;
        p.data = newVal;
        return oldVal;
    }

    public T remove(int idx){
        return remove(getNode(idx));
    }

    private void addBefore(Node<T> p, T x){
        Node<T> newNode = new Node<>(x, p.prev, p);
        newNode.prev.next = newNode;
        p.prev = newNode;
        theSize++;
        modCount++;
    }

    private T remove(Node<T> p){
        p.next.prev = p.prev;
        p.prev.next = p.next;
        theSize--;
        modCount++;
        return p.data;
    }

    private Node<T> getNode(int idx){
        return getNode(idx, 0, size() - 1);
    }

    private Node<T> getNode(int idx, int lower, int upper){
        Node<T> p;
        if(idx < lower || idx > upper){
            throw new IndexOutOfBoundsException();
        }

        if(idx < size() / 2){
            p = beginMarker.next;
            for(int i = 0; i < idx; i++){
                p = p.next;
            }
        }else{
            p = endMarker;
            for (int i = size(); i > idx ; i--) {
                p = p.prev;
            }
        }

        return p;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T>{

        private Node<T> current = beginMarker.next;
        private int expectedModCount = modCount;
        private boolean okToRemove = false;

        @Override
        public boolean hasNext() {
            return current != endMarker;
        }

        @Override
        public T next() {
            if( modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }

            if(!hasNext()){
                throw new NoSuchElementException();
            }

            T nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }



        @Override
        public void remove() {
            if(modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }

            if(!okToRemove){
                throw new IllegalStateException();
            }

            MyLinkedList.this.remove(current.prev);
            expectedModCount++;
            okToRemove = false;
        }
    }
}
