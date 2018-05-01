/*
 * This file is generated by jOOQ.
*/
package db.jooq.generated.automationDb.tables.records;


import db.jooq.generated.automationDb.tables.Propsview;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.TableRecordImpl;


/**
 * VIEW
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.0"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PropsviewRecord extends TableRecordImpl<PropsviewRecord> implements Record8<String, Integer, String, String, String, String, String, String> {

    private static final long serialVersionUID = 1612815604;

    /**
     * Setter for <code>automation.PROPSVIEW.PAGENAME</code>.
     */
    public void setPagename(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>automation.PROPSVIEW.PAGENAME</code>.
     */
    public String getPagename() {
        return (String) get(0);
    }

    /**
     * Setter for <code>automation.PROPSVIEW.GUIMAPID</code>.
     */
    public void setGuimapid(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>automation.PROPSVIEW.GUIMAPID</code>.
     */
    public Integer getGuimapid() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>automation.PROPSVIEW.MAPPEDCLASS</code>.
     */
    public void setMappedclass(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>automation.PROPSVIEW.MAPPEDCLASS</code>.
     */
    public String getMappedclass() {
        return (String) get(2);
    }

    /**
     * Setter for <code>automation.PROPSVIEW.CONTROLNAME</code>.
     */
    public void setControlname(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>automation.PROPSVIEW.CONTROLNAME</code>.
     */
    public String getControlname() {
        return (String) get(3);
    }

    /**
     * Setter for <code>automation.PROPSVIEW.CONTROLDESCRIPTION</code>.
     */
    public void setControldescription(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>automation.PROPSVIEW.CONTROLDESCRIPTION</code>.
     */
    public String getControldescription() {
        return (String) get(4);
    }

    /**
     * Setter for <code>automation.PROPSVIEW.LOCATORVALUE</code>.
     */
    public void setLocatorvalue(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>automation.PROPSVIEW.LOCATORVALUE</code>.
     */
    public String getLocatorvalue() {
        return (String) get(5);
    }

    /**
     * Setter for <code>automation.PROPSVIEW.LOCATORTYPE</code>.
     */
    public void setLocatortype(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>automation.PROPSVIEW.LOCATORTYPE</code>.
     */
    public String getLocatortype() {
        return (String) get(6);
    }

    /**
     * Setter for <code>automation.PROPSVIEW.STANDARDCLASS</code>.
     */
    public void setStandardclass(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>automation.PROPSVIEW.STANDARDCLASS</code>.
     */
    public String getStandardclass() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<String, Integer, String, String, String, String, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<String, Integer, String, String, String, String, String, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Propsview.PROPSVIEW.PAGENAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return Propsview.PROPSVIEW.GUIMAPID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Propsview.PROPSVIEW.MAPPEDCLASS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Propsview.PROPSVIEW.CONTROLNAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Propsview.PROPSVIEW.CONTROLDESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Propsview.PROPSVIEW.LOCATORVALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return Propsview.PROPSVIEW.LOCATORTYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Propsview.PROPSVIEW.STANDARDCLASS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getPagename();
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
        return getMappedclass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getControlname();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getControldescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getLocatorvalue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getLocatortype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getStandardclass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getPagename();
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
        return getMappedclass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getControlname();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getControldescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getLocatorvalue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getLocatortype();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getStandardclass();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord value1(String value) {
        setPagename(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord value2(Integer value) {
        setGuimapid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord value3(String value) {
        setMappedclass(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord value4(String value) {
        setControlname(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord value5(String value) {
        setControldescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord value6(String value) {
        setLocatorvalue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord value7(String value) {
        setLocatortype(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord value8(String value) {
        setStandardclass(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropsviewRecord values(String value1, Integer value2, String value3, String value4, String value5, String value6, String value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PropsviewRecord
     */
    public PropsviewRecord() {
        super(Propsview.PROPSVIEW);
    }

    /**
     * Create a detached, initialised PropsviewRecord
     */
    public PropsviewRecord(String pagename, Integer guimapid, String mappedclass, String controlname, String controldescription, String locatorvalue, String locatortype, String standardclass) {
        super(Propsview.PROPSVIEW);

        set(0, pagename);
        set(1, guimapid);
        set(2, mappedclass);
        set(3, controlname);
        set(4, controldescription);
        set(5, locatorvalue);
        set(6, locatortype);
        set(7, standardclass);
    }
}
