package com.cldbiz.userportal.repository.contact;

import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.repository.BaseRepository;

public interface ContactRepository extends BaseRepository<Contact, Long>, ContactRepositoryExt {

}
