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
package uk.ac.leeds.ccg.andyt.agdtcensus.sar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import uk.ac.leeds.ccg.andyt.generic.execution.AgeConverter;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.io.AbstractDataHandler;
import uk.ac.leeds.ccg.andyt.generic.io.AbstractDataRecord;

/**
 * For accessing HSARDataRecords and information about them.
 */
public class HSARDataHandler extends AbstractDataHandler {

    /**
     * For storing all HSARDataRecords
     */
    protected HSARDataRecord[] _HSARDataRecordArray;
    /**
     * A collection providing a convenient way to access data for specific
     * AgeSex classes for HRPs. Keys are AgeSex. Values are Vectors of
     * HSARDataRecords.
     */
    protected HashMap _AgeSexHRP_HSARDataRecordVector_HashMap;

    /**
     * A collection providing a convenient way to access all other
     * HSARDataRecords for a household with a given Household ID. Keys are
     * integer HID. Values are Vectors (of HSARDataRecords) with the first being
     * the Household Reference Person.
     */
    protected HashMap _HID_HSARDataRecordVector_HashMap;

    /** Creates a new instance of HSARDataHandler */
    public HSARDataHandler() {
    }

    /**
     * Creates a new instance of HSARDataHandler from aFile.
     * @param aFile
     */
    public HSARDataHandler(
            File aFile) {
        _Directory = aFile.getParentFile();
        init(_Directory);
        if (aFile.getName().endsWith(".dat")) {
            init(aFile.getParentFile());
            load(aFile);
            this._RecordLength = new ISARDataRecord().getSizeInBytes();
            loadIntoCache();
            File thisFile = new File(
                    _Directory,
                    this.getClass().getCanonicalName() + ".thisFile");
            Generic_StaticIO.writeObject(
                    this,
                    thisFile);
        } else {
            Object object = Generic_StaticIO.readObject(aFile);
            HSARDataHandler aHSARDataHandler = (HSARDataHandler) object;
            load(aFile);
            this._RecordLength = aHSARDataHandler._RecordLength;
            //this._RecordLength = new ISARDataRecord().getSizeInBytes();
            this._HSARDataRecordArray = aHSARDataHandler._HSARDataRecordArray;
            this._AgeSexHRP_HSARDataRecordVector_HashMap = aHSARDataHandler._AgeSexHRP_HSARDataRecordVector_HashMap;
            this._HID_HSARDataRecordVector_HashMap = aHSARDataHandler._HID_HSARDataRecordVector_HashMap;
        }
    }

    /**
     * Loads from source file
     * @param sourceFile
     * @param formattedFile
     * @throws java.io.IOException
     */
    public void formatSource(
            File sourceFile,
            File formattedFile)
            throws IOException {
        _Logger.entering(this.getClass().getCanonicalName(), "formatSource(File,File)");
        _File = formattedFile;
//        _File = new File (
//                _Directory,
//                HSARDataRecords.dat);
        if (!_File.exists()) {
            this._File.createNewFile();
        }
        this._RandomAccessFile = new RandomAccessFile(this._File, "rw");
        //File sourceFile = new File(
        //        "C:/Work/data/Census/2001/SAR/household/5278TAB/UKDA-5278-tab/tab/lichhd-051019.tab");
        BufferedReader aBufferedReader =
                new BufferedReader(
                new InputStreamReader(
                new FileInputStream(sourceFile)));
        StreamTokenizer aStreamTokenizer =
                new StreamTokenizer(aBufferedReader);
        Generic_StaticIO.setStreamTokenizerSyntax2(aStreamTokenizer);
        String line;
        long RecordID = 0L;
        HSARDataRecord aHSARDataRecord = new HSARDataRecord();
        // Skip the first line
        int tokenType = aStreamTokenizer.nextToken();
        while (tokenType != StreamTokenizer.TT_EOL) {
            tokenType = aStreamTokenizer.nextToken();
        }
        tokenType = aStreamTokenizer.nextToken();
        boolean parsed = false;
        while (tokenType != StreamTokenizer.TT_EOF) {
            switch (tokenType) {
                case StreamTokenizer.TT_EOL:
                    if (RecordID % 10000 == 0) {
                        log(aHSARDataRecord.toString());
                    }
                    // Write out householdSARRecord
                    if (parsed) {
                        aHSARDataRecord.write(this._RandomAccessFile);
                        // log( "this.tRandomAccessFile.length() " +
                        // this.tRandomAccessFile.length() );
                        RecordID++;
                    }
                    break;
                case StreamTokenizer.TT_WORD:
                    line = aStreamTokenizer.sval;
                    parsed = aHSARDataRecord.parse(
                            RecordID,
                            line);
                    break;
            }
            tokenType = aStreamTokenizer.nextToken();
        }
        log("Number of HSARDataRecords loaded " + (RecordID + 1L));
        this._RandomAccessFile.close();
        this._RandomAccessFile = new RandomAccessFile(this._File, "r");
        _Logger.exiting(this.getClass().getCanonicalName(), "formatSource(File,File)");
    }

