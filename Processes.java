public class Processes {

    int id = 0;
    int nextRef = 0;
    int prevRef = 0;
    int remainingRef = 0;
    int evictionCnt = 0;
    int faultCnt = 0;
    int residencyT = 0;
    int initialLoadT = 0;
    double A = 0;
    double B = 0;
    double C = 0;
    int[] pageTable;

    public Processes(int remainingRef, double A, double B, double C, int id, int S, int P) {
        this.remainingRef = remainingRef;
        this.A = A;
        this.B = B;
        this.C = C;
        this.evictionCnt = 0;
        this.faultCnt = 0;
        this.prevRef = (111 * (id+1)) % S;
        this.nextRef = (111 * (id+1)) % S;
        this.id = id;
        this.residencyT = 0;
        this.initialLoadT = 0;
        this.pageTable = new int[S/P];
    }

}
