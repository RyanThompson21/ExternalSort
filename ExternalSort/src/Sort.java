import java.io.*;
import java.nio.*;
import java.util.LinkedList;

/**
 * Handles Replacement Selection and multiway merge
 * 
 * @author ryanjt5
 * @version 4/21/2020
 */
public class Sort {

    private static ByteBuffer in;
    private ByteBuffer out;
    private static MinHeap heap;
    // stores the offset for each run
    private static LinkedList<Integer> runs;
    // same thing as the first one, need two for merging
    private static LinkedList<Integer> runs2;
    private static FileReader reader;
    private FileOutputStream w;
    private static RandomAccessFile f;


    /**
     * creates an object that will sort the given file
     * 
     * @param file
     *            file to be sorted
     * @throws FileNotFoundException
     */
    public Sort(String file) throws FileNotFoundException {
        runs = new LinkedList<Integer>();
        runs.add(0);
        runs2 = new LinkedList<Integer>();
        runs2.add(0);
        // 8192 bytes is 1 block
        byte[] i = new byte[8192];
        in = ByteBuffer.wrap(i);
        byte[] o = new byte[8192];
        out = ByteBuffer.wrap(o);
        // 4096 records is 8 blocks
        heap = new MinHeap();
        f = new RandomAccessFile(file, "r");
        w = new FileOutputStream("runs.bin");
    }


    /**
     * getter for the runs linked list
     * 
     * @return the list of runs
     */
    public LinkedList<Integer> getRuns() {
        return runs;
    }



    /**
     * gets the next record from the input buffer
     * 
     * @return the next record in the input buffer
     */
    public static Record nextRec() {
        Record rec = new Record(in.getLong(), in.getDouble());
        return rec;
    }


    /**
     * merges up to 8 runs from the inFile and prints to the outFile
     * 
     * @param outFile
     *            file being written to
     * @param inFile
     *            file being read from
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void multiwayMerge(String outFile, String inFile)
        throws IOException {

        heap = new MinHeap();
        reader = new FileReader(inFile);
        reader.getFile().seek(0);
        w = new FileOutputStream(outFile);

        // take 1st block of data from each run
        int i = 0;
        // the last element in blocks should always be 0
        int[] blocks = new int[runs.size()];
        // last entry in runs shouldn't be touched. Just there for indexing and
        // testing purposes
        while (i < runs.size() - 1 && i < 8) {
            int[] temp = addBlock(i, blocks);
            blocks = temp;
            i++;
        } // heap is populated input buffer should be empty
          // pop from heap and check to see if its the last from its block
          // if it is get another block from that run
        int count = 0;
        out.clear();
        while (count < reader.getFile().length() / 16) {
            Record rec = heap.removeMin();
            int runNum = rec.getFlag();
            out.put(rec.getTotal());
            blocks[runNum]--;
            count++;
            if (blocks[runNum] == 0) { // need to load in more data
                // check if there is actually more data in the run
                if (runs.get(runNum) == runs.get(runNum + 1)) {
                    break; 
                }
                else {
                    int[] t = addBlock(runNum, blocks);
                    blocks = t;
                }
            }
            if (!out.hasRemaining()) {
                w.write(out.array());
                out.clear();
            }

        } // EOF
        runs2.add(count * 16);
        runs = (LinkedList<Integer>)runs2.clone();
        runs2.clear();
        w.close();
    }


    /**
     * adds a block of data to the input buffer and then the heap
     * 
     * @param ind
     *            which run this block is coming from
     * @param b
     *            an int[] to keep track of which run a record is from
     * @return returns an int[]
     * @throws IOException
     */
    public int[] addBlock(int ind, int[] b) throws IOException {
        int[] blocks = b;
        in.clear();
        try {
            reader.readIn(in, runs, ind);
            in.position(0);
            for (int j = 0; j < 512; j++) {
                Record r = new Record(in.getLong(), in.getDouble(), ind);
                heap.insert(r);
                blocks[ind]++;
            }
        }
        catch (EOFException e) {
            return blocks;
        }
        return blocks;
    }


    /**
     * makes runs from original file
     * just for testing multiwayMerge
     * 
     * @throws IOException
     */
    public void runItDown() throws IOException {
        int count = 0;
        // RandomAccessFile run = new RandomAccessFile("runs.bin", "r");
        while (f.getFilePointer() < f.length()) {
            for (int i = 0; i < 4096; i++) {
                if (i % 512 == 0) {
                    in.clear();
                    f.read(in.array());
                    in.position(0);
                }
                Record r = new Record(in.getLong(), in.getDouble());
                heap.insert(r);
            }
            out.clear();
            while (!heap.isEmpty()) {
                out.put(heap.removeMin().getTotal());
                count++;
                if (!out.hasRemaining()) {
                    w.write(out.array());
                    out.clear();
                }
            }
            runs.add(count * 16);
        }
    }
}
