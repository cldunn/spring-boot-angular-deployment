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
import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.invoice.InvoiceRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.cldbiz.userportal.unit.repository.data.AccountData;
import com.cldbiz.userportal.unit.repository.data.InvoiceData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/customerData.xml", "/invoiceData.xml"})
public class InvoiceRepositoryTest extends BaseRepositoryTest {

	private static final Long TOTAL_ROWS = 3L;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");
		
		List<Invoice> invoices = invoiceRepository.findAll();
		Invoice invoice = invoices.get(0);
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke existsById here
		Boolean exists = invoiceRepository.existsById(invoice.getId());
		invoiceRepository.flush();
		
		// test for existence
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		log.info("whenCountAll_thenReturnLong");
		
		// invoke countAll here
		long invoiceCnt = invoiceRepository.countAll();
		invoiceRepository.flush();
		
		// check count
		assertThat(invoiceCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnInvoice() {
		log.info("whenFindById_thenReturnInvoice");
		
		// invoke findById here
		Optional<Invoice> sameInvoice = invoiceRepository.findById(3L);
		invoiceRepository.flush();
		
		// check for invoice and related account 
		assertThat(sameInvoice.orElse(null)).isNotNull();
		assertThat(sameInvoice.get().getAccount()).isNotNull();
	}

	@Test
	public void whenFindByIds_thenReturnInvoices() {
		log.info("whenFindByIds_thenReturnInvoices");
		
		// get all invoice ids
		List<Invoice> invoices = invoiceRepository.findAll();
		List<Long> invoiceIds = invoices.stream().map(Invoice::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		invoiceRepository.flush();

		// invoke findByIds here
		invoices = invoiceRepository.findByIds(invoiceIds);
		invoiceRepository.flush();
		
		// check all accounts were found
		assertThat(invoices.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check all invoices have account
		List<Invoice> invoicesSansAccount = invoices.stream()
				.filter(i -> i.getAccount() == null)
				.collect(Collectors.toList());
		
		assertThat(invoicesSansAccount.isEmpty());
	}

	@Test
	public void whenFindAll_thenReturnAllInvoices() {
		log.info("whenFindAll_thenReturnAllInvoices");
		
		// invoke findAll here
		List<Invoice> invoices = invoiceRepository.findAll();
		invoiceRepository.flush();
		
		// check all invoice found
		assertThat(invoices.size()).isEqualTo(TOTAL_ROWS.intValue());

		// check all invoices have account
		List<Invoice> invoicesSansAccount = invoices.stream()
				.filter(i -> i.getAccount() == null)
				.collect(Collectors.toList());
		
		assertThat(invoicesSansAccount.isEmpty());
	}

	@Test
	public void whenDeleteById_thenRemoveInvoice() {
		log.info("whenDeleteById_thenRemoveInvoice");
		
		Invoice invoice = invoiceRepository.findById(1L).get();
		
		// clear cache to test performance
		invoiceRepository.flush();

		// invoke deleteById here
		invoiceRepository.deleteById(invoice.getId());
		invoiceRepository.flush();
		
		// check invoice deleted
		List<Invoice> invoices = invoiceRepository.findAll();
		assertThat(invoices.contains(invoice)).isFalse();
		
		// check related account not deleted
		Optional<Account> account = accountRepository.findById(invoice.getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteByIds_thenRemoveInvoices() {
		log.info("whenDeleteByIds_thenRemoveInvoices");
		
		// get all invoice ids
		List<Invoice> invoices = invoiceRepository.findAll();
		List<Long> invoiceIds = invoices.stream().map(Invoice::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		invoiceRepository.flush();

		// invoke deleteByIds here
		invoiceRepository.deleteByIds(invoiceIds);
		invoiceRepository.flush();
		
		// check all invoices deleted
		long invoiceCnt = invoiceRepository.countAll();
		assertThat(invoiceCnt).isZero();

		invoices.forEach(invoice -> {
			// check for each deleted invoice its account remains
			Optional<Account> account = accountRepository.findById(invoice.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenDeleteByEntity_thenRemoveInvoice() {
		log.info("whenDeleteByEntity_thenRemoveInvoice");
		
		Invoice invoice = invoiceRepository.findById(3L).get();
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteByEntity here
		invoiceRepository.deleteByEntity(invoice);
		invoiceRepository.flush();
		
		// check invoice deleted
		List<Invoice> invoices = invoiceRepository.findAll();
		assertThat(invoices.contains(invoice)).isFalse();

		// check related account not deleted
		Optional<Account> account = accountRepository.findById(invoice.getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();
	}


	@Test
	public void whenDeleteByEntities_thenRemoveInvoices() {
		log.info("whenDeleteByEntities_thenRemoveInvoices");
		
		List<Invoice> invoices = invoiceRepository.findAll();
		
		// clear cache to test performance
		invoiceRepository.flush();

		// invoke deleteByEntities here
		invoiceRepository.deleteByEntities(invoices);
		invoiceRepository.flush();
		
		// check all invoices deleted
		long invoiceCnt = invoiceRepository.countAll();
		assertThat(invoiceCnt).isZero();
		

		invoices.forEach(invoice -> {
			// check for each deleted invoice its account remains
			Optional<Account> account = accountRepository.findById(invoice.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenSaveEntity_thenReturnSavedInvoice() {
		log.info("whenSaveEntity_thenReturnSavedInvoice");
		
		// create new invoice
		Invoice anotherInvoice = InvoiceData.getAnotherInvoice();
		
		// create and associate an existing account
		Account anotherAccount = AccountData.getAnotherExistingAccount();
		anotherInvoice.setAccount(anotherAccount);
		anotherAccount.getInvoices().add(anotherInvoice);
		
		// invoke saveEntity here
		Invoice savedInvoice = invoiceRepository.saveEntity(anotherInvoice);
		invoiceRepository.flush();
		
		// check invoice returned
		assertThat(savedInvoice.equals(anotherInvoice)).isTrue();
		
		// check invoice persisted
		long invoiceCnt = invoiceRepository.countAll();
		assertThat(invoiceCnt).isEqualTo(TOTAL_ROWS + 1);
		
		// check persisted invoice is same as created and returned invoice
		Optional<Invoice> rtrvInvoice = invoiceRepository.findById(savedInvoice.getId());
		assertThat(rtrvInvoice.orElse(null)).isNotNull();
		assertThat(rtrvInvoice.get().equals(anotherInvoice)).isTrue();
		assertThat(rtrvInvoice.get().equals(savedInvoice)).isTrue();
		
		// check returned invoice's account contains invoice
		assertThat(savedInvoice.getAccount()).isNotNull();
		assertThat(savedInvoice.getAccount().getInvoices().contains(anotherInvoice)).isTrue();
	}

	@Test
	public void whenSaveEntities_thenReturnSavedInvoices() {
		log.info("whenSaveEntities_thenReturnSavedInvoices");
		
		// create 1st invoice
		Invoice anotherInvoice = InvoiceData.getAnotherInvoice();
		
		// add 1st invoice to 1st existing account
		Account anotherAccount = AccountData.getAnotherExistingAccount();
		anotherInvoice.setAccount(anotherAccount);
		anotherAccount.getInvoices().add(anotherInvoice);
		
		// create 2nd invoice
		Invoice extraInvoice = InvoiceData.getExtraInvoice();

		// add 2nd invoice to 2nd existing account
		Account extraAccount = AccountData.getExtraExistingAccount(); 
		extraInvoice.setAccount(extraAccount);
		extraAccount.getInvoices().add(extraInvoice);
		
		// create array of new invoices
		List<Invoice> invoices = new ArrayList<Invoice>();
		invoices.add(anotherInvoice);
		invoices.add(extraInvoice);
		
		// invoke saveEntities here
		List<Invoice> savedInvoices = invoiceRepository.saveEntities(invoices);
		invoiceRepository.flush();
		
		// check invoices returned
		assertThat(savedInvoices.size() == 2).isTrue();
		
		// check returned invoices are the new invoices
		assertThat(invoices.stream().allMatch(t -> savedInvoices.contains(t))).isTrue();
		assertThat(savedInvoices.stream().allMatch(t -> invoices.contains(t))).isTrue();
		
		// check number of invoices persisted
		long invoiceCnt = invoiceRepository.countAll();
		assertThat(invoiceCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// check 1st invoice values persisted
		Optional<Invoice> rtrvAnotherInvoice = invoiceRepository.findById(anotherInvoice.getId());
		assertThat(rtrvAnotherInvoice.orElse(null)).isNotNull();
		assertThat(rtrvAnotherInvoice.get().equals(anotherInvoice)).isTrue();
		assertThat(rtrvAnotherInvoice.get().getAccount().equals(anotherAccount)).isTrue();
		assertThat(rtrvAnotherInvoice.get().getAccount().getInvoices().contains(anotherInvoice)).isTrue();
		
		// check 2nd invoice values persisted
		Optional<Invoice> rtrvExtaInvoice = invoiceRepository.findById(extraInvoice.getId());
		assertThat(rtrvExtaInvoice.orElse(null)).isNotNull();
		assertThat(rtrvExtaInvoice.get().equals(extraInvoice)).isTrue();
		assertThat(rtrvExtaInvoice.get().getAccount().equals(extraAccount)).isTrue();
		assertThat(rtrvExtaInvoice.get().getAccount().getInvoices().contains(extraInvoice)).isTrue();
	}

	@Test
	public void whenModified_thenInvoiceUpdated() {
		log.info("whenModified_thenInvoiceUpdated");
		
		// retrieve invoice
		Optional<Invoice> originalInvoice = invoiceRepository.findById(3L);
		
		// update invoice and related entities
		originalInvoice.get().setStatus("UPDATED - " + originalInvoice.get().getStatus());
		originalInvoice.get().getAccount().setAccountName("UPDATED - " + originalInvoice.get().getAccount().getAccountName());
		
		// retrieve invoice again 
		Optional<Invoice> rtrvdInvoice = invoiceRepository.findById(3L);
		
		// check invoice and related entities updated without save
		assertThat(originalInvoice.get().getStatus().equals((rtrvdInvoice.get().getStatus())));
		assertThat(originalInvoice.get().getAccount().getAccountName().equals((rtrvdInvoice.get().getAccount().getAccountName())));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info("whenExistsByDto_thenReturnTrue");
		
		// create invoice dto for search criteria
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setInvoiceNbr("315");
		
		// qualify search by related account name
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		invoiceDto.setAccountDto(accountDto);
		
		// invoke existsByDto here
		Boolean exists = invoiceRepository.existsByDto(invoiceDto);
		invoiceRepository.flush();
		
		// check existence
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		log.info("whenCountByDto_thenReturnCount");
		
		// create invoice dto for search criteria
		InvoiceDto invoiceDto = new InvoiceDto();
		
		// qualify search by related account name
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		
		invoiceDto.setAccountDto(accountDto);
		
		// invoke countByDto here
		long invoiceCnt =  invoiceRepository.countByDto(invoiceDto);
		invoiceRepository.flush();
		
		// check count
		assertThat(invoiceCnt).isGreaterThanOrEqualTo(2L);
	}

	@Test
	public void whenFindByDto_thenReturnInvoices() {
		log.info("whenFindByDto_thenReturnInvoices");
		
		// create invoice dto for search criteria
		InvoiceDto invoiceDto = new InvoiceDto();
		
		Account account = AccountData.getAnotherExistingAccount();  // Existing
		AccountDto accountDto = new AccountDto(account);
		
		invoiceDto.setAccountDto(accountDto);
		
		// invoke findByDto here
		List<Invoice> invoices = invoiceRepository.findByDto(invoiceDto);
		invoiceRepository.flush();
		
		// check invoices returned
		assertThat(invoices).isNotEmpty();
		
		// check invoices related to existing account
		invoices.stream().forEach(i -> {
			assertThat(i.getAccount()).isNotNull();
			assertThat(i.getAccount().getInvoices().contains(i)).isTrue();
		});
	}

	@Test
	public void whenFindPageByDto_thenReturnInvoices() {
		log.info("whenFindPageByDto_thenReturnInvoices");
		
		// create invoice dto for search criteria
		InvoiceDto invoiceDto = new InvoiceDto();
		
		Account account = AccountData.getAnotherExistingAccount(); // Existing
		AccountDto accountDto = new AccountDto(account);
		
		invoiceDto.setAccountDto(accountDto);
		
		// limit result to first two invoices
		invoiceDto.setStart(0);
		invoiceDto.setLimit(2);
		
		// invoke findPageByDto here
		List<Invoice> invoices = invoiceRepository.findPageByDto(invoiceDto);
		invoiceRepository.flush();
		
		// check invoices returned
		assertThat(invoices).isNotEmpty();
		
		// check no more than two invoices returned
		assertThat(invoices.size()).isLessThanOrEqualTo(2);
		
		// check invoices related to existing account
		invoices.stream().forEach(i -> {
			assertThat(i.getAccount()).isNotNull();
			assertThat(i.getAccount().getInvoices().contains(i)).isTrue();
		});
	}

	@Test
	public void whenSearchByDto_thenReturnInvoices() {
		log.info("whenSearchByDto_thenReturnInvoices");
		
		// create invoice dto for search criteria 
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		invoiceDto.setAccountDto(accountDto);
		
		// invoke searchByDto here
		List<Invoice> invoices = invoiceRepository.searchByDto(invoiceDto);
		invoiceRepository.flush();
		
		// check invoices found
		assertThat(invoices).isNotEmpty();
		
		// check invoices related to existing account
		invoices.stream().forEach(i -> {
			assertThat(i.getAccount()).isNotNull();
			assertThat(i.getAccount().getInvoices().contains(i)).isTrue();
			assertThat(i.getAccount().getShippingAddress().contains(accountDto.getShippingAddress()));
		});
	}

	@Test
	public void whenSearchPageByDto_thenReturnInvoices() {
		log.info("whenSearchPageByDto_thenReturnInvoices");
		
		// create invoice dto for search criteria
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		
		// limit results to first two invoices
		invoiceDto.setStart(0);
		invoiceDto.setLimit(2);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		invoiceDto.setAccountDto(accountDto);

		// invoke searchPageByDto here 
		List<Invoice> invoices = invoiceRepository.searchPageByDto(invoiceDto);
		invoiceRepository.flush();
		
		// check invoices found
		assertThat(invoices).isNotEmpty();
		
		// check no more that two invoices returned
		assertThat(invoices.size()).isLessThanOrEqualTo(2);

		// check invoices related to existing account
		invoices.stream().forEach(i -> {
			assertThat(i.getAccount()).isNotNull();
			assertThat(i.getAccount().getInvoices().contains(i)).isTrue();
			assertThat(i.getAccount().getShippingAddress().contains(accountDto.getShippingAddress()));
		});
	}
}
