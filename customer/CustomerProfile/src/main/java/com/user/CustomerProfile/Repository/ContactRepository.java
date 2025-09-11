package com.user.CustomerProfile.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.CustomerProfile.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>
{

}
