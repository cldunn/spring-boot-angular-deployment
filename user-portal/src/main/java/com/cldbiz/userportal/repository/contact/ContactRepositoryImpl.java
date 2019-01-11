package com.cldbiz.userportal.repository.contact;

import java.util.List;

import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.domain.QContact;
import com.cldbiz.userportal.dto.ContactDto;
import com.cldbiz.userportal.repository.AbstractRepositoryImpl;
import com.cldbiz.userportal.repository.DynBooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContactRepositoryImpl extends AbstractRepositoryImpl<Contact, ContactDto, Long> implements ContactRepositoryExt {

	@Override
	public List<Contact> findAll() {
		return jpaQueryFactory.selectFrom(QContact.contact).fetch();
	}

	@Override
	public List<Contact> findAllById(List<Long> contactIds) {
		QContact contact = QContact.contact;
		
		return jpaQueryFactory.selectFrom(contact)
				.where(contact.id.in(contactIds))
				.fetch();
	}

	@Override
	public List<Contact> findByDto(ContactDto contactDto) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = new DynBooleanBuilder<QContact, ContactDto>();
		Predicate predicate = builder.findPredicate(contact, contactDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(contact).where(predicate).fetch();
	}

	@Override
	public List<Contact> findPageByDto(ContactDto contactDto) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = new DynBooleanBuilder<QContact, ContactDto>();
		Predicate predicate = builder.findPredicate(contact, contactDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(contact)
				.where(predicate)
				.orderBy(sortBy(contactDto))
				.offset(contactDto.getStart().intValue())
				.limit(contactDto.getLimit().intValue())
				.fetch();

	}

	@Override
	public Long countSearchByDto(ContactDto contactDto) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = new DynBooleanBuilder<QContact, ContactDto>();
		Predicate predicate = builder.searchPredicate(contact, contactDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(contact)
				.where(predicate)
				.fetchCount();
	}

	@Override
	public List<Contact> searchByDto(ContactDto contactDto) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = new DynBooleanBuilder<QContact, ContactDto>();
		Predicate predicate = builder.searchPredicate(contact, contactDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(contact).where(predicate).fetch();
	}

	@Override
	public List<Contact> searchPageByDto(ContactDto contactDto) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = new DynBooleanBuilder<QContact, ContactDto>();
		Predicate predicate = builder.searchPredicate(contact, contactDto).asPredicate();
		
		return jpaQueryFactory.selectFrom(contact)
				.where(predicate)
				.orderBy(sortBy(contactDto))
				.offset(contactDto.getStart().intValue())
				.limit(contactDto.getLimit().intValue())
				.fetch();
	}

	@Override
	public OrderSpecifier[] sortBy(ContactDto contactDto) {
		PathBuilder pb = new PathBuilder<QContact>(QContact.class, "name");
		return sortOrderOf(pb, contactDto);
	}

}
