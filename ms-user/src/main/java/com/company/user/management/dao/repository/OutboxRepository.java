package com.company.user.management.dao.repository;

import com.company.user.management.dao.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

}
