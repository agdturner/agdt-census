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
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;

/**
 * A <code>class</code> for handling an individual
 * <code>Census_CASKS002DataRecord</code> and collections of
 * <code>CASKS002DataRecords</code>.
 */
public class Census_CASKS002DataHandler extends Census_AbstractDataHandler {

    public Census_CASKS002DataHandler() {
        File directory = new File("C:/Work/Projects/MoSeS/Workspace/");
        init(directory);
        try {
            this._File = new File(directory, "CASKS002DataRecords.dat");
            if (!this._File.exists()) {
                this._File.createNewFile();
            }
            this._RecordLength = new Census_CASKS002DataRecord().getSizeInBytes();
            // log("this.recordLength " + this.recordLength);
            this._RandomAccessFile = new RandomAccessFile(this._File, "r");
        } catch (IOException aIOException) {
            log(aIOException.getLocalizedMessage());
            System.exit(Generic_ErrorAndExceptionHandler.IOException);
        }
    }

    /**
     * Creates a new instance of CASKS002DataHandler with Records loaded from
     * formattedFile.
     *
     * @param formattedFile
     *            Formatted file of CASKS002DataRecords
     */
    public Census_CASKS002DataHandler(File formattedFile) {
        this.init(formattedFile.getParentFile());
        this._RecordLength = new Census_CASKS002DataRecord().getSizeInBytes();
        load(formattedFile);
        log("CASKS002DataRecords loaded successfully");
    }

    /**
     * Loads <code>CAS001DataRecords</code> and prints out n randomly
     * @param directory to load source data from
     * @param n the number of loaded data records to print out.
     */
    public void formatSourceData(
            File directory,
            int n) {
        try {
            _RandomAccessFile = new RandomAccessFile(this._File, "rw");
            // Load from source
            File infile;
            long long0 = 0L;
            long RecordID = 0L;
            // Load England
            infile = new File(
                    directory,
                    "KS002EnglandOA.csv");
            RecordID = format(infile, RecordID);
            log(infile.toString() + " formatted successfully " + RecordID + " records"); // 165665
            long0 = RecordID;
            // Load Wales
            infile = new File(
                    directory,
                    "KS002WalesOA.csv");
            RecordID = format(infile, RecordID);
            log(infile.toString() + " formatted successfully " + (RecordID - long0) + " records"); // 9769
            long0 = RecordID;
            // Load Scotland
            infile = new File(
                    directory,
                    "KS002ScotlandOA.csv");
            RecordID = format(infile, RecordID);
            log(infile.toString() + " formatted successfully " + (RecordID - long0) + " records"); // 42604
            long0 = RecordID;
            // Load Northern Ireland
            infile = new File(
                    directory,
                    "KS002NorthernIrelandOA.csv");
            RecordID = format(infile, RecordID);
            log(infile.toString() + " formatted successfully " + (RecordID - long0) + " records"); // 5022
            _RandomAccessFile.close();
            load(_File);
            print(20, new Random());
        } catch (IOException aIOException) {
            log(aIOException.getLocalizedMessage());
            System.exit(Generic_ErrorAndExceptionHandler.IOException);
        }
    }

