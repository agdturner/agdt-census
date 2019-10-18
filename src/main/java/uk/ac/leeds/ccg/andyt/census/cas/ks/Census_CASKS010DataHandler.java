/**
 * A component of a library for
 * <a href="http://www.geog.leeds.ac.uk/people/a.turner/projects/MoSeS">MoSeS</a>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
 */
package uk.ac.leeds.ccg.andyt.census.cas.ks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.census.core.Census_AbstractDataHandler;
import uk.ac.leeds.ccg.andyt.census.core.Census_AbstractDataRecord;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_ErrorAndExceptionHandler;

/**
 * A <code>class</code> for handling an individual
 * <code>Census_CASKS010DataRecord</code> and collections of
 * <code>CASKS010DataRecords</code>.
 * <ul>
 * <li>Used for formatting source data and testing it can be loaded to and
 * retrieved from a formatted file.</li>
 * <li>Developed for <a href="http://www.ncess.ac.uk/moses">MoSeS</a>.</li>
 * <li>Copyright (C) 2006 <a
 * href="http://www.geog.leeds.ac.uk/people/a.turner/">Andy Turner</a>, <a
 * href="http://www.leeds.ac.uk//">University of Leeds</a>.</li>
 * </ul>
 * 
 * @author <a href="http://www.geog.leeds.ac.uk/people/a.turner/">Andy
 *         Turner</a>
 * @version 1.0.0, 2006-08-10
 * @see Census_AbstractDataRecord
 */
public class Census_CASKS010DataHandler extends Census_AbstractDataHandler {

    /**
     * Creates a new instance of <code>CASKS010DataHandler</code> for handling
     * CASKS010DataRecords stored in a formatted <code>File</code> The default
     * <code>File</code> is hard coded. To specify the <code>File</code> use
     * <code>CASKS010DataHandler(File)</code>. To set a different default
     * <code>File</code> edit the source code and recompile.
     * @throws java.io.IOException
     */
    public Census_CASKS010DataHandler() throws IOException {
        // this( new File(
        // "C:/Work/Projects/MoSeS/Workspace/CASKS010DataRecords.dat" ) );
        // Want also to setDirectory();
        // initMemoryReserve();
        // Default this.directory, this.file, this.randomAccessFile
        File directory = new File("C:/Work/Projects/MoSeS/Workspace/");
        this.init(directory);
        this.file = new File(directory, "CASKS010DataRecords.dat");
        if (!this.file.exists()) {
            this.file.createNewFile();
        }
        this.recordLength = new Census_CASKS010DataRecord().getSizeInBytes();
        // log("this.recordLength " + this.recordLength);
        this.rAF = new RandomAccessFile(this.file, "r");
    }

    /**
     * Creates a new instance of CASKS010DataHandler with Records loaded from
     * formattedFile.
     *
     * @param formattedFile
     *            Formatted file of CASKS010DataRecords
     */
    public Census_CASKS010DataHandler(File formattedFile) {
        // initMemoryReserve();
        this.init(formattedFile.getParentFile());
        this.recordLength = new Census_CASKS010DataRecord().getSizeInBytes();
        load(formattedFile);
        log("CASKS010DataRecords loaded successfully");
    }

    /**
     * @param args
     *            the command line arguments No arguments are used.
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Census_CASKS010DataHandler aCASKS010DataHandler = new Census_CASKS010DataHandler();
        aCASKS010DataHandler.run();
    }

    /**
     * Top level run method
     */
    private void run() throws IOException {
        // run1( true, 20 );
        // run1( false, 20 );
        run2(20);
    }

    /**
     * Loads <code>CASKS010DataRecords</code> and prints out n randomly
     *
     * @param loadFromSource
     *            If true, data is loaded from hardcoded source files as
     *            downloaded from casweb. Otherwise, data is loaded from
     *            <code>this.formattedFile</code>
     * @param n
     *            The number of loaded data records to print out.
     */
    private void run2(int n) throws IOException {
        File file = new File(
                "C:/Work/Projects/MoSeS/Workspace/Leeds/MSOA/CASKS010DataRecords.dat");
        load(file);
        print(n, new Random());
    }

