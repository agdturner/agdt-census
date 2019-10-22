/**
 * A component of a library for
 * <a href="http://www.geog.leeds.ac.uk/people/a.turner/projects/MoSeS">MoSeS</a>.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 */
package uk.ac.leeds.ccg.andyt.census.cas;

import uk.ac.leeds.ccg.andyt.census.core.Census_AbstractHandler;
import uk.ac.leeds.ccg.andyt.census.core.Census_AbstractDataRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.census.core.Census_Environment;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_ErrorAndExceptionHandler;

/**
 * Class for handling CAS001 Data.
 */
public class Census_CAS001Handler extends Census_AbstractHandler {

    /**
     * Creates a new instance of CAS001DataHandler loading a default file.
     */
    public Census_CAS001Handler(Census_Environment e) throws IOException {
        super (e);
//        File directory = new File("C:/Work/Projects/MoSeS/Workspace/");
//        this.init(directory);
//        this.file = new File(env., "CAS001DataRecords.dat");
//        if (!this.file.exists()) {
//            this.file.createNewFile();
//        }
//        this.recordLength = new Census_CAS001DataRecord().getSizeInBytes();
//        // log("this.recordLength " + this.recordLength);
//        this.rAF = new RandomAccessFile(this.file, "r");
    }
//
//    /**
//     * Creates a new instance of CAS001DataHandler with Records loaded from
//     * aFile.
//     *
//     * @param f Formatted file of CAS001DataRecords.
//     */
//    public Census_CAS001Handler(File f) throws IOException {
//        // initMemoryReserve();
//        this.init(f.getParentFile());
//        this.recordLength = new Census_CAS001DataRecord().getSizeInBytes();
//        load(f);
//        env.env.log("CAS001DataRecords loaded successfully");
//    }

    /**
     * Loads <code>CAS001DataRecords</code> and prints out n randomly
     *
     * @param directory to load source data from
     * @param n the number of loaded data records to print out.
     * @throws java.io.IOException
     */
    public void formatSourceData(File directory, int n) throws IOException {
        rAF = new RandomAccessFile(this.file, "rw");
        // Load from source
        File infile;
        long long0 = 0L;
        /**
         * rID stores a record ID
         */
        long rID = 0L;
        // Load England
        infile = new File(directory, "CAS001EnglandOA.csv");
        rID = format(infile, rID);
        env.env.log(infile.toString() + " formatted successfully " + rID + " records"); // 165665
        long0 = rID;
        // Load Wales
        infile = new File(directory, "CAS001WalesOA.csv");
        rID = format(infile, rID);
        env.env.log(infile.toString() + " formatted successfully " + (rID - long0) + " records"); // 9769
        long0 = rID;
        // Load Scotland
        infile = new File(directory, "CAS001ScotlandOA.csv");
        rID = format(infile, rID);
        env.env.log(infile.toString() + " formatted successfully " + (rID - long0) + " records"); // 42604
        long0 = rID;
        // Load Northern Ireland
        infile = new File(
                directory,
                "CAS001NorthernIrelandOA.csv");
        rID = format(infile, rID);
        env.env.log(infile.toString() + " formatted successfully " + (rID - long0) + " records"); // 5022
        rAF.close();
        load(file);
        print(20, new Random());
    }

