/*
 * This file is generated by jOOQ.
*/
package db.jooq.generated.automationDb.tables.records;


import db.jooq.generated.automationDb.tables.Properties;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PropertiesRecord extends UpdatableRecordImpl<PropertiesRecord> implements Record6<Integer, Integer, String, String, String, String> {

    private static final long serialVersionUID = 753683705;

    /**
     * Setter for <code>automation.PROPERTIES.PROPERTYID</code>.
     */
    public void setPropertyid(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>automation.PROPERTIES.PROPERTYID</code>.
     */
    public Integer getPropertyid() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>automation.PROPERTIES.GUIMAPID</code>.
     */
    public void setGuimapid(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>automation.PROPERTIES.GUIMAPID</code>.
     */
    public Integer getGuimapid() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>automation.PROPERTIES.STANDARDCLASS</code>.
     */
    public void setStandardclass(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>automation.PROPERTIES.STANDARDCLASS</code>.
     */
    public String getStandardclass() {
        return (String) get(2);
    }

    /**
     * Setter for <code>automation.PROPERTIES.MAPPEDCLASS</code>.
     */
    public void setMappedclass(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>automation.PROPERTIES.MAPPEDCLASS</code>.
     */
    public String getMappedclass() {
        return (String) get(3);
    }

    /**
     * Setter for <code>automation.PROPERTIES.LOCATORVALUE</code>.
     */
    public void setLocatorvalue(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>automation.PROPERTIES.LOCATORVALUE</code>.
     */
    public String getLocatorvalue() {
        return (String) get(4);
    }

    /**
     * Setter for <code>automation.PROPERTIES.LOCATORTYPE</code>.
     */
    public void setLocatortype(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>automation.PROPERTIES.LOCATORTYPE</code>.
     */
    public String getLocatortype() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, Integer, String, String, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, Integer, String, String, String, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Properties.PROPERTIES.PROPERTYID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return Properties.PROPERTIES.GUIMAPID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Properties.PROPERTIES.STANDARDCLASS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Properties.PROPERTIES.MAPPEDCLASS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Properties.PROPERTIES.LOCATORVALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Properties.PROPERTIES.LOCATORTYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getPropertyid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getGuimapid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getStandardclass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getMappedclass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getLocatorvalue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getLocatortype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getPropertyid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getGuimapid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getStandardclass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getMappedclass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getLocatorvalue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getLocatortype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesRecord value1(Integer value) {
        setPropertyid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesRecord value2(Integer value) {
        setGuimapid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesRecord value3(String value) {
        setStandardclass(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesRecord value4(String value) {
        setMappedclass(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesRecord value5(String value) {
        setLocatorvalue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesRecord value6(String value) {
        setLocatortype(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesRecord values(Integer value1, Integer value2, String value3, String value4, String value5, String value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PropertiesRecord
     */
    public PropertiesRecord() {
        super(Properties.PROPERTIES);
    }

    /**
     * Create a detached, initialised PropertiesRecord
     */
    public PropertiesRecord(Integer propertyid, Integer guimapid, String standardclass, String mappedclass, String locatorvalue, String locatortype) {
        super(Properties.PROPERTIES);

        set(0, propertyid);
        set(1, guimapid);
        set(2, standardclass);
        set(3, mappedclass);
        set(4, locatorvalue);
        set(5, locatortype);
    }
}