    /**
     * Loads this.tAGEHHRPVectors and this.tHouseholdVectors
     */
    private void initVectors() {
        this._HID_HSARDataRecordVector_HashMap = new HashMap();
        this._AgeSexHRP_HSARDataRecordVector_HashMap = new HashMap();
        AgeSex aAgeSex;
        HSARDataRecord aHSARDataRecord;
        long nDataRecords = getNDataRecords();
        boolean HRP;
        int HHID;
        for (long RecordID = 0L; RecordID < nDataRecords; RecordID++) {
            aHSARDataRecord = getHSARDataRecord(RecordID);
            HRP = aHSARDataRecord.get_HRP();
            if (HRP) {
                aAgeSex = new AgeSex(
                        aHSARDataRecord.get_AGEH(),
                        aHSARDataRecord.get_SEX());
                if (this._AgeSexHRP_HSARDataRecordVector_HashMap.containsKey(aAgeSex)) {
                    ((Vector) this._AgeSexHRP_HSARDataRecordVector_HashMap.get(aAgeSex)).add(aHSARDataRecord);
                } else {
                    Vector aHSARDataRecordsVector = new Vector();
                    aHSARDataRecordsVector.add(aHSARDataRecord);
                    this._AgeSexHRP_HSARDataRecordVector_HashMap.put(
                            aAgeSex,
                            aHSARDataRecordsVector);
                }
            }
            HHID = aHSARDataRecord.get_HHID();
            if (this._HID_HSARDataRecordVector_HashMap.containsKey(HHID)) {
                // Add HRP at the start
                if (HRP) {
                    ((Vector) this._HID_HSARDataRecordVector_HashMap.get(HHID)).add(
                            0,
                            aHSARDataRecord);
                } else {
                    ((Vector) this._HID_HSARDataRecordVector_HashMap.get(HHID)).add(
                            aHSARDataRecord);
                }
            } else {
                Vector aHSARDataRecordsVector = new Vector();
                aHSARDataRecordsVector.add(aHSARDataRecord);
                this._HID_HSARDataRecordVector_HashMap.put(
                        HHID,
                        aHSARDataRecordsVector);
            }
        }
    //log("this.tHouseholdVectors.size() " + this._HID_HSARDataRecordsVector_HashMap.size());
    }

    /**
     * Randomly prints out 20 HSAR records
     */
    private void printHSARDataRecords(
            Random aRandom,
            int numberOfRecords) {
        HSARDataRecord aHSARDataRecord = new HSARDataRecord();
        int ID = Integer.MIN_VALUE;
        int Age;
        short aAgeClassHSARDataRecord;
        boolean Sex;
        AgeSex aAgeSex;
        for (int i = 0; i < numberOfRecords; i++) {
            Age = (short) aRandom.nextInt(100);
            aAgeClassHSARDataRecord = AgeConverter.getAgeClassHSARDataRecord(Age);
            Sex = aRandom.nextBoolean();
            aAgeSex = new AgeSex(
                    aAgeClassHSARDataRecord,
                    Sex);
            aHSARDataRecord = getHSARDataRecord(
                    aRandom,
                    aAgeSex);
            if (aHSARDataRecord != null) {
                log(aHSARDataRecord.toString());
            }
        }
    }

    /**
     * Loads HSARDataRecords into the cache.
     */
    public void loadIntoCache() {
        _Logger.entering(this.getClass().getCanonicalName(), "loadIntoCache()");
        long nDataRecords = super.getNDataRecords();
        if (nDataRecords > Integer.MAX_VALUE) {
            log("nDataRecords>Integer.MAX_VALUE");
            System.exit(4);
        }
        this._HSARDataRecordArray = new HSARDataRecord[(int) nDataRecords];
        try {
            this._RandomAccessFile.seek(0);
        } catch (IOException ioe0) {
            ioe0.printStackTrace();
        }
        for (int _HSARRecordID = 0; _HSARRecordID < nDataRecords; _HSARRecordID++) {
            this._HSARDataRecordArray[_HSARRecordID] = new HSARDataRecord(
                    this._RandomAccessFile);
            if (_HSARRecordID % 10000 == 0) {
                log("loadIntoCache " + _HSARRecordID);
            }
        }
        initVectors();
        _Logger.exiting(this.getClass().getCanonicalName(), "loadIntoCache()");
    }