    /**
     * Uses a <code>BufferedReader</code> and a <code>StreamTokenizer</code> to
     * read lines from the sourceFile <code>File</code>. The lines are converted
     * to <code>CAS001DataRecords</code> and written to
     * <code>this.tRandomAccessFile</code>.
     *
     * @param sourceFile The source CAS001DataRecords file to be formatted and
     * written to <code>this.tRandomAccessFile</code>.
     * @param RecordID The <code>RecordID</code> to assign to the first
     * <code>Census_CAS001DataRecord</code>.
     * @return The <code>RecordID</code> assigned to the last
     * <code>CAS001DataRecords</code>.
     */
    protected long format(File sourceFile, long RecordID) {
        try {
            BufferedReader br = env.env.io.getBufferedReader(sourceFile);
            StreamTokenizer st = new StreamTokenizer(br);
            env.env.io.setStreamTokenizerSyntax1(st);
            String line;
            Census_CAS001DataRecord rec = new Census_CAS001DataRecord();
            // Skip the first line
            int tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOL) {
                tokenType = st.nextToken();
            }
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        if (RecordID % 10000 == 0) {
                            env.env.log(rec.toString());
                        }
                        // Write out
                        rec.write(rAF);
                        RecordID++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        rec = new Census_CAS001DataRecord(RecordID, line);
                        break;
                }
                tokenType = st.nextToken();
            }
            env.env.log("Number of Records loaded = " + RecordID);
            br.close();
        } catch (IOException aIOException) {
            env.env.log(aIOException.getLocalizedMessage());
            System.exit(Generic_ErrorAndExceptionHandler.IOException);
        }
        return RecordID;
    }

    /**
     * @return a <code>Census_CAS001DataRecord</code> with
     * <code>Census_AbstractDataRecord.RecordID = RecordID</code>
     * @param RecordID The RecordID of the Census_CAS001DataRecord to be
     * returned.
     */
    @Override
    public Census_AbstractDataRecord getDataRecord(long RecordID) {
        return getCAS001DataRecord(RecordID);
    }

    /**
     * @return a <code>Census_CAS001DataRecord</code> with
     * <code>Census_CAS001DataRecord.RecordID = RecordID</code>
     * @param RecordID The RecordID of the Census_CAS001DataRecord to be
     * returned.
     */
    public Census_CAS001DataRecord getCAS001DataRecord(long RecordID) {
        Census_CAS001DataRecord result = null;
        try {
            this.rAF.seek(recordLength * RecordID);
            result = new Census_CAS001DataRecord(this.rAF);
        } catch (IOException aIOException) {
            env.env.log(aIOException.getLocalizedMessage());
            System.exit(Generic_ErrorAndExceptionHandler.IOException);
        }
        return result;
    }

    /**
     * Aggregates <code>CAS001DataRecords</code> from OA To Ward for the OA
     * records in the range [startRecordID,endRecordID] and writes the results
     * to aRandomAccessFile
     *
     * @param aRandomAccessFile <code>RandomAccessFile</code> to which results
     * are written
     * @param startRecordID The first OA RecordID in the sequence to be
     * aggregated.
     * @param endRecordID The last OA RecordID in the sequence to be aggregated.
     */
    public void aggregateOAToWard(
            RandomAccessFile aRandomAccessFile,
            long startRecordID,
            long endRecordID) {
        TreeMap result = new TreeMap();
        Census_CAS001DataRecord aCAS001DataRecord;
        Census_CAS001DataRecord bCAS001DataRecord;
        String zoneCode;
        Object zoneCodeWard;
        // long newRecordID = startRecordIDForLeeds - 1L;
        long newRecordID = -1L;
        for (long RecordID = startRecordID; RecordID < endRecordID; RecordID++) {
            aCAS001DataRecord = (Census_CAS001DataRecord) getDataRecord(RecordID);
            zoneCode = new String(aCAS001DataRecord.getZone_Code());
            zoneCodeWard = zoneCode.substring(0, 6);
            if (result.containsKey(zoneCodeWard)) {
                bCAS001DataRecord = (Census_CAS001DataRecord) result.get(zoneCodeWard);
                result.remove(zoneCodeWard);
                result.put(
                        zoneCodeWard,
                        aCAS001DataRecord.aggregate(bCAS001DataRecord));
            } else {
                result.put(
                        zoneCodeWard,
                        aCAS001DataRecord);
            }
        }
//        try {
//            write(aRandomAccessFile, result);
//        } catch (IOException aIOException) {
//            env.env.log(aIOException.getLocalizedMessage());
//            System.exit(Generic_ErrorAndExceptionHandler.IOException);
//        }
    }

    /**
     * Aggregates <code>CAS001DataRecords</code> from OA To MSOA for the OA
     * records in the range [startRecordID,endRecordID] and writes the results
     * to aRandomAccessFile
     *
     * @param raf <code>RandomAccessFile</code> to which results
     * are written
     * @param startRecordID The first OA RecordID in the sequence to be
     * aggregated.
     * @param endRecordID The last OA RecordID in the sequence to be aggregated.
     */
    public void aggregateOAToMSOA(            RandomAccessFile raf,            long startRecordID,
            long endRecordID) {
        TreeMap<Object, Census_CAS001DataRecord> r = new TreeMap();
        HashMap lookUpMSOAfromOAHashMap = null;//getOA2MSOA();
        Census_CAS001DataRecord rOA;
        Census_CAS001DataRecord rMSOA;
        String zoneCode;
        Object zoneCodeMSOA;
        // long newRecordID = startRecordIDForLeeds - 1L;
        long newRecordID = -1L;
        for (long RecordID = startRecordID; RecordID < endRecordID; RecordID++) {
            rOA = (Census_CAS001DataRecord) getDataRecord(RecordID);
            zoneCode = new String(rOA.getZone_Code());
            zoneCodeMSOA = lookUpMSOAfromOAHashMap.get(zoneCode);
            if (r.containsKey(zoneCodeMSOA)) {
                rMSOA = (Census_CAS001DataRecord) r.get(zoneCodeMSOA);
                r.remove(zoneCodeMSOA);
                r.put(zoneCodeMSOA, rOA.aggregate(rMSOA));
            } else {
                r.put(zoneCodeMSOA, rOA);
            }
        }
//        try {
//            write(raf, r);
//        } catch (IOException ex) {
//            env.env.log(ex.getLocalizedMessage());
//            System.exit(Generic_ErrorAndExceptionHandler.IOException);
//        }
    }

    /**
     * Aggregates <code>CAS001DataRecords</code>
     *
     * @param a_AreaCodesToAggregate_HashSet
     * @return
     */
    public Census_CAS001DataRecord aggregate(
            HashSet<String> a_AreaCodesToAggregate_HashSet) {
        boolean initZero = true;
        Census_CAS001DataRecord result = new Census_CAS001DataRecord(initZero);
        Census_CAS001DataRecord aCAS001DataRecord;
        Iterator<String> a_Iterator = a_AreaCodesToAggregate_HashSet.iterator();
        String areaCode;
        while (a_Iterator.hasNext()) {
            areaCode = a_Iterator.next();
            aCAS001DataRecord = null;//(Census_CAS001DataRecord) getDataRecord(areaCode);
            if (a_AreaCodesToAggregate_HashSet.contains(areaCode)) {
                result = result.aggregate(aCAS001DataRecord);
            }
        }
//        long end_RecordID = getNDataRecords();
//        for (long a_RecordID = 0; a_RecordID < end_RecordID; a_RecordID++) {
//            aCAS001DataRecord = (Census_CAS001DataRecord) getDataRecord(a_RecordID);
//            areaCode = new String(aCAS001DataRecord.getZone_Code());
//            if (a_AreaCodesToAggregate_HashSet.contains(areaCode)){
//                result = result.aggregate(aCAS001DataRecord);
//            }
//        }
        return result;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0,5,8,10,15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001HPAgeCount1_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(16);
        int tHPAge0to4
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge0to4()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge0to4();
        tCAS001AgeCountHashMap.put(0, tHPAge0to4);
        int tHPAge5to7
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge5()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge5()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge6()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge6()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge7()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge7();
        tCAS001AgeCountHashMap.put(5, tHPAge5to7);
        int tHPAge8to9
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge8()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge8()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge9()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge9();
        tCAS001AgeCountHashMap.put(8, tHPAge8to9);
        int tHPAge10to14
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge10to14()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge10to14();
        tCAS001AgeCountHashMap.put(10, tHPAge10to14);
        int tHPAge15
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge15()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(15, tHPAge15);
        int tHPAge16to19
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge16()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge16()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge17()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge17()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge18()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge18()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge19()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge19();
        tCAS001AgeCountHashMap.put(16, tHPAge16to19);
        int tHPAge20to24
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge20to24()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, tHPAge20to24);
        int tHPAge25to29
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge25to29()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, tHPAge25to29);
        int tHPAge30to44
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge30to34()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge30to34()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge35to39()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge35to39()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge40to44()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge40to44();
        tCAS001AgeCountHashMap.put(30, tHPAge30to44);
        int tHPAge45to59
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge45to49()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge45to49()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge50to54()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge50to54()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge55to59()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge55to59();
        tCAS001AgeCountHashMap.put(45, tHPAge45to59);
        int tHPAge60to64
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge60to64()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, tHPAge60to64);
        int tHPAge65to74
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge65to69()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge65to69()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge70to74()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(65, tHPAge65to74);
        int tHPAge75to84
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge75to79()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge75to79()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge80to84()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge80to84();
        tCAS001AgeCountHashMap.put(75, tHPAge75to84);
        int tHPAge85to89
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge85to89()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge85to89();
        tCAS001AgeCountHashMap.put(85, tHPAge85to89);
        int tHPAge90AndOver
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge90AndOver()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, tHPAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0,5,8,10,15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001HPAgeFemaleCount1_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(16);
        int tHPAge0to4
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge0to4();
        tCAS001AgeCountHashMap.put(0, tHPAge0to4);
        int tHPAge5to7
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge5()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge6()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge7();
        tCAS001AgeCountHashMap.put(5, tHPAge5to7);
        int tHPAge8to9
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge8()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge9();
        tCAS001AgeCountHashMap.put(8, tHPAge8to9);
        int tHPAge10to14
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge10to14();
        tCAS001AgeCountHashMap.put(10, tHPAge10to14);
        int tHPAge15
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge15();
        tCAS001AgeCountHashMap.put(15, tHPAge15);
        int tHPAge16to19
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge16()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge17()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge18()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge19();
        tCAS001AgeCountHashMap.put(16, tHPAge16to19);
        int tHPAge20to24
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge20to24();
        tCAS001AgeCountHashMap.put(20, tHPAge20to24);
        int tHPAge25to29
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge25to29();
        tCAS001AgeCountHashMap.put(25, tHPAge25to29);
        int tHPAge30to44
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge30to34()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge35to39()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge40to44();
        tCAS001AgeCountHashMap.put(30, tHPAge30to44);
        int tHPAge45to59
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge45to49()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge50to54()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge55to59();
        tCAS001AgeCountHashMap.put(45, tHPAge45to59);
        int tHPAge60to64
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge60to64();
        tCAS001AgeCountHashMap.put(60, tHPAge60to64);
        int tHPAge65to74
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge65to69()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge70to74();
        tCAS001AgeCountHashMap.put(65, tHPAge65to74);
        int tHPAge75to84
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge75to79()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge80to84();
        tCAS001AgeCountHashMap.put(75, tHPAge75to84);
        int tHPAge85to89
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge85to89();
        tCAS001AgeCountHashMap.put(85, tHPAge85to89);
        int tHPAge90AndOver
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, tHPAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0,5,8,10,15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001HPAgeMaleCount1_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(16);
        int tHPAge0to4
                = tCAS001DataRecord.getHouseholdResidentsMalesAge0to4();
        tCAS001AgeCountHashMap.put(0, tHPAge0to4);
        int tHPAge5to7
                = tCAS001DataRecord.getHouseholdResidentsMalesAge5()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge6()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge7();
        tCAS001AgeCountHashMap.put(5, tHPAge5to7);
        int tHPAge8to9
                = tCAS001DataRecord.getHouseholdResidentsMalesAge8()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge9();
        tCAS001AgeCountHashMap.put(8, tHPAge8to9);
        int tHPAge10to14
                = tCAS001DataRecord.getHouseholdResidentsMalesAge10to14();
        tCAS001AgeCountHashMap.put(10, tHPAge10to14);
        int tHPAge15
                = tCAS001DataRecord.getHouseholdResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(15, tHPAge15);
        int tHPAge16to19
                = tCAS001DataRecord.getHouseholdResidentsMalesAge16()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge17()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge18()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge19();
        tCAS001AgeCountHashMap.put(16, tHPAge16to19);
        int tHPAge20to24
                = tCAS001DataRecord.getHouseholdResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, tHPAge20to24);
        int tHPAge25to29
                = tCAS001DataRecord.getHouseholdResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, tHPAge25to29);
        int tHPAge30to44
                = tCAS001DataRecord.getHouseholdResidentsMalesAge30to34()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge35to39()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge40to44();
        tCAS001AgeCountHashMap.put(30, tHPAge30to44);
        int tHPAge45to59
                = tCAS001DataRecord.getHouseholdResidentsMalesAge45to49()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge50to54()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge55to59();
        tCAS001AgeCountHashMap.put(45, tHPAge45to59);
        int tHPAge60to64
                = tCAS001DataRecord.getHouseholdResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, tHPAge60to64);
        int tHPAge65to74
                = tCAS001DataRecord.getHouseholdResidentsMalesAge65to69()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(65, tHPAge65to74);
        int tHPAge75to84
                = tCAS001DataRecord.getHouseholdResidentsMalesAge75to79()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge80to84();
        tCAS001AgeCountHashMap.put(75, tHPAge75to84);
        int tHPAge85to89
                = tCAS001DataRecord.getHouseholdResidentsMalesAge85to89();
        tCAS001AgeCountHashMap.put(85, tHPAge85to89);
        int tHPAge90AndOver
                = tCAS001DataRecord.getHouseholdResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, tHPAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0-15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001HPAgeCount3_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(28);
        int tHPAge0
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge0()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge0();
        tCAS001AgeCountHashMap.put(0, tHPAge0);
        int tHPAge1
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge1()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge1();
        tCAS001AgeCountHashMap.put(1, tHPAge1);
        int tHPAge2
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge2()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge2();
        tCAS001AgeCountHashMap.put(2, tHPAge2);
        int tHPAge3
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge3()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge3();
        tCAS001AgeCountHashMap.put(3, tHPAge3);
        int tHPAge4
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge4()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge4();
        tCAS001AgeCountHashMap.put(4, tHPAge4);
        int tHPAge5
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge5()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge5();
        tCAS001AgeCountHashMap.put(5, tHPAge5);
        int tHPAge6
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge6()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge6();
        tCAS001AgeCountHashMap.put(6, tHPAge6);
        int tHPAge7
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge7()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge7();
        tCAS001AgeCountHashMap.put(7, tHPAge7);
        int tHPAge8
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge8()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge8();
        tCAS001AgeCountHashMap.put(8, tHPAge8);
        int tHPAge9
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge9()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge9();
        tCAS001AgeCountHashMap.put(9, tHPAge9);
        int tHPAge10
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge10()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge10();
        tCAS001AgeCountHashMap.put(10, tHPAge10);
        int tHPAge11
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge11()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge11();
        tCAS001AgeCountHashMap.put(11, tHPAge11);
        int tHPAge12
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge12()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge12();
        tCAS001AgeCountHashMap.put(12, tHPAge12);
        int tHPAge13
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge13()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge13();
        tCAS001AgeCountHashMap.put(13, tHPAge13);
        int tHPAge14
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge14()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge14();
        tCAS001AgeCountHashMap.put(14, tHPAge14);
        int tHPAge15
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge15()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(15, tHPAge15);
        int tHPAge16to19
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge16()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge16()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge17()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge17()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge18()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge18()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge19()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge19();
        tCAS001AgeCountHashMap.put(16, tHPAge16to19);
        int tHPAge20to24
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge20to24()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, tHPAge20to24);
        int tHPAge25to29
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge25to29()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, tHPAge25to29);
        int tHPAge30to44
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge30to34()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge30to34()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge35to39()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge35to39()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge40to44()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge40to44();
        tCAS001AgeCountHashMap.put(30, tHPAge30to44);
        int tHPAge45to59
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge45to49()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge45to49()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge50to54()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge50to54()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge55to59()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge55to59();
        tCAS001AgeCountHashMap.put(45, tHPAge45to59);
        int tHPAge60to64
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge60to64()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, tHPAge60to64);
        int tHPAge65to69
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge65to69()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge65to69();
        tCAS001AgeCountHashMap.put(65, tHPAge65to69);
        int tHPAge70to74
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge70to74()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(70, tHPAge70to74);
        int tHPAge75to79
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge75to79()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge75to79();
        tCAS001AgeCountHashMap.put(75, tHPAge75to79);
        int tHPAge80to84
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge80to84()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge80to84();
        tCAS001AgeCountHashMap.put(80, tHPAge80to84);
        int tHPAge85to89
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge85to89()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge85to89();
        tCAS001AgeCountHashMap.put(85, tHPAge85to89);
        int tHPAge90AndOver
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge90AndOver()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, tHPAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0-15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001HPAgeMaleCount3_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(28);
        int countAge0
                = tCAS001DataRecord.getHouseholdResidentsMalesAge0();
        tCAS001AgeCountHashMap.put(0, countAge0);
        int countAge1
                = tCAS001DataRecord.getHouseholdResidentsMalesAge1();
        tCAS001AgeCountHashMap.put(1, countAge1);
        int countAge2
                = tCAS001DataRecord.getHouseholdResidentsMalesAge2();
        tCAS001AgeCountHashMap.put(2, countAge2);
        int countAge3
                = tCAS001DataRecord.getHouseholdResidentsMalesAge3();
        tCAS001AgeCountHashMap.put(3, countAge3);
        int countAge4
                = tCAS001DataRecord.getHouseholdResidentsMalesAge4();
        tCAS001AgeCountHashMap.put(4, countAge4);
        int countAge5
                = tCAS001DataRecord.getHouseholdResidentsMalesAge5();
        tCAS001AgeCountHashMap.put(5, countAge5);
        int countAge6
                = tCAS001DataRecord.getHouseholdResidentsMalesAge6();
        tCAS001AgeCountHashMap.put(6, countAge6);
        int countAge7
                = tCAS001DataRecord.getHouseholdResidentsMalesAge7();
        tCAS001AgeCountHashMap.put(7, countAge7);
        int countAge8
                = tCAS001DataRecord.getHouseholdResidentsMalesAge8();
        tCAS001AgeCountHashMap.put(8, countAge8);
        int countAge9
                = tCAS001DataRecord.getHouseholdResidentsMalesAge9();
        tCAS001AgeCountHashMap.put(9, countAge9);
        int countAge10
                = tCAS001DataRecord.getHouseholdResidentsMalesAge10();
        tCAS001AgeCountHashMap.put(10, countAge10);
        int countAge11
                = tCAS001DataRecord.getHouseholdResidentsMalesAge11();
        tCAS001AgeCountHashMap.put(11, countAge11);
        int countAge12
                = tCAS001DataRecord.getHouseholdResidentsMalesAge12();
        tCAS001AgeCountHashMap.put(12, countAge12);
        int countAge13
                = tCAS001DataRecord.getHouseholdResidentsMalesAge13();
        tCAS001AgeCountHashMap.put(13, countAge13);
        int countAge14
                = tCAS001DataRecord.getHouseholdResidentsMalesAge14();
        tCAS001AgeCountHashMap.put(14, countAge14);
        int countAge15
                = tCAS001DataRecord.getHouseholdResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(15, countAge15);
        int countAge16to19
                = tCAS001DataRecord.getHouseholdResidentsMalesAge16()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge17()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge18()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge19();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getHouseholdResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getHouseholdResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getHouseholdResidentsMalesAge30to34()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge35to39()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge40to44();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getHouseholdResidentsMalesAge45to49()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge50to54()
                + tCAS001DataRecord.getHouseholdResidentsMalesAge55to59();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getHouseholdResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to69
                = tCAS001DataRecord.getHouseholdResidentsMalesAge65to69();
        tCAS001AgeCountHashMap.put(65, countAge65to69);
        int countAge70to74
                = tCAS001DataRecord.getHouseholdResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(70, countAge70to74);
        int countAge75to79
                = tCAS001DataRecord.getHouseholdResidentsMalesAge75to79();
        tCAS001AgeCountHashMap.put(75, countAge75to79);
        int countAge80to84
                = tCAS001DataRecord.getHouseholdResidentsMalesAge80to84();
        tCAS001AgeCountHashMap.put(80, countAge80to84);
        int countAge85to89
                = tCAS001DataRecord.getHouseholdResidentsMalesAge85to89();
        tCAS001AgeCountHashMap.put(85, countAge85to89);
        int countAge90AndOver
                = tCAS001DataRecord.getHouseholdResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, countAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0-15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001HPAgeFemaleCount3_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(28);
        int countAge0
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge0();
        tCAS001AgeCountHashMap.put(0, countAge0);
        int countAge1
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge1();
        tCAS001AgeCountHashMap.put(1, countAge1);
        int countAge2
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge2();
        tCAS001AgeCountHashMap.put(2, countAge2);
        int countAge3
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge3();
        tCAS001AgeCountHashMap.put(3, countAge3);
        int countAge4
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge4();
        tCAS001AgeCountHashMap.put(4, countAge4);
        int countAge5
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge5();
        tCAS001AgeCountHashMap.put(5, countAge5);
        int countAge6
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge6();
        tCAS001AgeCountHashMap.put(6, countAge6);
        int countAge7
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge7();
        tCAS001AgeCountHashMap.put(7, countAge7);
        int countAge8
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge8();
        tCAS001AgeCountHashMap.put(8, countAge8);
        int countAge9
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge9();
        tCAS001AgeCountHashMap.put(9, countAge9);
        int countAge10
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge10();
        tCAS001AgeCountHashMap.put(10, countAge10);
        int countAge11
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge11();
        tCAS001AgeCountHashMap.put(11, countAge11);
        int countAge12
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge12();
        tCAS001AgeCountHashMap.put(12, countAge12);
        int countAge13
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge13();
        tCAS001AgeCountHashMap.put(13, countAge13);
        int countAge14
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge14();
        tCAS001AgeCountHashMap.put(14, countAge14);
        int countAge15
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge15();
        tCAS001AgeCountHashMap.put(15, countAge15);
        int countAge16to19
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge16()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge17()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge18()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge19();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge30to34()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge35to39()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge40to44();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge45to49()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge50to54()
                + tCAS001DataRecord.getHouseholdResidentsFemalesAge55to59();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to69
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge65to69();
        tCAS001AgeCountHashMap.put(65, countAge65to69);
        int countAge70to74
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge70to74();
        tCAS001AgeCountHashMap.put(70, countAge70to74);
        int countAge75to79
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge75to79();
        tCAS001AgeCountHashMap.put(75, countAge75to79);
        int countAge80to84
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge80to84();
        tCAS001AgeCountHashMap.put(80, countAge80to84);
        int countAge85to89
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge85to89();
        tCAS001AgeCountHashMap.put(85, countAge85to89);
        int countAge90AndOver
                = tCAS001DataRecord.getHouseholdResidentsFemalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, countAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0,5,8,10,15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001CEPAgeCount1_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(15);
        int countAge0to4
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge0to4()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge0to4();
        tCAS001AgeCountHashMap.put(0, countAge0to4);
        int countAge5to7
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge5()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge5()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge6()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge6()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge7()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge7();
        tCAS001AgeCountHashMap.put(5, countAge5to7);
        int countAge8to9
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge8()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge8()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge9()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge9();
        tCAS001AgeCountHashMap.put(8, countAge8to9);
        int countAge10to14
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge10to14()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge10to14();
        tCAS001AgeCountHashMap.put(10, countAge10to14);
        int countAge15
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge15()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(15, countAge15);
        int countAge16to19
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge16()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge16()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge17()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge17()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge18()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge18()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge19()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge19();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge20to24()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge25to29()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge30to34()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge30to34()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge35to39()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge35to39()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge40to44()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge40to44();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge45to49()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge45to49()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge50to54()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge50to54()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge55to59()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge55to59();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge60to64()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to74
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge65to69()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge65to69()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge70to74()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(65, countAge65to74);
        int countAge75to84
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge75to79()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge75to79()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge80to84()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge80to84();
        tCAS001AgeCountHashMap.put(75, countAge75to84);
        int countAge85to89
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge85to89()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge85to89();
        tCAS001AgeCountHashMap.put(85, countAge85to89);
        int countAge90AndOver
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge90AndOver()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, countAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0,16,20,25,30,45,60,65,70,75; values are counts of the Communal
     * Establishment Population
     */
    public HashMap getCAS001CEPAgeCount2_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(9);
        int countAge0to15
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge0to4()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge0to4()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge5()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge5()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge6()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge6()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge7()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge7()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge8()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge8()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge9()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge9()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge10to14()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge10to14()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge15()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(0, countAge0to15);
        int countAge16to19
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge16()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge16()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge17()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge17()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge18()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge18()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge19()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge19();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge20to24()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge25to29()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge30to34()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge30to34()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge35to39()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge35to39()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge40to44()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge40to44();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge45to49()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge45to49()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge50to54()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge50to54()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge55to59()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge55to59();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge60to64()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to74
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge65to69()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge65to69()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge70to74()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(65, countAge65to74);
        int countAge75AndOver
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge75to79()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge75to79()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge80to84()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge80to84()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge85to89()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge85to89()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge90AndOver()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(75, countAge75AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0-15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001CEPAgeFemaleCount3_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(28);
        int countAge0
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge0();
        tCAS001AgeCountHashMap.put(0, countAge0);
        int countAge1
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge1();
        tCAS001AgeCountHashMap.put(1, countAge1);
        int countAge2
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge2();
        tCAS001AgeCountHashMap.put(2, countAge2);
        int countAge3
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge3();
        tCAS001AgeCountHashMap.put(3, countAge3);
        int countAge4
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge4();
        tCAS001AgeCountHashMap.put(4, countAge4);
        int countAge5
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge5();
        tCAS001AgeCountHashMap.put(5, countAge5);
        int countAge6
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge6();
        tCAS001AgeCountHashMap.put(6, countAge6);
        int countAge7
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge7();
        tCAS001AgeCountHashMap.put(7, countAge7);
        int countAge8
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge8();
        tCAS001AgeCountHashMap.put(8, countAge8);
        int countAge9
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge9();
        tCAS001AgeCountHashMap.put(9, countAge9);
        int countAge10
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge10();
        tCAS001AgeCountHashMap.put(10, countAge10);
        int countAge11
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge11();
        tCAS001AgeCountHashMap.put(11, countAge11);
        int countAge12
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge12();
        tCAS001AgeCountHashMap.put(12, countAge12);
        int countAge13
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge13();
        tCAS001AgeCountHashMap.put(13, countAge13);
        int countAge14
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge14();
        tCAS001AgeCountHashMap.put(14, countAge14);
        int countAge15
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge15();
        tCAS001AgeCountHashMap.put(15, countAge15);
        int countAge16to19
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge16();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge30to34();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge45to49();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to69
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge65to69();
        tCAS001AgeCountHashMap.put(65, countAge65to69);
        int countAge70to74
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge70to74();
        tCAS001AgeCountHashMap.put(70, countAge70to74);
        int countAge75to79
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge75to79();
        tCAS001AgeCountHashMap.put(75, countAge75to79);
        int countAge80to84
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge80to84();
        tCAS001AgeCountHashMap.put(80, countAge80to84);
        int countAge85to89
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge85to89();
        tCAS001AgeCountHashMap.put(85, countAge85to89);
        int countAge90AndOver
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, countAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0-15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001CEPAgeMaleCount3_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(28);
        int countAge0
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge0();
        tCAS001AgeCountHashMap.put(0, countAge0);
        int countAge1
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge1();
        tCAS001AgeCountHashMap.put(1, countAge1);
        int countAge2
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge2();
        tCAS001AgeCountHashMap.put(2, countAge2);
        int countAge3
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge3();
        tCAS001AgeCountHashMap.put(3, countAge3);
        int countAge4
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge4();
        tCAS001AgeCountHashMap.put(4, countAge4);
        int countAge5
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge5();
        tCAS001AgeCountHashMap.put(5, countAge5);
        int countAge6
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge6();
        tCAS001AgeCountHashMap.put(6, countAge6);
        int countAge7
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge7();
        tCAS001AgeCountHashMap.put(7, countAge7);
        int countAge8
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge8();
        tCAS001AgeCountHashMap.put(8, countAge8);
        int countAge9
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge9();
        tCAS001AgeCountHashMap.put(9, countAge9);
        int countAge10
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge10();
        tCAS001AgeCountHashMap.put(10, countAge10);
        int countAge11
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge11();
        tCAS001AgeCountHashMap.put(11, countAge11);
        int countAge12
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge12();
        tCAS001AgeCountHashMap.put(12, countAge12);
        int countAge13
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge13();
        tCAS001AgeCountHashMap.put(13, countAge13);
        int countAge14
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge14();
        tCAS001AgeCountHashMap.put(14, countAge14);
        int countAge15
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(15, countAge15);
        int countAge16to19
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge16();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge30to34();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge45to49();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to69
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge65to69();
        tCAS001AgeCountHashMap.put(65, countAge65to69);
        int countAge70to74
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(70, countAge70to74);
        int countAge75to79
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge75to79();
        tCAS001AgeCountHashMap.put(75, countAge75to79);
        int countAge80to84
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge80to84();
        tCAS001AgeCountHashMap.put(80, countAge80to84);
        int countAge85to89
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge85to89();
        tCAS001AgeCountHashMap.put(85, countAge85to89);
        int countAge90AndOver
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, countAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0-15,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the
     * Communal Establishment Population
     */
    public HashMap getCAS001CEPAgeCount3_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(28);
        int countAge0
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge0()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge0();
        tCAS001AgeCountHashMap.put(0, countAge0);
        int countAge1
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge1()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge1();
        tCAS001AgeCountHashMap.put(1, countAge1);
        int countAge2
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge2()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge2();
        tCAS001AgeCountHashMap.put(2, countAge2);
        int countAge3
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge3()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge3();
        tCAS001AgeCountHashMap.put(3, countAge3);
        int countAge4
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge4()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge4();
        tCAS001AgeCountHashMap.put(4, countAge4);
        int countAge5
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge5()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge5();
        tCAS001AgeCountHashMap.put(5, countAge5);
        int countAge6
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge6()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge6();
        tCAS001AgeCountHashMap.put(6, countAge6);
        int countAge7
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge7()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge7();
        tCAS001AgeCountHashMap.put(7, countAge7);
        int countAge8
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge8()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge8();
        tCAS001AgeCountHashMap.put(8, countAge8);
        int countAge9
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge9()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge9();
        tCAS001AgeCountHashMap.put(9, countAge9);
        int countAge10
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge10()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge10();
        tCAS001AgeCountHashMap.put(10, countAge10);
        int countAge11
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge11()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge11();
        tCAS001AgeCountHashMap.put(11, countAge11);
        int countAge12
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge12()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge12();
        tCAS001AgeCountHashMap.put(12, countAge12);
        int countAge13
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge13()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge13();
        tCAS001AgeCountHashMap.put(13, countAge13);
        int countAge14
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge14()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge14();
        tCAS001AgeCountHashMap.put(14, countAge14);
        int countAge15
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge15()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(15, countAge15);
        int countAge16to19
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge16()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge16()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge17()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge17()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge18()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge18()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge19()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge19();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge20to24()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge25to29()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge30to34()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge35to39()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge35to39()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge40to44()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge40to44();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge45to49()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge45to49()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge50to54()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge50to54()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge55to59()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge55to59();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge60to64()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to69
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge65to69()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge65to69();
        tCAS001AgeCountHashMap.put(65, countAge65to69);
        int countAge70to74
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge70to74()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(70, countAge70to74);
        int countAge75to79
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge75to79()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge75to79();
        tCAS001AgeCountHashMap.put(75, countAge75to79);
        int countAge80to84
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge80to84()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge80to84();
        tCAS001AgeCountHashMap.put(80, countAge80to84);
        int countAge85to89
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge85to89()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge85to89();
        tCAS001AgeCountHashMap.put(85, countAge85to89);
        int countAge90AndOver
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge90AndOver()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, countAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the Communal
     * Establishment Population
     */
    public HashMap getCAS001CEPAgeFemaleCount4_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(28);
        int countAge0to15
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge0to4()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge5to9()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge10to14()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge15();
        tCAS001AgeCountHashMap.put(0, countAge0to15);
        int countAge16to19
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge16();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge30to34();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge45to49();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to69
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge65to69();
        tCAS001AgeCountHashMap.put(65, countAge65to69);
        int countAge70to74
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge70to74();
        tCAS001AgeCountHashMap.put(70, countAge70to74);
        int countAge75to79
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge75to79();
        tCAS001AgeCountHashMap.put(75, countAge75to79);
        int countAge80to84
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge80to84();
        tCAS001AgeCountHashMap.put(80, countAge80to84);
        int countAge85to89
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge85to89();
        tCAS001AgeCountHashMap.put(85, countAge85to89);
        int countAge90AndOver
                = tCAS001DataRecord.getCommunalEstablishmentResidentsFemalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, countAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

    /**
     * @param tCAS001DataRecord
     * @return A HashMap with keys representing age categories:
     * 0,16,20,25,30,45,60,65,70,75,80,85,90; values are counts of the Communal
     * Establishment Population
     */
    public HashMap getCAS001CEPAgeMaleCount4_HashMap(
            Census_CAS001DataRecord tCAS001DataRecord) {
        HashMap tCAS001AgeCountHashMap = new HashMap(28);
        int countAge0to15
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge0to4()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge5to9()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge10to14()
                + tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge15();
        tCAS001AgeCountHashMap.put(0, countAge0to15);
        int countAge16to19
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge16();
        tCAS001AgeCountHashMap.put(16, countAge16to19);
        int countAge20to24
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge20to24();
        tCAS001AgeCountHashMap.put(20, countAge20to24);
        int countAge25to29
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge25to29();
        tCAS001AgeCountHashMap.put(25, countAge25to29);
        int countAge30to44
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge30to34();
        tCAS001AgeCountHashMap.put(30, countAge30to44);
        int countAge45to59
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge45to49();
        tCAS001AgeCountHashMap.put(45, countAge45to59);
        int countAge60to64
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge60to64();
        tCAS001AgeCountHashMap.put(60, countAge60to64);
        int countAge65to69
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge65to69();
        tCAS001AgeCountHashMap.put(65, countAge65to69);
        int countAge70to74
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge70to74();
        tCAS001AgeCountHashMap.put(70, countAge70to74);
        int countAge75to79
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge75to79();
        tCAS001AgeCountHashMap.put(75, countAge75to79);
        int countAge80to84
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge80to84();
        tCAS001AgeCountHashMap.put(80, countAge80to84);
        int countAge85to89
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge85to89();
        tCAS001AgeCountHashMap.put(85, countAge85to89);
        int countAge90AndOver
                = tCAS001DataRecord.getCommunalEstablishmentResidentsMalesAge90AndOver();
        tCAS001AgeCountHashMap.put(90, countAge90AndOver);
        return tCAS001AgeCountHashMap;
    }

}