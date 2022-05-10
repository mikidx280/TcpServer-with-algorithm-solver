import com.sun.istack.internal.NotNull;

import java.util.*;

public class DFSVisit {

    private List<Collection<Node<Index>>> allPaths;

    public DFSVisit() {
        allPaths = new ArrayList<>();
    }

    public List<Collection<Node<Index>>> dfsVisit(@NotNull Traversable<Index> oMtx, @NotNull Node<Index> oSource, @NotNull Node<Index> oDestination, @NotNull HashSet<Node<Index>> visited, @NotNull ArrayList<Node<Index>> path) {
        visit(oMtx, oSource, oDestination, visited, path);
        return shortestPath(allPaths);
    }

    public List<Collection<Node<Index>>> allPaths(@NotNull Traversable<Index> oMtx,@NotNull Node<Index> oSource,@NotNull Node<Index> oDestination,@NotNull HashSet<Node<Index>> visited,@NotNull ArrayList<Node<Index>> path) {
        visit(oMtx, oSource, oDestination, visited, path);
        return allPaths;
    }

    private void visit(@NotNull Traversable<Index> oMtx,@NotNull Node<Index> oSource,@NotNull Node<Index> oDestination,@NotNull HashSet<Node<Index>> visited,@NotNull ArrayList<Node<Index>> path) {
        // the same index
        if (oSource.equals(oDestination)) {
            if (!allPaths.contains(path)) {
                ArrayList<Node<Index>> copyPath = (ArrayList<Node<Index>>) path.clone();
                allPaths.add(copyPath);
            }
            return;
        }
        if (!path.contains(oSource)) path.add(oSource);
        if (!visited.contains(oSource)) visited.add(oSource);
        for (Node<Index> reachable : oMtx.getReachableNodes(oSource)) {
            if (visited.contains(reachable))
                continue;
            path.add(reachable);
            visited.add(reachable);
            dfsVisit(oMtx, reachable, oDestination, visited, path);
            visited.remove(reachable);
            path.remove(path.size() - 1);
        }
    }

    private List<Collection<Node<Index>>> shortestPath (List<Collection<Node<Index>>> toSort) {
        List<Collection<Node<Index>>> shortestPaths = new ArrayList<>();

        if (toSort.toArray().length == 0 ){
            return toSort;
        }


        List<Integer> list = new ArrayList<>();

        for (Collection<Node<Index>> temp:toSort){
            list.add(temp.toArray().length);
        }


        Optional<Integer> minNumber = list.stream()
                .min((i, j) -> i.compareTo(j));


        int minForSerch = minNumber.get();

        for (Collection<Node<Index>> temp:toSort){
            if (temp.toArray().length == minForSerch ){
                shortestPaths.add(temp);
            }
        }
        return shortestPaths;
    }

}