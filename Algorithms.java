import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Algorithms {

    static int indx = 0;

    static int getNextRandomNum() throws IOException {
        int randomNum = 0;

        String currDir = System.getProperty("user.dir");
        randomNum = Integer.parseInt(Files.readAllLines(Paths.get(currDir+"/"+ "random-numbers")).get(indx));


        indx++;
        return randomNum;

    }


    static int getCaseNum(double y, Processes curr) {
        int caseNum = 0;
        if (y < curr.A) caseNum = 1;
        else if (y < curr.A + curr.B) caseNum = 2;
        else if (y < curr.A + curr.B + curr.C) caseNum = 3;
        else caseNum = 4;

        return caseNum;
    }

    static int getNextRef(int prevRef, int caseNum, int S) throws IOException {
        int returnVal = 0;

        if (caseNum == 1) {
            returnVal = (prevRef + 1) % S;
        }
        else if (caseNum == 2) {
            if (prevRef-5 < 0) returnVal = ((prevRef - 5 % S) + S) % S;
            else returnVal = (prevRef - 5) % S;
        }
        else if (caseNum == 3) {
            returnVal = (prevRef + 4) % S;
        }
        else {
            int randomNum = getNextRandomNum();
            returnVal = (randomNum + S) % S;
        }


        return returnVal;
    }


    static void LRU(HashMap<String, Integer> m, ArrayList<Processes> pLst, int processSize, int numFrameEntries, int pageSize, int debugInfo, int jobMix) throws IOException {
        int initialRefRemaining = pLst.get(0).remainingRef;
        int currT = 1;
        int pIndx = 0;


        while (pLst.get(pLst.size()-1).remainingRef > 0) {

            Processes curr = pLst.get(pIndx);

            for (int i = 0; i < 3; i++) {
                if (curr.remainingRef == 0) break;
                String toEvictKey = "";
                int processIndxToEvict = 0;
                int initialLoadT = 0;
                int residencyT = 0;
                int val = 0;
                int prevRef = 0;
                int nextRef = 0;
                int pageNum = 0;
                double y = 0;
                int caseNum = 0;
                int pageEvicted = 0;

                nextRef = curr.nextRef;

                pageNum = nextRef/pageSize;

                String currKey = (curr.id + 1) + "_" + pageNum;
                if (m.containsKey(currKey)) {
                    m.replace(currKey, currT);
                }

                else {
                    curr.faultCnt++;

                    if (m.size() < numFrameEntries) {
                        m.put(currKey, currT);
                        curr.pageTable[pageNum] = currT;
                    }

                    else {
                        toEvictKey = Collections.min(m.entrySet(), Map.Entry.comparingByValue()).getKey();
                        processIndxToEvict = Integer.parseInt(toEvictKey.substring(0, toEvictKey.indexOf("_")))-1;
                        pageEvicted = Integer.parseInt(toEvictKey.substring(toEvictKey.indexOf("_")+1));
                        val = m.get(toEvictKey);

                        Processes evicting = pLst.get(processIndxToEvict);
                        initialLoadT = evicting.pageTable[pageEvicted];
                        residencyT = currT - initialLoadT;
                        evicting.residencyT += residencyT;

                        evicting.evictionCnt++;

                        m.remove(toEvictKey, val);

                        m.put(currKey, currT);
                        curr.pageTable[pageNum] = currT;

                    }

                }

                curr.remainingRef--;
                currT++;

                prevRef = nextRef;
                y = (double)getNextRandomNum()/(Integer.MAX_VALUE+1d);
                caseNum = getCaseNum(y, curr);
                nextRef = getNextRef(prevRef, caseNum, processSize);
                curr.nextRef = nextRef;

            }

            pIndx++;
            if (pIndx == pLst.size()) pIndx = 0;

        }


        int totalFault = 0;
        int totalResidencyT = 0;
        int totalEvictionT = 0;
        System.out.println();

        System.out.println("The machine size is " + (numFrameEntries*pageSize) + ".");
        System.out.println("The page size is " + pageSize + ".");
        System.out.println("The process size is " + processSize + ".");
        System.out.println("The job mix number is " + jobMix + ".");
        System.out.println("The number of references per process is " + initialRefRemaining + ".");
        System.out.println("The replacement algorithm is lru.");

        System.out.println();
        for (int i = 0; i < pLst.size(); i++) {
            Processes curr = pLst.get(i);

            int evictionCnt = curr.evictionCnt;
            if (evictionCnt == 0) {
                System.out.println("Process " + (curr.id+1) + " had " + curr.faultCnt + " faults.");
                System.out.println("       With no evictions, the average residence is undefined.");
            }
            else System.out.println("Process " + (curr.id+1) + " had " + curr.faultCnt + " faults and " + ((double)curr.residencyT/curr.evictionCnt) + " average residency.");

            totalFault += curr.faultCnt;
            totalResidencyT += curr.residencyT;
            totalEvictionT += curr.evictionCnt;

        }
        System.out.println();

        if (totalEvictionT == 0) {
            System.out.println("The total number of faults is " + totalFault + ".");
            System.out.println("       With no evictions, the overall average residence is undefined.");
        }
        else System.out.println("The total number of faults is " + totalFault + " and the overall average residency is " + ((double)totalResidencyT/totalEvictionT) + ".");

    }



    static void FIFO(HashMap<String, Integer> m, ArrayList<Processes> pLst, int processSize, int numFrameEntries, int pageSize, int debugInfo, int jobMix) throws IOException {
        int initialRefRemaining = pLst.get(0).remainingRef;
        int currT = 1;
        int pIndx = 0;


        while (pLst.get(pLst.size()-1).remainingRef > 0) {

            Processes curr = pLst.get(pIndx);

            for (int i = 0; i < 3; i++) {
                if (curr.remainingRef == 0) break;
                String toEvictKey = "";
                int processIndxToEvict = 0;
                int initialLoadT = 0;
                int residencyT = 0;
                int val = 0;
                int prevRef = 0;
                int nextRef = 0;
                int pageNum = 0;
                double y = 0;
                int caseNum = 0;
                int pageEvicted = 0;

                nextRef = curr.nextRef;

                pageNum = nextRef/pageSize;


                String currKey = (curr.id + 1) + "_" + pageNum;
                if (m.containsKey(currKey)) {
                }

                else {
                    curr.faultCnt++;

                    if (m.size() < numFrameEntries) {
                        m.put(currKey, currT);
                        curr.pageTable[pageNum] = currT;
                    }

                    else {
                        toEvictKey = Collections.min(m.entrySet(), Map.Entry.comparingByValue()).getKey();
                        processIndxToEvict = Integer.parseInt(toEvictKey.substring(0, toEvictKey.indexOf("_")))-1;
                        pageEvicted = Integer.parseInt(toEvictKey.substring(toEvictKey.indexOf("_")+1));
                        val = m.get(toEvictKey);

                        Processes evicting = pLst.get(processIndxToEvict);
                        initialLoadT = evicting.pageTable[pageEvicted];
                        residencyT = currT - initialLoadT;
                        evicting.residencyT += residencyT;

                        evicting.evictionCnt++;

                        m.remove(toEvictKey, val);

                        m.put(currKey, currT);
                        curr.pageTable[pageNum] = currT;

                    }

                }

                curr.remainingRef--;
                currT++;

                prevRef = nextRef;
                y = (double)getNextRandomNum()/(Integer.MAX_VALUE+1d);
                caseNum = getCaseNum(y, curr);
                nextRef = getNextRef(prevRef, caseNum, processSize);
                curr.nextRef = nextRef;

            }

            pIndx++;
            if (pIndx == pLst.size()) pIndx = 0;

        }


        int totalFault = 0;
        int totalResidencyT = 0;
        int totalEvictionT = 0;

        System.out.println();

        System.out.println("The machine size is " + (numFrameEntries*pageSize) + ".");
        System.out.println("The page size is " + pageSize + ".");
        System.out.println("The process size is " + processSize + ".");
        System.out.println("The job mix number is " + jobMix + ".");
        System.out.println("The number of references per process is " + initialRefRemaining + ".");
        System.out.println("The replacement algorithm is fifo.");

        System.out.println();
        for (int i = 0; i < pLst.size(); i++) {
            Processes curr = pLst.get(i);

            int evictionCnt = curr.evictionCnt;
            if (evictionCnt == 0) {
                System.out.println("Process " + (curr.id+1) + " had " + curr.faultCnt + " faults.");
                System.out.println("       With no evictions, the average residence is undefined.");
            }
            else System.out.println("Process " + (curr.id+1) + " had " + curr.faultCnt + " faults and " + ((double)curr.residencyT/curr.evictionCnt) + " average residency.");

            totalFault += curr.faultCnt;
            totalResidencyT += curr.residencyT;
            totalEvictionT += curr.evictionCnt;

        }
        System.out.println();

        if (totalEvictionT == 0) {
            System.out.println("The total number of faults is " + totalFault + ".");
            System.out.println("       With no evictions, the overall average residence is undefined.");
        }
        else System.out.println("The total number of faults is " + totalFault + " and the overall average residency is " + ((double)totalResidencyT/totalEvictionT) + ".");

    }





    static void Random(ArrayList<String> Pp, ArrayList<Processes> pLst, int processSize, int numFrameEntries, int pageSize, int debugInfo, int jobMix) throws IOException {
        int initialRefRemaining = pLst.get(0).remainingRef;
        int currT = 1;
        int pIndx = 0;


        while (pLst.get(pLst.size()-1).remainingRef > 0) {

            Processes curr = pLst.get(pIndx);

            for (int i = 0; i < 3; i++) {
                if (curr.remainingRef == 0) break;
                String toEvictKey = "";
                int processIndxToEvict = 0;
                int initialLoadT = 0;
                int residencyT = 0;
                int prevRef = 0;
                int nextRef = 0;
                int pageNum = 0;
                double y = 0;
                int caseNum = 0;
                int pageEvicted = 0;
                int indxAvailable = 0;
                int randomNumForReplacement = 0;
                int indxToEvict = 0;

                nextRef = curr.nextRef;

                pageNum = nextRef/pageSize;

                String currKey = (curr.id + 1) + "_" + pageNum;
                if (Pp.contains(currKey)) {
                }

                else {
                    curr.faultCnt++;

                    if (Pp.lastIndexOf(" ") != -1) {
                        indxAvailable = Pp.lastIndexOf(" ");
                        Pp.remove(indxAvailable);
                        Pp.add(indxAvailable, currKey);
                        curr.pageTable[pageNum] = currT;
                    }


                    else {
                        randomNumForReplacement = getNextRandomNum();
                        indxToEvict = (randomNumForReplacement + numFrameEntries) % numFrameEntries;
                        toEvictKey = Pp.get(indxToEvict);
                        processIndxToEvict = Integer.parseInt(toEvictKey.substring(0, toEvictKey.indexOf("_")))-1;
                        pageEvicted = Integer.parseInt(toEvictKey.substring(toEvictKey.indexOf("_")+1));
                        Processes evicting = pLst.get(processIndxToEvict);
                        initialLoadT = evicting.pageTable[pageEvicted];
                        residencyT = currT - initialLoadT;
                        evicting.residencyT += residencyT;

                        evicting.evictionCnt++;
                        Pp.remove(indxToEvict);


                        Pp.add(indxToEvict, currKey);
                        curr.pageTable[pageNum] = currT;


                    }

                }

                curr.remainingRef--;
                currT++;

                prevRef = nextRef;
                y = (double)getNextRandomNum()/(Integer.MAX_VALUE+1d);
                caseNum = getCaseNum(y, curr);
                nextRef = getNextRef(prevRef, caseNum, processSize);
                curr.nextRef = nextRef;

            }

            pIndx++;
            if (pIndx == pLst.size()) pIndx = 0;

        }


        int totalFault = 0;
        int totalResidencyT = 0;
        int totalEvictionT = 0;

        System.out.println();

        System.out.println("The machine size is " + (numFrameEntries*pageSize) + ".");
        System.out.println("The page size is " + pageSize + ".");
        System.out.println("The process size is " + processSize + ".");
        System.out.println("The job mix number is " + jobMix + ".");
        System.out.println("The number of references per process is " + initialRefRemaining + ".");
        System.out.println("The replacement algorithm is random.");

        System.out.println();
        for (int i = 0; i < pLst.size(); i++) {
            Processes curr = pLst.get(i);

            int evictionCnt = curr.evictionCnt;
            if (evictionCnt == 0) {
                System.out.println("Process " + (curr.id+1) + " had " + curr.faultCnt + " faults.");
                System.out.println("       With no evictions, the average residence is undefined.");
            }
            else System.out.println("Process " + (curr.id+1) + " had " + curr.faultCnt + " faults and " + ((double)curr.residencyT/curr.evictionCnt) + " average residency.");

            totalFault += curr.faultCnt;
            totalResidencyT += curr.residencyT;
            totalEvictionT += curr.evictionCnt;

        }
        System.out.println();

        if (totalEvictionT == 0) {
            System.out.println("The total number of faults is " + totalFault + ".");
            System.out.println("       With no evictions, the overall average residence is undefined.");
        }
        else System.out.println("The total number of faults is " + totalFault + " and the overall average residency is " + ((double)totalResidencyT/totalEvictionT) + ".");

    }
}


