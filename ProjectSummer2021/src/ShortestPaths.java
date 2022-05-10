import java.util.*;
import java.util.concurrent.ExecutionException;

public class ShortestPaths<T> {
    private Queue<List<Node<T>>> WorkingQueue;
    private LinkedHashSet<Node<T>> finished;


    private Matrix matrix;
    private Index start;
    private Index end;

    private TraversableMatrix traversable;

    public ShortestPaths() {
        WorkingQueue = new LinkedList<>();
        finished = new LinkedHashSet<>();
    }
    public ShortestPaths(Matrix matrix, Index start, Index end) {
        this.matrix = matrix;
        this.start = start;
        this.end = end;

        this.traversable = new TraversableMatrix(this.matrix);
        this.traversable.setStartIndex(new Index(start.getRow(),start.getColumn()));
        this.traversable.setEndIndex(new Index(end.getRow(),end.getColumn()));
    }
    public List<List<Node<T>>> traverse(Traversable<T> partOfGraph)
    {
        List<Node<T>> path = new ArrayList<>();
        List<List<Node<T>>> finalpath = new ArrayList<>();
        path.add(partOfGraph.getOrigin());
        WorkingQueue.add(path);
        while(!WorkingQueue.isEmpty())//work until no more new paths found
        {
            path = WorkingQueue.poll();
            Node<T> lastVis = path.get(path.size() - 1);//take the last node in the list that we found
            if(path.contains(partOfGraph.getEnd())) //if the path conatins the ending node, add the path to the list
            {
                finalpath.add(path);
            }
            Collection<Node<T>> reachableNodes = partOfGraph.getReachableNodes(lastVis);
            finished.add(lastVis);
            for (Node<T> singleReachableNode : reachableNodes)
            {
                if (!finished.contains(singleReachableNode))
                {
                    List<Node<T>> newPath = new ArrayList<>(path);
                    newPath.add(singleReachableNode);
                    WorkingQueue.add(newPath);
                }

            }
        }
        return finalpath;
    }
    public List<List<Node<Index>>> findShortestPaths()  throws ExecutionException, InterruptedException {

        ShortestPaths<Index> BFS = new ShortestPaths<>();
        List<List<Node<Index>>> allPathsList = new ArrayList<>();
        List<List<Node<Index>>> shortestPathsList = new ArrayList<>();

        Integer min = null;
        allPathsList = BFS.traverse(traversable);
        if(allPathsList.size() == 0)
        {
            return null;
        }

        min = allPathsList.get(0).size();
        min = findMin(min,allPathsList);

        for(List<Node<Index>> list : allPathsList)
        {
            if(min == list.size())
            {

                shortestPathsList.add(list);
            }
        }
        return shortestPathsList;
    }

    public int findMin(int min, List<List<Node<Index>>> allPathsList )
    {  for(List<Node<Index>> list : allPathsList)
    {
        if(min > list.size())
        {
            min = list.size();
        }

    }
        return min;
    }
}
