package com.cinema.dev.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for grouping users by department
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptUsersDTO {
    private Integer idDept;
    private String deptNom;
    private List<UtilisateurDisplayDTO> utilisateurs;
}
