-- Schema for CRM Event Synchronization System project 
 
-- Drop schema crm_sync 
DROP SCHEMA IF EXISTS crm_sync CASCADE; 
 
-- Create schema crm_sync 
CREATE SCHEMA IF NOT EXISTS crm_sync 
    AUTHORIZATION crm_user; 
 
-- Drop tables 
DROP TABLE IF EXISTS crm_sync.attendee; 
DROP TABLE IF EXISTS crm_sync.campaign; 
 
-- Create table campaign 
CREATE TABLE crm_sync.campaign ( 
	 id VARCHAR(100) NOT NULL, 
	 sub_campaign_id VARCHAR(100) 
 ); 
 
 ALTER TABLE crm_sync.campaign ADD CONSTRAINT campaign_pk PRIMARY KEY (id); 
 
 
 -- Create table attendee 
 CREATE TABLE crm_sync.attendee ( 
 	 id SERIAL, 
 	 cn VARCHAR(100), 
 	 first_name VARCHAR(100) NOT NULL, 
 	 last_name VARCHAR(100) NOT NULL, 
 	 birth_date DATE, 
 	 partner_id VARCHAR(100) NOT NULL, 
 	 is_companion BOOLEAN NOT NULL, 
 	 qr_code VARCHAR(255) NOT NULL, 
 	 campaign_id VARCHAR(100) NOT NULL 
 ); 
 
 ALTER TABLE crm_sync.attendee ADD CONSTRAINT attendee_pk PRIMARY KEY (id); 
 ALTER TABLE crm_sync.attendee ADD CONSTRAINT attendee_qr_unique UNIQUE (qr_code); 
 ALTER TABLE crm_sync.attendee ADD CONSTRAINT attendee_campaign_fk FOREIGN KEY (campaign_id) REFERENCES crm_sync.campaign(id) ON DELETE CASCADE;
