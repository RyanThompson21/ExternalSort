import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * @author ryanjt5
 * @version 4/24/20
 */
public class Externalsort {

    /**
     * executes the program
     * 
     * @param args
     *            input
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Sort sort = new Sort(args[0]);
        int count = 0;
        sort.runItDown();
        // create initial runs
        while (sort.getRuns().size() > 2) { // 524288 = 512*64*16
            if (count % 2 == 0) {
                sort.multiwayMerge(args[0], "runs.bin");
            }
            else {
                sort.multiwayMerge("runs.bin", args[0]);
            }
            count++;
        }
        if (count % 2 == 1) {
            printRecs(args[0]);
        }
        else {
            printRecs("runs.bin");
        }
    }


    /**
     * prints the 1st record from each block in the file
     * 
     * @param name
     *            file name
     * @throws IOException
     */
    private static void printRecs(String name) throws IOException {
        RandomAccessFile file = new RandomAccessFile(name, "r");
        int count = 0;
        String recs = "";
        // System.out.println("printRecs File: " + file.length());
        for (int i = 0; i < file.length() / 16; i += 512) {
            file.seek(i * 16);
            Record rec = new Record(file.readLong(), file.readDouble());
            // System.out.println(rec.toString());
            count++;
            if (count % 5 == 0) {
                recs = recs + rec.toString() + "\n";
            }
            else {
                recs = recs + rec.toString() + " ";
            }
        }
        System.out.println(recs);
        file.close();
    }

}