    /**
     * @param aRecordID
     * The RecordID of the HSARDataRecord to be returned.
     * @return 
     */
    public AbstractDataRecord getDataRecord(long aRecordID) {
        return getHSARDataRecord(aRecordID);
    }

    /**
     * @param aRecordID
     * The RecordID of the HSARDataRecord to be returned.
     * @return 
     */
    public HSARDataRecord getHSARDataRecord(long aRecordID) {
        try {
            return this._HSARDataRecordArray[(int) aRecordID];
        } catch (NullPointerException aNullPointerException) {
            try {
                this._RandomAccessFile.seek(_RecordLength * aRecordID);
                return new HSARDataRecord(this._RandomAccessFile);
            } catch (IOException ioe0) {
                ioe0.printStackTrace();
                return null;
            }
        }
    }

    /**
     * @return A pseudo random HRP HSARDataRecord.
     * @param aRandom
     * The Random used to select.
     */
    public HSARDataRecord getHSARDataRecord(
            Random aRandom) {
        int age;
        short aAgeClassHSARDataRecord;
        HSARDataRecord aHSARDataRecord;
        boolean sex;
        AgeSex aAgeSex;
        do {
            age = aRandom.nextInt(100);
            aAgeClassHSARDataRecord = AgeConverter.getAgeClassHSARDataRecord(age);
            sex = aRandom.nextBoolean();
            aAgeSex = new AgeSex(
                    aAgeClassHSARDataRecord,
                    sex);
            aHSARDataRecord = getHSARDataRecord(aRandom, aAgeSex);
        } while (aHSARDataRecord == null);
        return aHSARDataRecord;
    }

    /**
     * @return A Vector of HSARDataRecords for the Household which contains
     * aHRPHSARDataRecord.
     * @param aHRPHSARDataRecord
     */
    public Vector getHSARDataRecordsVector(
            HSARDataRecord aHRPHSARDataRecord) {
        if (this._HID_HSARDataRecordVector_HashMap.containsKey(aHRPHSARDataRecord.get_HHID())) {
            return (Vector) this._HID_HSARDataRecordVector_HashMap.get(aHRPHSARDataRecord.get_HHID());
        } else {
            log("HSARDataRecord" + aHRPHSARDataRecord.toString() + "is not an HRP!");
            return (Vector) null;
        }
    }

    /**
     * @return A Vector of Vectors of HSARDataRecords for the Households with
     *         HRP as in aHRPHSARDataRecordsHashSet
     * @param aHRPHSARDataRecordsHashSet
     *            A HashSet of HSARDataRecords for HRP for the HSARDataRecords
     *            returned in the Vector of Vectors
     */
    public Vector getHSARDataRecordsVectors(
            HashSet aHRPHSARDataRecordsHashSet) {
        Vector result = new Vector(aHRPHSARDataRecordsHashSet.size());
        Iterator aHRPHSARDataRecordsHashSetIterator = aHRPHSARDataRecordsHashSet.iterator();
        Vector aHousehold;
        while (aHRPHSARDataRecordsHashSetIterator.hasNext()) {
            aHousehold = getHSARDataRecordsVector((HSARDataRecord) aHRPHSARDataRecordsHashSetIterator.next());
            result.add(aHousehold);
        }
        return result;
    }

    /**
     * @param aHPHRPHSARDataRecordsHashSet
     * @return All <code>HSARDataRecords</code> in a HashSet for those HRP in
     *         aHPHRPHSARDataRecordsHashSet
     */
    public HashSet getHSARDataRecordsHashSet(
            HashSet aHPHRPHSARDataRecordsHashSet) {
        // Get all Householders
        HashSet tHSARDataRecords = new HashSet();
        HSARDataRecord aHRPHSARDataRecord;
        Vector aHousehold;
        Iterator tIterator = aHPHRPHSARDataRecordsHashSet.iterator();
        while (tIterator.hasNext()) {
            aHRPHSARDataRecord = (HSARDataRecord) tIterator.next();
            aHousehold = getHSARDataRecordsVector(aHRPHSARDataRecord);
            tHSARDataRecords.addAll(aHousehold);
        }
        return tHSARDataRecords;
    }

    /**
     * @param tHPHRPs
     * @return All <code>HSARDataRecords</code> for the Households with HRPs as
     *         in HSARDataRecord[] tHPHRPs
     */
    public Vector getHSARDataRecords(HSARDataRecord[] tHPHRPs) {
        // Get all Householders
        Vector result = new Vector();
        Vector aHousehold;
        for (int i = 0; i < tHPHRPs.length; i++) {
            // log("i " + i + " from " + tHPHRPs.length );
            if (tHPHRPs[i] == null) {
                boolean debug = true;
                log("Null in getHSARDataRecords(HSARDataRecord[])!");
            } else {
                aHousehold = getHSARDataRecordsVector(tHPHRPs[i]);
                result.addAll(aHousehold);
            }
        }
        return result;
    }

