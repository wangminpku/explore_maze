package cn.edu.pku.sei.shared;

import java.util.ArrayList;
import java.util.HashSet;

public class Graph<E> implements BareG {
    
    
    
    public static class Edge<E> implements BareE {
        public int weight;
        public Vertex<E> from;
        public Vertex<E> to;

        protected Edge(Vertex<E> from, Vertex<E> to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public int getFrom() {
            return from.index;
        }
        
        public Vertex<E> getToVertex(){
            return to;
        }

        public int getTo() {
            return to.index;
        }

        public Vertex<E> fromVertex() {
            return from;
        }

        public Vertex<E> toVertex() {
            return to;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d: %d) ", from.index, to.index, weight);
        }

        public void setWeight(int w) {
            weight = w;
        }
    }

    public static class Vertex<E> implements BareV {
        private int index;
        protected HashSet<Edge<E>> edges;
        protected HashSet<BareE> bareEdges;
        protected E data;

        public Vertex(int index, E data) {
            this.index = index;
            this.data = data;
            edges = new HashSet<Edge<E>>();
            bareEdges = new HashSet<BareE>();
        }

        public int getIndex() {
            return index;
        }

        public String toString() {
            String s;

            s = "";
            for (Edge<E> e : edges) {
                s += e.toString();
            }

            return s;
        }

        public Iterable<Edge<E>> getEdges() {
            return edges;
        }

        public int hashCode() {
            int tmp = index;
            if (tmp < 0)
                tmp = -tmp;
            return tmp;
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if ((obj == null) || (obj.getClass() != this.getClass()))
                return false;
            // object must be Vertex at this point
            Vertex<E> test = (Vertex<E>) obj;
            return index == test.index;
        }

        public E getData() {
            return data;
        }

        @Override
        public Iterable<BareE> getBareEdges() {
            return bareEdges;
        }

    };

    public ArrayList<Vertex<E>> vertices;

    public Graph() {
        vertices = new ArrayList<Vertex<E>>();

    }

    public int addVertex(E data) {
        Vertex<E> v = new Vertex(vertices.size(), data);
        vertices.add(v);
        return v.getIndex();
    }

    public void addEdge(int from, int to, int weight) {
        if ((vertices.get(from) == null) || (vertices.get(to) == null)) {
            String msg = String.format("Attempt to add edge with non-existent vertex %d -- %d%n", from, to);
            throw new IllegalArgumentException(msg);
        }
        addEdge(vertices.get(from), vertices.get(to), weight);
    }

    protected void addEdge(Vertex<E> from, Vertex<E> to, int weight) {
        Edge<E> e = new Edge<E>(from, to, weight);
        from.edges.add(e);
        from.bareEdges.add(e);
    }

    public boolean checkVertex(BareV at) {
        if (at == null)
            return false;
        if (vertices.get(at.getIndex()) == null)
            return false;
        return true;
    }

    public Iterable<Vertex<E>> getVertices() {
        return vertices;
    }


    public Vertex<E> getVertex(int index) {
        if (index < vertices.size())
            return vertices.get(index);
        else return null; 
    }

    public String toString() {
        String s;
        int i;

        s = "";
        for (i = 0; i < vertices.size(); i++) {
            if (vertices.get(i) == null) {
                continue;
            }
            s += i + ": " + vertices.get(i).toString() + "\n";
        }

        return s;
    }

    public boolean checkUid(int uid) {
        return ((uid >= 0) && (uid < vertices.size()));
    }


    public void removeEdge(int from, int to) {
        Vertex<E> v = vertices.get(from);
        Edge match = null;
        for (Edge<E> e : v.edges) {
            if (e.getTo() == to) {
                match = e;
                break;
            }
        }
        v.edges.remove(match);
        v.bareEdges.remove(match);
    }

    public void addEdge(Edge<E> edge) {
        Vertex<E> v = vertices.get(edge.getFrom());
        v.edges.add(edge);
        v.bareEdges.add(edge);
    }

};
