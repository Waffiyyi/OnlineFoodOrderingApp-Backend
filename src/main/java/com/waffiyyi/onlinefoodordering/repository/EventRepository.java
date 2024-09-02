package com.waffiyyi.onlinefoodordering.repository;

import com.waffiyyi.onlinefoodordering.model.Event;
import com.waffiyyi.onlinefoodordering.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findAllByRestaurant(Restaurant restaurant);
}