    /**
     * @param tHPHRPRecordIDs
     * @return All <code>HSARDataRecords</code> for the Households with HRPs as
     *         in Vector tHPHRPs
     */
    public Vector getHSARDataRecords(Vector tHPHRPRecordIDs) {
        // Get all Householders
        Vector result = new Vector();
        Vector aHousehold;
        for (int i = 0; i < tHPHRPRecordIDs.size(); i++) {
            // log("i " + i + " from " + tHPHRPs.length );
            if (tHPHRPRecordIDs.get(i) == null) {
                log("Null in getHSARDataRecords(Vector)!");
            } else {
                aHousehold = getHSARDataRecordsVector(getHSARDataRecord((Long) tHPHRPRecordIDs.get(i)));
                result.addAll(aHousehold);
            }
        }
        return result;
    }

    /**
     * @param aRandom
     * @param aAgeSex
     * @return A pseudo random HSARDataRecord with AgeSex as in aAgeSex.
     */
    public HSARDataRecord getHSARDataRecord(
            Random aRandom,
            AgeSex aAgeSex) {
        Object value = this._AgeSexHRP_HSARDataRecordVector_HashMap.get(aAgeSex);
        if (value != null) {
            Vector aHSARDataRecordVector = (Vector) value;
            if (aHSARDataRecordVector.size() > 0) {
                return (HSARDataRecord) aHSARDataRecordVector.get(
                        aRandom.nextInt(aHSARDataRecordVector.size()));
            }
        }
        return null;
    }

    public HashMap get_AgeSexHRP_HSARDataRecordVector_HashMap() {
        return _AgeSexHRP_HSARDataRecordVector_HashMap;
    }

    public HashMap get_HID_HSARDataRecordVector_HashMap() {
        if (_HID_HSARDataRecordVector_HashMap == null){
            initVectors();
        }
        return _HID_HSARDataRecordVector_HashMap;
    }

    /**
     * Method to be used to look up ISARDataRecord from ISARDataRecord._ID.
     * @return a HashMap for looking up RecordID from ID
     */
    public HashMap get_ID_RecordID_HashMap() {
        HashMap tID_RecordID_HashMap = new HashMap();
        HSARDataRecord aHSARDataRecord;
        for (long RecordID = 0; RecordID < getNDataRecords(); RecordID++) {
            aHSARDataRecord = getHSARDataRecord(RecordID);
            tID_RecordID_HashMap.put(
                    aHSARDataRecord.get_ID(),
                    RecordID);
        }
        return tID_RecordID_HashMap;
    }

    @Override
    public long getNDataRecords() {
        return this._HSARDataRecordArray.length;
    }

    /**
     * A simple class for distinguishing HSARDataRecord with _HRP = true into
     * those with the same of _AGEH, _Sex
     */
    public class AgeSex
            implements Serializable, Comparable {

        /**
         * For storing _AGE
         */
        protected short _Age;
        /**
         * For storing the _SEX
         */
        protected boolean _Sex;

        public AgeSex() {
        }

        public AgeSex(
                short _Age,
                boolean _Sex) {
            this._Age = _Age;
            this._Sex = _Sex;
        }

        public AgeSex(
                AgeSex aAgeSex) {
            this._Age = aAgeSex._Age;
            this._Sex = aAgeSex._Sex;
        }

        public AgeSex(
                HSARDataRecord aHSARDataRecord) {
            _Age = aHSARDataRecord._AGEH;
            _Sex = aHSARDataRecord._SEX;
        }

        public short get_Age() {
            return _Age;
        }

        public boolean is_Sex() {
            return _Sex;
        }

        @Override
        public boolean equals(Object o) {
            return (compareTo(o) == 0);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + this._Age;
            hash = 89 * hash + (this._Sex ? 1 : 0);
            return hash;
        }

        public int compareTo(Object o) {
            if (o != null) {
                if (o instanceof AgeSex) {
                    AgeSex aAgeSex = (AgeSex) o;
                    if (aAgeSex._Age > _Age) {
                        return 1;
                    }
                    if (aAgeSex._Sex == true && _Sex == false) {
                        return 1;
                    }
                    if (aAgeSex._Sex == false && _Sex == true) {
                        return -1;
                    }
                    return 0;
                }
            }
            return -1;
        }
    }
}