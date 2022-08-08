package com.bouff.oids.util;

/**
 * A basic tree comprised of '{@link BasicTreeNode}'.
 *
 * @param <T> the value for the node's type.
 */
public class BasicTree<T> {

    /**
     * The root node of the tree.
     */
    private BasicTreeNode<T> root;

    /**
     * Constructor.
     */
    public BasicTree() {
        root = new BasicTreeNode<>();
    }

    public BasicTreeNode<T> getRoot() {
        return root;
    }
}