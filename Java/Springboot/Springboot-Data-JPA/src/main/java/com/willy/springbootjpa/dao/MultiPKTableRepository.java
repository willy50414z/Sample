package com.willy.springbootjpa.dao;

import org.springframework.data.repository.CrudRepository;

import com.willy.springbootjpa.entity.MultiPKPo;
import com.willy.springbootjpa.entity.MultiPKPoPK;

public interface MultiPKTableRepository extends CrudRepository<MultiPKPo, MultiPKPoPK>{

}
