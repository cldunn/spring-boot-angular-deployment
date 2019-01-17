package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.ContactDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.repository.contact.ContactRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.cldbiz.userportal.unit.repository.data.ContactData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DatabaseSetup("/contactData.xml")
public class ContactRepositoryTest extends BaseRepositoryTest {
	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	ContactRepository contactRepository;
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		List<Contact> contacts = contactRepository.findAll();
		Contact contact = contacts.get(0);
		
		Boolean exists = contactRepository.existsById(contact.getId());
		contactRepository.flush();
		
		assertThat(contactRepository.existsById(contact.getId())).isTrue();
	}
	

	@Test
	public void whenCountAll_thenReturnLong() {
		long contactCnt = contactRepository.countAll();
		contactRepository.flush();
		
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS);
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
	public void whenFindByIds_thenReturnContacts() {
		List<Contact> contacts = contactRepository.findAll();
		List<Long> contactIds = contacts.stream().map(Contact::getId).collect(Collectors.toList());
		
		contacts = contactRepository.findByIds(contactIds);
		
		assertThat(contactIds.size() == contacts.size()).isTrue();
	}

	@Test
	public void whenFindAll_thenReturnAllContacts() {
		List<Contact> contacts = contactRepository.findAll();
		
		assertThat(contacts.size()).isEqualTo(TOTAL_ROWS.intValue());
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
		
		long contactCnt = contactRepository.countAll();

		assertThat(contactCnt).isZero();
	}

	@Test
	public void whenDeleteByEntity_thenRemoveContact() {
		List<Contact> contacts = contactRepository.findAll();
		Contact contact = contacts.get(0);
		
		contactRepository.deleteByEntity(contact);
		
		contacts = contactRepository.findAll();

		assertThat(contacts.contains(contact)).isFalse();
	}

	@Test
	public void whenDeleteByEntities_thenRemoveContacts() {
		List<Contact> contacts = contactRepository.findAll();
		
		contactRepository.deleteByEntities(contacts);
		
		long contactCnt = contactRepository.countAll();

		assertThat(contactCnt).isZero();
	}
	
	@Test
	public void whenSaveEntity_thenReturnSavedContact() {
		Contact anotherContact = ContactData.getAnotherContact();
		
		Contact savedContact = contactRepository.saveEntity(anotherContact);
		assertThat(savedContact.equals(anotherContact)).isTrue();
		
		long contactCnt = contactRepository.countAll();
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS + 1);
	}

	@Test
	public void whenSaveEntities_thenReturnSavedContacts() {
		Contact anotherContact = ContactData.getAnotherContact();
		
		Contact extraContact = ContactData.getExtraContact();
		
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(anotherContact);
		contacts.add(extraContact);
		
		List<Contact> savedContacts = contactRepository.saveEntities(contacts);
		
		assertThat(savedContacts.size() == 2).isTrue();
		
		assertThat(contacts.stream().allMatch(t -> savedContacts.contains(t))).isTrue();
		assertThat(savedContacts.stream().allMatch(t -> contacts.contains(t))).isTrue();
		
		long contactCnt = contactRepository.countAll();
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS + 2);
		
		assertThat(contactRepository.existsById(savedContacts.get(0).getId())).isTrue();
		assertThat(contactRepository.existsById(savedContacts.get(1).getId())).isTrue();
	}

	@Test
	public void whenModified_thenContactUpdated() {
		Optional<Contact> originalContact = contactRepository.findById(3L);
		originalContact.get().setName("UPDATED - " + originalContact.get().getName());
		
		Optional<Contact> rtrvdContact = contactRepository.findById(3L);
		assertThat(originalContact.get().getName().equals((rtrvdContact.get().getName())));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Tom");
		
		Boolean exists = contactRepository.existsByDto(contactDto);
		contactRepository.flush();
		
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		ContactDto contactDto = new ContactDto();
		contactDto.setEmail("@gmail.com");
		
		long contactCnt =  contactRepository.countByDto(contactDto);
		contactRepository.flush();
		
		assertThat(contactCnt).isGreaterThanOrEqualTo(2L);
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
