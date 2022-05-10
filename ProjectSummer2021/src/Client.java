import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("127.0.0.1", 8010);
        System.out.println("From Client: Created Socket");

        ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

        int[][] matrix1 = {
                {1,0,0,1,1},
                {1,1,0,0,0},
                {1,1,0,1,1},
                {1,1,0,1,1},
                {0,0,0,0,0},
        };

        toServer.writeObject("matrix");
        toServer.writeObject(matrix1);
        System.out.println("From Client: Matrix Created");
        System.out.println(new Matrix(matrix1).toString());

        // Task 1
        toServer.writeObject("getOnesIndices");
        List<Index> oneIndices =
                new ArrayList<Index>((List<Index>) fromServer.readObject());
        System.out.println("From Client: 1-Value Indices are: " + oneIndices);
        // Task 2
        toServer.writeObject("setStartIndex");
        toServer.writeObject(new Index(0,1));
        toServer.writeObject("setEndIndex");
        toServer.writeObject(new Index(3,3));
        toServer.writeObject("getShortestPath");
        try {
            List<List<Node<Index>>> shortest = new ArrayList<>((List<List<Node<Index>>>) fromServer.readObject());
            System.out.println("From Client: The shortest paths" + shortest);
        } catch (NullPointerException e) {
            System.out.println("From Client: There is no path between start to end");
        }
        // Task 3
        toServer.writeObject("findSubmarines");
        int numOfSubmarine = (int) fromServer.readObject();
        System.out.println("From Client: Number of Submarine is: " + numOfSubmarine);


        System.out.println("\n\n\n\n\n");

        int[][] matrix2 = {
                {1,0,0},
                {1,1,0},
                {1,0,1},
        };

        toServer.writeObject("matrix");
        toServer.writeObject(matrix2);
        System.out.println("From Client: Matrix Created");
        System.out.println(new Matrix(matrix2).toString());

        // Task 1
        toServer.writeObject("getOnesIndices");
        List<Index> oneIndices1 =
                new ArrayList<Index>((List<Index>) fromServer.readObject());
        System.out.println("From Client: 1-Value Indices are: " + oneIndices1);
                // Task 2
                toServer.writeObject("setStartIndex");
        toServer.writeObject(new Index(0,0));
        toServer.writeObject("setEndIndex");
        toServer.writeObject(new Index(2,0));
        toServer.writeObject("getShortestPath");
        try {
            List<List<Node<Index>>> shortest = new ArrayList<>((List<List<Node<Index>>>) fromServer.readObject());
            System.out.println("From Client: The shortest paths" + shortest);
        } catch (NullPointerException e) {
            System.out.println("From Client: There is no path between start to end");
        }
        // Task 3
        toServer.writeObject("findSubmarines");
        int numOfSubmarine1 = (int) fromServer.readObject();
        System.out.println("From Client: Number of Submarine is: " + numOfSubmarine1);

        System.out.println("\n\n\n\n\n");


        // Task 4
        int[][] arr = {
                {100, 100, 100},
                {300, 900, 500},
                {300, 32, 52}

        };

        System.out.println("From Client: Matrix Created");
        System.out.println(new Matrix(arr).toString());
        toServer.writeObject("matrix");
        toServer.writeObject(arr);

        toServer.writeObject("getLightestWeightedPath");
        toServer.writeObject(new Index(0,0));
        toServer.writeObject(new Index(2,1));

        List<ArrayList<Node<Index>>> list = (List<ArrayList<Node<Index>>>) fromServer.readObject();
        System.out.println("From Client: lightest Paths"+ list.toString()+"\n\n\n");

        toServer.writeObject("stop");
        System.out.println("From Client: Close all streams");
        fromServer.close();
        toServer.close();
        socket.close();
        System.out.println("From Client: Closed operational socket");
    }
}
