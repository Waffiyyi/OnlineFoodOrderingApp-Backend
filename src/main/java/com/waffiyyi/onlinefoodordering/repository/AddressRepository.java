package com.waffiyyi.onlinefoodordering.repository;

import com.waffiyyi.onlinefoodordering.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findAllByCityAndStateProvinceAndPostalCodeAndStreetAddressAndPlace(
     String city
     , String stateProvince, String postalCode, String streetAdresss, String place);
}
