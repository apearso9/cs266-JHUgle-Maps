package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;

import java.util.ArrayList;
import java.util.List;

/**
    An implementation of a directed graph using incidence lists
    for sparse graphs where most things aren't connected.
    @param <V> Vertex element type.
    @param <E> Edge element type.
*/
public class SparseGraph<V, E> implements Graph<V, E> {

    // Class for a vertex of type V
    private final class VertexNode<V> implements Vertex<V> {
        V data;
        Graph<V, E> owner;
        List<Edge<E>> outgoing;
        List<Edge<E>> incoming;
        Object label;
        double distance;
        boolean visited;

        VertexNode(V v) {
            this.data = v;
            this.outgoing = new ArrayList<>();
            this.incoming = new ArrayList<>();
            this.label = null;
            this.distance = 0;
            this.visited = false;
        }

        @Override
        public V get() {
            return this.data;
        }

        @Override
        public void put(V v) {
            this.data = v;
        }

    }

    //Class for an edge of type E
    private final class EdgeNode<E> implements Edge<E> {
        E data;
        Graph<V, E> owner;
        VertexNode<V> from;
        VertexNode<V> to;
        Object label;

        // Constructor for a new edge
        EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
            this.from = f;
            this.to = t;
            this.data = e;
            this.label = null;
        }

        @Override
        public E get() {
            return this.data;
        }

