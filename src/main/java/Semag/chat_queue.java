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
public class chat_queue<E>  {

    private LinkedList<E> list = new LinkedList<>();

    public chat_queue() {
    }

    public void enqueue(E e) {
        list.addLast(e);
    }

    public E dequeue() {
        return list.removeFirst();
    }

   public int getSize() {
        return list.size();
    }


    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return "MyQueue{" + "list=" + list + '}';
    }
}

