import java.io.*;
import java.nio.*;
import java.util.LinkedList;

/**
 * @author ryanjt5
 * @version 4/21/2020
 *
 */
public class FileReader {
    private RandomAccessFile file;


    /**
     * creates a new fileReader
     * 
     * @param f
     *            name of the new file
     */
    public FileReader(String f) {
        try {
            file = new RandomAccessFile(f, "r");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * getter for the file
     * 
     * @return the file
     */
    public RandomAccessFile getFile() {
        return file;
    }

    /**
     * fills buffer with data starting at a given offset
     * 
     * @param in
     *            buffer to be filled
     * @param runs
     *            list of runs
     * @param ind
     *            index of run
     * @throws IOException
     */
    public void readIn(ByteBuffer in, LinkedList<Integer> runs, int ind)
        throws IOException {
        int updateOff = 0;
        int off = runs.get(ind);
        for (int i = 0; i < in.limit(); i++) {
            try {
                if (in.hasRemaining()) {
                    file.seek(off);
                    // 8192 bytes is 1 block
                    for (int j = 0; j < 8192; j++) {
                        updateOff++;
                        in.put(file.readByte());
                    }
                    runs.set(ind, off + updateOff);
                }
            }
            catch (BufferOverflowException e) {
                runs.set(ind, off + updateOff);
                break;
            }
        }
    }
}