    /**
     * Loads <code>CAS001DataRecords</code> and prints out n randomly
     * @param directory to load source data from
     * @param n the number of loaded data records to print out.
     * @throws java.io.IOException
     */
    public void formatSourceData(
            File directory,
            int n)
            throws IOException {
        rAF = new RandomAccessFile(this.file, "rw");
        File infile;
        long long0 = 0L;
        long RecordID = 0L;
        // Load England
        infile = new File(
                directory,
                "KS010EnglandOA.csv");
        RecordID = format(infile, RecordID);
        log(infile.toString() + " formatted successfully " + RecordID + " records"); // 165665
        long0 = RecordID;
        // Load Wales
        infile = new File(
                directory,
                "KS010WalesOA.csv");
        RecordID = format(infile, RecordID);
        log(infile.toString() + " formatted successfully " + (RecordID - long0) + " records"); // 9769
        long0 = RecordID;
        // Load Scotland
        infile = new File(
                directory,
                "KS010ScotlandOA.csv");
        RecordID = format(infile, RecordID);
        log(infile.toString() + " formatted successfully " + (RecordID - long0) + " records"); // 42604
        long0 = RecordID;
        // Load Northern Ireland
        infile = new File(
                directory,
                "KS010NorthernIrelandOA.csv");
        RecordID = format(infile, RecordID);
        log(infile.toString() + " formatted successfully " + (RecordID - long0) + " records"); // 5022
        rAF.close();
        load(file);
        print(20, new Random());
    }

    /**
     * Uses a <code>BufferedReader<code> and a <code>StreamTokenizer</code> to
     * read lines from the sourceFile <code>File</code>. The lines are converted
     * to <code>CASKS010DataRecords</code> and written to
     * <code>this.tRandomAccessFile</code>.
     *
     * @param sourceFile
     *            The source CASKS010DataRecords file to be formatted and
     *            written to <code>this.tRandomAccessFile</code>.
     * @param RecordID
     *            The <code>RecordID</code> to assign to the first
     *            <code>Census_CASKS010DataRecord</code>.
     * @return The <code>RecordID</code> assigned to the last
     *         <code>CASKS010DataRecords</code>.
     * @throws java.io.IOException
     */
    protected long format(File sourceFile, long RecordID) throws IOException {
        log("format( File( " + sourceFile.toString() + " ), RecordID( " + RecordID + " ))");
        BufferedReader br = env.env.io.getBufferedReader(sourceFile);
        StreamTokenizer st = new StreamTokenizer(br);
        env.env.io.setStreamTokenizerSyntax1(st);
        String string0 = new String();
        String string1;
        String string2;
        long long0;
        long longZero = 0L;
        Census_CASKS010DataRecord rec = new Census_CASKS010DataRecord();
        boolean print = false;
        int int10000 = 10000;
        // Skip the first line
        int tokenType = st.nextToken();
        while (tokenType != StreamTokenizer.TT_EOL) {
            tokenType = st.nextToken();
        }
        tokenType = st.nextToken();
        while (tokenType != StreamTokenizer.TT_EOF) {
            switch (tokenType) {
                case StreamTokenizer.TT_EOL:
                    long0 = RecordID % int10000;
                    print = (long0 == longZero);
                    if (print) {
                        string2 = rec.toString();
                        log(string2);
                        string2 = string0;
                    }
                    // Write out
                    rec.write(rAF);
                    RecordID++;
                    break;
                case StreamTokenizer.TT_WORD:
                    string1 = st.sval;
                    rec = new Census_CASKS010DataRecord(RecordID, string1);
                    break;
            }
            string1 = string0;
            tokenType = st.nextToken();
        }
        log("Number of Records loaded = " + RecordID);
        return RecordID;
    }

    /**
     * @return a <code>Census_CASKS010DataRecord</code> with
     *         <code>Census_AbstractDataRecord.RecordID = RecordID</code>
     * @param RecordID
     *            The RecordID of the Census_CASKS010DataRecord to be returned.
     */
    public Census_AbstractDataRecord getDataRecord(long RecordID) {
        return getCASKS010DataRecord(RecordID);
    }

