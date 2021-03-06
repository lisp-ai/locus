package locus.graph.factories;
import locus.graph.base.DenseDigraph;
import locus.graph.base.SparseDigraph;
import java.util.Arrays;
import java.util.List;

public class BasicDigraphFactory {
    public static DenseDigraph completeDigraph(int n) {
        // full true boolean matrix
        boolean[] rowArray = new boolean[n];
        Arrays.fill(rowArray, true);
        boolean[][] matrix = new boolean[n][n];
        Arrays.fill(matrix, rowArray);

        return new DenseDigraph(n, matrix);
    }

    public static SparseDigraph cycleDigraph(int n) {
        var adjacency = new List[n];

        for(int i = 0; i < n; i++) {
            var next = (i+1) % n;
            adjacency[i] = List.of(next);
        }

        return new SparseDigraph(n, adjacency);
    }

    public static SparseDigraph pathDigraph(int n) {
        var adjacency = new List[n];

        // fill the out neighbours for all non-final vertices
        for(int i = 0; i < (n-1); i++) {
            var next = i+1;
            adjacency[i] = List.of(next);
        }

        // we need to set the last value to an empty list so it is not null
        adjacency[n-1] = List.of();

        // return the resulting digraph
        return new SparseDigraph(n, adjacency);
    }

}
