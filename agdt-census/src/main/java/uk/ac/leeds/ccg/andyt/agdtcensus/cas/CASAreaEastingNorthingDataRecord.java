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
package uk.ac.leeds.ccg.andyt.agdtcensus.cas;

import java.io.IOException;
import java.io.RandomAccessFile;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_ErrorAndExceptionHandler;

public class CASAreaEastingNorthingDataRecord extends AbstractCASDataRecord {

    /*
     * Area is in hectares
     * Easting and Northing are as per the Ordnance Survey Great Britain
     * National Grid projection for England, Wales and Scotland.
     * Not sure about Northern Ireland...
     */
    /**
     * Area
     */
    protected double _Area;
    /**
     * Easting
     */
    protected double _Easting;
    /**
     * Northing
     */
    protected double _Northing;

    public CASAreaEastingNorthingDataRecord() {
        _Init();
    }

    public CASAreaEastingNorthingDataRecord(CASAreaEastingNorthingDataRecord a_CASAreaEastingNorthingDataRecord){
        init(a_CASAreaEastingNorthingDataRecord);
    }

    public CASAreaEastingNorthingDataRecord(boolean initZero) {
        initZero();
    }

    /**
     * @param RecordID
     *            The RecordID to be assigned to this
     * @param line
     *            The Comma Seperated Value <code>String</code>
     * @throws java.io.IOException
     */
    public CASAreaEastingNorthingDataRecord(
            long RecordID,
            String line)
            throws IOException {
        String[] fields = line.split(",");
        this._RecordID = RecordID;
        this.Zone_Code = fields[0].substring(1,11).toCharArray();
        // From Table KS002
        this._Area = new Double(fields[1]).doubleValue();
        this._Easting = new Double(fields[2]).doubleValue();
        this._Northing = new Double(fields[3]).doubleValue();
    }

    /**
     * @param aRandomAccessFile
     * @throws java.io.IOException
     */
    public CASAreaEastingNorthingDataRecord(
            RandomAccessFile aRandomAccessFile)
            throws IOException {
        this._RecordID = aRandomAccessFile.readLong();
        this.Zone_Code = new char[10];
        for (int i = 0; i < this.Zone_Code.length; i++) {
            Zone_Code[i] = aRandomAccessFile.readChar();
        }
        this._Area = aRandomAccessFile.readDouble();
        this._Easting = aRandomAccessFile.readDouble();
        this._Northing = aRandomAccessFile.readDouble();
    }

    /**
     * Initialises all fields.
     */
    @Override
    protected void _Init() {
        super._Init();
        this._Area = Double.MIN_VALUE;
        this._Easting = Double.MIN_VALUE;
        this._Northing = Double.MIN_VALUE;
    }

    /**
     * Initialises all fields.
     */
    protected void initZero() {
        super._Init();
        this._Area = 0;
        this._Easting = 0;
        this._Northing = 0;
    }

    protected void init(
            CASAreaEastingNorthingDataRecord a_CASAreaEastingNorthingDataRecord) {
        super.init(a_CASAreaEastingNorthingDataRecord);
        this._Area = a_CASAreaEastingNorthingDataRecord._Area;
        this._Easting = a_CASAreaEastingNorthingDataRecord._Easting;
        this._Northing = a_CASAreaEastingNorthingDataRecord._Northing;
    }

    /**
     * Returns a string description of this;
     * @return 
     */
    @Override
    public String toString() {
        return super.toString() + 
                ", _Area " + _Area +
                ", _Easting " + _Easting +
                ", _Northing " + _Northing;
    }

    /**
     * @return A Comma Seperated Version (CSV) <code>String</code> of the values
     *         of the <code>Fields</code> of <code>this</code>.
     */
    @Override
    public String toCSVString() {
        String result = super.toCSVString();
        result += "," + this._Area;
        result += "," + this._Easting;
        result += "," + this._Northing;
        return result;
    }

    /**
     * @return A Comma Seperated Version (CSV) <code>String</code> of the names
     *         of the <code>Fields</code> of <code>this</code>
     */
    @Override
    public String toCSVStringFields() {
        String result = super.toCSVStringFields();
        result += ",_Area";
        result += ",_Easting";
        result += ",_Northing";
        return result;
    }

    /**
     * Returns a copy of this._Area
     * @return 
     */
    public double get_Area() {
        return this._Area;
    }

        /**
     * Returns a copy of this._Easting
     * @return 
     */
    public double get_Easting() {
        return this._Easting;
    }

        /**
     * Returns a copy of this._Northing
     * @return 
     */
    public double get_Northing() {
        return this._Northing;
    }

    /**
     * Writes <code>this</code> to <code>aRandomAccessFile</code> at the current
     * position.
     *
     * @param aRandomAccessFile
     *            The <code>RandomAccessFile</code> this is written to.
     */
    @Override
    public void write(RandomAccessFile aRandomAccessFile) {
        super.write(aRandomAccessFile);
        write(
                aRandomAccessFile,
                true);
    }

    /**
     * Writes <code>this</code> to <code>aRandomAccessFile</code> at the current
     * position.
     *
     * @param aRandomAccessFile
     *            The <code>RandomAccessFile</code> this is written to.
     * @param avoidCallToSuper
     *            If true super.write() is not called
     */
    public void write(
            RandomAccessFile aRandomAccessFile,
            boolean avoidCallToSuper) {
        if (avoidCallToSuper) {
            try {
                aRandomAccessFile.writeDouble(_Area);
                aRandomAccessFile.writeDouble(_Easting);
                aRandomAccessFile.writeDouble(_Northing);
            } catch (IOException aIOException) {
                System.err.println(aIOException.getLocalizedMessage());
                System.exit(Generic_ErrorAndExceptionHandler.IOException);
            }
        } else {
            write(aRandomAccessFile);
        }
    }

    /**
     * Returns the size of this in bytes as a long. 
     * @return 
     */
    @Override
    public long getSizeInBytes() {
        long result = super.getSizeInBytes();
        result += (3L * (long) Double.SIZE) / getNumberOfBitsInByte();
        return result;
    }
}