        @Override
        public void put(E e) {
            this.data = e;
        }
    }

    private List<Vertex<V>> vertices;
    private List<Edge<E>> edges;

    /** Constructor for instantiating a graph. */
    public SparseGraph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    // Checks vertex belongs to this graph
    private void checkOwner(VertexNode<V> toTest) {
        if (toTest.owner != this) {
            throw new PositionException();
        }
    }

    // Checks edge belongs to this graph
    private void checkOwner(EdgeNode<E> toTest) {
        if (toTest.owner != this) {
            throw new PositionException();
        }
    }

    // Converts the vertex back to a VertexNode to use internally
    private VertexNode<V> convert(Vertex<V> v) throws PositionException {
        try {
            VertexNode<V> gv = (VertexNode<V>) v;
            this.checkOwner(gv);
            return gv;
        } catch (ClassCastException ex) {
            throw new PositionException();
        }
    }

    // Converts and edge back to a EdgeNode to use internally
    private EdgeNode<E> convert(Edge<E> e) throws PositionException {
        try {
            EdgeNode<E> ge = (EdgeNode<E>) e;
            this.checkOwner(ge);
            return ge;
        } catch (ClassCastException ex) {
            throw new PositionException();
        }
    }

    private boolean duplicateEdge(Vertex<V> from, Vertex<V> to) {
        VertexNode<V> vertFrom = convert(from);
        for (int i = 0; i < vertFrom.outgoing.size(); i++) {
            Edge<E> toCheck = vertFrom.outgoing.get(i);
            EdgeNode<E> edgeToCheck = convert(toCheck);
            if (edgeToCheck.to == to) {
                return true;
            }
        }
        return false;
    }

    /** Returns the current distance stored in a Vertex.
     * @param theVertex the vertex distance desired.
     * @return the distance from that vertex to start.
     */
    public double getDistance(Vertex<V> theVertex) {
        VertexNode<V> yeah = convert(theVertex);
        return yeah.distance;
    }

    /** Updates the current distance stored in a Vertex.
     * @param theVertex the vertex distance desired.
     * @param dist the distance to change to.
     */
    public void setDistance(Vertex<V> theVertex, double dist) {
        VertexNode<V> yeah = convert(theVertex);
        yeah.distance = dist;
    }

    /** Says whether a vertex has already been visited.
     * @param v the Vertex to check.
     * @return whether it's been visited.
     */
    public boolean getVisited(Vertex<V> v) {
        VertexNode<V> yeah = convert(v);
        return yeah.visited;
    }

    /** Updates whether a vertex has been visited.
     * @param v the vertex.
     * @param trueOrFalse whether it's been visited.
     */
    public void setVisited(Vertex<V> v, boolean trueOrFalse) {
        VertexNode<V> yeah = convert(v);
        yeah.visited = trueOrFalse;
    }

    @Override
    public Vertex<V> insert(V v) {
        VertexNode<V> adding = new VertexNode<>(v);
        adding.owner = this;
        this.vertices.add(adding);
        return adding;
    }

    @Override
    public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
            throws PositionException, InsertionException {
        VertexNode<V> vertexFrom = convert(from);
        VertexNode<V> vertexTo = convert(to);
        if (!this.vertices.contains(vertexTo) ||
                !this.vertices.contains(vertexFrom)) {
            throw new PositionException();
        }
        EdgeNode<E> adding = new EdgeNode<>(vertexFrom, vertexTo, e);
        if (duplicateEdge(from, to) || from.equals(to)) {
            throw new InsertionException();
        }
        vertexFrom.outgoing.add(adding);
        vertexTo.incoming.add(adding);
        this.edges.add(adding);
        adding.owner = this;
        return adding;
    }

    @Override
    public V remove(Vertex<V> v) throws PositionException,
            RemovalException {
        if (!this.vertices.contains(v)) {
            throw new PositionException();
        }
        VertexNode<V> toRemove = convert(v);
        if (!toRemove.outgoing.isEmpty() || !toRemove.incoming.isEmpty()) {
            throw new RemovalException();
        }
        V theData = toRemove.data;
        this.vertices.remove(v);
        return theData;
    }

    @Override
    public E remove(Edge<E> e) throws PositionException {
        if (!this.edges.contains(e)) {
            throw new PositionException();
        }
        EdgeNode<E> toRemove = convert(e);
        VertexNode<V> from = toRemove.from;
        VertexNode<V> to = toRemove.to;
        from.outgoing.clear();
        to.incoming.clear();
        E theData = toRemove.data;
        this.edges.remove(e);
        return theData;
    }

    @Override
    public Iterable<Vertex<V>> vertices() {
        List<Vertex<V>> copy = new ArrayList<>();
        for (int i = 0; i < this.vertices.size(); i++) {
            copy.add(i, this.vertices.get(i));
        }
        return copy;
    }

    @Override
    public Iterable<Edge<E>> edges() {
        List<Edge<E>> copy = new ArrayList<>();
        for (int i = 0; i < this.edges.size(); i++) {
            copy.add(i, this.edges.get(i));
        }
        return copy;
    }

    @Override
    public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
        VertexNode<V> toSearch = convert(v);
        List<Edge<E>> copy = new ArrayList<>();
        for (int i = 0; i < toSearch.outgoing.size(); i++) {
            copy.add(i, toSearch.outgoing.get(i));
        }
        return copy;
    }

    @Override
    public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
        VertexNode<V> toSearch = convert(v);
        List<Edge<E>> copy = new ArrayList<>();
        for (int i = 0; i < toSearch.incoming.size(); i++) {
            copy.add(i, toSearch.incoming.get(i));
        }
        return copy;
    }

    @Override
    public Vertex<V> from(Edge<E> e) throws PositionException {
        if (!this.edges.contains(e)) {
            throw new PositionException();
        }
        EdgeNode<E> myEdge = convert(e);
        return myEdge.from;
    }

    @Override
    public Vertex<V> to(Edge<E> e) throws PositionException {
        if (!this.edges.contains(e)) {
            throw new PositionException();
        }
        EdgeNode<E> myEdge = convert(e);
        return myEdge.to;
    }

    @Override
    public void label(Vertex<V> v, Object l) throws PositionException {
        if (!this.vertices.contains(v)) {
            throw new PositionException();
        }
        VertexNode<V> myVert = convert(v);
        myVert.label = l;
    }

    @Override
    public void label(Edge<E> e, Object l) throws PositionException {
        if (!this.edges.contains(e)) {
            throw new PositionException();
        }
        EdgeNode<E> myEdge = convert(e);
        myEdge.label = l;
    }

    @Override
    public Object label(Vertex<V> v) throws PositionException {
        if (!this.vertices.contains(v)) {
            throw new PositionException();
        }
        VertexNode<V> myVert = convert(v);
        return myVert.label;
    }

    @Override
    public Object label(Edge<E> e) throws PositionException {
        if (!this.edges.contains(e)) {
            throw new PositionException();
        }
        EdgeNode<E> myEdge = convert(e);
        return myEdge.label;
    }

    @Override
    public void clearLabels() {
        for (Vertex<V> vertex : this.vertices) {
            VertexNode<V> myVertex = convert(vertex);
            myVertex.label = null;
        }
        for (Edge<E> edge : this.edges) {
            EdgeNode<E> myEdge = convert(edge);
            myEdge.label = null;
        }
    }

    private String vertexString(Vertex<V> v) {
        return "\"" + v.get() + "\"";
    }

    private String verticesToString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex<V> v : this.vertices) {
            sb.append("  ").append(vertexString(v)).append("\n");
        }
        return sb.toString();
    }

    private String edgeString(Edge<E> e) {
        return String.format("%s -> %s [label=\"%s\"]",
                this.vertexString(this.from(e)),
                this.vertexString(this.to(e)),
                e.get());
    }

    private String edgesToString() {
        String edgs = "";
        for (Edge<E> e : this.edges) {
            edgs += "    " + this.edgeString(e) + ";\n";
        }
        return edgs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n")
                .append(this.verticesToString())
                .append(this.edgesToString())
                .append("}");
        return sb.toString();
    }
}
