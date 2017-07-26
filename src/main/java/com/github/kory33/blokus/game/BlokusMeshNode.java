package com.github.kory33.blokus.game;

import com.github.kory33.blokus.util.GraphNode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a node in a mesh-representation of a Blokus board.
 *
 * This is a wrapper class of <code>GraphNode&lt;Void&gt;</code>
 */
/*package-private*/ class BlokusMeshNode {
    private final GraphNode<Void> graphNode;

    /**
     * Create empty mesh node
     */
    /*package-private*/ BlokusMeshNode() {
        this.graphNode = new GraphNode<>(null);
    }

    private BlokusMeshNode(GraphNode<Void> graphNode) {
        this.graphNode = graphNode;
    }

    @NotNull
    @Contract(pure = true)
    /*package-private*/ Void getData() {
        return graphNode.getData();
    }

    /*package-private*/ void setData(@NotNull Void data) {
        graphNode.setData(data);
    }

    @Contract(pure = true)
    /*package-private*/ Set<BlokusMeshNode> getConnectedNodes() {
        Set<BlokusMeshNode> resultSet = new HashSet<>();
        graphNode.getConnectedNodes().forEach(node -> resultSet.add(new BlokusMeshNode(node)));
        return resultSet;
    }

    /*package-private*/ void addEdgeTo(@NotNull BlokusMeshNode targetNode) {
        graphNode.addUndirectedEdgeTo(targetNode.graphNode);
    }

    /*package-private*/ void removeEdgeTo(@NotNull BlokusMeshNode targetNode) {
        graphNode.removeBidirectedEdgeTo(targetNode.graphNode);
    }

    /*package-private*/ void disconnectAllEdges() {
        this.getConnectedNodes().forEach(this::removeEdgeTo);
    }

    /*package-private*/ boolean isConnectedWith(@NotNull BlokusMeshNode targetNode) {
        return graphNode.isConnectedWith(targetNode.graphNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlokusMeshNode that = (BlokusMeshNode) o;
        return Objects.equals(graphNode, that.graphNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graphNode);
    }
}
