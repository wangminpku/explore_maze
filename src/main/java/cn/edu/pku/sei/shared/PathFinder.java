package cn.edu.pku.sei.shared;

import java.util.*;

/**
 * @author wangmin
 */

public class PathFinder {

    /**
     * This member always holds the cost of the path (if any)
     * found by the most recently finished solving operation. 
     * MIN_VALUE is used to signal that the value is not yet valid. 
     */
    public static int lastCost = Integer.MIN_VALUE;
	/**
	 * First, computes a shortest path from a source vertex to a destination
	 * vertex in a graph by using Dijkstra's algorithm. Second, visits and saves
	 * (in a stack) each vertex in the path, in reverse order starting from the
	 * destination vertex, by using the map object pred. Third, uses a
	 * List and Stack to generate the return Integer List by first pushing 
	 * each vertex into the stack, and then poping vertices 
	 * from the stack and adding the index of each to the 
	 * return list. The vertex indices in the return object are now in the
	 * right order. Note that the getIndex() method is called from a
	 * BareV object (vertex) to get its original integer name.
	 *
	 * @param G
	 *            - The graph in which a shortest path is to be computed
	 * @param source
	 *            - The first vertex of the shortest path
	 * @param dest
	 *            - The last vertex of the shortest path
	 * @return A List of Integers corresponding the the vertices on the path
	 *         in order from source to dest. 
	 *
	 *         The contents of an example String object: Path Length: 5 Path
	 *         Cost: 4 Path: 0 4 2 5 7 9
	 *
	 * @throws NullPointerException
	 *             - If any arugment is null
	 *
	 * @throws RuntimeException
	 *             - If the given source or dest vertex is not in the graph
	 *
	 */

    public static List<Integer> findPath(BareG g, BareV source, BareV dest) {
          lastCost = Integer.MIN_VALUE;
          
          //
          // the supplied heap, and stack. 
          // you may also use HashMap and HashSet from JCF. 
          // the following is only here so that the app will run (but not
          // product correct results when first unpacked from the templates.

        int vertex_num = ((Graph) g).vertices.size();
        int[] prev = new int[vertex_num];
        int[] dist = new int[vertex_num];
        int[][] mMatrix = new int[vertex_num][vertex_num];
        List<Integer> P = new ArrayList<Integer>();
        for (int i = 0; i < vertex_num; i++) {
            for (int j = 0; j < vertex_num; j++) {
                mMatrix[i][j] = Integer.MAX_VALUE;
            }
            Iterator<BareE> iterator = g.getVertex(i).getBareEdges().iterator();
            while (iterator.hasNext()) {
                BareE edge = iterator.next();
                mMatrix[i][edge.getToVertex().getIndex()] = edge.getWeight();
            }
        }

        boolean[] flag = new boolean[vertex_num];
        for (int i = 0; i < vertex_num; i++) {
            flag[i] = false;
            prev[i] = -1;
            dist[i] = mMatrix[source.getIndex()][i];

        }
        flag[source.getIndex()] = true;
        dist[source.getIndex()] = 0;
        int k = 0;
        for (int i = 1; i < vertex_num; i++) {
            // 寻找当前最小的路径；
            // 即，在未获取最短路径的顶点中，找到离vs最近的顶点(k)。
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < vertex_num; j++) {
                if (flag[j] == false && dist[j] < min) {
                    min = dist[j];
                    k = j;
                }
            }
            // 标记"顶点k"为已经获取到最短路径
            flag[k] = true;

            // 修正当前最短路径和前驱顶点
            // 即，当已经"顶点k的最短路径"之后，更新"未获取最短路径的顶点的最短路径和前驱顶点"。
            for (int j = 0; j < vertex_num; j++) {
                int tmp = (mMatrix[k][j] == Integer.MAX_VALUE ? Integer.MAX_VALUE : (min + mMatrix[k][j]));
                if (flag[j] == false && (tmp < dist[j])) {
                    dist[j] = tmp;
                    prev[j] = k;
                }
            }
        }
//        for(int i=0;i<vertex_num;i++){
//            System.out.println("shortest path:-----------------");
//            System.out.println(dist[i]);
//        }
        LinkedStack<Integer> path = new LinkedStack<Integer>();
        int index = dest.getIndex();
        while (prev[index] != -1) {
            path.push(prev[index]);
//            System.out.println(prev[index]);
            index = prev[index];
        }

//        System.out.println("PATH：");
        int path_len = path.size();
        P.add(source.getIndex());
        for (int i = 0; i < path_len; i++) {
            P.add(path.pop());
        }
        P.add(dest.getIndex());
        lastCost = dist[dest.getIndex()];
        return P;
    }
    
    /**
     * A pair class with two components of types V and C, where V is a vertex
     * type and C is a cost type.
     */

    private static class Vpair<V, C extends Comparable<? super C>> implements
            Comparable<Vpair<V, C>> {
        private V node;
        private C cost;

        Vpair(V n, C c) {
            node = n;
            cost = c;
        }

        public V getVertex() {
            return node;
        }

        public C getCost() {
            return cost;
        }

        public int compareTo(Vpair<V, C> other) {
            return cost.compareTo(other.getCost());
        }

        public String toString() {
            return "<" + node.toString() + ", " + cost.toString() + ">";
        }

        public int hashCode() {
            return node.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if ((obj == null) || (obj.getClass() != this.getClass()))
                return false;
            // object must be Vpair at this point
            Vpair<?, ?> test = (Vpair<?, ?>) obj;
            return (node == test.node || (node != null && node
                    .equals(test.node)));
        }
    }


}
