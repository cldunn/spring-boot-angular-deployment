package com.cldbiz.userportal.repository.contact;

import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.repository.AbstractRepository;

public interface ContactRepository extends AbstractRepository<Contact, Long>, ContactRepositoryExt {

}
