package com.bouff.oids.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A basic tree node.
 *
 * It is comprised of a parent node, a list of child nodes, and a type.
 * Note: for equality and hashing, only the node value is used.
 *
 * @param <T> the type for the node's value.
 */
public class BasicTreeNode<T> {

    /**
     * The node's parent.
     */
    private BasicTreeNode<T> parent = null;

    /**
     * The node's value.
     */
    private T value = null;

    /**
     * The node's children.
     */
    private List<BasicTreeNode<T>> children = new ArrayList<>();

    /**
     * Constructor.
     */
    public BasicTreeNode() {
    }

    /**
     * Constructor.
     *
     * @param nodeValue  the node's value.
     * @param parentNode the node's parent.
     */
    public BasicTreeNode(T nodeValue, BasicTreeNode<T> parentNode) {
        value = nodeValue;
        parent = parentNode;
    }

    public List<BasicTreeNode<T>> getChildren() {
        return children;
    }

    public void addChild(BasicTreeNode<T> child) {
        children.add(child);
    }

    /**
     * Find a child that contains the desired value.
     *
     * @param value the value.
     * @return      the child node that contains the desired value or null (if no matches found).
     */
    public BasicTreeNode<T> getChild(T value) {
        for (BasicTreeNode<T> node : getChildren()) {
            if (Objects.equals(value, node.getValue())) {
                return node;
            }
        }
        return null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T nodeValue) {
        value = nodeValue;
    }

    public BasicTreeNode<T> getParent() {
        return parent;
    }

    public void setParent(BasicTreeNode<T> parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicTreeNode<?> that = (BasicTreeNode<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
