package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.ContactDto;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.contact.ContactRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.cldbiz.userportal.unit.repository.data.ContactData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @DatabaseSetup("/contactData.xml")
public class ContactRepositoryTest extends BaseRepositoryTest {
	private static final Long TOTAL_ROWS = 7L;
	
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ContactRepository contactRepository;
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");
		
		List<Contact> contacts = contactRepository.findAll();
		contactRepository.flush();
		
		Contact contact = contacts.get(0);
		
		// clear cache to test performance
		contactRepository.flush();

		// invoke existsById here
		Boolean exists = contactRepository.existsById(contact.getId());
		contactRepository.flush();
		
		assertThat(contactRepository.existsById(contact.getId())).isTrue();
	}
	

	@Test
	public void whenCountAll_thenReturnLong() {
		log.info("whenCountAll_thenReturnCount");
		
		// invoke countAll here
		long contactCnt = contactRepository.countAll();
		contactRepository.flush();
		
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnContact() {
		log.info("whenFindById_thenReturnContact");
		
		// invoke findById here
		Optional<Contact> sameContact = contactRepository.findById(1L);
		contactRepository.flush();
		
		assertThat(sameContact.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindByIds_thenReturnContacts() {
		log.info("whenFindByIds_thenReturnContacts");
		
		List<Contact> contacts = contactRepository.findAll();
		List<Long> contactIds = contacts.stream().map(Contact::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		contactRepository.flush();
		
		// invoke findByIds here
		contacts = contactRepository.findByIds(contactIds);
		contactRepository.flush();
		
		assertThat(contacts.size()).isEqualTo(TOTAL_ROWS.intValue());
	}

	@Test
	public void whenFindAll_thenReturnAllContacts() {
		log.info("whenFindAll_thenReturnAllContacts");
		
		// invoke findAll here
		List<Contact> contacts = contactRepository.findAll();
		contactRepository.flush();
		
		assertThat(contacts.size()).isEqualTo(TOTAL_ROWS.intValue());
	}

	@Test
	public void whenDeleteById_thenRemoveContact() {
		log.info("whenDeleteById_thenRemoveContact");
		
		List<Contact> contacts = contactRepository.findAll();
		Contact contact = contacts.get(0);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setContactDto(new ContactDto(contact));
		List<Account> accounts = accountRepository.findByDto(accountDto);
		
		accounts.forEach(account -> account.setContact(null));
		
		// clear cache to test performance
		contactRepository.flush();

		// invoke deleteById here
		contactRepository.deleteById(contact.getId());
		contactRepository.flush();
		
		contacts = contactRepository.findAll();

		assertThat(contacts.contains(contact)).isFalse();
	}

	@Test
	public void whenDeleteByIds_thenRemoveAllContacts() {
		log.info("whenDeleteByIds_thenRemoveAllContacts");
		
		List<Contact> contacts = contactRepository.findAll();
		List<Long> contactIds = contacts.stream().map(Contact::getId).collect(Collectors.toList());
		
		contacts.forEach(contact -> {
			AccountDto accountDto = new AccountDto();
			accountDto.setContactDto(new ContactDto(contact));
			List<Account> accounts = accountRepository.findByDto(accountDto);
			
			accounts.forEach(account -> account.setContact(null));
		});
		
		// clear cache to test performance
		contactRepository.flush();

		// invoke deleteByIds here
		contactRepository.deleteByIds(contactIds);
		contactRepository.flush();
		
		long contactCnt = contactRepository.countAll();

		assertThat(contactCnt).isZero();
	}

	@Test
	public void whenDeleteByEntity_thenRemoveContact() {
		log.info("whenDeleteByEntity_thenRemoveContact");
		
		List<Contact> contacts = contactRepository.findAll();
		Contact contact = contacts.get(0);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setContactDto(new ContactDto(contact));
		List<Account> accounts = accountRepository.findByDto(accountDto);
		
		accounts.forEach(account -> account.setContact(null));

		// clear cache to test performance
		contactRepository.flush();

		// invoke deleteByEntity here
		contactRepository.deleteByEntity(contact);
		contactRepository.flush();

		contacts = contactRepository.findAll();

		assertThat(contacts.contains(contact)).isFalse();
	}

	@Test
	public void whenDeleteByEntities_thenRemoveContacts() {
		log.info("whenDeleteByEntities_thenRemoveContacts");
		
		List<Contact> contacts = contactRepository.findAll();
		
		contacts.forEach(contact -> {
			AccountDto accountDto = new AccountDto();
			accountDto.setContactDto(new ContactDto(contact));
			List<Account> accounts = accountRepository.findByDto(accountDto);
			
			accounts.forEach(account -> account.setContact(null));
		});

		// clear cache to test performance
		contactRepository.flush();

		// invoke deleteByEntities here
		contactRepository.deleteByEntities(contacts);
		contactRepository.flush();
		
		long contactCnt = contactRepository.countAll();

		assertThat(contactCnt).isZero();
	}
	
	@Test
	public void whenSaveEntity_thenReturnSavedContact() {
		log.info("whenSaveEntity_thenReturnSavedContact");
		
		Contact anotherContact = ContactData.getAnotherContact();
		
		// invoke saveEntity here
		Contact savedContact = contactRepository.saveEntity(anotherContact);
		contactRepository.flush();
		
		assertThat(savedContact.equals(anotherContact)).isTrue();
		
		long contactCnt = contactRepository.countAll();
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS + 1);
	}

	@Test
	public void whenSaveEntities_thenReturnSavedContacts() {
		log.info("whenSaveEntities_thenReturnSavedContacts");
		
		Contact anotherContact = ContactData.getAnotherContact();
		
		Contact extraContact = ContactData.getExtraContact();
		
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(anotherContact);
		contacts.add(extraContact);
		
		// invoke saveEntities here
		List<Contact> savedContacts = contactRepository.saveEntities(contacts);
		contactRepository.flush();
		
		assertThat(savedContacts.size() == 2).isTrue();
		
		// check that all savedContacts are the same as all contacts
		assertThat(contacts.stream().allMatch(t -> savedContacts.contains(t))).isTrue();
		assertThat(savedContacts.stream().allMatch(t -> contacts.contains(t))).isTrue();
		
		// check total of two contacts persisted
		long contactCnt = contactRepository.countAll();
		assertThat(contactCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// check saved contacts persisted
		assertThat(contactRepository.existsById(savedContacts.get(0).getId())).isTrue();
		assertThat(contactRepository.existsById(savedContacts.get(1).getId())).isTrue();
	}

	@Test
	public void whenModified_thenContactUpdated() {
		log.info("whenModified_thenContactUpdated");
		Optional<Contact> originalContact = contactRepository.findById(3L);
		originalContact.get().setName("UPDATED - " + originalContact.get().getName());
		
		// check modifications automatically persisted
		Optional<Contact> rtrvdContact = contactRepository.findById(3L);
		assertThat(originalContact.get().getName().equals((rtrvdContact.get().getName())));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info("whenExistsByDto_thenReturnTrue");
		
		// qualify count by partial contact name
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Tom");
		
		// invoke existsByDto here
		Boolean exists = contactRepository.existsByDto(contactDto);
		contactRepository.flush();
		
		// test existence
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		log.info("whenCountByDto_thenReturnCount");
		
		// qualify count by partial contact email
		ContactDto contactDto = new ContactDto();
		contactDto.setEmail("@gmail.com");
		
		// invoke countByDto here
		long contactCnt =  contactRepository.countByDto(contactDto);
		contactRepository.flush();
		
		// test count
		assertThat(contactCnt).isGreaterThanOrEqualTo(2L);
	}


	@Test
	public void whenFindByDto_thenReturnContacts() {
		log.info("whenFindByDto_thenReturnContacts");
		
		// qualify search by contact name
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Alex Thurman");
		
		// invoke findByDto here
		List<Contact> contacts = contactRepository.findByDto(contactDto);
		contactRepository.flush();
		
		assertThat(contacts).isNotEmpty();
	}

	@Test
	public void whenFindPageByDto_thenReturnContacts() {
		log.info("whenFindPageByDto_thenReturnContacts");
		
		// qualify search by contact name
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Alex Thurman");
		
		// limit result to first two contacts
		contactDto.setStart(0);
		contactDto.setLimit(2);
		
		// invoke findPageByDto here 
		List<Contact> contacts = contactRepository.findPageByDto(contactDto);
		contactRepository.flush();
		
		// check no more that two contacts returned
		assertThat(contacts).isNotEmpty();
		assertThat(contacts.size()).isLessThanOrEqualTo(2);
	}

	@Test
	public void whenSearchByDto_thenReturnContacts() {
		log.info("whenSearchByDto_thenReturnContacts");
		
		// qualify search by partial contact ph #
		ContactDto contactDto = new ContactDto();
		contactDto.setPhone("(555)");
		
		// invoke searchByDto here 
		List<Contact> contacts = contactRepository.searchByDto(contactDto);
		contactRepository.flush();
		
		// check contacts returned
		assertThat(contacts).isNotEmpty();
	}

	@Test
	public void whenSearchPageByDto_thenReturnContacts() {
		log.info("whenSearchPageByDto_thenReturnContacts");
		
		// qualify search by partial contact email
		ContactDto contactDto = new ContactDto();
		contactDto.setEmail("gmail.com");
		
		// limit result to first two contacts
		contactDto.setStart(0);
		contactDto.setLimit(2);
		
		// invoke searchPageByDto here 
		List<Contact> contacts = contactRepository.searchPageByDto(contactDto);
		contactRepository.flush();
		
		// check no more that two contacts returned
		assertThat(contacts).isNotEmpty();
		assertThat(contacts.size()).isLessThanOrEqualTo(2);
	}
}
