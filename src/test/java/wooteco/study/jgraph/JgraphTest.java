package wooteco.study.jgraph;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JgraphTest {

    @Test
    public void getDijkstraShortestPath() {
        String source = "v3";
        String target = "v1";
        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath
            = new DijkstraShortestPath<>(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        System.out.println(shortestPath);

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("WeightedMultigraph 사용 가능 여부 확인")
    @Test
    public void getKShortestPaths_UsedWeightedMultigraph_Success() {
        String source = "v3";
        String target = "v1";

        WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        List<GraphPath<String, DefaultWeightedEdge>> paths
            = new KShortestPaths<>(graph, 1000).getPaths(source, target);

        System.out.println(paths);

        assertThat(paths).hasSize(2);
        paths.forEach(it -> {
            assertThat(it.getVertexList()).startsWith(source);
            assertThat(it.getVertexList()).endsWith(target);
        });
    }

    @Test
    public void getKShortestPaths() {
        String source = "v3";
        String target = "v1";

        Multigraph<String, DefaultWeightedEdge> graph = new Multigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");

        graph.addEdge("v1", "v2");
        graph.addEdge("v2", "v3");
        graph.addEdge("v1", "v3");

        List<GraphPath<String, DefaultWeightedEdge>> paths
            = new KShortestPaths<>(graph, 1000).getPaths(source, target);

        System.out.println(paths);

        assertThat(paths).hasSize(2);
        paths.forEach(it -> {
            assertThat(it.getVertexList()).startsWith(source);
            assertThat(it.getVertexList()).endsWith(target);
        });
    }
}
