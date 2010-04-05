/*
 * JGrass - Free Open Source Java GIS http://www.jgrass.org 
 * (C) HydroloGIS - www.hydrologis.com 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.hydrologis.edc.annotatedclasses;

import static eu.hydrologis.edc.utils.Constants.EDC_SCHEMA;
import static eu.hydrologis.edc.utils.Constants.ENDDAY;
import static eu.hydrologis.edc.utils.Constants.ENDMONTH;
import static eu.hydrologis.edc.utils.Constants.ID;
import static eu.hydrologis.edc.utils.Constants.OID;
import static eu.hydrologis.edc.utils.Constants.PERMISSIONSCONTROLLEVEL;
import static eu.hydrologis.edc.utils.Constants.STARTDAY;
import static eu.hydrologis.edc.utils.Constants.STARTMONTH;
import static eu.hydrologis.edc.utils.Constants.UNITS_ID;
import static eu.hydrologis.edc.utils.Constants.VALUE;
import static eu.hydrologis.edc.utils.Constants.VALUEDESCRIPTION_ID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The Reservoirs Control Levels Table.
 * 
 * <p>
 * This table holds the control levels of the reservoirs.
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Silvia Franceschi (www.hydrologis.com)
 */
@Entity
@Table(name = PERMISSIONSCONTROLLEVEL, schema = EDC_SCHEMA)
public class PermissionsControlLevelsTable {

    /**
     * The unique id of the permissionscontrollevel record.
     */
    @Id
    @Column(name = OID, nullable = false)
    private Long oid;

    /**
     * The id of the permissionscontrollevel set.
     */
    @Column(name = ID, nullable = false)
    private Long id;

    /**
     * The start month for the level.
     */
    @Column(name = STARTMONTH, nullable = false)
    private Integer startMonth;

    /**
     * The start day for the level.
     */
    @Column(name = STARTDAY, nullable = false)
    private Integer startDay;

    /**
     * The end month for the level.
     */
    @Column(name = ENDMONTH, nullable = false)
    private Integer endMonth;

    /**
     * The end day for the level.
     */
    @Column(name = ENDDAY, nullable = false)
    private Integer endDay;

    /**
     * The level value.
     */
    @Column(name = VALUE, nullable = false)
    private Double value;

    /**
     * The aggregation type.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = VALUEDESCRIPTION_ID, referencedColumnName = ID, nullable = false)
    private ValueDescriptionTable valueDescription;

    /**
     * The value's unit.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = UNITS_ID, referencedColumnName = ID, nullable = false)
    private UnitsTable unit;

    /*
     * ==================================
     */

    public void setOid( Long oid ) {
        this.oid = oid;
    }

    public Long getOid() {
        return oid;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth( Integer startMonth ) {
        this.startMonth = startMonth;
    }

    public Integer getStartDay() {
        return startDay;
    }

    public void setStartDay( Integer startDay ) {
        this.startDay = startDay;
    }

    public Integer getEndMonth() {
        return endMonth;
    }

    public void setEndMonth( Integer endMonth ) {
        this.endMonth = endMonth;
    }

    public Integer getEndDay() {
        return endDay;
    }

    public void setEndDay( Integer endDay ) {
        this.endDay = endDay;
    }

    public Double getValue() {
        return value;
    }

    public void setValue( Double value ) {
        this.value = value;
    }

    public ValueDescriptionTable getValueDescription() {
        return valueDescription;
    }

    public void setValueDescription( ValueDescriptionTable valueDescription ) {
        this.valueDescription = valueDescription;
    }

    public void setUnit( UnitsTable unit ) {
        this.unit = unit;
    }

    public UnitsTable getUnit() {
        return unit;
    }

}