/**
 * 
 */
package com.hp.p2p.repository;

import org.springframework.data.repository.CrudRepository;

import com.hp.p2p.domain.CallLog;

/**
 * @author wangxif
 *
 */
public interface CallLogRepository extends CrudRepository<CallLog, Long> {

}
