import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.*;

/*
 * Today, I used the JGraphT library to compute the mincut of the resulting graph.
 * (after having failed to customize a Kargers algorithm multiple times)
 * 
 * Fortunately, the weight of the mincut was always 3 for me, so it fulfilled the challenge requirements.
 * 
 * compile command:
 *     javac -cp jgrapht-core-1.5.2.jar:jgrapht-io-1.5.2.jar Part1.java
 * 
 * execution command:
 *     java -cp jgrapht-core-1.5.2.jar:jgrapht-io-1.5.2.jar:. Part1
 */
public class Part1
{
    public static void main(String[] args) throws IOException
    {
        Map<String, List<String>> vertices = new HashMap<>();

        Files.readString(Path.of("input.txt")).lines().forEach(line -> {
            var tokens = Arrays.asList(line.replace(":", "").split(" "));
            vertices.computeIfAbsent(tokens.getFirst(), k -> new ArrayList<>());
            for (var token : tokens.subList(1, tokens.size())) {
                vertices.get(tokens.getFirst()).add(token);
                vertices.computeIfAbsent(token, k -> new ArrayList<>()).add(tokens.getFirst());
            }
        });

        DefaultUndirectedGraph<String, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        for (var v : vertices.entrySet()) {
            graph.addVertex(v.getKey());
        }

        for (var v : vertices.entrySet()) {
            for (var n : v.getValue()) {
                graph.addEdge(v.getKey(), n);
            }
        }

        var minCut = new StoerWagnerMinimumCut<>(graph);

        if (minCut.minCutWeight() != 3) {
            System.err.println("weight of mincut was not 3, instead " + minCut.minCutWeight());
            System.exit(1);
        }

        int totalNodes = graph.vertexSet().size();
        int sideANodes = minCut.minCut().size();

        System.out.println((totalNodes-sideANodes) * sideANodes);
    }
}
