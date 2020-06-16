import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {


        int M = 0;
        int P = 0;
        int S = 0;
        int J = 0;
        int N = 0;
        String R = "";
        int D = 0;


        if (args.length == 6) {
            M = Integer.parseInt(args[0]);
            P = Integer.parseInt(args[1]);
            S = Integer.parseInt(args[2]);
            J = Integer.parseInt(args[3]);
            N = Integer.parseInt(args[4]);
            R = args[5].trim();
        }






        ArrayList<Processes> pLst = new ArrayList<Processes>();

        if (J == 1) {
            Processes p = new Processes(N, 1, 0, 0, 0, S, P);
            pLst.add(p);
        }

        else if (J == 2) {
            for (int i = 0; i < 4; i++) {
                Processes p = new Processes(N, 1, 0, 0, i, S, P);
                pLst.add(p);
            }
        }

        else if (J == 3) {
            for (int i = 0; i < 4; i++) {
                Processes p = new Processes(N, 0, 0, 0, i, S, P);
                pLst.add(p);
            }
        }

        else {
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    Processes p = new Processes(N, 0.75, 0.25, 0, i, S, P);
                    pLst.add(p);
                }
                else if (i == 1) {
                    Processes p = new Processes(N, 0.75, 0, 0.25, i, S, P);
                    pLst.add(p);
                }

                else if (i == 2) {
                    Processes p = new Processes(N, 0.75, 0.125, 0.125, i, S, P);
                    pLst.add(p);
                }

                else {
                    Processes p = new Processes(N, 0.5, 0.125, 0.125, i, S, P);
                    pLst.add(p);
                }
            }
        }


        Algorithms algo = new Algorithms();
        int numFrameEntries = M/P;

        ArrayList<String> Pp = new ArrayList<>();
        for (int i = 0; i < numFrameEntries; i++) {
            Pp.add(i, " ");
        }

        if (R.equals("lru")) algo.LRU(new HashMap<String, Integer>(), pLst, S, numFrameEntries, P, D, J);
        if (R.equals("fifo")) algo.FIFO(new HashMap<String, Integer>(), pLst, S, numFrameEntries, P, D, J);
        if (R.equals("random")) algo.Random(Pp, pLst, S, numFrameEntries, P, D, J);








    }
}
