package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.dto.ContactDto;
import com.cldbiz.userportal.repository.contact.ContactRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DatabaseSetup("/contactData.xml")
public class ContactRepositoryTest extends BaseRepositoryTest {
	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	ContactRepository contactRepository;
	
	@Test
	public void whenCount_thenReturnCount() {
		long contactCnt = contactRepository.count();
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenDelete_thenRemoveContact() {
		List<Contact> contacts = contactRepository.findAll();
		Contact contact = contacts.get(0);
		
		contactRepository.delete(contact);
		
		contacts = contactRepository.findAll();

		assertThat(contacts.contains(contact)).isFalse();
	}

	@Test
	public void whenDeleteAll_thenRemoveAllContacts() {
		List<Contact> contacts = contactRepository.findAll();
		
		contactRepository.deleteAll(contacts);
		
		long contactCnt = contactRepository.count();

		assertThat(contactCnt).isZero();
	}
	
	@Test
	public void whenDeleteById_thenRemoveContact() {
		List<Contact> contacts = contactRepository.findAll();
		Contact contact = contacts.get(0);
		
		contactRepository.deleteById(contact.getId());
		
		contacts = contactRepository.findAll();

		assertThat(contacts.contains(contact)).isFalse();
	}

	@Test
	public void whenDeleteByIds_thenRemoveAllContacts() {
		List<Contact> contacts = contactRepository.findAll();
		List<Long> contactIds = contacts.stream().map(Contact::getId).collect(Collectors.toList());
		
		contactRepository.deleteByIds(contactIds);
		
		long contactCnt = contactRepository.count();

		assertThat(contactCnt).isZero();
	}

	@Test
	public void whenExistsById_thenReturnTrue() {
		List<Contact> contacts = contactRepository.findAll();
		Contact contact = contacts.get(0);
		
		assertThat(contactRepository.existsById(contact.getId())).isTrue();
	}
	
	@Test
	public void whenFindAll_thenReturnAllContacts() {
		List<Contact> contacts = contactRepository.findAll();
		
		assertThat(contacts.size()).isEqualTo(TOTAL_ROWS.intValue());
	}

	@Test
	public void whenFindAllById_thenReturnAllContacts() {
		List<Contact> contacts = contactRepository.findAll();
		List<Long> contactIds = contacts.stream().map(Contact::getId).collect(Collectors.toList());
		
		contacts = contactRepository.findAllById(contactIds);
		
		assertThat(contactIds.size() == contacts.size()).isTrue();
	}

	@Test
	public void whenFindById_thenReturnContact() {
		List<Contact> contacts = contactRepository.findAll();
		Contact contact = contacts.get(0);
		
		Optional<Contact> sameContact = contactRepository.findById(contact.getId());
		
		assertThat(sameContact.orElse(null)).isNotNull();
		assertThat(contact.equals(sameContact.get())).isTrue();
	}

	@Test
	public void whenSave_thenReturnSavedContact() {
		Contact anotherContact = ContactDynData.getAnotherContact();
		
		Contact savedContact = contactRepository.save(anotherContact);
		assertThat(savedContact.equals(anotherContact)).isTrue();
		
		long contactCnt = contactRepository.count();
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS + 1);
	}

	@Test
	public void whenModified_thenContactUpdated() {
		Optional<Contact> originalContact = contactRepository.findById(3L);
		originalContact.get().setName("UPDATED - " + originalContact.get().getName());
		
		Optional<Contact> rtrvdContact = contactRepository.findById(3L);
		assertThat(originalContact.get().getName().equals((rtrvdContact.get().getName())));
	}

	@Test
	public void whenSaveAndFlush_thenReturnSavedContact() {
		Contact anotherContact = ContactDynData.getAnotherContact();
		
		Contact savedContact = contactRepository.saveAndFlush(anotherContact);
		assertThat(savedContact.equals(anotherContact)).isTrue();
		
		long contactCnt = contactRepository.count();
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS + 1);
	}

	@Test
	public void whenSaveAll_thenReturnSavedContacts() {
		Contact anotherContact = ContactDynData.getAnotherContact();
		
		Contact extraContact = ContactDynData.getExtraContact();
		
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(anotherContact);
		contacts.add(extraContact);
		
		List<Contact> savedContacts = contactRepository.saveAll(contacts);
		
		assertThat(savedContacts.size() == 2).isTrue();
		
		assertThat(contacts.stream().allMatch(t -> savedContacts.contains(t))).isTrue();
		assertThat(savedContacts.stream().allMatch(t -> contacts.contains(t))).isTrue();
		
		long contactCnt = contactRepository.count();
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS + 2);
		
		assertThat(contactRepository.existsById(savedContacts.get(0).getId())).isTrue();
		assertThat(contactRepository.existsById(savedContacts.get(1).getId())).isTrue();
	}

	@Test
	public void whenFindByDto_thenReturnContacts() {
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Alex Thurman");
		
		List<Contact> contacts = contactRepository.findByDto(contactDto);
		
		assertThat(contacts).isNotEmpty();
	}

	@Test
	public void whenFindPageByDto_thenReturnContacts() {
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Alex Thurman");
		contactDto.setStart(0);
		contactDto.setLimit(2);
		
		List<Contact> contacts = contactRepository.findPageByDto(contactDto);
		
		assertThat(contacts.size()).isLessThanOrEqualTo(2);
	}

	@Test
	public void whenCountSearchByDto_thenReturnCount() {
		ContactDto contactDto = new ContactDto();
		
		contactDto.setPhone("(555)");
		
		Long contactCount = contactRepository.countSearchByDto(contactDto);
		contactRepository.flush();
		
		assertThat(contactCount).isGreaterThan(0L);
	}

	@Test
	public void whenSearchByDto_thenReturnContacts() {
		ContactDto contactDto = new ContactDto();
		contactDto.setPhone("(555)");
		
		List<Contact> contacts = contactRepository.searchByDto(contactDto);
		
		assertThat(contacts).isNotEmpty();
	}

	@Test
	public void whenSearchPageByDto_thenReturnContacts() {
		ContactDto contactDto = new ContactDto();
		contactDto.setEmail("gmail.com");
		contactDto.setStart(0);
		contactDto.setLimit(2);
		
		List<Contact> contacts = contactRepository.searchPageByDto(contactDto);
		
		assertThat(contacts.size()).isLessThanOrEqualTo(2);
	}
}
