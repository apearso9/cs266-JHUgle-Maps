package hw8.tests;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import hw8.Graph;
import hw8.Vertex;
import hw8.Edge;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public abstract class GraphTest {

    protected Graph<String, String> graph;

    protected abstract Graph<String, String> createGraph();

    @Before
    public void setupGraph() {
        this.graph = createGraph();
    }

    @Test
    public void simpleInsertEdge() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);
        ArrayList<Edge<String>> toSearch = new ArrayList<Edge<String>>();
        toSearch.add(e1);
        graph.label(e1, "Edge1 Label");
        graph.label(v1, "Vertex1 Label");

        assertEquals(v1, graph.from(e1));
        assertEquals(v2, graph.to(e1));
        assertEquals("11", e1.get());
        assertEquals(toSearch, graph.outgoing(v1));
        assertEquals(toSearch, graph.incoming(v2));
        assertEquals("Edge1 Label", graph.label(e1));
        assertEquals("Vertex1 Label", graph.label(v1));
    }

    @Test
    public void simpleRemoveEdge() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);

        assertEquals("11", graph.remove(e1));
        assertEquals("1", graph.remove(v1));
        assertEquals("2", graph.remove(v2));
        assertEquals("digraph {\n}", graph.toString());
    }

    @Test (expected = InsertionException.class)
    public void insertDuplicateEdge() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);
        String myOtherEdge = "12";
        Edge<String> e2 = graph.insert(v1, v2, myOtherEdge);
    }

    @Test (expected = InsertionException.class)
    public void insertSelfLoop() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v1, myEdge);
    }

    @Test (expected = PositionException.class)
    public void removeFalseVertex() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        graph.remove(v1);

        graph.remove(v1);
    }

    @Test (expected = RemovalException.class)
    public void removeVertexWithEdge() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);

        graph.remove(v1);
    }

    @Test (expected = PositionException.class)
    public void removeFalseEdge() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);
        graph.remove(e1);

        graph.remove(e1);
    }

    @Test
    public void vertexGetAndPut() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);

        assertEquals("1", v1.get());
        v1.put("one");
        assertEquals("one", v1.get());
    }

    @Test
    public void edgeGetAndPut() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);

        assertEquals("11", e1.get());
        e1.put("eleven");
        assertEquals("eleven", e1.get());
    }

    @Test (expected = PositionException.class)
    public void findNonexistentFrom() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);
        graph.remove(e1);
        Vertex<String> bad = graph.from(e1);
    }

    @Test (expected = PositionException.class)
    public void findNonexistentTo() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);
        graph.remove(e1);
        Vertex<String> bad = graph.to(e1);
    }

    @Test
    public void vertexLabelCheck() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);

        assertNull(graph.label(v1));
        graph.label(v1, "check");
        assertEquals("check", graph.label(v1));
    }

    @Test
    public void edgeLabelCheck() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);

        assertNull(graph.label(e1));
        graph.label(e1, "check");
        assertEquals("check", graph.label(e1));
    }

    @Test
    public void clearLabelsTest() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "11";
        Edge<String> e1 = graph.insert(v1, v2, myEdge);
        graph.label(v1, "v1");
        graph.label(v2, "v2");
        graph.label(e1, "e1");
        graph.clearLabels();

        assertNull(graph.label(v1));
        assertNull(graph.label(v2));
        assertNull(graph.label(e1));
    }

    @Test
    public void complexInsertTest() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "12";
        Edge<String> e12 = graph.insert(v1, v2, myEdge);
        String myV3 = "3";
        Vertex<String> v3 = graph.insert(myV3);
        String myV4 = "4";
        Vertex<String> v4 = graph.insert(myV4);
        myEdge = "34";
        Edge<String> e34 = graph.insert(v3, v4, myEdge);
        myEdge = "14";
        Edge<String> e14 = graph.insert(v1, v4, myEdge);
        myEdge = "41";
        Edge<String> e41 = graph.insert(v4, v1, myEdge);
        myEdge = "24";
        Edge<String> e24 = graph.insert(v2, v4, myEdge);
        myEdge = "13";
        Edge<String> e13 = graph.insert(v1, v3, myEdge);

        ArrayList<Edge<String>> toSearch = new ArrayList<Edge<String>>();
        toSearch.add(e12);
        toSearch.add(e14);
        toSearch.add(e13);
        assertEquals(toSearch, graph.outgoing(v1));
        toSearch.clear();
        toSearch.add(e12);
        assertEquals(toSearch, graph.incoming(v2));
        toSearch.clear();
        toSearch.add(e24);
        assertEquals(toSearch, graph.outgoing(v2));
        toSearch.clear();
        toSearch.add(e34);
        toSearch.add(e14);
        toSearch.add(e24);
        assertEquals(toSearch, graph.incoming(v4));
        toSearch.clear();
        toSearch.add(e34);
        assertEquals(toSearch, graph.outgoing(v3));
        toSearch.clear();
        toSearch.add(e13);
        assertEquals(toSearch, graph.incoming(v3));
    }

    @Test
    public void complexRemoveTest() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "12";
        Edge<String> e12 = graph.insert(v1, v2, myEdge);
        String myV3 = "3";
        Vertex<String> v3 = graph.insert(myV3);
        String myV4 = "4";
        Vertex<String> v4 = graph.insert(myV4);
        myEdge = "34";
        Edge<String> e34 = graph.insert(v3, v4, myEdge);
        myEdge = "14";
        Edge<String> e14 = graph.insert(v1, v4, myEdge);
        myEdge = "41";
        Edge<String> e41 = graph.insert(v4, v1, myEdge);
        myEdge = "24";
        Edge<String> e24 = graph.insert(v2, v4, myEdge);
        myEdge = "13";
        Edge<String> e13 = graph.insert(v1, v3, myEdge);

        assertEquals("12", graph.remove(e12));
        assertEquals("34", graph.remove(e34));
        assertEquals("14", graph.remove(e14));
        assertEquals("41", graph.remove(e41));
        assertEquals("24", graph.remove(e24));
        assertEquals("13", graph.remove(e13));
        assertEquals("1", graph.remove(v1));
        assertEquals("2", graph.remove(v2));
        assertEquals("3", graph.remove(v3));
        assertEquals("4", graph.remove(v4));
        assertEquals("digraph {\n}", graph.toString());
    }

    @Test
    public void vertexIterator() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "12";
        Edge<String> e12 = graph.insert(v1, v2, myEdge);
        String myV3 = "3";
        Vertex<String> v3 = graph.insert(myV3);
        String myV4 = "4";
        Vertex<String> v4 = graph.insert(myV4);

        Iterable<Vertex<String>> toIterate = graph.vertices();
        Iterator<Vertex<String>> iterator = toIterate.iterator();
        int size = 0;
        while (iterator.hasNext()) {
            size++;
            iterator.next();
        }
        assertEquals(4, size);
    }

    @Test
    public void iteratorTest() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "12";
        Edge<String> e12 = graph.insert(v1, v2, myEdge);
        String myV3 = "3";
        Vertex<String> v3 = graph.insert(myV3);
        String myV4 = "4";
        Vertex<String> v4 = graph.insert(myV4);
        myEdge = "34";
        Edge<String> e34 = graph.insert(v3, v4, myEdge);
        myEdge = "14";
        Edge<String> e14 = graph.insert(v1, v4, myEdge);
        myEdge = "41";
        Edge<String> e41 = graph.insert(v4, v1, myEdge);
        myEdge = "24";
        Edge<String> e24 = graph.insert(v2, v4, myEdge);
        myEdge = "13";
        Edge<String> e13 = graph.insert(v1, v3, myEdge);

        Iterable<Edge<String>> toIterate = graph.edges();
        Iterator<Edge<String>> iterator = toIterate.iterator();
        int size = 0;
        while (iterator.hasNext()) {
            size++;
            iterator.next();
        }
        assertEquals(6, size);

        Iterable<Vertex<String>> toIterate2 = graph.vertices();
        Iterator<Vertex<String>> iterator2 = toIterate2.iterator();
        size = 0;
        while (iterator2.hasNext()) {
            size++;
            iterator2.next();
        }
        assertEquals(4, size);

        Iterable<Edge<String>> toIterate3 = graph.outgoing(v1);
        Iterator<Edge<String>> iterator3 = toIterate3.iterator();
        size = 0;
        while (iterator3.hasNext()) {
            size++;
            iterator3.next();
        }
        assertEquals(3, size);

        Iterable<Edge<String>> toIterate4 = graph.incoming(v4);
        Iterator<Edge<String>> iterator4 = toIterate4.iterator();
        size = 0;
        while (iterator4.hasNext()) {
            size++;
            iterator4.next();
        }
        assertEquals(3, size);
    }

    @Test
    public void toStringTest() {
        String myVertex = "1";
        Vertex<String> v1 = graph.insert(myVertex);
        String myVertex2 = "2";
        Vertex<String> v2 = graph.insert(myVertex2);
        String myEdge = "12";
        Edge<String> e12 = graph.insert(v1, v2, myEdge);
        String myV3 = "3";
        Vertex<String> v3 = graph.insert(myV3);
        String myV4 = "4";
        Vertex<String> v4 = graph.insert(myV4);
        myEdge = "34";
        Edge<String> e34 = graph.insert(v3, v4, myEdge);
        myEdge = "14";
        Edge<String> e14 = graph.insert(v1, v4, myEdge);
        myEdge = "41";
        Edge<String> e41 = graph.insert(v4, v1, myEdge);
        myEdge = "24";
        Edge<String> e24 = graph.insert(v2, v4, myEdge);
        myEdge = "13";
        Edge<String> e13 = graph.insert(v1, v3, myEdge);
        String expected = "digraph {\n" +
                "  \"1\"\n" +
                "  \"2\"\n" +
                "  \"3\"\n" +
                "  \"4\"\n" +
                "    \"1\" -> \"2\" [label=\"12\"];\n" +
                "    \"3\" -> \"4\" [label=\"34\"];\n" +
                "    \"1\" -> \"4\" [label=\"14\"];\n" +
                "    \"4\" -> \"1\" [label=\"41\"];\n" +
                "    \"2\" -> \"4\" [label=\"24\"];\n" +
                "    \"1\" -> \"3\" [label=\"13\"];\n" +
                "}";
        assertEquals(expected, graph.toString());
    }

    @Test
    public void emptyToStringTest() {
        String expected = "digraph {\n" + "}";
        assertEquals(expected, graph.toString());
        String myV4 = "4";
        Vertex<String> v4 = graph.insert(myV4);
        graph.remove(v4);
        assertEquals(expected, graph.toString());
    }

}
