package com.crm.sync.worker.repository;

import com.crm.sync.worker.entity.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Attendee entities.
 */
@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {

}
