package com.example.testdbflow.db;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sqlcipher.SQLCipherOpenHelper;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;

public class SQLCipherHelperImpl extends SQLCipherHelperImplEx {
    public SQLCipherHelperImpl(DatabaseDefinition databaseDefinition, DatabaseHelperListener listener) {
        super(databaseDefinition, listener);
    }

    /**
     * @return The SQLCipher secret for opening this database.
     */
    @Override
    protected String getCipherSecret() {
        return "mgg";
    }
}