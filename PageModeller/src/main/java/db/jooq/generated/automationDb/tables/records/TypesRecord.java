/*
 * This file is generated by jOOQ.
*/
package db.jooq.generated.automationDb.tables.records;


import db.jooq.generated.automationDb.tables.Types;

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
public class TypesRecord extends UpdatableRecordImpl<TypesRecord> implements Record6<Integer, String, String, String, Byte, String> {

    private static final long serialVersionUID = -1854364082;

    /**
     * Setter for <code>automation.TYPES.CLASSID</code>.
     */
    public void setClassid(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>automation.TYPES.CLASSID</code>.
     */
    public Integer getClassid() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>automation.TYPES.CLASS</code>.
     */
    public void setClass_(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>automation.TYPES.CLASS</code>.
     */
    public String getClass_() {
        return (String) get(1);
    }

    /**
     * Setter for <code>automation.TYPES.TYPE</code>.
     */
    public void setType(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>automation.TYPES.TYPE</code>.
     */
    public String getType() {
        return (String) get(2);
    }

    /**
     * Setter for <code>automation.TYPES.ABRV</code>.
     */
    public void setAbrv(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>automation.TYPES.ABRV</code>.
     */
    public String getAbrv() {
        return (String) get(3);
    }

    /**
     * Setter for <code>automation.TYPES.HASEXTENDEDPROPS</code>.
     */
    public void setHasextendedprops(Byte value) {
        set(4, value);
    }

    /**
     * Getter for <code>automation.TYPES.HASEXTENDEDPROPS</code>.
     */
    public Byte getHasextendedprops() {
        return (Byte) get(4);
    }

    /**
     * Setter for <code>automation.TYPES.PROPERTYMAP</code>.
     */
    public void setPropertymap(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>automation.TYPES.PROPERTYMAP</code>.
     */
    public String getPropertymap() {
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
    public Row6<Integer, String, String, String, Byte, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Integer, String, String, String, Byte, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Types.TYPES.CLASSID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Types.TYPES.CLASS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Types.TYPES.TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Types.TYPES.ABRV;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field5() {
        return Types.TYPES.HASEXTENDEDPROPS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Types.TYPES.PROPERTYMAP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getClassid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getClass_();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getAbrv();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component5() {
        return getHasextendedprops();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getPropertymap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getClassid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getClass_();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getAbrv();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value5() {
        return getHasextendedprops();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getPropertymap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypesRecord value1(Integer value) {
        setClassid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypesRecord value2(String value) {
        setClass_(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypesRecord value3(String value) {
        setType(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypesRecord value4(String value) {
        setAbrv(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypesRecord value5(Byte value) {
        setHasextendedprops(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypesRecord value6(String value) {
        setPropertymap(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TypesRecord values(Integer value1, String value2, String value3, String value4, Byte value5, String value6) {
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
     * Create a detached TypesRecord
     */
    public TypesRecord() {
        super(Types.TYPES);
    }

    /**
     * Create a detached, initialised TypesRecord
     */
    public TypesRecord(Integer classid, String class_, String type, String abrv, Byte hasextendedprops, String propertymap) {
        super(Types.TYPES);

        set(0, classid);
        set(1, class_);
        set(2, type);
        set(3, abrv);
        set(4, hasextendedprops);
        set(5, propertymap);
    }
}
