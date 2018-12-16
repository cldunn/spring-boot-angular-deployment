package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.domain.Term;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.TermDto;
import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.customer.CustomerRepository;
import com.cldbiz.userportal.repository.term.TermRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/termData.xml", "/accountData.xml", "/customerData.xml", "/invoiceData.xml", "/purchaseOrderData.xml"})
public class AccountRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	TermRepository termRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AccountRepository accountRepository;

	@Test
	public void whenCount_thenReturnCount() {
		long accountCnt = accountRepository.count();
		accountRepository.flush();
		
		assertThat(accountCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenDelete_thenRemoveAccount() {
		List<Account> accounts = accountRepository.findAll();
		Account account = accounts.stream().filter(a -> a.getId().equals(3L)).findFirst().get();
		
		accountRepository.delete(account);
		accountRepository.flush();
		
		accounts = accountRepository.findAll();
		
		assertThat(accounts.contains(account)).isFalse();

		Optional<Term> term = termRepository.findById(account.getTerm().getId());
		assertThat(term.orElse(null)).isNotNull();

		Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
		assertThat(customer.orElse(null)).isNull();
		
		// TODO check associated invoice deleted too
		// TODO check associated purchaseOrder too
	}
	
	@Test
	public void whenDeleteAll_thenRemoveAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		
		accountRepository.deleteAll(accounts);
		accountRepository.flush();
		
		long accountCnt = accountRepository.count();

		assertThat(accountCnt).isZero();
		
		accounts.forEach(account -> {
			Optional<Term> term = termRepository.findById(account.getTerm().getId());
			assertThat(term.orElse(null)).isNotNull();

			Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
			assertThat(customer.orElse(null)).isNull();
		});
		
		// TODO check associated invoice deleted too
		// TODO check associated purchaseOrder too

	}
	
	@Test
	public void whenDeleteById_thenRemoveAccount() {
		List<Account> accounts = accountRepository.findAll();
		Account account = accounts.get(0);
		
		accountRepository.deleteById(account.getId());
		accountRepository.flush();
		
		accounts = accountRepository.findAll();

		assertThat(accounts.contains(account)).isFalse();
		
		Optional<Term> term = termRepository.findById(account.getTerm().getId());
		assertThat(term.orElse(null)).isNotNull();

		Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
		assertThat(customer.orElse(null)).isNull();

		// TODO check associated invoice deleted too
		// TODO check associated purchaseOrder too
	}


	@Test
	public void whenDeleteByIds_thenRemoveAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		
		List<Long> accountIds = accounts.stream().map(Account::getId).collect(Collectors.toList());
		
		accountRepository.deleteByIds(accountIds);
		accountRepository.flush();
		
		long accountCnt = accountRepository.count();

		assertThat(accountCnt).isZero();

		accounts.forEach(account -> {
			Optional<Term> term = termRepository.findById(account.getTerm().getId());
			assertThat(term.orElse(null)).isNotNull();

			Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
			assertThat(customer.orElse(null)).isNull();
		});

		// TODO check associated invoice deleted too
		// TODO check associated purchaseOrder too
	}

	@Test
	public void whenExistsById_thenReturnTrue() {
		List<Account> accounts = accountRepository.findAll();
		Account account = accounts.get(0);
		
		Boolean exists = accountRepository.existsById(account.getId());
		accountRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenFindAll_thenReturnAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		accountRepository.flush();
		
		assertThat(accounts.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(accounts.get(0).getTerm()).isNotNull();
		assertThat(accounts.get(0).getCustomer()).isNotNull();
		
		Optional<Account> invoicedAccount = accounts.stream().
			    filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty())).
			    findFirst();
		
		assertThat(invoicedAccount.orElse(null)).isNotNull();
		
		Optional<Account> purcheOrderdAccount = accounts.stream().
			    filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty())).
			    findFirst();
		
		assertThat(purcheOrderdAccount.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindAllById_thenReturnAllAccounts() {
		List<Account> accounts = accountRepository.findAll();
		List<Long> accountIds = accounts.stream().map(Account::getId).collect(Collectors.toList());
		
		accounts = accountRepository.findAllById(accountIds);
		
		assertThat(accounts.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(accounts.get(0).getTerm()).isNotNull();
		assertThat(accounts.get(0).getCustomer()).isNotNull();
		
		Optional<Account> invoicedAccount = accounts.stream().
			    filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty())).
			    findFirst();
		
		assertThat(invoicedAccount.orElse(null)).isNotNull();
		
		Optional<Account> purcheOrderdAccount = accounts.stream().
			    filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty())).
			    findFirst();
		
		assertThat(purcheOrderdAccount.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindById_thenReturnAccount() {
		Optional<Account> sameAccount = accountRepository.findById(3L);
		accountRepository.flush();
		
		assertThat(sameAccount.orElse(null)).isNotNull();
		
		assertThat(sameAccount.get().getTerm()).isNotNull();
		assertThat(sameAccount.get().getCustomer()).isNotNull();
		assertThat(sameAccount.get().getInvoices().isEmpty()).isFalse();
		assertThat(sameAccount.get().getPurchaseOrders().isEmpty()).isFalse();
	}

	@Test
	public void whenSave_thenReturnSavedAccount() {
		Account anotherAccount = getAnotherAccount();
		
		Term anotherTerm = getAnotherTerm();
		anotherAccount.setTerm(anotherTerm);
		
		Customer anotherCustomer = getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);
		
		List<Invoice> someInvoices = getSomeInvoices();
		someInvoices.forEach(i -> i.setAccount(anotherAccount));
		anotherAccount.setInvoices(someInvoices);
		
		List<PurchaseOrder> somePurchaseOrders = getSomePurchaseOrders();
		somePurchaseOrders.forEach(po -> po.setAccount(anotherAccount));
		anotherAccount.setPurchaseOrders(somePurchaseOrders);
		
		Account savedAccount = accountRepository.save(anotherAccount);
		accountRepository.flush();
		
		assertThat(savedAccount.equals(anotherAccount)).isTrue();
		
		long accountCnt = accountRepository.count();
		assertThat(accountCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Account> rtrvAccount = accountRepository.findById(savedAccount.getId());
		assertThat(rtrvAccount.orElse(null)).isNotNull();
		assertThat(rtrvAccount.get().equals(anotherAccount)).isTrue();
		assertThat(rtrvAccount.get().equals(savedAccount)).isTrue();
		assertThat(rtrvAccount.get().getTerm().equals(anotherTerm)).isTrue();
		assertThat(rtrvAccount.get().getCustomer().equals(anotherCustomer)).isTrue();

		assertThat(Boolean.FALSE.equals(rtrvAccount.get().getInvoices().isEmpty()));
		assertThat(rtrvAccount.get().getInvoices().stream().allMatch(t -> someInvoices.contains(t))).isTrue();
		assertThat(someInvoices.stream().allMatch(t -> rtrvAccount.get().getInvoices().contains(t))).isTrue();

		assertThat(Boolean.FALSE.equals(rtrvAccount.get().getPurchaseOrders().isEmpty()));
		assertThat(rtrvAccount.get().getPurchaseOrders().stream().allMatch(t -> somePurchaseOrders.contains(t))).isTrue();
		assertThat(somePurchaseOrders.stream().allMatch(t -> rtrvAccount.get().getPurchaseOrders().contains(t))).isTrue();
	}
	
	@Test
	public void whenModified_thenAccountUpdated() {
		Optional<Account> originalAccount = accountRepository.findById(3L);
		originalAccount.get().setBillingAddress("UPDATED - " + originalAccount.get().getBillingAddress());
		originalAccount.get().getTerm().setDescription("UPDATED - " + originalAccount.get().getTerm().getDescription());
		originalAccount.get().getCustomer().setFirstName("UPDATED - " + originalAccount.get().getCustomer().getFirstName());
		originalAccount.get().getInvoices().get(0).setInvoiceNbr("UPDATED - " + originalAccount.get().getInvoices().get(0).getInvoiceNbr());
		originalAccount.get().getPurchaseOrders().get(0).setOrderIdentifier("UPDATED - " + originalAccount.get().getPurchaseOrders().get(0).getOrderIdentifier());
		
		Optional<Account> rtrvdAccount = accountRepository.findById(3L);
		assertThat(originalAccount.get().getBillingAddress().equals((rtrvdAccount.get().getBillingAddress())));
		assertThat(originalAccount.get().getTerm().getDescription().equals((rtrvdAccount.get().getTerm().getDescription())));
		assertThat(originalAccount.get().getCustomer().getFirstName().equals((rtrvdAccount.get().getCustomer().getFirstName())));
		assertThat(originalAccount.get().getInvoices().get(0).getInvoiceNbr().equals((rtrvdAccount.get().getInvoices().get(0).getInvoiceNbr())));
		assertThat(originalAccount.get().getPurchaseOrders().get(0).getOrderIdentifier().equals((rtrvdAccount.get().getPurchaseOrders().get(0).getOrderIdentifier())));
	}

	@Test
	public void whenSaveAll_thenReturnSavedAccounts() {
		Account anotherAccount = getAnotherAccount();
		
		Term anotherTerm = getAnotherTerm();
		anotherAccount.setTerm(anotherTerm);
		
		Customer anotherCustomer = getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);
		
		List<Invoice> someInvoices = getSomeInvoices();
		someInvoices.forEach(i -> i.setAccount(anotherAccount));
		anotherAccount.setInvoices(someInvoices);
		
		List<PurchaseOrder> somePurchaseOrders = getSomePurchaseOrders();
		somePurchaseOrders.forEach(po -> po.setAccount(anotherAccount));
		anotherAccount.setPurchaseOrders(somePurchaseOrders);
		
		Account extraAccount = getExtraAccount();
		
		Term extraTerm = getExtraTerm();
		extraAccount.setTerm(extraTerm);
		
		Customer extraCustomer = getExtraCustomer();
		extraCustomer.setAccount(extraAccount);
		extraAccount.setCustomer(extraCustomer);
		
		List<Invoice> moreInvoices = getMoreInvoices();
		moreInvoices.forEach(i -> i.setAccount(extraAccount));
		extraAccount.setInvoices(moreInvoices);
		
		List<PurchaseOrder> morePurchaseOrders = getMorePurchaseOrders();
		morePurchaseOrders.forEach(po -> po.setAccount(extraAccount));
		extraAccount.setPurchaseOrders(morePurchaseOrders);
		
		List<Account> accounts = new ArrayList<Account>();
		accounts.add(anotherAccount);
		accounts.add(extraAccount);
		
		List<Account> savedAccounts = accountRepository.saveAll(accounts);
		accountRepository.flush();
		
		assertThat(savedAccounts.size() == 2).isTrue();
		
		assertThat(accounts.stream().allMatch(t -> savedAccounts.contains(t))).isTrue();
		assertThat(savedAccounts.stream().allMatch(t -> accounts.contains(t))).isTrue();
		
		long accountCnt = accountRepository.count();
		assertThat(accountCnt).isEqualTo(TOTAL_ROWS + 2);
		
		Optional<Account> rtrvAnotherAccount = accountRepository.findById(anotherAccount.getId());
		assertThat(rtrvAnotherAccount.orElse(null)).isNotNull();
		assertThat(rtrvAnotherAccount.get().equals(anotherAccount)).isTrue();
		assertThat(rtrvAnotherAccount.get().getTerm().equals(anotherTerm)).isTrue();
		assertThat(rtrvAnotherAccount.get().getCustomer().equals(anotherCustomer)).isTrue();
		
		assertThat(Boolean.FALSE.equals(rtrvAnotherAccount.get().getInvoices().isEmpty()));
		assertThat(rtrvAnotherAccount.get().getInvoices().stream().allMatch(t -> someInvoices.contains(t))).isTrue();
		assertThat(someInvoices.stream().allMatch(t -> rtrvAnotherAccount.get().getInvoices().contains(t))).isTrue();

		assertThat(Boolean.FALSE.equals(rtrvAnotherAccount.get().getPurchaseOrders().isEmpty()));
		assertThat(rtrvAnotherAccount.get().getPurchaseOrders().stream().allMatch(t -> somePurchaseOrders.contains(t))).isTrue();
		assertThat(somePurchaseOrders.stream().allMatch(t -> rtrvAnotherAccount.get().getPurchaseOrders().contains(t))).isTrue();

		
		Optional<Account> rtrvExtaAccount = accountRepository.findById(extraAccount.getId());
		assertThat(rtrvExtaAccount.orElse(null)).isNotNull();
		assertThat(rtrvExtaAccount.get().equals(extraAccount)).isTrue();
		assertThat(rtrvExtaAccount.get().getTerm().equals(extraTerm)).isTrue();
		assertThat(rtrvExtaAccount.get().getCustomer().equals(extraCustomer)).isTrue();
		
		assertThat(Boolean.FALSE.equals(rtrvExtaAccount.get().getInvoices().isEmpty()));
		assertThat(rtrvExtaAccount.get().getInvoices().stream().allMatch(t -> moreInvoices.contains(t))).isTrue();
		assertThat(moreInvoices.stream().allMatch(t -> rtrvExtaAccount.get().getInvoices().contains(t))).isTrue();

		assertThat(Boolean.FALSE.equals(rtrvExtaAccount.get().getPurchaseOrders().isEmpty()));
		assertThat(rtrvExtaAccount.get().getPurchaseOrders().stream().allMatch(t -> morePurchaseOrders.contains(t))).isTrue();
		assertThat(morePurchaseOrders.stream().allMatch(t -> rtrvExtaAccount.get().getPurchaseOrders().contains(t))).isTrue();
	}
	
	@Test
	public void whenSaveAndFlush_thenReturnSavedAccount() {
		Account anotherAccount = getAnotherAccount();
		
		Term anotherTerm = getAnotherTerm();
		anotherAccount.setTerm(anotherTerm);
		
		// Manually resolve bi-directional references
		Customer anotherCustomer = getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);
		
		// Manually resolve bi-directional references
		List<Invoice> someInvoices = getSomeInvoices();
		someInvoices.forEach(i -> i.setAccount(anotherAccount));
		anotherAccount.setInvoices(someInvoices);
		
		// Manually resolve bi-directional references
		List<PurchaseOrder> somePurchaseOrders = getSomePurchaseOrders();
		somePurchaseOrders.forEach(po -> po.setAccount(anotherAccount));
		anotherAccount.setPurchaseOrders(somePurchaseOrders);
		
		Account savedAccount = accountRepository.saveAndFlush(anotherAccount);
		
		assertThat(savedAccount.equals(anotherAccount)).isTrue();
		
		long accountCnt = accountRepository.count();
		assertThat(accountCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Account> rtrvAccount = accountRepository.findById(savedAccount.getId());
		assertThat(rtrvAccount.orElse(null)).isNotNull();
		assertThat(rtrvAccount.get().equals(anotherAccount)).isTrue();
		assertThat(rtrvAccount.get().equals(savedAccount)).isTrue();
		assertThat(rtrvAccount.get().getTerm().equals(anotherTerm)).isTrue();
		assertThat(rtrvAccount.get().getCustomer().equals(anotherCustomer)).isTrue();

		assertThat(Boolean.FALSE.equals(rtrvAccount.get().getInvoices().isEmpty()));
		assertThat(rtrvAccount.get().getInvoices().stream().allMatch(t -> someInvoices.contains(t))).isTrue();
		assertThat(someInvoices.stream().allMatch(t -> rtrvAccount.get().getInvoices().contains(t))).isTrue();

		assertThat(Boolean.FALSE.equals(rtrvAccount.get().getPurchaseOrders().isEmpty()));
		assertThat(rtrvAccount.get().getPurchaseOrders().stream().allMatch(t -> somePurchaseOrders.contains(t))).isTrue();
		assertThat(somePurchaseOrders.stream().allMatch(t -> rtrvAccount.get().getPurchaseOrders().contains(t))).isTrue();
	}

	@Test
	public void whenFindByDto_thenReturnAccounts() {
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Superior Dry Cleaners");
		
		List<Account> accounts = accountRepository.findByDto(accountDto);
		accountRepository.flush();
		
		assertThat(accounts).isNotEmpty();
		
		Optional<Account> account = accounts.stream()
			.filter(a -> a.getTerm() != null)
			.filter(a -> a.getCustomer() != null)
			.filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty()))
			.filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty()))
			.findFirst();
		
		assertThat(account.orElse(null)).isNotNull();
		assertThat(account.get().getAccountName().equals(accountDto.getAccountName())).isTrue();
	}

	@Test
	public void whenFindPageByDto_thenReturnAccounts() {
		AccountDto accountDto = new AccountDto();
		accountDto.setActive(true);
		accountDto.setStart(0);
		accountDto.setLimit(2);
		
		List<Account> accounts = accountRepository.findPageByDto(accountDto);
		accountRepository.flush();
		
		assertThat(accounts.size()).isEqualTo(2);
		
		Optional<Account> account = accounts.stream()
			.filter(a -> a.getTerm() != null)
			.filter(a -> a.getCustomer() != null)
			.filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty()))
			.filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty()))
			.findFirst();
		
		assertThat(account.orElse(null)).isNotNull();
	}

	@Test
	public void whenSearchByDto_thenReturnAccounts() {
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Superior");
		
		List<Account> accounts = accountRepository.searchByDto(accountDto);
		accountRepository.flush();
		
		assertThat(accounts).isNotEmpty();
		
		Optional<Account> account = accounts.stream()
			.filter(a -> a.getTerm() != null)
			.filter(a -> a.getCustomer() != null)
			.filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty()))
			.filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty()))
			.findFirst();
		
		assertThat(account.orElse(null)).isNotNull();
	}

	@Test
	public void whenSearchPageByDto_thenReturnAccounts() {
		AccountDto accountDto = new AccountDto();
		accountDto.setBillingAddress("Dallas");
		accountDto.setStart(0);
		accountDto.setLimit(2);
		
		List<Account> accounts = accountRepository.searchPageByDto(accountDto);
		accountRepository.flush();
		
		assertThat(accounts.size()).isEqualTo(2);
		
		Optional<Account> account = accounts.stream()
			.filter(a -> a.getTerm() != null)
			.filter(a -> a.getCustomer() != null)
			.filter(a -> Boolean.FALSE.equals(a.getInvoices().isEmpty()))
			.filter(a -> Boolean.FALSE.equals(a.getPurchaseOrders().isEmpty()))
			.findFirst();
		
		assertThat(account.orElse(null)).isNotNull();
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
	
	private Term getAnotherTerm() {
		Optional<Term> term = termRepository.findById(1L);

		return term.orElse(null);
	}

	private Term getExtraTerm() {
		Optional<Term> term = termRepository.findById(2L);

		return term.orElse(null);
	}

	private Customer getAnotherCustomer() {
		Customer anotherCustomer = new Customer();
		anotherCustomer.setFirstName("Amy");
		anotherCustomer.setLastName("Winecroft");
		anotherCustomer.setWorkEmail("amy.winecroft@yahoo.com");
		anotherCustomer.setWorkPhone("(555) 777-7654");
		anotherCustomer.setCanContact(true);
		
		return anotherCustomer;
	}

	private Customer getExtraCustomer() {
		Customer extraCustomer = new Customer();
		extraCustomer.setFirstName("Barnaby");
		extraCustomer.setLastName("Jones");
		extraCustomer.setWorkEmail("barnaby.jones@yahoo.com");
		extraCustomer.setWorkPhone("(555) 222-3333");
		extraCustomer.setCanContact(true);
		
		return extraCustomer;
	}
	
	private List<Invoice> getSomeInvoices() {
		List<Invoice> invoices = new ArrayList<Invoice>();
		
		Invoice anotherInvoice = new Invoice();
		anotherInvoice.setInvoiceNbr("27");
		anotherInvoice.setDueDate(LocalDate.now());
		anotherInvoice.setStatus("PENDING");
		
		Invoice extraInvoice = new Invoice();
		extraInvoice.setInvoiceNbr("28");
		extraInvoice.setDueDate(LocalDate.now());
		extraInvoice.setStatus("PENDING");
		
		invoices.add(anotherInvoice);
		invoices.add(extraInvoice);
		
		return invoices;
	}

	private List<Invoice> getMoreInvoices() {
		List<Invoice> invoices = new ArrayList<Invoice>();
		
		Invoice anotherInvoice = new Invoice();
		anotherInvoice.setInvoiceNbr("420");
		anotherInvoice.setDueDate(LocalDate.now());
		anotherInvoice.setStatus("PAST DUE");
		
		Invoice extraInvoice = new Invoice();
		extraInvoice.setInvoiceNbr("421");
		extraInvoice.setDueDate(LocalDate.now());
		extraInvoice.setStatus("COMPLETED");
		
		invoices.add(anotherInvoice);
		invoices.add(extraInvoice);
		
		return invoices;
	}

	private List<PurchaseOrder> getSomePurchaseOrders() {
		List<PurchaseOrder> puchaseOrders = new ArrayList<PurchaseOrder>();
		
		PurchaseOrder anotherPurchaseOrder = new PurchaseOrder();
		anotherPurchaseOrder.setOrderIdentifier("EOM-27");
		anotherPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		anotherPurchaseOrder.setInvoiced(true);
		anotherPurchaseOrder.setStatus("RECIEVED");
		
		PurchaseOrder extraPurchaseOrder = new PurchaseOrder();
		extraPurchaseOrder.setOrderIdentifier("PIA-28");
		extraPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		extraPurchaseOrder.setInvoiced(false);
		extraPurchaseOrder.setStatus("SHIPPED");
		
		puchaseOrders.add(anotherPurchaseOrder);
		puchaseOrders.add(extraPurchaseOrder);
		
		return puchaseOrders;
	}

	private List<PurchaseOrder> getMorePurchaseOrders() {
		List<PurchaseOrder> puchaseOrders = new ArrayList<PurchaseOrder>();
		
		PurchaseOrder anotherPurchaseOrder = new PurchaseOrder();
		anotherPurchaseOrder.setOrderIdentifier("NET30-270");
		anotherPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		anotherPurchaseOrder.setInvoiced(true);
		anotherPurchaseOrder.setStatus("RECIEVED");
		
		PurchaseOrder extraPurchaseOrder = new PurchaseOrder();
		extraPurchaseOrder.setOrderIdentifier("NET30-228");
		extraPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		extraPurchaseOrder.setInvoiced(false);
		extraPurchaseOrder.setStatus("SHIPPED");
		
		puchaseOrders.add(anotherPurchaseOrder);
		puchaseOrders.add(extraPurchaseOrder);
		
		return puchaseOrders;
	}
}
