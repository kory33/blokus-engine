package com.github.kory33.blokus.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a node in a directed graph.
 *
 * Node represented by this class is capable of having an internally stored data of type <code>D</code>.
 *
 * @author Kory
 */
public class GraphNode<D> {
    @NotNull private D data;

    private final Set<GraphNode<D>> connectedNodes = new HashSet<>();

    public GraphNode(@NotNull D data) {
        this.data = data;
    }

    @NotNull
    @Contract(pure = true)
    public D getData() {
        return this.data;
    }

    public void setData(@NotNull D data) {
        this.data = data;
    }

    @Contract(pure = true)
    public Set<GraphNode<D>> getConnectedNodes() {
        return this.connectedNodes;
    }

    /**
     * Add a bidirectional connection between the given node and this node.
     * @param targetNode node to which a new connection is to be created.
     */
    public void addUndirectedEdgeTo(@NotNull GraphNode<D> targetNode) {
        this.addDirectedEdgeTo(targetNode);
        targetNode.addDirectedEdgeTo(this);
    }

    /**
     * Add a uni-directional connection from this node to the given node
     * @param targetNode node to which a new connection is to be created.
     */
    public void addDirectedEdgeTo(@NotNull GraphNode<D> targetNode) {
        this.connectedNodes.add(targetNode);
    }

    /**
     * Attempt to remove a bidirected edge between this node and target node.
     * <p>
     * This method does nothing if there is no bidirectional connection to the target node.
     * </p>
     * @param targetNode target node whose bidirected arrow towards this node will be removed
     */
    public void removeBidirectedEdgeTo(@NotNull GraphNode<D> targetNode) {
        if (!(this.isConnectedWith(targetNode) && targetNode.isConnectedWith(this))) {
            return;
        }

        this.removeDirectedEdgeTo(targetNode);
        targetNode.removeDirectedEdgeTo(targetNode);
    }

    /**
     * Attempt to remove a directed edge from this node to the target node.
     * @param targetNode node towards which an arrow(to be removed) is pointing from this node
     */
    public boolean removeDirectedEdgeTo(@NotNull GraphNode<D> targetNode) {
        return this.connectedNodes.remove(targetNode);
    }

    /**
     * Returns boolean value indicating if this node has an arrow pointing towards the given node
     * @param targetNode inspection target node
     * @return true if this node has an arrow pointing towards the given node
     */
    public boolean isConnectedWith(@NotNull GraphNode<D> targetNode) {
        return this.connectedNodes.contains(targetNode);
    }
}
