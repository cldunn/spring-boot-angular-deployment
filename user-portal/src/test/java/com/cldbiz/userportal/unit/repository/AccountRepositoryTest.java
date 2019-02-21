package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.ContactDto;
import com.cldbiz.userportal.dto.CustomerDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.dto.PurchaseOrderDto;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.contact.ContactRepository;
import com.cldbiz.userportal.repository.customer.CustomerRepository;
import com.cldbiz.userportal.repository.invoice.InvoiceRepository;
import com.cldbiz.userportal.repository.purchaseOrder.PurchaseOrderRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.cldbiz.userportal.unit.repository.data.AccountData;
import com.cldbiz.userportal.unit.repository.data.ContactData;
import com.cldbiz.userportal.unit.repository.data.CustomerData;
import com.cldbiz.userportal.unit.repository.data.InvoiceData;
import com.cldbiz.userportal.unit.repository.data.PurchaseOrderData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/customerData.xml", "/invoiceData.xml", "/purchaseOrderData.xml"})
public class AccountRepositoryTest extends BaseRepositoryTest {
	
	private static final Long TOTAL_ROWS = 7L;
	
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;

	@Test
	public void whenStart_thenPopulateDatabase() {
		Boolean exists = accountRepository.existsById(101L);
		assertThat(exists).isTrue();
	}
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");
		
		List<Account> accounts = accountRepository.findAll();
		Account account = accounts.get(0);
		
		// clear cache to test performance
		accountRepository.flush();
		
		// invoke existsById here
		Boolean exists = accountRepository.existsById(account.getId());
		accountRepository.flush();
		
		// check for existence
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnCount() {
		log.info("whenCountAll_thenReturnCount");
		
		// invoke countAll here
		long accountCnt = accountRepository.countAll();
		accountRepository.flush();
		
		// check count
		assertThat(accountCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnAccount() {
		log.info("whenFindById_thenReturnAccount");
		
		// invoke findById here
		Optional<Account> sameAccount = accountRepository.findById(3L);
		accountRepository.flush();
		
		// check for account and related entities  
		assertThat(sameAccount.orElse(null)).isNotNull();
		assertThat(sameAccount.get().getContact()).isNotNull();
		assertThat(sameAccount.get().getCustomer()).isNotNull();
		assertThat(sameAccount.get().getInvoices().isEmpty()).isFalse();
		assertThat(sameAccount.get().getPurchaseOrders().isEmpty()).isFalse();
	}

	@Test
	public void whenFindByIds_thenReturnAccounts() {
		log.info("whenFindByIds_thenReturnAccounts");
		
		// get all account ids
		List<Account> accounts = accountRepository.findAll();
		List<Long> accountIds = accounts.stream().map(Account::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		accountRepository.flush();
		
		// invoke findByIds here
		accounts = accountRepository.findByIds(accountIds);
		accountRepository.flush();
		
		// check all accounts were found
		assertThat(accounts.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check contact/customer exist
		assertThat(accounts.get(0).getContact()).isNotNull();
		assertThat(accounts.get(0).getCustomer()).isNotNull();
		
		/* check at least one test account has invoices */
		Optional<Account> invoicedAccount = accounts.stream().
			    filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty())).
			    findFirst();
		
		assertThat(invoicedAccount.orElse(null)).isNotNull();
		
		/* check at least one test account has purchaseOrders */
		Optional<Account> purcheOrderdAccount = accounts.stream().
			    filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty())).
			    findAny();
		
		assertThat(purcheOrderdAccount.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindAll_thenReturnAllAccounts() {
		log.info("whenFindAll_thenReturnAllAccounts");
		
		// invoke findAll here
		List<Account> accounts = accountRepository.findAll();
		accountRepository.flush();
		
		assertThat(accounts.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check contact/customer exist
		assertThat(accounts.get(0).getContact()).isNotNull();
		assertThat(accounts.get(0).getCustomer()).isNotNull();
		
		/* check at least one test account has invoices */
		Optional<Account> invoicedAccount = accounts.stream().
			    filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty())).
			    findFirst();
		
		assertThat(invoicedAccount.orElse(null)).isNotNull();
		
		/* check at least one test account has purchaseOrders */
		Optional<Account> purcheOrderdAccount = accounts.stream().
			    filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty())).
			    findFirst();
		
