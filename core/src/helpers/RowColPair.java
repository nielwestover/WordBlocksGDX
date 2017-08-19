package helpers;

public class RowColPair {
    public int Col;
    public int Row;

    public RowColPair(int first, int second) {
        this.Row = first;
        this.Col = second;
    }

    public void setRow(int val) {
        this.Row = val;
    }

    public void setCol(int val) {
        this.Col = val;
    }

    public String toString() {
        return this.Row + ", " + this.Col;
    }

    public Boolean equals(RowColPair p) {
        boolean z = this.Row == p.Row && this.Col == p.Col;
        return Boolean.valueOf(z);
    }
}
