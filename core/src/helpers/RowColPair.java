package helpers;

/**
 * Created by a2558 on 3/8/2016.
 */

public class RowColPair {
    public RowColPair(int first, int second) {
        this.Row = first;
        this.Col = second;
    }

    public int Row;
    public int Col;


    public void setRow(int val) {
        Row = val;
    }


    public void setCol(int val) {
        Col = val;
    }

    @Override
    public String toString() {
        return Row + ", " + Col;
    }

    public Boolean equals(RowColPair p) {

        return Row == p.Row && Col == p.Col;
    }
};