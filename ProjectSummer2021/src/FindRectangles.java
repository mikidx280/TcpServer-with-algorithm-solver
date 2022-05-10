import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FindRectangles {
    private static ThreadPoolExecutor threadPool;
    static int count=0;
    static int goal=0;
    static int target;
    static boolean TellMeTruth=true;
    public static int findRectangles(int matrix[][]) {
        TellMeTruth=true;
        int copymatrix[][]=new int [matrix.length][matrix[0].length];
        for(int i=0;i<matrix.length;i++){
            for (int j=0; j< matrix[0].length;j++){
                copymatrix[i][j]=matrix[i][j];
            }
        }
        List<Point> recCordinatesPoints = new ArrayList<Point>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 1) {
                    recCordinatesPoints.addAll(caputreCoordinates(matrix, i, j));
                }
            }
        }
        for(int x=0; x<recCordinatesPoints.size();x++){
            if(copymatrix[recCordinatesPoints.get(x).x][recCordinatesPoints.get(x).y]==0) {
                TellMeTruth = false;
            }
        }
        for(int x=0; x<recCordinatesPoints.size();x=x+2) {
            if (recCordinatesPoints.get(x).x == recCordinatesPoints.get(x + 1).x && recCordinatesPoints.get(x).y == recCordinatesPoints.get(x + 1).y) {
                TellMeTruth = false;
            }
        }

        for(int x=0; x<recCordinatesPoints.size();x=x+2) {
            for (int y = recCordinatesPoints.get(x).x; y <= recCordinatesPoints.get(x + 1).x; y++) {
                for (int z = recCordinatesPoints.get(x).y; z <= recCordinatesPoints.get(x + 1).y; z++) {
                    if (copymatrix[y][z]==0){
                        TellMeTruth=false;
                    }
                }
            }
        }
        if(TellMeTruth==false){
            return 0;
        }

        Callable<Integer> task1 = () -> {
            if(count<recCordinatesPoints.size()) {
                count++;
            }
            if(count%2==0){
                return 1;
            }
            return 0;

        };

        threadPool = new ThreadPoolExecutor(5, 10, 10,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        while(count!=recCordinatesPoints.size()) {
            Future<Integer> futureTask = threadPool.submit(task1);
            try {
                goal = goal + futureTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        count=0;
        target=goal;
        goal=0;
        return target;
    }
    private static List<Point> caputreCoordinates(int matrix[][], int i, int j) {

        int colStart = j;
        Point startPoint = new Point(i, j);

        while (j < matrix[0].length && matrix[i][j] == 1) {
            matrix[i][j] = 0;
            j++;
        }

        int colEnd = --j;
        i++;
        j = colStart;
        while (i < matrix.length && matrix[i][j] == 1) {
            while (j <= colEnd) {
                matrix[i][j] = 0;
                j++;
            }
            j = colStart;
            i++;
        }

        Point endPoint = new Point(--i, colEnd);
        List<Point> listPoint = new ArrayList<Point>();
        listPoint.add(startPoint);
        listPoint.add(endPoint);

        return listPoint;
    }

//    public static void main(String[] args) {
//        int[][] matrix1 = {
//                {1,0,0,1,1},
//                {0,1,0,0,0},
//                {1,1,0,1,1},
//                {1,1,0,1,1},
//                {0,0,0,0,0},
//        };
//
//        int[][] matrix2 = {
//                {1,0,1},
//                {1,0,1},
//                {0,0,0},
//        };
//
//        System.out.println(findRectangles(matrix1));
//        System.out.println(findRectangles(matrix2));
//    }
}




class Point {
    int x = -1;
    int y = -1;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }




}