    /**
     * Uses a <code>BufferedReader<code> and a <code>StreamTokenizer</code> to
     * read lines from the sourceFile <code>File</code>. The lines are converted
     * to <code>CASKS002DataRecords</code> and written to
     * <code>this.tRandomAccessFile</code>.
     *
     * @param sourceFile
     *            The source CASKS002DataRecords file to be formatted and
     *            written to <code>this.tRandomAccessFile</code>.
     * @param RecordID
     *            The <code>RecordID</code> to assign to the first
     *            <code>Census_CASKS002DataRecord</code>.
     * @return The <code>RecordID</code> assigned to the last
     *         <code>CASKS002DataRecords</code>.
     */
    protected long format(
            File sourceFile,
            long RecordID) {
        try {
            BufferedReader aBufferedReader =
                    new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(sourceFile)));
            StreamTokenizer aStreamTokenizer = new StreamTokenizer(aBufferedReader);
            Generic_IO.setStreamTokenizerSyntax1(aStreamTokenizer);
            String string0 = new String();
            String string1;
            String string2;
            long long0;
            long longZero = 0L;
            Census_CASKS002DataRecord aCASKS002DataRecord = new Census_CASKS002DataRecord();
            boolean print = false;
            int int10000 = 10000;
            // Skip the first line
            int tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOL) {
                tokenType = aStreamTokenizer.nextToken();
            }
            tokenType = aStreamTokenizer.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        long0 = RecordID % int10000;
                        print = (long0 == longZero);
                        if (print) {
                            string2 = aCASKS002DataRecord.toString();
                            log(string2);
                            string2 = string0;
                        }
                        // Write out
                        aCASKS002DataRecord.write(_RandomAccessFile);
                        RecordID++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        string1 = aStreamTokenizer.sval;
                        aCASKS002DataRecord = new Census_CASKS002DataRecord(RecordID, string1);
                        break;
                }
                string1 = string0;
                tokenType = aStreamTokenizer.nextToken();
            }
            log("Number of Records loaded = " + RecordID);
        } catch (IOException aIOException) {
            log(aIOException.getLocalizedMessage());
            System.exit(Generic_ErrorAndExceptionHandler.IOException);
        }
        return RecordID;
    }

    /**
     * @return a <code>Census_CASKS002DataRecord</code> with
     *         <code>Census_AbstractDataRecord.RecordID = RecordID</code>
     * @param RecordID
     *            The RecordID of the Census_CASKS002DataRecord to be returned.
     */
    public Census_AbstractDataRecord getDataRecord(long RecordID) {
        return getCASKS002DataRecord(RecordID);
    }

    /**
     * @return a <code>Census_CASKS002DataRecord</code> with
     *         <code>Census_CASKS002DataRecord.RecordID = RecordID</code>
     * @param RecordID
     *            The RecordID of the Census_CASKS002DataRecord to be returned.
     */
    public Census_CASKS002DataRecord getCASKS002DataRecord(long RecordID) {
        Census_CASKS002DataRecord result = null;
        try {
            this._RandomAccessFile.seek(_RecordLength * RecordID);
            result = new Census_CASKS002DataRecord(this._RandomAccessFile);
        } catch (IOException aIOException) {
            System.err.println(aIOException.getLocalizedMessage());
            System.exit(Generic_ErrorAndExceptionHandler.IOException);
        }
        return result;
    }

    /**
     * Aggregates <code>CASKS002DataRecords</code> from OA To Ward for the OA
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
    public void aggregateOAToWard(
            RandomAccessFile aRandomAccessFile,
            long startRecordID,
            long endRecordID)
            throws IOException {
        TreeMap result = new TreeMap();
        Census_CASKS002DataRecord aCASKS002DataRecord;
        Census_CASKS002DataRecord bCASKS002DataRecord;
        String zoneCode;
        Object zoneCodeWard;
        // long newRecordID = startRecordIDForLeeds - 1L;
        long newRecordID = -1L;
        for (long RecordID = startRecordID; RecordID < endRecordID; RecordID++) {
            aCASKS002DataRecord = (Census_CASKS002DataRecord) getDataRecord(RecordID);
            zoneCode = new String(aCASKS002DataRecord.getZone_Code());
            zoneCodeWard = zoneCode.substring(0, 6);
            if (result.containsKey(zoneCodeWard)) {
                bCASKS002DataRecord = (Census_CASKS002DataRecord) result.get(zoneCodeWard);
                result.remove(zoneCodeWard);
                result.put(zoneCodeWard, aCASKS002DataRecord.aggregate(bCASKS002DataRecord));
            } else {
                result.put(zoneCodeWard, aCASKS002DataRecord);
            }
        }
        write(aRandomAccessFile, result);
    }

    /**
     * Aggregates <code>CASKS002DataRecords</code> from OA To MSOA for the OA
     * records in the range [startRecordID,endRecordID] and writes the results
     * to aRandomAccessFile
     *
     * @param aRandomAccessFile
     *            <code>RandomAccessFile</code> to which results are written
     * @param startRecordID
     *            The first OA RecordID in the sequence to be aggregated.
     * @param endRecordID
     *            The last OA RecordID in the sequence to be aggregated.
     */
    public void aggregateOAToMSOA(
            RandomAccessFile aRandomAccessFile,
            long startRecordID,
            long endRecordID) {
        TreeMap result = new TreeMap();
        HashMap lookUpMSOAfromOAHashMap = get_LookUpMSOAfromOAHashMap();
        Census_CASKS002DataRecord aCASKS002DataRecord;
        Census_CASKS002DataRecord bCASKS002DataRecord;
        String zoneCode;
        Object zoneCodeMSOA;
        for (long RecordID = startRecordID; RecordID < endRecordID; RecordID++) {
            aCASKS002DataRecord = (Census_CASKS002DataRecord) getDataRecord(RecordID);
            zoneCode = new String(aCASKS002DataRecord.getZone_Code());
            zoneCodeMSOA = lookUpMSOAfromOAHashMap.get(zoneCode);
            if (result.containsKey(zoneCodeMSOA)) {
                bCASKS002DataRecord = (Census_CASKS002DataRecord) result.get(zoneCodeMSOA);
                result.remove(zoneCodeMSOA);
                result.put(zoneCodeMSOA, aCASKS002DataRecord.aggregate(bCASKS002DataRecord));
            } else {
                result.put(zoneCodeMSOA, aCASKS002DataRecord);
            }
        }
        try {
            write(aRandomAccessFile, result);
        } catch (IOException aIOException) {
            log(aIOException.getLocalizedMessage());
            System.exit(Generic_ErrorAndExceptionHandler.IOException);
        }
    }
}