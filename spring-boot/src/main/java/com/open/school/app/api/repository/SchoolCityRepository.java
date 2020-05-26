
package com.open.school.app.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.open.school.app.api.entity.SchoolCityEntity;

@Repository
public interface SchoolCityRepository extends JpaRepository<SchoolCityEntity, Long> {

}
