package com.jskingen.carlocation.dao;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by ChneY on 2017/4/13.
 */

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0 && newVersion == 1) {
            RealmObjectSchema personSchema = schema.get("UserProject");
            personSchema.addField("projectMileage", String.class);
            oldVersion = 1;
        }
    }
}