    /**
     * @return a <code>Census_CASKS010DataRecord</code> with
     *         <code>Census_CASKS010DataRecord.RecordID = RecordID</code>
     * @param RecordID
     *            The RecordID of the Census_CASKS010DataRecord to be returned.
     */
    public Census_CASKS010DataRecord getCASKS010DataRecord(long RecordID) {
        Census_CASKS010DataRecord result = null;
        try {
            this.rAF.seek(recordLength * RecordID);
            result = new Census_CASKS010DataRecord(this.rAF);
        } catch (IOException aIOException) {
            System.err.println(aIOException.getLocalizedMessage());
            System.exit(Generic_ErrorAndExceptionHandler.IOException);
        }
        return result;
    }

    /**
     * Aggregates <code>CASKS010DataRecords</code> from OA To Ward for the OA
     * records in the range [startRecordID,endRecordID] and writes the results
     * to aRandomAccessFile
     *
     * @param aRandomAccessFile
     *            <code>RandomAccessFile</code> to which results are written
     * @param startRecordID
     *            The first OA RecordID in the sequence to be aggregated.
     * @param endRecordID
     *            The last OA RecordID in the sequence to be aggregated.
     * @throws java.io.IOException
     */
    public void aggregateOAToWard(RandomAccessFile aRandomAccessFile,
            long startRecordID, long endRecordID) throws IOException {
        TreeMap result = new TreeMap();
        Census_CASKS010DataRecord aCASKS010DataRecord;
        Census_CASKS010DataRecord bCASKS010DataRecord;
        String zoneCode;
        Object zoneCodeWard;
        // long newRecordID = startRecordIDForLeeds - 1L;
        long newRecordID = -1L;
        for (long RecordID = startRecordID; RecordID < endRecordID; RecordID++) {
            aCASKS010DataRecord = (Census_CASKS010DataRecord) getDataRecord(RecordID);
            zoneCode = new String(aCASKS010DataRecord.getZone_Code());
            zoneCodeWard = zoneCode.substring(0, 6);
            if (result.containsKey(zoneCodeWard)) {
                bCASKS010DataRecord = (Census_CASKS010DataRecord) result.get(zoneCodeWard);
                result.remove(zoneCodeWard);
                result.put(zoneCodeWard, aCASKS010DataRecord.aggregate(bCASKS010DataRecord));
            } else {
                result.put(zoneCodeWard, aCASKS010DataRecord);
            }
        }
        write(aRandomAccessFile, result);
    }

    /**
     * Aggregates <code>CASKS010DataRecords</code> from OA To MSOA for the OA
     * records in the range [startRecordID,endRecordID] and writes the results
     * to aRandomAccessFile
     *
     * @param aRandomAccessFile
     *            <code>RandomAccessFile</code> to which results are written
     * @param startRecordID
     *            The first OA RecordID in the sequence to be aggregated.
     * @param endRecordID
     *            The last OA RecordID in the sequence to be aggregated.
     * @throws java.io.IOException
     */
    public void aggregateOAToMSOA(RandomAccessFile aRandomAccessFile,
            long startRecordID, long endRecordID) throws IOException {
        TreeMap result = new TreeMap();
        HashMap lookUpMSOAfromOAHashMap = get_LookUpMSOAfromOAHashMap();
        Census_CASKS010DataRecord aCASKS010DataRecord;
        Census_CASKS010DataRecord bCASKS010DataRecord;
        String zoneCode;
        Object zoneCodeMSOA;
        // long newRecordID = startRecordIDForLeeds - 1L;
        long newRecordID = -1L;
        for (long RecordID = startRecordID; RecordID < endRecordID; RecordID++) {
            aCASKS010DataRecord = (Census_CASKS010DataRecord) getDataRecord(RecordID);
            zoneCode = new String(aCASKS010DataRecord.getZone_Code());
            zoneCodeMSOA = lookUpMSOAfromOAHashMap.get(zoneCode);
            if (result.containsKey(zoneCodeMSOA)) {
                bCASKS010DataRecord = (Census_CASKS010DataRecord) result.get(zoneCodeMSOA);
                result.remove(zoneCodeMSOA);
                result.put(zoneCodeMSOA, aCASKS010DataRecord.aggregate(bCASKS010DataRecord));
            } else {
                result.put(zoneCodeMSOA, aCASKS010DataRecord);
            }
        }
        write(aRandomAccessFile, result);
    }
}