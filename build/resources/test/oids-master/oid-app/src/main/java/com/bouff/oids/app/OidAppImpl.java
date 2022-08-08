package com.bouff.oids.app;

import java.util.Set;

import com.bouff.oids.util.BasicTree;
import com.bouff.oids.util.BasicTreeNode;

/**
 * An implementation of '{@link OidApp}'.
 */
public class OidAppImpl implements OidApp {

    /**
     * A basic tree containing OID prefixes.
     */
    private BasicTree<Integer> oidTree = new BasicTree<>();

    /**
     * Constructor.
     *
     * @param oids the OID prefixes to use to build the tree.
     */
    public OidAppImpl(Set<String> oids) {
        oids.forEach(this::addToTree);
    }

    @Override
    public boolean isOidDescendant(String candiateOid) {

        BasicTreeNode<Integer> currentNode = oidTree.getRoot();
        String[] oidParts = oidToArray(candiateOid);

        boolean moreToProcess = false;
        for (String oidPart : oidParts) {

            BasicTreeNode<Integer> childNode = currentNode.getChild(Integer.parseInt(oidPart));
            if (childNode == null) {

                // The OID is fully contained in the tree (reached leaf node).
                if (currentNode.getChildren().isEmpty()) {
                    return true;
                }

                // The OID does not exist in the tree.
                return false;
            }

            currentNode = childNode;
            moreToProcess = !currentNode.getChildren().isEmpty();
        }

        // Handle special cases: the OID is identical to a prefix (true) or it is an ancestor (false).
        return !moreToProcess;
    }

    /**
     * Add an OID to the tree.
     *
     * @param oid the OID to add to the tree.
     */
    private void addToTree(String oid) {

        BasicTreeNode<Integer> currentNode = oidTree.getRoot();
        String[] oidParts = oidToArray(oid);

        for (String oidPart : oidParts) {

            Integer oidVal = Integer.parseInt(oidPart);

            // Attempt to find a child with the given value.
            BasicTreeNode<Integer> childNode = currentNode.getChild(oidVal);

            // No child found, so create it and add it to the tree.
            if (childNode == null) {
                childNode = new BasicTreeNode<>(oidVal, currentNode);
                currentNode.getChildren().add(childNode);
            }

            currentNode = childNode;
        }
    }

    /**
     * Convert an oid (in its String representation) into a String array.
     *
     * @param oid the oid.
     * @return    the array of Strings.
     */
    private String[] oidToArray(String oid) {

        // Remove the leading dot (avoids a leading empty String being interpreted as a value).
        oid = oid.replaceFirst("\\.", "");
        return oid.split("\\.");
    }
}
