package com.crm.sync.worker.mapper;

import com.crm.sync.worker.dto.AttendeeDto;
import com.crm.sync.worker.entity.Attendee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AttendeeMapper {

    public abstract AttendeeDto toDto(Attendee attendee);
    public abstract Attendee toEntity(AttendeeDto attendeeDto);

    public abstract List<AttendeeDto> toDto(List<Attendee> attendees);
    public abstract List<Attendee> toEntity(List<AttendeeDto> attendeesDto);

}
