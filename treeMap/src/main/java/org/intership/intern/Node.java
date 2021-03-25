package org.intership.intern;

import java.util.Map;

public final class Node<K, V> implements Map.Entry<K, V> {

    private final K key;
    private V value;
    private Node<K, V> left;
    private Node<K, V> right;
    private Node<K, V> parent;

    public Node(K key, V value, Node<K, V> parent) {
        this.key = key;
        this.value = value;
        this.parent = parent;
    }

    public Node<K, V> getLeft() {
        return left;
    }

    public void setLeft(Node<K, V> left) {
        this.left = left;
    }

    public Node<K, V> getRight() {
        return right;
    }

    public void setRight(Node<K, V> right) {
        this.right = right;
    }

    public Node<K, V> getParent() {
        return parent;
    }

    public void setParent(Node<K, V> parent) {
        this.parent = parent;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V prevValue = this.value;
        this.value = value;
        return prevValue;
    }
}
