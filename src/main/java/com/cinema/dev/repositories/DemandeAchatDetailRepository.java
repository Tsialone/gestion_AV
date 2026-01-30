package com.cinema.dev.repositories;

import com.cinema.dev.models.DemandeAchatDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeAchatDetailRepository extends JpaRepository<DemandeAchatDetail, DemandeAchatDetail.DemandeAchatDetailId> {
}