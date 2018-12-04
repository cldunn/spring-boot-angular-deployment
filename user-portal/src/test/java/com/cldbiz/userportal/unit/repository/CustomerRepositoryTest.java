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
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.customer.CustomerRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/termData.xml", "/accountData.xml", "/customerData.xml"})
public class CustomerRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 3L;
		
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AccountRepository accountRepository;

	@Test
	public void whenCount_thenReturnCount() {
		long customerCnt = customerRepository.count();
		assertThat(customerCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenDelete_thenRemoveCustomer() {
		List<Customer> customers = customerRepository.findAll();
		Customer customer = customers.get(0);
		
		customerRepository.delete(customer);
		
		customers = customerRepository.findAll();

		assertThat(customers.contains(customer)).isFalse();

		Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
		assertThat(account.orElse(null)).isNull();
	}

	@Test
	public void whenDeleteAll_thenRemoveAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		
		customerRepository.deleteAll(customers);
		
		long customerCnt = customerRepository.count();

		assertThat(customerCnt).isZero();
		
		customers.forEach(customer -> {
			Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
			assertThat(account.orElse(null)).isNull();
		});
	}
	
	@Test
	public void whenDeleteById_thenRemoveCustomer() {
		List<Customer> customers = customerRepository.findAll();
		Customer customer = customers.get(0);
		
		customerRepository.deleteById(customer.getId());
		
		customers = customerRepository.findAll();

		assertThat(customers.contains(customer)).isFalse();
		
		Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
		assertThat(account.orElse(null)).isNull();
	}

	@Test
	public void whenDeleteByIds_thenRemoveAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		
		List<Long> customerIds = customers.stream().map(Customer::getId).collect(Collectors.toList());
		
		customerRepository.deleteByIds(customerIds);
		
		long customerCnt = customerRepository.count();

		assertThat(customerCnt).isZero();

		customers.forEach(customer -> {
			Optional<Account> account = accountRepository.findById(customer.getAccount().getId());
			assertThat(account.orElse(null)).isNull();
		});
	}

	@Test
	public void whenExistsById_thenReturnTrue() {
		List<Customer> customers = customerRepository.findAll();
		Customer customer = customers.get(0);
		
		assertThat(customerRepository.existsById(customer.getId())).isTrue();
	}

	@Test
	public void whenFindAll_thenReturnAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		
		assertThat(customers.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(customers.get(0).getAccount()).isNotNull();
	}

	@Test
	public void whenFindAllById_thenReturnAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		List<Long> customerIds = customers.stream().map(Customer::getId).collect(Collectors.toList());
		
		customers = customerRepository.findAllById(customerIds);
		
		assertThat(customers.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(customers.get(0).getAccount()).isNotNull();
	}

	@Test
	public void whenFindById_thenReturnCustomer() {
		List<Customer> customers = customerRepository.findAll();
		
		Optional<Customer> customer = customers.stream()
				.filter(c -> c.getAccount() != null)
				.findFirst();
		
		assertThat(customer.orElse(null)).isNotNull();
		
		Optional<Customer> sameCustomer = customerRepository.findById(customer.get().getId());
		
		assertThat(sameCustomer.orElse(null)).isNotNull();
		assertThat(customer.get().equals(sameCustomer.get())).isTrue();
		
		assertThat(sameCustomer.get().getAccount()).isNotNull();
	}

	@Test
	public void whenSave_thenReturnSavedCustomer() {
		Customer anotherCustomer = getAnotherCustomer();
		
		Account anotherAccount = getAnotherAccount();
		anotherCustomer.setAccount(anotherAccount);
		
		Customer savedCustomer = customerRepository.save(anotherCustomer);
		assertThat(savedCustomer.equals(anotherCustomer)).isTrue();
		
		long customerCnt = customerRepository.count();
		assertThat(customerCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Customer> rtrvCustomer = customerRepository.findById(savedCustomer.getId());
		assertThat(rtrvCustomer.orElse(null)).isNotNull();
		assertThat(rtrvCustomer.get().equals(anotherCustomer)).isTrue();
		assertThat(rtrvCustomer.get().equals(savedCustomer)).isTrue();
		assertThat(rtrvCustomer.get().getAccount().equals(anotherAccount)).isTrue();
	}
	
	@Test
	public void whenSaveAll_thenReturnSavedCustomers() {
		Customer anotherCustomer = getAnotherCustomer();
		
		Account anotherAccount = getAnotherAccount();
		anotherCustomer.setAccount(anotherAccount);
		
		Customer extraCustomer = getExtraCustomer();
		
		Account extraAccount = getExtraAccount();
		extraCustomer.setAccount(extraAccount);
		
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(anotherCustomer);
		customers.add(extraCustomer);
		
		List<Customer> savedCustomers = customerRepository.saveAll(customers);
		
		assertThat(savedCustomers.size() == 2).isTrue();
		
		assertThat(customers.stream().allMatch(t -> savedCustomers.contains(t))).isTrue();
		assertThat(savedCustomers.stream().allMatch(t -> customers.contains(t))).isTrue();
		
		long customerCnt = customerRepository.count();
		assertThat(customerCnt).isEqualTo(TOTAL_ROWS + 2);
		
		Optional<Customer> rtrvAnotherCustomer = customerRepository.findById(anotherCustomer.getId());
		assertThat(rtrvAnotherCustomer.orElse(null)).isNotNull();
		assertThat(rtrvAnotherCustomer.get().equals(anotherCustomer)).isTrue();
		assertThat(rtrvAnotherCustomer.get().getAccount().equals(anotherAccount)).isTrue();
		
		Optional<Customer> rtrvExtaCustomer = customerRepository.findById(extraCustomer.getId());
		assertThat(rtrvExtaCustomer.orElse(null)).isNotNull();
		assertThat(rtrvExtaCustomer.get().equals(extraCustomer)).isTrue();
		assertThat(rtrvExtaCustomer.get().getAccount().equals(extraAccount)).isTrue();
	}
	
	@Test
	public void whenSaveAndFlush_thenReturnSavedCustomer() {
		Customer anotherCustomer = getAnotherCustomer();
		
		Account anotherAccount = getAnotherAccount();
		anotherCustomer.setAccount(anotherAccount);
		
		Customer savedCustomer = customerRepository.saveAndFlush(anotherCustomer);
		assertThat(savedCustomer.equals(anotherCustomer)).isTrue();
		
		long customerCnt = customerRepository.count();
		assertThat(customerCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Customer> rtrvCustomer = customerRepository.findById(savedCustomer.getId());
		assertThat(rtrvCustomer.orElse(null)).isNotNull();
		assertThat(rtrvCustomer.get().equals(anotherCustomer)).isTrue();
		assertThat(rtrvCustomer.get().equals(savedCustomer)).isTrue();
		assertThat(rtrvCustomer.get().getAccount().equals(anotherAccount)).isTrue();
	}

	@Test
	public void whenFindByDto_thenReturnCustomers() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCompany("Superior Dry Cleaners");
		
		List<Customer> customers = customerRepository.findByDto(customerDto);
		
		assertThat(customers).isNotEmpty();
		
		Optional<Customer> customer = customers.stream()
			.filter(c -> c.getAccount() != null)
			.findFirst();
		
		assertThat(customer.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindPageByDto_thenReturnCustomers() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCanContact(true);
		customerDto.setStart(0);
		customerDto.setLimit(2);
		
		List<Customer> customers = customerRepository.findPageByDto(customerDto);
		
		assertThat(customers.size()).isEqualTo(2);
		
		Optional<Customer> customer = customers.stream()
			.filter(c -> c.getAccount() != null)
			.findFirst();
		
		assertThat(customer.orElse(null)).isNotNull();
	}

	@Test
	public void whenSearchByDto_thenReturnCustomers() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setCompany("Cleaners");
		
		List<Customer> customers = customerRepository.searchByDto(customerDto);
		
		assertThat(customers).isNotEmpty();
		
		Optional<Customer> customer = customers.stream()
			.filter(c -> c.getAccount() != null)
			.findFirst();
		
		assertThat(customer.orElse(null)).isNotNull();
	}

	@Test
	public void whenSearchPageByDto_thenReturnCustomers() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setWorkEmail(".com");
		customerDto.setStart(0);
		customerDto.setLimit(2);
		
		List<Customer> customers = customerRepository.searchPageByDto(customerDto);
		
		assertThat(customers.size()).isEqualTo(2);
		
		Optional<Customer> customer = customers.stream()
			.filter(c -> c.getAccount() != null)
			.findFirst();
		
		assertThat(customer.orElse(null)).isNotNull();
	}

	private Customer getAnotherCustomer() {
		Customer anotherCustomer = new Customer();
		anotherCustomer.setFirstName("Jack");
		anotherCustomer.setLastName("Sprat");
		anotherCustomer.setWorkEmail("jack.sprat.yahoo.com");
		anotherCustomer.setWorkPhone("(555) 123-9876");
		anotherCustomer.setCanContact(true);
		
		return anotherCustomer;
	}

	private Customer getExtraCustomer() {
		Customer extraCustomer = new Customer();
		extraCustomer.setFirstName("Jane");
		extraCustomer.setLastName("Crowe");
		extraCustomer.setWorkEmail("jave.crowe.yahoo.com");
		extraCustomer.setWorkPhone("(555) 777-5487");
		extraCustomer.setCanContact(true);
		
		return extraCustomer;
	}
	
	private Account getAnotherAccount() {
		Account anotherAccount = new Account();
		anotherAccount.setAccountName("John Doe");
		anotherAccount.setCreditCard("2345678934564567");
		anotherAccount.setBillingAddress("258 Pelican Dr. Carrollton TX 23455");
		anotherAccount.setShippingAddress("258 Pelican Dr. Carrollton TX 23455");
		anotherAccount.setActive(true);
		
		 return anotherAccount;
	}

	private Account getExtraAccount() {
		Account extraAccount = new Account();
		extraAccount.setAccountName("Jane Doe");
		extraAccount.setCreditCard("7777888899991111");
		extraAccount.setBillingAddress("777 Absolute Dr. Carrollton TX 23455");
		extraAccount.setShippingAddress("777 Absolute Dr. Carrollton TX 23455");
		extraAccount.setActive(true);
		
		 return extraAccount;
	
	}
}
