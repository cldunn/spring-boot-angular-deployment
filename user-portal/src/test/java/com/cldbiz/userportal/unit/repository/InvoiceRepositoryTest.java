package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.Invoice;
import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.InvoiceDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.invoice.InvoiceRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/customerData.xml", "/invoiceData.xml"})
public class InvoiceRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 3L;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	InvoiceRepository invoiceRepository;

	
	@Test
	public void whenCount_thenReturnCount() {
		long invoiceCnt = invoiceRepository.count();
		invoiceRepository.flush();
		
		assertThat(invoiceCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenDelete_thenRemoveInvoice() {
		List<Invoice> invoices = invoiceRepository.findAll();
		Invoice invoice = invoices.stream().filter(a -> a.getId().equals(3L)).findFirst().get();
		
		invoiceRepository.delete(invoice);
		invoiceRepository.flush();
		
		invoices = invoiceRepository.findAll();
		
		assertThat(invoices.contains(invoice)).isFalse();

		Optional<Account> account = accountRepository.findById(invoice.getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteAll_thenRemoveAllInvoices() {
		List<Invoice> invoices = invoiceRepository.findAll();
		
		invoiceRepository.deleteAll(invoices);
		invoiceRepository.flush();
		
		long invoiceCnt = invoiceRepository.count();

		assertThat(invoiceCnt).isZero();
		
		invoices.forEach(invoice -> {
			Optional<Account> account = accountRepository.findById(invoice.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenDeleteById_thenRemoveInvoice() {
		List<Invoice> invoices = invoiceRepository.findAll();
		Invoice invoice = invoices.get(0);
		
		invoiceRepository.deleteById(invoice.getId());
		invoiceRepository.flush();
		
		invoices = invoiceRepository.findAll();

		assertThat(invoices.contains(invoice)).isFalse();
		
		Optional<Account> account = accountRepository.findById(invoice.getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteByIds_thenRemoveAllInvoices() {
		List<Invoice> invoices = invoiceRepository.findAll();

		List<Long> invoiceIds = invoices.stream().map(Invoice::getId).collect(Collectors.toList());
		
		invoiceRepository.deleteByIds(invoiceIds);
		invoiceRepository.flush();
		
		long invoiceCnt = invoiceRepository.count();

		assertThat(invoiceCnt).isZero();

		invoices.forEach(invoice -> {
			Optional<Account> account = accountRepository.findById(invoice.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenExistsById_thenReturnTrue() {
		List<Invoice> invoices = invoiceRepository.findAll();
		Invoice invoice = invoices.get(0);
		
		Boolean exists = invoiceRepository.existsById(invoice.getId());
		invoiceRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenFindAll_thenReturnAllInvoices() {
		List<Invoice> invoices = invoiceRepository.findAll();
		invoiceRepository.flush();
		
		assertThat(invoices.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(invoices.get(0).getAccount()).isNotNull();
	}

	@Test
	public void whenFindAllById_thenReturnAllInvoices() {
		List<Invoice> invoices = invoiceRepository.findAll();
		List<Long> invoiceIds = invoices.stream().map(Invoice::getId).collect(Collectors.toList());
		
		invoices = invoiceRepository.findAllById(invoiceIds);
		invoiceRepository.flush();
		
		assertThat(invoices.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(invoices.get(0).getAccount()).isNotNull();
	}

	@Test
	public void whenFindById_thenReturnInvoice() {
		Optional<Invoice> sameInvoice = invoiceRepository.findById(3L);
		invoiceRepository.flush();
		
		assertThat(sameInvoice.orElse(null)).isNotNull();
		assertThat(sameInvoice.get().getAccount()).isNotNull();
	}

	@Test
	public void whenSave_thenReturnSavedInvoice() {
		Invoice anotherInvoice = getAnotherInvoice();
		Account anotherAccount = getAnotherAccount(); 

		anotherInvoice.setAccount(anotherAccount);
		anotherAccount.getInvoices().add(anotherInvoice);
		
		Invoice savedInvoice = invoiceRepository.save(anotherInvoice);
		invoiceRepository.flush();
		
		assertThat(savedInvoice.equals(anotherInvoice)).isTrue();
		
		long invoiceCnt = invoiceRepository.count();
		assertThat(invoiceCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Invoice> rtrvInvoice = invoiceRepository.findById(savedInvoice.getId());
		assertThat(rtrvInvoice.orElse(null)).isNotNull();
		assertThat(rtrvInvoice.get().equals(anotherInvoice)).isTrue();
		assertThat(rtrvInvoice.get().equals(savedInvoice)).isTrue();
		
		assertThat(savedInvoice.getAccount()).isNotNull();
		assertThat(savedInvoice.getAccount().getInvoices().contains(anotherInvoice)).isTrue();
	}

	@Test
	public void whenModified_thenInvoiceUpdated() {
		Optional<Invoice> originalInvoice = invoiceRepository.findById(3L);
		originalInvoice.get().setStatus("UPDATED - " + originalInvoice.get().getStatus());
		originalInvoice.get().getAccount().setAccountName("UPDATED - " + originalInvoice.get().getAccount().getAccountName());
		
		Optional<Invoice> rtrvdInvoice = invoiceRepository.findById(3L);
		assertThat(originalInvoice.get().getStatus().equals((rtrvdInvoice.get().getStatus())));
		assertThat(originalInvoice.get().getAccount().getAccountName().equals((rtrvdInvoice.get().getAccount().getAccountName())));
	}

	@Test
	public void whenSaveAll_thenReturnSavedInvoices() {
		Invoice anotherInvoice = getAnotherInvoice();
		Account anotherAccount = getAnotherAccount(); 

		anotherInvoice.setAccount(anotherAccount);
		anotherAccount.getInvoices().add(anotherInvoice);
		
		Invoice extraInvoice = getExtraInvoice();
		Account extraAccount = getExtraAccount(); 
		
		extraInvoice.setAccount(extraAccount);
		extraAccount.getInvoices().add(extraInvoice);
		
		List<Invoice> invoices = new ArrayList<Invoice>();
		invoices.add(anotherInvoice);
		invoices.add(extraInvoice);
		
		List<Invoice> savedInvoices = invoiceRepository.saveAll(invoices);
		invoiceRepository.flush();
		
		assertThat(savedInvoices.size() == 2).isTrue();
		
		assertThat(invoices.stream().allMatch(t -> savedInvoices.contains(t))).isTrue();
		assertThat(savedInvoices.stream().allMatch(t -> invoices.contains(t))).isTrue();
		
		long invoiceCnt = invoiceRepository.count();
		assertThat(invoiceCnt).isEqualTo(TOTAL_ROWS + 2);
		
		Optional<Invoice> rtrvAnotherInvoice = invoiceRepository.findById(anotherInvoice.getId());
		assertThat(rtrvAnotherInvoice.orElse(null)).isNotNull();
		assertThat(rtrvAnotherInvoice.get().equals(anotherInvoice)).isTrue();
		assertThat(rtrvAnotherInvoice.get().getAccount().equals(anotherAccount)).isTrue();
		assertThat(rtrvAnotherInvoice.get().getAccount().getInvoices().contains(anotherInvoice)).isTrue();
		
		Optional<Invoice> rtrvExtaInvoice = invoiceRepository.findById(extraInvoice.getId());
		assertThat(rtrvExtaInvoice.orElse(null)).isNotNull();
		assertThat(rtrvExtaInvoice.get().equals(extraInvoice)).isTrue();
		assertThat(rtrvExtaInvoice.get().getAccount().equals(extraAccount)).isTrue();
		assertThat(rtrvExtaInvoice.get().getAccount().getInvoices().contains(extraInvoice)).isTrue();
	}

	@Test
	public void whenSaveAndFlush_thenReturnSavedInvoice() {
		Invoice anotherInvoice = getAnotherInvoice();
		Account anotherAccount = getAnotherAccount(); 

		anotherInvoice.setAccount(anotherAccount);
		anotherAccount.getInvoices().add(anotherInvoice);

		Invoice savedInvoice = invoiceRepository.saveAndFlush(anotherInvoice);
		
		assertThat(savedInvoice.equals(anotherInvoice)).isTrue();
		
		long invoiceCnt = invoiceRepository.count();
		assertThat(invoiceCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<Invoice> rtrvInvoice = invoiceRepository.findById(savedInvoice.getId());
		assertThat(rtrvInvoice.orElse(null)).isNotNull();
		assertThat(rtrvInvoice.get().equals(anotherInvoice)).isTrue();
		assertThat(rtrvInvoice.get().equals(savedInvoice)).isTrue();
		assertThat(rtrvInvoice.get().getAccount().equals(anotherAccount)).isTrue();
		assertThat(rtrvInvoice.get().getAccount().getInvoices().contains(anotherInvoice)).isTrue();
	}

	@Test
	public void whenFindByDto_thenReturnInvoices() {
		InvoiceDto invoiceDto = new InvoiceDto();
		
		Account account = getAnotherAccount();
		AccountDto accountDto = new AccountDto(account);
		
		invoiceDto.setAccountDto(accountDto);
		
		List<Invoice> invoices = invoiceRepository.findByDto(invoiceDto);
		invoiceRepository.flush();
		
		assertThat(invoices).isNotEmpty();
		
		Optional<Invoice> invoice = invoices.stream().findFirst();
		
		assertThat(invoice.orElse(null)).isNotNull();
		assertThat(invoice.get().getAccount()).isNotNull();
		assertThat(invoice.get().getAccount().getInvoices().contains(invoice.get())).isTrue();
	}

	@Test
	public void whenFindPageByDto_thenReturnInvoices() {
		InvoiceDto invoiceDto = new InvoiceDto();
		
		Account account = getAnotherAccount();
		AccountDto accountDto = new AccountDto(account);
		
		invoiceDto.setAccountDto(accountDto);
		
		invoiceDto.setStart(0);
		invoiceDto.setLimit(2);
		
		List<Invoice> invoices = invoiceRepository.findPageByDto(invoiceDto);
		invoiceRepository.flush();
		
		assertThat(invoices.size()).isLessThanOrEqualTo(2);
	}

	@Test
	public void whenCountSearchByDto_thenReturnCount() {
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		invoiceDto.setAccountDto(accountDto);
		
		Long invoiceCount = invoiceRepository.countSearchByDto(invoiceDto);
		invoiceRepository.flush();
		
		assertThat(invoiceCount).isGreaterThan(0L);
	}

	@Test
	public void whenSearchByDto_thenReturnInvoices() {
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		invoiceDto.setAccountDto(accountDto);
		
		List<Invoice> invoices = invoiceRepository.searchByDto(invoiceDto);
		invoiceRepository.flush();
		
		assertThat(invoices).isNotEmpty();
		assertThat(invoices.get(0).getAccount()).isNotNull();
	}

	@Test
	public void whenSearchPageByDto_thenReturnInvoices() {
		InvoiceDto invoiceDto = new InvoiceDto();
		invoiceDto.setStatus("PENDING");
		invoiceDto.setStart(0);
		invoiceDto.setLimit(2);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		invoiceDto.setAccountDto(accountDto);

		List<Invoice> invoices = invoiceRepository.searchPageByDto(invoiceDto);
		invoiceRepository.flush();
		
		assertThat(invoices.size()).isLessThanOrEqualTo(2);
		assertThat(invoices.get(0).getAccount()).isNotNull();

	}

	private Invoice getAnotherInvoice() {
		Invoice invoice = new Invoice();
		
		invoice.setInvoiceNbr("777");
		invoice.setDueDate(LocalDate.now());
		invoice.setStatus("PAST DUE");
		
		return invoice;
	}
	
	private Invoice getExtraInvoice() {
		Invoice invoice = new Invoice();
		
		invoice.setInvoiceNbr("26");
		invoice.setDueDate(LocalDate.now());
		invoice.setStatus("NEW");

		return invoice;
	}

	private Account getAnotherAccount() {
		Optional<Account> account = accountRepository.findById(1L);
		return account.orElse(null);
	}

	private Account getExtraAccount() {
		Optional<Account> account = accountRepository.findById(2L);
		return account.orElse(null);
	}

}
