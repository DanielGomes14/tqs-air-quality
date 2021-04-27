package tqs.airquality.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.airquality.models.City;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {
}
