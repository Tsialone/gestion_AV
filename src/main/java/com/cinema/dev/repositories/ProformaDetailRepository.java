package com.cinema.dev.repositories;

import com.cinema.dev.models.ProformaDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProformaDetailRepository extends JpaRepository<ProformaDetail, ProformaDetail.ProformaDetailId> {
}