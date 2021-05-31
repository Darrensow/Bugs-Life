/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semag;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author xianp
 */
public class chat_queue<E> implements Serializable {

    private LinkedList<E> list = new LinkedList<>();

    public chat_queue(E[] e) {
        for (int i = 0; i < e.length; i++) {
            enqueue(e[i]);
        }
    }

    public chat_queue() {
    }

    public void enqueue(E e) {
        list.addLast(e);
    }

    public E dequeue() {
        return list.removeFirst();
    }

    public E getElement(int i) {  // just get the element without remove
        return list.get(i);

    }

    public E peek() { // just get first element without remove
        return list.peek();
    }

    public int getSize() {
        return list.size();
    }

    public boolean contains(E e) {
        return list.contains(e);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return "MyQueue{" + "list=" + list + '}';
    }
}

