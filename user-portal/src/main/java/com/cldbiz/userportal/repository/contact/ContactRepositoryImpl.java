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
	public Boolean existsByDto(ContactDto contactDto, Predicate... predicates) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = searchByCriteria(contactDto, predicates);
		
		return jpaQueryFactory.selectFrom(contact).where(builder.asPredicate()).fetchCount() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}
	
	@Override
	public Long countByDto(ContactDto contactDto, Predicate... predicates) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = searchByCriteria(contactDto, predicates);
		
		return jpaQueryFactory.selectFrom(contact).where(builder.asPredicate()).fetchCount();
	}
	
	@Override
	public List<Contact> findByIds(List<Long> contactIds) {
		QContact contact = QContact.contact;
		
		return jpaQueryFactory.selectFrom(contact)
				.where(contact.id.in(contactIds))
				.fetch();
	}
	
	@Override
	public List<Contact> findAll() {
		QContact contact = QContact.contact;
		
		return jpaQueryFactory.selectFrom(contact).fetch();
	}
	
	@Override
	public List<Contact> findByDto(ContactDto contactDto, Predicate... predicates) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = findByCriteria(contactDto, predicates);
		
		return jpaQueryFactory.selectFrom(contact).where(builder.asPredicate()).fetch();
	}

	@Override
	public List<Contact> findPageByDto(ContactDto contactDto, Predicate... predicates) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = findByCriteria(contactDto, predicates);
		
		return jpaQueryFactory.selectFrom(contact)
				.where(builder.asPredicate())
				.orderBy(sortBy(contactDto))
				.offset(contactDto.getStart().intValue())
				.limit(contactDto.getLimit().intValue())
				.fetch();

	}

	@Override
	public List<Contact> searchByDto(ContactDto contactDto, Predicate... predicates) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = searchByCriteria(contactDto, predicates);
		
		return jpaQueryFactory.selectFrom(contact).where(builder.asPredicate()).fetch();
	}

	@Override
	public List<Contact> searchPageByDto(ContactDto contactDto, Predicate... predicates) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = searchByCriteria(contactDto, predicates);
		
		return jpaQueryFactory.selectFrom(contact)
				.where(builder.asPredicate())
				.orderBy(sortBy(contactDto))
				.offset(contactDto.getStart().intValue())
				.limit(contactDto.getLimit().intValue())
				.fetch();
	}

	protected DynBooleanBuilder<QContact, ContactDto> findByCriteria(ContactDto contactDto, Predicate... predicates) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = new DynBooleanBuilder<QContact, ContactDto>();
		Predicate predicate = builder.findPredicate(contact, contactDto, predicates).asPredicate();

		return builder;
	}
	
	protected DynBooleanBuilder<QContact, ContactDto> searchByCriteria(ContactDto contactDto, Predicate... predicates) {
		QContact contact = QContact.contact;
		
		DynBooleanBuilder<QContact, ContactDto> builder = new DynBooleanBuilder<QContact, ContactDto>();
		Predicate predicate = builder.searchPredicate(contact, contactDto, predicates).asPredicate();

		return builder;
	}


	@Override
	public OrderSpecifier[] sortBy(ContactDto contactDto) {
		PathBuilder pb = new PathBuilder<QContact>(QContact.class, "name");
		return sortOrderOf(pb, contactDto);
	}

}
