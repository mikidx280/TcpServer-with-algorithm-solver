import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class WeightedPath {

    // Data member
    private List<Collection<Node<Index>>> weightedPaths;

    public WeightedPath(){ weightedPaths = new ArrayList<>();}

    public List<Collection<Node<Index>>> findLightest(int[][] inputArr, Node<Index> source, Node<Index> dest){

        int[][]oneArray = new int[inputArr.length][inputArr.length];

        for(int i=0;i<oneArray.length;i++){
            for(int j=0;j<oneArray[i].length;j++){
                oneArray[i][j]=1;
            }
        }


        Matrix oneMatrix = new Matrix(oneArray);
        TraversableMatrix myOneTraversable = new TraversableMatrix(oneMatrix);

        Matrix matrix = new Matrix(inputArr);
        TraversableMatrix myTraversable1 = new TraversableMatrix(matrix);

        DFSVisit algorithm = new DFSVisit();

        List<Collection<Node<Index>>> shortestPaths = algorithm.allPaths(myOneTraversable,source,dest,new HashSet<>(),new ArrayList<>());

        int sum=0;
        int minWeight = 0;
        // Algo to find min path weight
        for(Collection<Node<Index>> temp:shortestPaths){
            for(Node<Index> tempInd:temp){
                sum+=myTraversable1.matrix.getValue(tempInd.getData());
            }
            if (minWeight==0)
                minWeight=sum;
            else if(sum<minWeight)
                minWeight=sum;

            sum=0;

        }

        for(Collection<Node<Index>> temp:shortestPaths){
            for(Node<Index> tempInd:temp){
                sum+=myTraversable1.matrix.getValue(tempInd.getData());
            }
            if(sum==minWeight)
            {

                weightedPaths.add(temp);
            }
            sum=0;
        }
        return weightedPaths;
    }
}