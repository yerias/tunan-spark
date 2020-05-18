package com.tunan.java.ormapping.dao;

import java.io.Serializable;

public interface BaseDAO {

    <T> Serializable save(T t);

}
