import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * This class handles server.Matrix-related tasks
 */
public class MatrixIHandler implements IHandler {
    private Matrix matrix;
    private Index startIndex;
    private Index endIndex;
    private volatile boolean doWork = true;

    @Override
    public void resetMembers() {
        this.matrix = null;
        this.startIndex = null;
        this.endIndex = null;
        this.doWork = true;
    }

    @Override
    public void handle(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {
        /*
        Send data as bytes.
        Read data as bytes then transform to meaningful data
        ObjectInputStream and ObjectOutputStream can read and write both primitives and objects
         */
        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);

        boolean doWork = true;
        // handle client's tasks
        while (doWork) {

            switch (objectInputStream.readObject().toString()) {
                case "matrix": {
                    // client will now send a 2d array. handler will create a matrix object
                    int[][] tempArray = (int[][]) objectInputStream.readObject();
                    System.out.println("Server: Got 2d array");
                    this.matrix = new Matrix(tempArray);
                    this.matrix.printMatrix();
                    break;
                }
                case "getNeighbors": {
                    // handler will receive an index, then compute its neighbors
                    Index tempIndex = (Index) objectInputStream.readObject();
                    if (this.matrix != null) {
                        List<Index> neighbors = new ArrayList<>(this.matrix.getNeighbors(tempIndex));
                        System.out.println("Server: neighbors of " + tempIndex + ":  " + neighbors);
                        // send to socket's outputstream
                        objectOutputStream.writeObject(neighbors);
                    }
                    break;
                }
                case "getReachables": {
                    // handler will receive an index, then compute its neighbors
                    Index tempIndex = (Index) objectInputStream.readObject();
                    if (this.matrix != null) {
                        List<Index> reachables = new ArrayList<>(this.matrix.getReachable(tempIndex));
                        System.out.println("Server: neighbors of " + tempIndex + ":  " + reachables);
                        // send to socket's outputstream
                        objectOutputStream.writeObject(reachables);
                    }
                    break;
                }
                case "getOnesIndices": {
                    // handler will return all 1-value indices
                    if (this.matrix != null) {
                        List<Index> indices = new ArrayList<>(this.matrix.getOnesIndices());
                        System.out.println("Server: indices of 1-value: " + indices);
                        // send to socket's outputstream
                        objectOutputStream.writeObject(indices);
                        break;
                    }
                }
                case "getShortestPath": {
                    try {
                        ShortestPaths shortPaths = new ShortestPaths(matrix, this.startIndex, this.endIndex);
                        List<List<Node<Index>>> shortestPaths = shortPaths.findShortestPaths();
                        if (shortestPaths == null) {
                            objectOutputStream.writeObject(null);
                        } else {
                            objectOutputStream.writeObject(shortestPaths);
                            System.out.println("Server: shortest paths from" + startIndex + "to " + endIndex + " is: " + shortestPaths);
                        }
                    } catch (NullPointerException | ExecutionException | InterruptedException nullPointerException) {
                        return;
                    }
                    break;

                }
                case "setStartIndex": {
                    startIndex = (Index) objectInputStream.readObject();
                    break;
                }
                case "setEndIndex": {
                    endIndex = (Index) objectInputStream.readObject();
                    break;
                }
                case "findSubmarines": {
                    int numOfSubmarines = FindRectangles.findRectangles(matrix.getPrimitiveMatrix());
                    objectOutputStream.writeObject(numOfSubmarines);

                    if (numOfSubmarines == 0) {
                        System.out.println("Server: no submarine");
                    } else {
                        System.out.println("Server: There is " + numOfSubmarines + " Submarines");
                    }

                    break;
                }
                case "getLightestWeightedPath": {

                    Index from = (Index) objectInputStream.readObject();
                    Index to = (Index) objectInputStream.readObject();

                    WeightedPath weightedPath = new WeightedPath();

                    List<Collection<Node<Index>>> list =
                            weightedPath.findLightest(matrix.getPrimitiveMatrix(), new Node<Index>(from), new Node<Index>(to));

                    objectOutputStream.writeObject(list);
                    System.out.println("Server: the lightest path from " + from.toString() + " to " + to.toString() + " is: " + list.toString());
                    break;
                }
                case "resetMembers":{
                    this.matrix = null;
                    this.startIndex = null;
                    this.endIndex = null;
                    break;
                }
                case "stop": {
                    System.out.println("Server: client closed socket");
                    doWork = false;
                    break;
                }
            }
        }
    }
}
