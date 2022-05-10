import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements adapter/wrapper/decorator design pattern
 */
public class TraversableMatrix implements Traversable<Index> {
    protected final Matrix matrix;
    protected Index startIndex;
    protected Index endIndex;

    public TraversableMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public Index getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Index startIndex) {
        this.startIndex = startIndex;
    }
    public Index getEndIndex(){return endIndex;}
    public void setEndIndex(Index startIndex) {
        this.endIndex = startIndex;
    }

    @Override
    public Node<Index> getOrigin() throws NullPointerException{
        if (this.startIndex == null) throw new NullPointerException("start index is not initialized");
        return new Node<>(this.startIndex);

    }
    @Override
    public Node<Index> getEnd() throws NullPointerException{
        if (this.endIndex == null) throw new NullPointerException("End index is not initialized");
        return new Node<>(this.endIndex);
    }

    @Override
    public Collection<Node<Index>> getReachableNodes(Node<Index> someNode) {
        List<Node<Index>> reachableIndices = new ArrayList<>();
        for (Index index : this.matrix.getNeighbors(someNode.getData())) {
            if (matrix.getValue(index) == 1) {
                // A neighboring index whose value is 1
                Node<Index> indexNode = new Node<>(index, someNode);
                reachableIndices.add(indexNode);
            }
        }
        return reachableIndices;
    }


    public Collection<Node<Index>> getReachableNodesWithStreams(Node<Index> someNode) {
        return this.matrix.getNeighbors(someNode.getData())
                .stream().filter(index -> matrix.getValue(index)==1).map(index -> new Node<>(index,someNode))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return matrix.toString();
    }
}