		assertThat(purcheOrderdAccount.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteById_thenRemoveAccount() {
		log.info("whenDeleteById_thenRemoveAccount");
		
		Account account = accountRepository.findById(1L).get();
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteById here
		accountRepository.deleteById(account.getId());
		accountRepository.flush();
		
		// check account deleted
		List<Account> accounts = accountRepository.findAll();
		assertThat(accounts.contains(account)).isFalse();
		
		/* check delete cascaded for account contact*/
		Optional<Contact> contact = contactRepository.findById(account.getContact().getId());
		assertThat(contact.orElse(null)).isNull();

		/* check delete cascaded for account customer*/
		Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
		assertThat(customer.orElse(null)).isNull();

		/* check delete cascaded for account invoices */
		List<Long> invoiceIds = account.getInvoices().stream().map(Invoice::getId).collect(Collectors.toList());
		List<Invoice> invoices = invoiceRepository.findByIds(invoiceIds);
		assertThat(invoices.isEmpty());
		
		/* check delete cascaded for account purchase orders */
		List<Long> purchaseOrderIds = account.getPurchaseOrders().stream().map(PurchaseOrder::getId).collect(Collectors.toList());
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByIds(purchaseOrderIds);
		assertThat(purchaseOrders.isEmpty());
	}

	@Test
	public void whenDeleteByIds_thenRemoveAllAccounts() {
		log.info("whenDeleteByIds_thenRemoveAllAccounts");
		
		// get all account ids
		List<Account> accounts = accountRepository.findAll();
		List<Long> accountIds = accounts.stream().map(Account::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteByIds here
		accountRepository.deleteByIds(accountIds);
		accountRepository.flush();
		
		// check all accounts deleted
		long accountCnt = accountRepository.countAll();
		assertThat(accountCnt).isZero();

		accounts.forEach(account -> {
			/* check delete cascaded for each account contact*/
			Optional<Contact> contact = contactRepository.findById(account.getContact().getId());
			assertThat(contact.orElse(null)).isNull();

			/* check delete cascaded for each account customer */
			Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
			assertThat(customer.orElse(null)).isNull();
			
			/* check delete cascaded for each account's invoices */
			List<Long> invoiceIds = account.getInvoices().stream().map(Invoice::getId).collect(Collectors.toList());
			List<Invoice> invoices = invoiceRepository.findByIds(invoiceIds);
			assertThat(invoices.isEmpty());
			
			/* check delete cascaded for each account's purchase orders */
			List<Long> purchaseOrderIds = account.getPurchaseOrders().stream().map(PurchaseOrder::getId).collect(Collectors.toList());
			List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByIds(purchaseOrderIds);
			assertThat(purchaseOrders.isEmpty());
		});
	}

	@Test
	public void whenDeleteByEntity_thenRemoveAccount() {
		log.info("whenDeleteByEntity_thenRemoveAccount");
		
		Account account = accountRepository.findById(3L).get();

		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteByEntity here
		accountRepository.deleteByEntity(account);
		accountRepository.flush();
		
		// check account deleted
		List<Account> accounts = accountRepository.findAll();
		assertThat(accounts.contains(account)).isFalse();

		/* check delete cascaded for account contact*/
		Optional<Contact> contact = contactRepository.findById(account.getContact().getId());
		assertThat(contact.orElse(null)).isNull();

		/* check delete cascaded for account customer */
		Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
		assertThat(customer.orElse(null)).isNull();
		
		/* check delete cascaded for account's invoices */
		List<Long> invoiceIds = account.getInvoices().stream().map(Invoice::getId).collect(Collectors.toList());
		List<Invoice> invoices = invoiceRepository.findByIds(invoiceIds);
		assertThat(invoices.isEmpty());
		
		/* check delete cascaded for account's purchase orders */
		List<Long> purchaseOrderIds = account.getPurchaseOrders().stream().map(PurchaseOrder::getId).collect(Collectors.toList());
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByIds(purchaseOrderIds);
		assertThat(purchaseOrders.isEmpty());
	}
	
	@Test
	public void whenDeleteByEntities_thenRemoveAccounts() {
		log.info("whenDeleteByEntities_thenRemoveAccounts");
		
		List<Account> accounts = accountRepository.findAll();
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteByEntities here
		accountRepository.deleteByEntities(accounts);
		accountRepository.flush();
		
		// check all accunts deleted
		long accountCnt = accountRepository.countAll();
		assertThat(accountCnt).isZero();
		
		accounts.forEach(account -> {
			/* check delete cascaded for each account contact */
			Optional<Contact> contact = contactRepository.findById(account.getContact().getId());
			assertThat(contact.orElse(null)).isNull();

			/* check delete cascaded for each account customer */
			Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
			assertThat(customer.orElse(null)).isNull();
			
			/* check delete cascaded for each account's invoices */
			List<Long> invoiceIds = account.getInvoices().stream().map(Invoice::getId).collect(Collectors.toList());
			List<Invoice> invoices = invoiceRepository.findByIds(invoiceIds);
			assertThat(invoices.isEmpty());
			
			/* check delete cascaded for each account's purchase order */
			List<Long> purchaseOrderIds = account.getPurchaseOrders().stream().map(PurchaseOrder::getId).collect(Collectors.toList());
			List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByIds(purchaseOrderIds);
			assertThat(purchaseOrders.isEmpty());
		});
	}
	
	@Test
	public void whenSaveByEntity_thenReturnSavedAccount() {
		log.info("whenSaveByEntity_thenReturnSavedAccount");
		
		// create new account
		Account anotherAccount = AccountData.getAnotherAccount();
		
		// add new contact to account
		Contact anotherContact = ContactData.getAnotherContact();
		anotherAccount.setContact(anotherContact);
		
		// add new customer to account
		Customer anotherCustomer = CustomerData.getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);
		
		// add new invoices to account 
		List<Invoice> someInvoices = InvoiceData.getSomeInvoices();
		someInvoices.forEach(i -> i.setAccount(anotherAccount));
		anotherAccount.setInvoices(someInvoices);
		
		// add new purchase orders to account
		List<PurchaseOrder> somePurchaseOrders = PurchaseOrderData.getSomePurchaseOrders();
		somePurchaseOrders.forEach(po -> po.setAccount(anotherAccount));
		anotherAccount.setPurchaseOrders(somePurchaseOrders);
		
		// invoke saveByEntity here
		Account savedAccount = accountRepository.saveEntity(anotherAccount);
		accountRepository.flush();
		
		// confirmed persisted account returned
		assertThat(savedAccount.equals(anotherAccount)).isTrue();
		
		// confirmed account persisted
		long accountCnt = accountRepository.countAll();
		assertThat(accountCnt).isEqualTo(TOTAL_ROWS + 1);
		
		// retrieve saved account from store
		Optional<Account> rtrvAccount = accountRepository.findById(savedAccount.getId());
		assertThat(rtrvAccount.orElse(null)).isNotNull();
		
		/* use lombock equals to check account retrieved */
		assertThat(rtrvAccount.get().equals(anotherAccount)).isTrue();

		/* use lombock equals to check account returned by saveEntity */
		assertThat(rtrvAccount.get().equals(savedAccount)).isTrue();
		
		/* use lombock equals to check contact saved */
		assertThat(rtrvAccount.get().getContact().equals(anotherContact)).isTrue();

		/* use lombock equals to check customer saved */
		assertThat(rtrvAccount.get().getCustomer().equals(anotherCustomer)).isTrue();

		/* use stream allMatch to check invoices were saved */
		assertThat(Boolean.FALSE.equals(rtrvAccount.get().getInvoices().isEmpty()));
		assertThat(rtrvAccount.get().getInvoices().stream().allMatch(t -> someInvoices.contains(t))).isTrue();
		assertThat(someInvoices.stream().allMatch(t -> rtrvAccount.get().getInvoices().contains(t))).isTrue();

		/* use stream allMatch to check purchase orders were saved */
		assertThat(Boolean.FALSE.equals(rtrvAccount.get().getPurchaseOrders().isEmpty()));
		assertThat(rtrvAccount.get().getPurchaseOrders().stream().allMatch(t -> somePurchaseOrders.contains(t))).isTrue();
		assertThat(somePurchaseOrders.stream().allMatch(t -> rtrvAccount.get().getPurchaseOrders().contains(t))).isTrue();
	}

	@Test
	public void whenSaveEntities_thenReturnSavedAccounts() {
		log.info("whenSaveEntities_thenReturnSavedAccounts");
		
		// create 1st new account
		Account anotherAccount = AccountData.getAnotherAccount();
		
		// add new contact to 1st account
		Contact anotherContact = ContactData.getAnotherContact();
		anotherAccount.setContact(anotherContact);
		
		// add new customer to 1st account
		Customer anotherCustomer = CustomerData.getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);
		
		// add new invoices to 1st account
		List<Invoice> someInvoices = InvoiceData.getSomeInvoices();
		someInvoices.forEach(i -> i.setAccount(anotherAccount));
		anotherAccount.setInvoices(someInvoices);
		
		// add new purchase orders to 1st account
		List<PurchaseOrder> somePurchaseOrders = PurchaseOrderData.getSomePurchaseOrders();
		somePurchaseOrders.forEach(po -> po.setAccount(anotherAccount));
		anotherAccount.setPurchaseOrders(somePurchaseOrders);
		
		// create 2nd new account
		Account extraAccount = AccountData.getExtraAccount();
		
		// add new contact to 2nd account
		Contact extraContact = ContactData.getExtraContact();
		extraAccount.setContact(extraContact);
		
		// add new customer to 2nd account
		Customer extraCustomer = CustomerData.getExtraCustomer();
		extraCustomer.setAccount(extraAccount);
		extraAccount.setCustomer(extraCustomer);
		
		// add new invoices to 2nd account
		List<Invoice> moreInvoices = InvoiceData.getMoreInvoices();
		moreInvoices.forEach(i -> i.setAccount(extraAccount));
		extraAccount.setInvoices(moreInvoices);
		
		// add new purchase orders to 2nd account
		List<PurchaseOrder> morePurchaseOrders = PurchaseOrderData.getMorePurchaseOrders();
		morePurchaseOrders.forEach(po -> po.setAccount(extraAccount));
		extraAccount.setPurchaseOrders(morePurchaseOrders);
		
		// create array of new accounts
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(anotherAccount);
		accounts.add(extraAccount);
		
		// invoke saveByEntities here
		List<Account> savedAccounts = accountRepository.saveEntities(accounts);
		accountRepository.flush();
		
		assertThat(savedAccounts.size() == 2).isTrue();
		
		// check new accounts returned from saveEntities
		assertThat(accounts.stream().allMatch(t -> savedAccounts.contains(t))).isTrue();
		assertThat(savedAccounts.stream().allMatch(t -> accounts.contains(t))).isTrue();
		
		// check new accounts added
		long accountCnt = accountRepository.countAll();
		assertThat(accountCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// retrieve saved account from store
		Optional<Account> rtrvAnotherAccount = accountRepository.findById(anotherAccount.getId());
		assertThat(rtrvAnotherAccount.orElse(null)).isNotNull();
		
		/* use lombock equals to check 1st account saved */
		assertThat(rtrvAnotherAccount.get().equals(anotherAccount)).isTrue();
		
		/* use lombock equals to check 1st account's contact saved */
		assertThat(rtrvAnotherAccount.get().getContact().equals(anotherContact)).isTrue();

		/* use lombock equals to check 1st account's customer saved */
		assertThat(rtrvAnotherAccount.get().getCustomer().equals(anotherCustomer)).isTrue();
		
		/* use stream allMatch to check 1st account's invoices saved */
		assertThat(Boolean.FALSE.equals(rtrvAnotherAccount.get().getInvoices().isEmpty()));
		assertThat(rtrvAnotherAccount.get().getInvoices().stream().allMatch(t -> someInvoices.contains(t))).isTrue();
		assertThat(someInvoices.stream().allMatch(t -> rtrvAnotherAccount.get().getInvoices().contains(t))).isTrue();

		/* use stream allMatch to check 1st account's purchase orders saved */
		assertThat(Boolean.FALSE.equals(rtrvAnotherAccount.get().getPurchaseOrders().isEmpty()));
		assertThat(rtrvAnotherAccount.get().getPurchaseOrders().stream().allMatch(t -> somePurchaseOrders.contains(t))).isTrue();
		assertThat(somePurchaseOrders.stream().allMatch(t -> rtrvAnotherAccount.get().getPurchaseOrders().contains(t))).isTrue();
		
		/* check new 2nd account received id */
		Optional<Account> rtrvExtaAccount = accountRepository.findById(extraAccount.getId());
		assertThat(rtrvExtaAccount.orElse(null)).isNotNull();
		
		/* use lombock equals to check 2nd account saved */
		assertThat(rtrvExtaAccount.get().equals(extraAccount)).isTrue();
		
		/* use lombock equals to check 2nd account's contact saved */
		assertThat(rtrvExtaAccount.get().getContact().equals(extraContact)).isTrue();
		
		/* use lombock equals to check 2nd account's customer saved */
		assertThat(rtrvExtaAccount.get().getCustomer().equals(extraCustomer)).isTrue();
		
		/* use stream allMatch to check 2nd account's invoices saved */
		assertThat(Boolean.FALSE.equals(rtrvExtaAccount.get().getInvoices().isEmpty()));
		assertThat(rtrvExtaAccount.get().getInvoices().stream().allMatch(t -> moreInvoices.contains(t))).isTrue();
		assertThat(moreInvoices.stream().allMatch(t -> rtrvExtaAccount.get().getInvoices().contains(t))).isTrue();

		/* use stream allMatch to check 2nd account's purchase orders saved */
		assertThat(Boolean.FALSE.equals(rtrvExtaAccount.get().getPurchaseOrders().isEmpty()));
		assertThat(rtrvExtaAccount.get().getPurchaseOrders().stream().allMatch(t -> morePurchaseOrders.contains(t))).isTrue();
		assertThat(morePurchaseOrders.stream().allMatch(t -> rtrvExtaAccount.get().getPurchaseOrders().contains(t))).isTrue();
	}

	@Test
	public void whenModified_thenAccountUpdated() {
		log.info("whenModified_thenAccountUpdated");
		
		// retrieve account
		Optional<Account> originalAccount = accountRepository.findById(3L);
		
		// update account and related entities
		originalAccount.get().setBillingAddress("UPDATED - " + originalAccount.get().getBillingAddress());
		originalAccount.get().getContact().setName("UPDATED - " + originalAccount.get().getContact().getName());
		originalAccount.get().getCustomer().setFirstName("UPDATED - " + originalAccount.get().getCustomer().getFirstName());
		originalAccount.get().getInvoices().get(0).setInvoiceNbr("UPDATED - " + originalAccount.get().getInvoices().get(0).getInvoiceNbr());
		originalAccount.get().getPurchaseOrders().get(0).setOrderIdentifier("UPDATED - " + originalAccount.get().getPurchaseOrders().get(0).getOrderIdentifier());
		
		// retrieve account again 
		Optional<Account> rtrvdAccount = accountRepository.findById(3L);
		
		// check account and related entities updated without save
		assertThat(originalAccount.get().getBillingAddress().equals((rtrvdAccount.get().getBillingAddress())));
		assertThat(originalAccount.get().getContact().getName().equals((rtrvdAccount.get().getContact().getName())));
		assertThat(originalAccount.get().getCustomer().getFirstName().equals((rtrvdAccount.get().getCustomer().getFirstName())));
		assertThat(originalAccount.get().getInvoices().get(0).getInvoiceNbr().equals((rtrvdAccount.get().getInvoices().get(0).getInvoiceNbr())));
		assertThat(originalAccount.get().getPurchaseOrders().get(0).getOrderIdentifier().equals((rtrvdAccount.get().getPurchaseOrders().get(0).getOrderIdentifier())));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info("whenExistsByDto_thenReturnTrue");
		
		// create account dto for search criteria
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Superior Dry Cleaners");
		
		ContactDto suportDto = new ContactDto();
		suportDto.setName("Tom Anderson");
		accountDto.setContactDto(suportDto);
		
		CustomerDto customerDto = new CustomerDto();
		customerDto.setWorkEmail("alex.crowe.yahoo.com");
		accountDto.setCustomerDto(customerDto);
		
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		accountDto.asParam.setInvoiceDto(invoiceDto);
		
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setStatus("SHIPPED");
		accountDto.asParam.setPurchaseOrderDto(purchaseOrderDto);

		// invoke existsByDto here
		Boolean exists = accountRepository.existsByDto(accountDto);
		accountRepository.flush();
		
		// check existence
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		log.info("whenCountByDto_thenReturnCount");
		
		// create account dto for search criteria
		AccountDto accountDto = new AccountDto();
		accountDto.setBillingAddress("Dallas");
		
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		accountDto.asParam.setInvoiceDto(invoiceDto);

		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setStatus("SHIPPED");
		accountDto.asParam.setPurchaseOrderDto(purchaseOrderDto);

		// invoke countByDto here
		long accountCnt =  accountRepository.countByDto(accountDto);
		accountRepository.flush();
		
		// check count
		assertThat(accountCnt).isGreaterThanOrEqualTo(2L);
	}
	
	@Test
	public void whenFindByDto_thenReturnAccounts() {
		log.info("whenFindByDto_thenReturnAccounts");
		
		// create account dto for search criteria
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Superior Dry Cleaners");
		
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Tom Anderson");
		accountDto.setContactDto(contactDto);
		
		CustomerDto customerDto = new CustomerDto();
		customerDto.setWorkEmail("alex.crowe.yahoo.com");
		accountDto.setCustomerDto(customerDto);
		
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		accountDto.asParam.setInvoiceDto(invoiceDto);

		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setStatus("SHIPPED");
		accountDto.asParam.setPurchaseOrderDto(purchaseOrderDto);

		// invoke findByDto here
		List<Account> accounts = accountRepository.findByDto(accountDto);
		accountRepository.flush();
		
		assertThat(accounts).isNotEmpty();
		
		// get an account having all related entities
		Optional<Account> account = accounts.stream()
			.filter(a -> a.getContact() != null)
			.filter(a -> a.getCustomer() != null)
			.filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty()))
			.filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty()))
			.findAny();
		
		// test that account exists and has matching qualifiers 
		assertThat(account.orElse(null)).isNotNull();
		assertThat(account.get().getAccountName().equals(accountDto.getAccountName())).isTrue();
		assertThat(account.get().getContact().getName().equals(contactDto.getName())).isTrue();
		assertThat(account.get().getCustomer().getWorkEmail().equals(accountDto.getCustomerDto().getWorkEmail())).isTrue();
		assertThat(account.get().getInvoices().get(0).getStatus().equals(invoiceDto.getStatus())).isTrue();
		assertThat(account.get().getPurchaseOrders().get(0).getStatus().equals(purchaseOrderDto.getStatus())).isTrue();
	}

	@Test
	public void whenFindPageByDto_thenReturnAccounts() {
		log.info("whenFindPageByDto_thenReturnAccounts");
		
		// create account dto for search criteria
		AccountDto accountDto = new AccountDto();
		accountDto.setActive(true);
		
		// limit to first two accounts 
		accountDto.setStart(0);
		accountDto.setLimit(2);
		
		// qualify related invoices
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		accountDto.asParam.setInvoiceDto(invoiceDto);

		// qualify related purchase orders
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setStatus("SHIPPED");
		accountDto.asParam.setPurchaseOrderDto(purchaseOrderDto);

		// invoke findPageByDto here
		List<Account> accounts = accountRepository.findPageByDto(accountDto);
		accountRepository.flush();
		
		assertThat(accounts.size()).isLessThanOrEqualTo(2);
		
		// get an account having all related entities
		Optional<Account> account = accounts.stream()
			.filter(a -> a.getContact() != null)
			.filter(a -> a.getCustomer() != null)
			.filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty()))
			.filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty()))
			.findFirst();
		
		// test that account exists and has matching qualifiers 
		assertThat(account.orElse(null)).isNotNull();
		assertThat(account.get().getActive().equals(accountDto.getActive())).isTrue();
		assertThat(account.get().getInvoices().get(0).getStatus().equals(invoiceDto.getStatus())).isTrue();
		assertThat(account.get().getPurchaseOrders().get(0).getStatus().equals(purchaseOrderDto.getStatus())).isTrue();

	}

	@Test
	public void whenSearchByDto_thenReturnAccounts() {
		log.info("whenSearchByDto_thenReturnAccounts");
		
		// create account dto for search criteria
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Superior");
		
		// qualify related contact
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Anderson");
		accountDto.setContactDto(contactDto);
		
		// qualify related customer
		CustomerDto customerDto = new CustomerDto();
		customerDto.setWorkEmail("yahoo.com");
		accountDto.setCustomerDto(customerDto);
		
		// qualify related invoices
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		accountDto.asParam.setInvoiceDto(invoiceDto);

		// qualify related purchase orders
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setStatus("SHIPPED");
		accountDto.asParam.setPurchaseOrderDto(purchaseOrderDto);

		// invoke searchByDto here
		List<Account> accounts = accountRepository.searchByDto(accountDto);
		accountRepository.flush();
		
		assertThat(accounts).isNotEmpty();
		
		// get an account having all related entities
		Optional<Account> account = accounts.stream()
			.filter(a -> a.getContact() != null)
			.filter(a -> a.getCustomer() != null)
			.filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty()))
			.filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty()))
			.findFirst();
		
		// test that account exists and has matching qualifiers 
		assertThat(account.orElse(null)).isNotNull();
		assertThat(account.get().getAccountName().contains(accountDto.getAccountName())).isTrue();
		assertThat(account.get().getContact().getName().contains(contactDto.getName())).isTrue();
		assertThat(account.get().getCustomer().getWorkEmail().contains(customerDto.getWorkEmail())).isTrue();
		assertThat(account.get().getInvoices().get(0).getStatus().contains(invoiceDto.getStatus())).isTrue();
		assertThat(account.get().getPurchaseOrders().get(0).getStatus().contains(purchaseOrderDto.getStatus())).isTrue();
	}

	@Test
	public void whenSearchPageByDto_thenReturnAccounts() {
		log.info("whenSearchPageByDto_thenReturnAccounts");
		
		// create account dto for search
		AccountDto accountDto = new AccountDto();
		accountDto.setBillingAddress("Dallas");
		
		// limit to 2 accounts max 
		accountDto.setStart(0);
		accountDto.setLimit(2);
		
		// qualify related contact
		ContactDto contactDto = new ContactDto();
		contactDto.setName("Anderson");
		accountDto.setContactDto(contactDto);
		
		// qualify related customer
		CustomerDto customerDto = new CustomerDto();
		customerDto.setWorkEmail("yahoo.com");
		accountDto.setCustomerDto(customerDto);
		
		// qualify related invoices
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		accountDto.asParam.setInvoiceDto(invoiceDto);

		// qualify related purchase orders
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setStatus("SHIPPED");
		accountDto.asParam.setPurchaseOrderDto(purchaseOrderDto);

		// invoke searchPageByDto here
		List<Account> accounts = accountRepository.searchPageByDto(accountDto);
		accountRepository.flush();
		
		assertThat(accounts.size()).isLessThanOrEqualTo(2);
		
		// get an account having all related entities
		Optional<Account> account = accounts.stream()
			.filter(a -> a.getContact() != null)
			.filter(a -> a.getCustomer() != null)
			.filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty()))
			.filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty()))
			.findFirst();
		
		assertThat(account.orElse(null)).isNotNull();
		assertThat(account.get().getBillingAddress().contains(accountDto.getBillingAddress())).isTrue();
		assertThat(account.get().getContact().getName().contains(contactDto.getName())).isTrue();
		assertThat(account.get().getCustomer().getWorkEmail().contains(customerDto.getWorkEmail())).isTrue();
		assertThat(account.get().getInvoices().get(0).getStatus().contains(invoiceDto.getStatus())).isTrue();
		assertThat(account.get().getPurchaseOrders().get(0).getStatus().contains(purchaseOrderDto.getStatus())).isTrue();
	}

}
