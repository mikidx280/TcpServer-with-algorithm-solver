import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Matrix implements Serializable {

    private int[][] primitiveMatrix;

    public Matrix(int[][] oArray){
        List<int[]> list = new ArrayList<>();
        for (int[] row : oArray) {
            int[] clone = row.clone();
            list.add(clone);
        }
        primitiveMatrix = list.toArray(new int[0][]);
    }
    public void printMatrix(){
        for (int[] row : primitiveMatrix) {
            String s = Arrays.toString(row);
            System.out.println(s);
        }
    }
    public final int[][] getPrimitiveMatrix() {
        return primitiveMatrix;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : primitiveMatrix) {
            stringBuilder.append(Arrays.toString(row));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
    public Collection<Index> getNeighbors(final Index index){
        Collection<Index> list = new ArrayList<>();
        int extracted = -1;
        try{
            extracted = primitiveMatrix[index.row+1][index.column];
            list.add(new Index(index.row+1,index.column));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row][index.column+1];
            list.add(new Index(index.row,index.column+1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row-1][index.column];
            list.add(new Index(index.row-1,index.column));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row][index.column-1];
            list.add(new Index(index.row,index.column-1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row-1][index.column-1];
            list.add(new Index(index.row-1,index.column-1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row-1][index.column+1];
            list.add(new Index(index.row-1,index.column+1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row+1][index.column+1];
            list.add(new Index(index.row+1,index.column+1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row+1][index.column-1];
            list.add(new Index(index.row+1,index.column-1));
        }catch (ArrayIndexOutOfBoundsException ignored){}

        return list;
    }
    public int getValue(Index index) {
        return primitiveMatrix[index.row][index.column];
    }
    public void setValue(Index index, int value){ primitiveMatrix[index.getRow()][index.getColumn()] = value;}
    public Collection<Index> getReachable(Index index) {
        ArrayList<Index> filteredIndices = new ArrayList<>();
        this.getNeighbors(index).stream().filter(i-> getValue(i)==1)
                .map(neighbor->filteredIndices.add(neighbor)).collect(Collectors.toList());
        return filteredIndices;
    }
    public Collection<Index> getOnesIndices() {
        ArrayList<Index> oneIndices = new ArrayList<>();

        for (int i=0; i<primitiveMatrix.length; i++){
            for(int j=0; j< primitiveMatrix.length; j++){
                if(primitiveMatrix[i][j] == 1){
                    oneIndices.add(new Index(i,j));
                }
            }
        }

        return oneIndices;
    }
    public int length(){
        return primitiveMatrix.length;
    }

    public static void main(String[] args) {
        int[][] source = {
                {0, 1, 0},
                {1, 0, 1},
                {1, 0, 1}
        };
        Matrix matrix = new Matrix(source);
        for(int i=0; i< matrix.length();i++){
            for(int j=0; j< matrix.length(); j++){
                System.out.println(matrix.getNeighbors(new Index(i,j)));
            }
        }
    }
}

/*
 * To find if the component is valid we calculated the area of the rectangle
 * for doing that we used a function that gets the sum of each column in the matrix
 * then from the heights array we can calculate the max area in this component
 * and if its the same as its size its a valid rectangle
 *
 * here we receive a heights array
 * then we calculate for this array the max rectangle size
 *
 * The function finds how many valid submarine exists in the matrix first the function finds all the connected components
 * for each component bigger then one, a new thread is created to check if it is a valid submarine
 * if it is, it adds one to the total count of how many submarine found
 * writelock is been locked before entering critical section to insure only one writes to the variable
 */
