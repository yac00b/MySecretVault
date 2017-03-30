package com.jakubwyc.mysecretvault.repository;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmUser extends RealmObject {

    @PrimaryKey
    public String login;

    @Required
    public byte[] passwordHash;

}