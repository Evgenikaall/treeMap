package org.intership.intern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

public class StudentMap<K, V> implements Map<K, V> {

    private Node<K, V> rootNode;
    private final Comparator<K> comparator;
    private int n;

    public StudentMap() {
        comparator = null;
    }

    public StudentMap(final Comparator<K> comparator) {
        this.comparator = comparator;
    }

    public StudentMap(final Map<? extends K, ? extends V> map){
        this.comparator = null;
        putAll(map);
    }


    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public boolean containsKey(Object o) {
        return nonNull(get(o));
    }

    @Override
    public boolean containsValue(Object o) {
        return values().contains(o);
    }

    @Override
    public V get(Object o) {
        if(isNull(o)) {
            throw new IllegalArgumentException();
        }
        if(isNull(comparator)){
            Comparable<? super K> comparable = (Comparable<? super K>) o;
            // unchecked ,  must throw ClassCastException
        }
        Node<K, V> node = getNodeByKey(o);
        return isNull(node) ? null : node.getValue();
    }

    @Override
    public V put(K key, V value) {
        if(isNull(key)|| isNull(value)){
            throw new IllegalArgumentException();
        }
        Node<K, V> node = this.rootNode;
        if (isNull(node)) {
            this.rootNode = new Node<>(key, value, null);
            n = 1;
        } else {
            int comparingResult = compareKeys(node.getKey(), key);
            Node<K, V> prevNode = null;
            boolean nodeSelector = false;
            while (nonNull(node) && comparingResult != 0) {
                prevNode = node;
                if (comparingResult > 0) {
                    node = node.getLeft();
                    nodeSelector = false;
                } else if (comparingResult < 0) {
                    node = node.getRight();
                    nodeSelector = true;
                }

                if (nonNull(node))
                    comparingResult = compareKeys(node.getKey(), key);
            }
            if (isNull(node)) {
                n++;
                node = new Node<>(key, value, prevNode);
                if (!nodeSelector) prevNode.setLeft(node);
                else prevNode.setRight(node);
            } else if(comparingResult == 0){
                V currentValue = node.getValue();
                node.setValue(value);
                return currentValue;
            }
        }
        return null;
    }

    @Override
    public V remove(Object o) {
        if(isNull(o)) throw new IllegalArgumentException();
        Node<K, V> node = getNodeByKey(o);
        V value = null;
        if (nonNull(node) && node != rootNode) {

            Node<K, V> nodeParent = node.getParent();
            value = node.getValue();

            if (isNull(node.getLeft()) && isNull(node.getRight())) {

                if (nodeParent.getRight() == node) nodeParent.setRight(null);
                else nodeParent.setLeft(null);

            } else {
                Node<K, V> rightNode = node.getRight();
                if (isNull(node.getLeft())) {
                    rightNode.setParent(nodeParent);
                    if (nodeParent.getRight() == node) nodeParent.setRight(node.getRight());
                    else nodeParent.setLeft(node.getRight());

                } else {

                    Node<K, V> leftNode = node.getLeft();
                    leftNode.setRight(rightNode);
                    leftNode.setParent(nodeParent);
                    rightNode.setParent(leftNode);

                    if (nodeParent.getLeft() == node) nodeParent.setLeft(leftNode);
                    else nodeParent.setRight(leftNode);

                }
            }
            n--;
            node = null;

        } else {
            if (node == rootNode) clear();
        }
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if (!map.isEmpty()) {
            for (K key : map.keySet()) {
                put(key, map.get(key));
                n++;
            }
        }
    }

    @Override
    public void clear() {
        n = 0;
        rootNode = null;
    }

    // question
    @Override
    public Set<K> keySet() {
        Set<K> studentSet = new TreeSet<>(comparator);
        putKeyInCollection(studentSet, rootNode);
        return studentSet;
    }


    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();
        putValueInCollection(values, rootNode);
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new TreeSet<>((o1, o2) -> compareKeys(o1.getKey(), o2.getKey()));
        putEntryInSet(set, rootNode);
        return set;
    }

    private Node<K, V> getNodeByKey(Object o) {
        Node<K, V> node = this.rootNode;
        if (nonNull(node)) {
            int comparingResult = compareKeys(node.getKey(), (K) o);

            if (comparingResult == 0) return node;
            while (nonNull(node) && comparingResult != 0) {

                if (comparingResult > 0) node = node.getLeft();
                else node = node.getRight();

                if (nonNull(node))
                    comparingResult = compareKeys(node.getKey(), (K) o);
            }
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder mapString = new StringBuilder();
        if (nonNull(rootNode)) {
            collectKeysIntoString(mapString, rootNode);
            mapString.deleteCharAt(mapString.length()-1);
        }
        return "{" + mapString.toString().replaceAll(" ", ", ") + "}";
    }

    private int compareKeys(Object o1, Object o2) {
        return isNull(comparator) ?
                ((Comparable<? super K>) o1).compareTo((K) o2) :
                comparator.compare((K) o1, (K) o2);
    }

    private void putKeyInCollection(Collection<K> collection, Node<K, V> node) {
        if (nonNull(node)) {
            collection.add(node.getKey());
            putKeyInCollection(collection, node.getLeft());
            putKeyInCollection(collection, node.getRight());
        }
    }

    private void putValueInCollection(Collection<V> collection, Node<K, V> node) {
        if (nonNull(node)) {
            collection.add(node.getValue());
            putValueInCollection(collection, node.getLeft());
            putValueInCollection(collection, node.getRight());
        }
    }

    private void collectKeysIntoString(StringBuilder stringBuilder, Node<K, V> node) {
        if (nonNull(node)) {
            // another way of resolving
            // stringBuilder.append(String.format("%s=%s ",node.getKey(), node.getValue()));
            stringBuilder.append(node.getKey().toString());
            stringBuilder.append("=").append(node.getValue().toString());
            stringBuilder.append(" ");
            collectKeysIntoString(stringBuilder, node.getLeft());
            collectKeysIntoString(stringBuilder, node.getRight());
        }
    }

    private void putEntryInSet(Set<Entry<K, V>> set, Node<K, V> node) {
        if (nonNull(node)) {
            set.add(node);
            putEntryInSet(set, node.getLeft());
            putEntryInSet(set, node.getRight());
        }
    }

}

