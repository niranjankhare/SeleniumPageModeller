/*
 * This file is generated by jOOQ.
*/
package db.jooq.generated.automationDb.tables;


import db.jooq.generated.automationDb.Automation;
import db.jooq.generated.automationDb.Indexes;
import db.jooq.generated.automationDb.Keys;
import db.jooq.generated.automationDb.tables.records.PagesRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


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
public class Pages extends TableImpl<PagesRecord> {

    private static final long serialVersionUID = -327522503;

    /**
     * The reference instance of <code>automation.PAGES</code>
     */
    public static final Pages PAGES = new Pages();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PagesRecord> getRecordType() {
        return PagesRecord.class;
    }

    /**
     * The column <code>automation.PAGES.PAGEID</code>.
     */
    public final TableField<PagesRecord, Integer> PAGEID = createField("PAGEID", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>automation.PAGES.PAGENAME</code>.
     */
    public final TableField<PagesRecord, String> PAGENAME = createField("PAGENAME", org.jooq.impl.SQLDataType.VARCHAR(50), this, "");

    /**
     * The column <code>automation.PAGES.PARENTID</code>.
     */
    public final TableField<PagesRecord, Integer> PARENTID = createField("PARENTID", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * The column <code>automation.PAGES.PAGEDESCRIPTION</code>.
     */
    public final TableField<PagesRecord, String> PAGEDESCRIPTION = createField("PAGEDESCRIPTION", org.jooq.impl.SQLDataType.VARCHAR(150), this, "");

    /**
     * Create a <code>automation.PAGES</code> table reference
     */
    public Pages() {
        this(DSL.name("PAGES"), null);
    }

    /**
     * Create an aliased <code>automation.PAGES</code> table reference
     */
    public Pages(String alias) {
        this(DSL.name(alias), PAGES);
    }

    /**
     * Create an aliased <code>automation.PAGES</code> table reference
     */
    public Pages(Name alias) {
        this(alias, PAGES);
    }

    private Pages(Name alias, Table<PagesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Pages(Name alias, Table<PagesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Automation.AUTOMATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PAGES_PAGENAME, Indexes.PAGES_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<PagesRecord, Integer> getIdentity() {
        return Keys.IDENTITY_PAGES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<PagesRecord> getPrimaryKey() {
        return Keys.KEY_PAGES_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<PagesRecord>> getKeys() {
        return Arrays.<UniqueKey<PagesRecord>>asList(Keys.KEY_PAGES_PRIMARY, Keys.KEY_PAGES_PAGENAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pages as(String alias) {
        return new Pages(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pages as(Name alias) {
        return new Pages(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Pages rename(String name) {
        return new Pages(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Pages rename(Name name) {
        return new Pages(name, null);
    }
}
