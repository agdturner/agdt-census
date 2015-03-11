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

/**
 * A <code>class</code> collection of the following
 * <code>AbstractCASDataRecords</code>:
 * <ul>
 * <li>CAS001DataRecord</li>
 * <li>CAS002DataRecord</li>
 * <li>CAS003DataRecord</li>
 * <li>CASKS002DataRecord</li>
 * <li>CASKS006DataRecord</li>
 * <li>CASKS008DataRecord</li>
 * <li>CASKS010DataRecord</li>
 * <li>CASKS017DataRecord</li>
 * <li>CASKS020DataRecord</li>
 * <li>CASKS09bDataRecord</li>
 * <li>CASKS09cDataRecord</li>
 * <li>CASKS12bDataRecord</li>
 * <li>CASKS12cDataRecord</li>
 * <li>CASKS14bDataRecord</li>
 * <li>CASKS14cDataRecord</li>
 * </ul>
 * <ul>
 * <li>Developed for <a href="http://www.ncess.ac.uk/moses">MoSeS</a>.</li>
 * <li>Copyright (C) 2006 <a
 * href="http://www.geog.leeds.ac.uk/people/a.turner/">Andy Turner</a>, <a
 * href="http://www.leeds.ac.uk//">University of Leeds</a>.</li>
 * </ul>
 * 
 * @author <a href="http://www.geog.leeds.ac.uk/people/a.turner/">Andy
 *         Turner</a>
 * @version 1.0.0, 2007-11-16
 * @see CASDataHandler
 */
public class CASDataRecord_2 extends CASDataRecord {

	/**
	 * Serializable class version number for swapping
	 * (serialization/deserialization)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * CASKS006DataRecord
	 */
	protected CASKS006DataRecord _CASKS006DataRecord;

	/** Creates a new CASDataRecord_2 */
	public CASDataRecord_2() {
		_Init();
	}

	/** Creates a new CASDataRecord_2
     * @param _CASDataHandler
     * @param tRecordID */
	public CASDataRecord_2(
            CASDataHandler _CASDataHandler,
            long tRecordID) {
		init(_CASDataHandler, tRecordID);
	}

	/**
	 * Initialises all fields.
	 */
	protected void _Init() {
		super._Init();
		this.tCASKS006DataRecord = new CASKS006DataRecord();
	}

	/**
	 * Initialises all fields.
     * @param _CASDataHandler_2
     * @param _RecordID
     * @throws java.io.IOException
	 */
	protected void init(CASDataHandler_2 _CASDataHandler_2, long _RecordID)
			throws IOException {
		super.init(_CASDataHandler_2, _RecordID);
		this._CASKS006DataRecord = (CASKS006DataRecord) _CASDataHandler_2
				.getCASKS006DataHandler().getDataRecord(_RecordID);
	}

	/**
	 * Returns a string description of this;
     * @return 
	 */
	public String toString() {
		return super.toString() + "CAS006DataRecord "
				+ this._CASKS006DataRecord.toString();
	}

	/**
	 * Returns the size of this CASDataRecord in bytes as a long. This does not
	 * account for serialVersionUID. A boolean is assumed to be the same size as
	 * an int in bytes.
     * @return 
	 */
	public long getSizeInBytes() {
		return super.getSizeInBytes()
				+ this._CASKS006DataRecord.getSizeInBytes();
	}

	/**
	 * Returns this._CASKS006DataRecord
     * @return 
	 */
	public CASKS006DataRecord getCASKS006DataRecord() {
		return this._CASKS006DataRecord;
	}

	// /**
	// * Writes <code>this</code> to <code>aRandomAccessFile</code> at the
	// current position.
	// * @param aRandomAccessFile The <code>RandomAccessFile</code> this is
	// written to.
	// */
	// @Override public void write( RandomAccessFile aRandomAccessFile ) throws
	// IOException {
	// super.write( aRandomAccessFile );
	// write( aRandomAccessFile, false );
	// }
	//    
	// /**
	// * Writes <code>this</code> to <code>aRandomAccessFile</code> at the
	// current position.
	// * @param aRandomAccessFile The <code>RandomAccessFile</code> this is
	// written to.
	// * @param avoidCallToSuper If true super.write() is not called
	// */
	// public void write(
	// RandomAccessFile aRandomAccessFile,
	// boolean avoidCallToSuper )
	// throws IOException {
	// if ( avoidCallToSuper ) {
	// } else {
	// write( aRandomAccessFile );
	// }
	// }
}
