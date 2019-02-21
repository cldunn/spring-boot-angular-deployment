package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.contact.ContactRepository;
import com.cldbiz.userportal.repository.customer.CustomerRepository;
import com.cldbiz.userportal.repository.invoice.InvoiceRepository;
import com.cldbiz.userportal.repository.purchaseOrder.PurchaseOrderRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.cldbiz.userportal.unit.repository.data.AccountData;
import com.cldbiz.userportal.unit.repository.data.ContactData;
import com.cldbiz.userportal.unit.repository.data.CustomerData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/customerData.xml"})
public class CustomerRepositoryTest extends BaseRepositoryTest {

	private static final Long TOTAL_ROWS = 7L;
		
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;

	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");

		// get existing customer
		List<Customer> customers = customerRepository.findAll();
		Customer customer = customers.get(0);
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke existsById here
		Boolean exists = customerRepository.existsById(customer.getId());
		customerRepository.flush();
		
		// test for existence
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		log.info("whenCountAll_thenReturnLong");
		
		// invoke countAll here
		long customerCnt = customerRepository.countAll();
		customerRepository.flush();
		
		// check count
		assertThat(customerCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnCustomer() {
		log.info("whenFindById_thenReturnCustomer");
		
		// invoke findById here 
		Optional<Customer> sameCustomer = customerRepository.findById(3L);
		customerRepository.flush();
		
		// check customer returned
		assertThat(sameCustomer.orElse(null)).isNotNull();
		
		// check it has mandatory account
		Optional<Account> account = accountRepository.findById(sameCustomer.get().getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindByIds_thenReturnCustomers() {
		log.info("whenFindByIds_thenReturnCustomers");
		
		// get all customer ids
		List<Customer> customers = customerRepository.findAll();
		List<Long> customerIds = customers.stream().map(Customer::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		customerRepository.flush();

		// invoke findByIds here
		customers = customerRepository.findByIds(customerIds);
		customerRepository.flush();
		
		// check all customers returned
		assertThat(customers.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check each customer returned has mandatory account
		customers.forEach(customer -> {
			Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenFindAll_thenReturnAllCustomers() {
		log.info("whenFindAll_thenReturnAllCustomers");
		
		// invoke findAll here
		List<Customer> customers = customerRepository.findAll();
		customerRepository.flush();
		
		// check all customers returned
		assertThat(customers.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check each customer returned has mandatory account
		customers.forEach(customer -> {
			Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenDeleteById_thenRemoveCustomer() {
		log.info("whenDeleteById_thenRemoveCustomer");
		
		Customer customer = customerRepository.findById(1L).get();
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteById here
		customerRepository.deleteById(customer.getId());
		customerRepository.flush();
		
		List<Customer> customers = customerRepository.findAll();

		// check customer deleted
		assertThat(customers.contains(customer)).isFalse();
		
		// check related account also deleted
		Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
		assertThat(account.orElse(null)).isNull();
	}

	@Test
	public void whenDeleteByIds_thenRemoveCustomers() {
		log.info("whenDeleteByIds_thenRemoveCustomers");
		
		// get all customer ids
		List<Customer> customers = customerRepository.findAll();
		List<Long> customerIds = customers.stream().map(Customer::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		accountRepository.flush();
		// 
		// invoke deleteByIds here
		customerRepository.deleteByIds(customerIds);
		customerRepository.flush();
		
		long customerCnt = customerRepository.countAll();

		assertThat(customerCnt).isZero();

		customers.forEach(customer -> {
			/* check delete cascaded for each customer account */
			Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
			assertThat(account.orElse(null)).isNull();
		});
	}

	@Test
	public void whenDeleteByEntity_thenRemoveCustomer() {
		log.info("whenDeleteByEntity_thenRemoveCustomer");
		
		Customer customer = customerRepository.findById(3L).get();
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteByEntity here
		customerRepository.deleteByEntity(customer);
		customerRepository.flush();
		
		List<Customer> customers = customerRepository.findAll();

		assertThat(customers.contains(customer)).isFalse();

		/* check delete cascaded for customer account */
		Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
		assertThat(account.orElse(null)).isNull();
	}

	@Test
	public void whenDeleteByEntities_thenRemoveCustomers() {
		log.info("whenDeleteByEntities_thenRemoveCustomers");
		
		// get all customers
		List<Customer> customers = customerRepository.findAll();
		
		// clear cache to test performance
		customerRepository.flush();

		// invoke deleteByEntities here
		customerRepository.deleteByEntities(customers);
		customerRepository.flush();
		
		long customerCnt = customerRepository.countAll();

		assertThat(customerCnt).isZero();
		
		customers.forEach(customer -> {
			/* check delete cascaded for each customer account */
			Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
			assertThat(account.orElse(null)).isNull();
		});
	}
	
	@Test
	public void whenSaveEntity_thenReturnSavedCustomer() {
		log.info("whenSaveEntity_thenReturnSavedCustomer");
		
		// create new customer
		Customer anotherCustomer = CustomerData.getAnotherCustomer();
		
		// Manually establish bi-directional account relationship
		Account anotherAccount = AccountData.getAnotherAccount();
		Contact anotherContact = ContactData.getAnotherContact();
		anotherAccount.setContact(anotherContact);
		
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);
		
		// invoke saveByEntity here
		Customer savedCustomer = customerRepository.saveEntity(anotherCustomer);
		customerRepository.flush();
		
		// confirmed persisted customer returned
		assertThat(savedCustomer.equals(anotherCustomer)).isTrue();
		
		// confirmed customer persisted
		long customerCnt = customerRepository.countAll();
		assertThat(customerCnt).isEqualTo(TOTAL_ROWS + 1);
		
		// retrieve saved customer from store
		Optional<Customer> rtrvCustomer = customerRepository.findById(savedCustomer.getId());
		assertThat(rtrvCustomer.orElse(null)).isNotNull();
		
		// check retrieved customer matches created/returned customer
		assertThat(rtrvCustomer.get().equals(anotherCustomer)).isTrue();
		assertThat(rtrvCustomer.get().equals(savedCustomer)).isTrue();

		/* use lombock equals to check account persisted */
		assertThat(rtrvCustomer.get().getAccount().equals(anotherAccount)).isTrue();
		assertThat(rtrvCustomer.get().getAccount().getContact().equals(anotherContact)).isTrue();
	    assertThat(rtrvCustomer.get().getAccount().getCustomer().equals(anotherCustomer)).isTrue();
	}
	
	@Test
	public void whenSaveAll_thenReturnSavedCustomers() {
		log.info("whenSaveAll_thenReturnSavedCustomers");
		
		// create new customer
		Customer anotherCustomer = CustomerData.getAnotherCustomer();
		
		// Manually establish bi-directional account relationship
		Account anotherAccount = AccountData.getAnotherAccount();
		Contact anotherContact = ContactData.getAnotherContact();
		anotherAccount.setContact(anotherContact);

		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);
		
		// create 2nd customer
		Customer extraCustomer = CustomerData.getExtraCustomer();
		
		// Manually establish bi-directional account relationship
		Account extraAccount = AccountData.getExtraAccount();
		Contact extraContact = ContactData.getExtraContact();
		extraAccount.setContact(extraContact);
		
		extraCustomer.setAccount(extraAccount);
		extraAccount.setCustomer(extraCustomer);
		
		// create array of new customers
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(anotherCustomer);
		customers.add(extraCustomer);
		
		// invoke saveByEntities here
		List<Customer> savedCustomers = customerRepository.saveEntities(customers);
		customerRepository.flush();
		
		assertThat(savedCustomers.size() == 2).isTrue();
		
		// check new customers returned from saveEntities
		assertThat(customers.stream().allMatch(t -> savedCustomers.contains(t))).isTrue();
		assertThat(savedCustomers.stream().allMatch(t -> customers.contains(t))).isTrue();
		
		// check new customers persisted
		long customerCnt = customerRepository.countAll();
		assertThat(customerCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// retrieve 1st customer from store
		Optional<Customer> rtrvAnotherCustomer = customerRepository.findById(anotherCustomer.getId());
		assertThat(rtrvAnotherCustomer.orElse(null)).isNotNull();
		
		// use lombock equals to check 1st customer a nd related account persisted 
		assertThat(rtrvAnotherCustomer.get().equals(anotherCustomer)).isTrue();
		assertThat(rtrvAnotherCustomer.get().getAccount().equals(anotherAccount)).isTrue();
		assertThat(rtrvAnotherCustomer.get().getAccount().getContact().equals(anotherContact)).isTrue();
		assertThat(rtrvAnotherCustomer.get().getAccount().getCustomer().equals(anotherCustomer)).isTrue();
		
		// retrieve 2nd customer from store
		Optional<Customer> rtrvExtaCustomer = customerRepository.findById(extraCustomer.getId());
		assertThat(rtrvExtaCustomer.orElse(null)).isNotNull();
		
		// use lombock equals to check 2nd customer a nd related account persisted 
		assertThat(rtrvExtaCustomer.get().equals(extraCustomer)).isTrue();
		assertThat(rtrvExtaCustomer.get().getAccount().equals(extraAccount)).isTrue();
		assertThat(rtrvExtaCustomer.get().getAccount().getContact().equals(extraContact)).isTrue();
		assertThat(rtrvExtaCustomer.get().getAccount().getCustomer().equals(extraCustomer)).isTrue();
	}
	
	@Test
	public void whenModified_thenCustomertUpdated() {
		log.info("whenModified_thenCustomertUpdated");
		
		// retrieve customer
		Optional<Customer> originalCustomer = customerRepository.findById(3L);
		
		// update customer and related entities
		originalCustomer.get().setFirstName("UPDATED - " + originalCustomer.get().getFirstName());
		originalCustomer.get().getAccount().setBillingAddress("UPDATED - " + originalCustomer.get().getAccount().getBillingAddress());
		
		// retrieve customer again
		Optional<Customer> rtrvdCustomer = customerRepository.findById(3L);
		
		// check customer and related entities updated without save
		assertThat(originalCustomer.get().getFirstName().equals((originalCustomer.get().getFirstName())));
		assertThat(originalCustomer.get().getAccount().getBillingAddress().equals((rtrvdCustomer.get().getAccount().getBillingAddress())));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info("whenExistsByDto_thenReturnTrue");
		
		// create customer dto with qualifiers, including related entities
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCompany("Target");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		customerDto.setAccountDto(accountDto);
		
		// invoke existsByDto here
		Boolean exists = customerRepository.existsByDto(customerDto);
		customerRepository.flush();
		
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnLong() {
		log.info("whenCountByDto_thenReturnLong");
		
		// create customer dto with qualifiers using related entities
		CustomerDto customerDto = new CustomerDto();
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("Dallas");
		customerDto.setAccountDto(accountDto);
		
		// invoke countByDto here
		long customerCnt =  customerRepository.countByDto(customerDto);
		customerRepository.flush();
		
		assertThat(customerCnt).isGreaterThanOrEqualTo(2L);
	}

	@Test
	public void whenFindByDto_thenReturnCustomers() {
		log.info("whenFindByDto_thenReturnCustomers");
		
		// create customer dto with qualifiers, including related entities
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCompany("Superior Dry Cleaners");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("1234 Main St. Dallas Texas 75002");
		customerDto.setAccountDto(accountDto);
		
		// invoke findByDto here
		List<Customer> customers = customerRepository.findByDto(customerDto);
		customerRepository.flush();
		
		assertThat(customers).isNotEmpty();
		
		// get an customer having all related entities
		Optional<Customer> customer = customers.stream()
			.filter(c -> c.getAccount() != null)
			.findFirst();
		
		// test that customer exists and has matching qualifiers 
		assertThat(customer.orElse(null)).isNotNull();
		assertThat(customer.get().getCompany().equals(customerDto.getCompany())).isTrue();
		assertThat(customer.get().getAccount().getShippingAddress().equals(customerDto.getAccountDto().getShippingAddress())).isTrue();
	}

	@Test
	public void whenFindPageByDto_thenReturnCustomers() {
		log.info("whenFindPageByDto_thenReturnCustomers");
		
		// create customer dto with qualifiers
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCanContact(true);
		
		// limit search to first two customers 
		customerDto.setStart(0);
		customerDto.setLimit(2);
		
		// invoke findPageByDto here
		List<Customer> customers = customerRepository.findPageByDto(customerDto);
		customerRepository.flush();
		
		assertThat(customers.size()).isGreaterThan(0);
		assertThat(customers.size()).isLessThanOrEqualTo(2);
		
		// get an customer having account
		Optional<Customer> customer = customers.stream()
			.filter(c -> c.getAccount() != null)
			.findFirst();
		
		// check customer matches qualifier
		assertThat(customer.get().getCanContact().equals(customerDto.getCanContact())).isTrue();
		
		// check customer has account
		assertThat(customer.get().getAccount()).isNotNull();
	}

	@Test
	public void whenSearchByDto_thenReturnCustomers() {
		log.info("whenSearchByDto_thenReturnCustomers");
		
		// create customer dto with qualifiers
		CustomerDto customerDto = new CustomerDto();
		customerDto.setWorkPhone("555");
		
		
		// Add qualifier to related account
		AccountDto accountDto = new AccountDto();
		accountDto.setBillingAddress("Dallas");
		customerDto.setAccountDto(accountDto);
		
		
		// invoke searchByDto here
		List<Customer> customers = customerRepository.searchByDto(customerDto);
		customerRepository.flush();
		
		// check some such customers exist
		assertThat(customers).isNotEmpty();
		
		// find first with mandatory account
		Optional<Customer> customer = customers.stream()
			.filter(c -> c.getAccount() != null)
			.findFirst();
		
		// check customer/account match qualifiers 
		assertThat(customer.orElse(null)).isNotNull();
		assertThat(customer.get().getWorkPhone().contains(customerDto.getWorkPhone())).isTrue();
		assertThat(customer.get().getAccount().getBillingAddress().contains(customerDto.getAccountDto().getBillingAddress())).isTrue();
	}

	@Test
	public void whenSearchPageByDto_thenReturnCustomers() {
		log.info("whenSearchPageByDto_thenReturnCustomers");
		
		// create customer dto with qualifiers
		CustomerDto customerDto = new CustomerDto();
		customerDto.setWorkEmail(".com");
		
		// limit search to first two customers 
		customerDto.setStart(0);
		customerDto.setLimit(2);
		
		// invoke searchPageByDto here
		List<Customer> customers = customerRepository.searchPageByDto(customerDto);
		customerRepository.flush();
		
		assertThat(customers.size()).isGreaterThan(0);
		assertThat(customers.size()).isLessThanOrEqualTo(2);
		
		// get customer having account
		Optional<Customer> customer = customers.stream()
			.filter(c -> c.getAccount() != null)
			.findFirst();
		
		// check customer matches qualifiers 
		assertThat(customer.orElse(null)).isNotNull();
		assertThat(customer.get().getWorkEmail().contains(customerDto.getWorkEmail())).isTrue();
	}
}
