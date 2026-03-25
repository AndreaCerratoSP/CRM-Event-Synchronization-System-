package com.crm.sync.worker.repository;

import com.crm.sync.worker.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, String> {

    @Query("SELECT c FROM Campaign c LEFT JOIN FETCH c.attendees WHERE c.campagnId = :campaignId")
    Campaign findByIdWithAttendees(@Param("campaignId") String campaignId);
}
