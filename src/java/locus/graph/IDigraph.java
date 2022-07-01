package locus.graph;
import java.util.List;

/**
 * The core abstraction of unlabeled directed graphs. A labeled directed graph can
 * be defined over an unlabeled directed graph using some labeling scheme.
 */
public interface IDigraph {

    /**
     * The number of vertices of a graph.
     *
     * @return the number of vertices of this graph.
     */
    public int order();

    /**
     * The number of edges of a graph.
     *
     * @return the number of edges in this graph.
     */
    public int size();

    /**
     * This method checks if an edge exists at a given location. The parameter
     * order corresponds to the direction of the edge.
     *
     * @param x the first coordinate of the edge
     * @param y the second coordinate of the edge
     * @return a boolean indicating existence of the given edge
     */
    public boolean containsEdge(int x, int y);

    /**
     * Returns the out neighbours of a vertex. Suppose that V is a vertex in a digraph G
     * then its out neighbours are vertices of the form W such that (V,W) is an edge
     * in the digraph.
     *
     * @param n the argument vertex
     * @return the out neighbours of a vertex
     */
    public List<Integer> outNeighbours(int n);

    /**
     * Returns the in neighbours of a vertex. Suppose that V is a vertex in a digraph G
     * then its in neighbours are vertices of the form W such that (W,V) is an edge
     * in the digraph.
     *
     * @param n the argument vertex
     * @return the in neighbours of a vertex
     */
    public List<Integer> inNeighbours(int n);

}