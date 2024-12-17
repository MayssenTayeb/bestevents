package com.Events.bestevents.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Events.bestevents.models.Registration;

public interface RegistrationsRepository  extends JpaRepository<Registration, Integer> {

}
