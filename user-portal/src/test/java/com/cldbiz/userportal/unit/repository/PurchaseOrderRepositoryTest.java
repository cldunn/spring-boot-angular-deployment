package com.cldbiz.userportal.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.domain.Account;
import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.PurchaseOrderDto;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.customer.CustomerRepository;
import com.cldbiz.userportal.repository.lineItem.LineItemRepository;
import com.cldbiz.userportal.repository.purchaseOrder.PurchaseOrderRepository;
import com.cldbiz.userportal.repository.term.TermRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/termData.xml", "/accountData.xml", "/customerData.xml", "/productData.xml", "/purchaseOrderData.xml", "/lineItemData.xml"})
public class PurchaseOrderRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	TermRepository termRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	LineItemRepository lineItemRepository;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;

	@Test
	public void whenCount_thenReturnCount() {
		long purchaseOrderCnt = purchaseOrderRepository.count();
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrderCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenDelete_thenRemovePurchaseOrder() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		PurchaseOrder purchaseOrder = purchaseOrders.stream().filter(a -> a.getId().equals(3L)).findFirst().get();
		
		purchaseOrderRepository.delete(purchaseOrder);
		purchaseOrderRepository.flush();
		
		purchaseOrders = purchaseOrderRepository.findAll();
		
		assertThat(purchaseOrders.contains(purchaseOrder)).isFalse();

		Optional<Account> account = accountRepository.findById(purchaseOrder.getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();

		Optional<LineItem> deletedLineItem = purchaseOrder.getLineItems().stream().filter(li -> {
			Optional<LineItem> lineItem = lineItemRepository.findById(li.getId());
			return lineItem.orElse(null) != null;
		}).findFirst();
		assertThat(deletedLineItem.orElse(null)).isNull();
	}

	@Test
	public void whenDeleteAll_thenRemoveAllPurchaseOrders() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		
		purchaseOrderRepository.deleteAll(purchaseOrders);
		purchaseOrderRepository.flush();
		
		long purchaseOrderCnt = purchaseOrderRepository.count();

		assertThat(purchaseOrderCnt).isZero();
		
		purchaseOrders.forEach(purchaseOrder -> {
			Optional<Account> account = accountRepository.findById(purchaseOrder.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();

			Optional<LineItem> deletedLineItem = purchaseOrder.getLineItems().stream().filter(li -> {
				Optional<LineItem> lineItem = lineItemRepository.findById(li.getId());
				return lineItem.orElse(null) != null;
			}).findFirst();
			assertThat(deletedLineItem.orElse(null)).isNull();
		});
	}

	@Test
	public void whenDeleteById_thenRemovePurchaseOrder() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		PurchaseOrder purchaseOrder = purchaseOrders.get(0);
		
		purchaseOrderRepository.deleteById(purchaseOrder.getId());
		purchaseOrderRepository.flush();
		
		purchaseOrders = purchaseOrderRepository.findAll();

		assertThat(purchaseOrders.contains(purchaseOrder)).isFalse();
		
		Optional<Account> account = accountRepository.findById(purchaseOrder.getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();

		Optional<LineItem> deletedLineItem = purchaseOrder.getLineItems().stream().filter(li -> {
			Optional<LineItem> lineItem = lineItemRepository.findById(li.getId());
			return lineItem.orElse(null) != null;
		}).findFirst();
		assertThat(deletedLineItem.orElse(null)).isNull();
	}

	@Test
	public void whenDeleteByIds_thenRemoveAllPurchaseOrders() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		
		List<Long> purchaseOrderIds = purchaseOrders.stream().map(PurchaseOrder::getId).collect(Collectors.toList());
		
		purchaseOrderRepository.deleteByIds(purchaseOrderIds);
		purchaseOrderRepository.flush();
		
		long purchaseOrderCnt = purchaseOrderRepository.count();

		assertThat(purchaseOrderCnt).isZero();

		purchaseOrders.forEach(purchaseOrder -> {
			Optional<Account> account = accountRepository.findById(purchaseOrder.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();

			Optional<LineItem> deletedLineItem = purchaseOrder.getLineItems().stream().filter(li -> {
				Optional<LineItem> lineItem = lineItemRepository.findById(li.getId());
				return lineItem.orElse(null) != null;
			}).findFirst();
			assertThat(deletedLineItem.orElse(null)).isNull();
		});
	}

	@Test
	public void whenExistsById_thenReturnTrue() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		PurchaseOrder purchaseOrder = purchaseOrders.get(0);
		
		Boolean exists = purchaseOrderRepository.existsById(purchaseOrder.getId());
		purchaseOrderRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenFindAll_thenReturnAllPurchaseOrders() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrders.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(purchaseOrders.get(0).getAccount()).isNotNull();
		
		Optional<PurchaseOrder> purchaseOrder = purchaseOrders.stream().
			    filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty())).
			    findFirst();
		
		assertThat(purchaseOrder.orElse(null)).isNotNull();
	}
	
	@Test
	public void whenFindAllById_thenReturnAllPurchaseOrders() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		List<Long> purchaseOrderIds = purchaseOrders.stream().map(PurchaseOrder::getId).collect(Collectors.toList());
		
		purchaseOrders = purchaseOrderRepository.findAllById(purchaseOrderIds);
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrders.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(purchaseOrders.get(0).getAccount()).isNotNull();
		
		Optional<PurchaseOrder> purchaseOrder = purchaseOrders.stream().
			    filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty())).
			    findFirst();
		
		assertThat(purchaseOrder.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindById_thenReturnPurchaseOrder() {
		Optional<PurchaseOrder> samePurchaseOrder = purchaseOrderRepository.findById(3L);
		purchaseOrderRepository.flush();
		
		assertThat(samePurchaseOrder.orElse(null)).isNotNull();
		
		assertThat(samePurchaseOrder.get().getAccount()).isNotNull();
		assertThat(samePurchaseOrder.get().getLineItems().isEmpty()).isFalse();
	}

	@Test
	public void whenSave_thenReturnSavedPurchaseOrder() {
		PurchaseOrder anotherPurchaseOrder = getAnotherPurchaseOrder();
		
		Account anotherAccount = getAnotherAccount();
		anotherPurchaseOrder.setAccount(anotherAccount);
		anotherAccount.getPurchaseOrders().add(anotherPurchaseOrder);
		
		List<LineItem>someLineItems = getSomeLineItems();
		anotherPurchaseOrder.setLineItems(someLineItems);
		
		PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(anotherPurchaseOrder);
		purchaseOrderRepository.flush();
		
		assertThat(savedPurchaseOrder.equals(anotherPurchaseOrder)).isTrue();
		
		long purchaseOrderCnt = purchaseOrderRepository.count();
		assertThat(purchaseOrderCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<PurchaseOrder> rtrvPurchaseOrder = purchaseOrderRepository.findById(savedPurchaseOrder.getId());
		assertThat(rtrvPurchaseOrder.orElse(null)).isNotNull();
		assertThat(rtrvPurchaseOrder.get().equals(anotherPurchaseOrder)).isTrue();
		assertThat(rtrvPurchaseOrder.get().equals(savedPurchaseOrder)).isTrue();
		assertThat(rtrvPurchaseOrder.get().getAccount().equals(anotherAccount)).isTrue();

		assertThat(Boolean.FALSE.equals(rtrvPurchaseOrder.get().getLineItems().isEmpty()));
		assertThat(rtrvPurchaseOrder.get().getLineItems().stream().allMatch(t -> someLineItems.contains(t))).isTrue();
		assertThat(someLineItems.stream().allMatch(t -> rtrvPurchaseOrder.get().getLineItems().contains(t))).isTrue();
	}

	@Test
	public void whenModified_thenPurchaseOrderUpdated() {
		Optional<PurchaseOrder> originalPurchaseOrder = purchaseOrderRepository.findById(3L);
		originalPurchaseOrder.get().setOrderIdentifier("UPDATED - " + originalPurchaseOrder.get().getOrderIdentifier());
		originalPurchaseOrder.get().getAccount().setAccountName("UPDATED - " + originalPurchaseOrder.get().getAccount().getAccountName());
		originalPurchaseOrder.get().getLineItems().get(0).setQuantity(originalPurchaseOrder.get().getLineItems().get(0).getQuantity() + 1);
		
		Optional<PurchaseOrder> rtrvdPurchaseOrder = purchaseOrderRepository.findById(3L);
		assertThat(originalPurchaseOrder.get().getOrderIdentifier().equals((rtrvdPurchaseOrder.get().getOrderIdentifier())));
		assertThat(originalPurchaseOrder.get().getAccount().getAccountName().equals((rtrvdPurchaseOrder.get().getAccount().getAccountName())));
		assertThat(originalPurchaseOrder.get().getLineItems().get(0).getQuantity().equals((rtrvdPurchaseOrder.get().getLineItems().get(0).getQuantity())));
	}

	@Test
	public void whenSaveAll_thenReturnSavedPurchaseOrders() {
		PurchaseOrder anotherPurchaseOrder = getAnotherPurchaseOrder();
		
		// Manually resolve bi-directional references
		Account anotherAccount = getAnotherAccount();
		anotherPurchaseOrder.setAccount(anotherAccount);
		anotherAccount.getPurchaseOrders().add(anotherPurchaseOrder);
		
		List<LineItem> someLineItems = getSomeLineItems();
		anotherPurchaseOrder.setLineItems(someLineItems);
		
		PurchaseOrder extraPurchaseOrder = getExtraPurchaseOrder();
		
		// Manually resolve bi-directional references
		Account extraAccount = getExtraAccount();
		extraPurchaseOrder.setAccount(extraAccount);
		extraAccount.getPurchaseOrders().add(extraPurchaseOrder);
		
		List<LineItem> moreLineItems = getMoreLineItems();
		extraPurchaseOrder.setLineItems(moreLineItems);
		
		List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
		purchaseOrders.add(anotherPurchaseOrder);
		purchaseOrders.add(extraPurchaseOrder);
		
		List<PurchaseOrder> savedPurchaseOrders = purchaseOrderRepository.saveAll(purchaseOrders);
		purchaseOrderRepository.flush();
		
		assertThat(savedPurchaseOrders.size() == 2).isTrue();
		
		assertThat(purchaseOrders.stream().allMatch(t -> savedPurchaseOrders.contains(t))).isTrue();
		assertThat(savedPurchaseOrders.stream().allMatch(t -> purchaseOrders.contains(t))).isTrue();
		
		long purchaseOrderCnt = purchaseOrderRepository.count();
		assertThat(purchaseOrderCnt).isEqualTo(TOTAL_ROWS + 2);
		
		Optional<PurchaseOrder> rtrvAnotherPurchaseOrder = purchaseOrderRepository.findById(anotherPurchaseOrder.getId());
		assertThat(rtrvAnotherPurchaseOrder.orElse(null)).isNotNull();
		assertThat(rtrvAnotherPurchaseOrder.get().equals(anotherPurchaseOrder)).isTrue();
		assertThat(rtrvAnotherPurchaseOrder.get().getAccount().equals(anotherAccount)).isTrue();
		
		assertThat(Boolean.FALSE.equals(rtrvAnotherPurchaseOrder.get().getLineItems().isEmpty()));
		assertThat(rtrvAnotherPurchaseOrder.get().getLineItems().stream().allMatch(t -> someLineItems.contains(t))).isTrue();
		assertThat(someLineItems.stream().allMatch(t -> rtrvAnotherPurchaseOrder.get().getLineItems().contains(t))).isTrue();

		Optional<PurchaseOrder> rtrvExtraPurchaseOrder = purchaseOrderRepository.findById(extraPurchaseOrder.getId());
		assertThat(rtrvExtraPurchaseOrder.orElse(null)).isNotNull();
		assertThat(rtrvExtraPurchaseOrder.get().equals(extraPurchaseOrder)).isTrue();
		assertThat(rtrvExtraPurchaseOrder.get().getAccount().equals(extraAccount)).isTrue();
		
		assertThat(Boolean.FALSE.equals(rtrvExtraPurchaseOrder.get().getLineItems().isEmpty()));
		assertThat(rtrvExtraPurchaseOrder.get().getLineItems().stream().allMatch(t -> moreLineItems.contains(t))).isTrue();
		assertThat(moreLineItems.stream().allMatch(t -> rtrvExtraPurchaseOrder.get().getLineItems().contains(t))).isTrue();
	}
	
	@Test
	public void whenSaveAndFlush_thenReturnSavedPurchaseOrder() {
		PurchaseOrder anotherPurchaseOrder = getAnotherPurchaseOrder();
		
		Account anotherAccount = getAnotherAccount();
		anotherPurchaseOrder.setAccount(anotherAccount);
		anotherAccount.getPurchaseOrders().add(anotherPurchaseOrder);
		
		List<LineItem> someLineItems = getSomeLineItems();
		anotherPurchaseOrder.setLineItems(someLineItems);
		
		PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.saveAndFlush(anotherPurchaseOrder);
		
		assertThat(savedPurchaseOrder.equals(anotherPurchaseOrder)).isTrue();
		
		long purchaseOrderCnt = purchaseOrderRepository.count();
		assertThat(purchaseOrderCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<PurchaseOrder> rtrvPurchaseOrder = purchaseOrderRepository.findById(savedPurchaseOrder.getId());
		assertThat(rtrvPurchaseOrder.orElse(null)).isNotNull();
		assertThat(rtrvPurchaseOrder.get().equals(anotherPurchaseOrder)).isTrue();
		assertThat(rtrvPurchaseOrder.get().equals(savedPurchaseOrder)).isTrue();
		assertThat(rtrvPurchaseOrder.get().getAccount().equals(anotherAccount)).isTrue();

		assertThat(Boolean.FALSE.equals(rtrvPurchaseOrder.get().getLineItems().isEmpty()));
		assertThat(rtrvPurchaseOrder.get().getLineItems().stream().allMatch(t -> someLineItems.contains(t))).isTrue();
		assertThat(someLineItems.stream().allMatch(t -> rtrvPurchaseOrder.get().getLineItems().contains(t))).isTrue();
	}

	@Test
	public void whenFindByDto_thenReturnPurchaseOrders() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30-417");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		purchaseOrderDto.setAccountDto(accountDto);
		
		/* TODO incorporate find product in any lineitem for purchaseOrder */
		/* TODO incorporate find product in all lineitems for purchaseOrder */
		
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrders).isNotEmpty();
		
		Optional<PurchaseOrder> purchaseOrder = purchaseOrders.stream()
			.filter(p -> p.getAccount() != null)
			.filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty()))
			.findFirst();
		
		assertThat(purchaseOrder.orElse(null)).isNotNull();
		assertThat(purchaseOrder.get().getOrderIdentifier().equals(purchaseOrderDto.getOrderIdentifier())).isTrue();
		assertThat(purchaseOrder.get().getAccount().getAccountName().equals(purchaseOrderDto.getAccountDto().getAccountName())).isTrue();
	}

	@Test
	public void whenFindPageByDto_thenReturnPurchaseOrders() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setStatus("SHIPPED");
		purchaseOrderDto.setStart(0);
		purchaseOrderDto.setLimit(2);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		purchaseOrderDto.setAccountDto(accountDto);

		/* TODO incorporate find product in any lineitem for purchaseOrder */
		/* TODO incorporate find product in all lineitems for purchaseOrder */

		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findPageByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrders.isEmpty()).isFalse();
		assertThat(purchaseOrders.size()).isLessThanOrEqualTo(2);
		
		Optional<PurchaseOrder> purchaseOrder = purchaseOrders.stream()
			.filter(p -> p.getAccount() != null)
			.filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty()))
			.findFirst();
		
		assertThat(purchaseOrder.orElse(null)).isNotNull();
		assertThat(purchaseOrder.get().getStatus().equals(purchaseOrderDto.getStatus())).isTrue();
		assertThat(purchaseOrder.get().getAccount().getAccountName().equals(purchaseOrderDto.getAccountDto().getAccountName())).isTrue();
	}

	@Test
	public void whenCountSearchByDto_thenReturnCount() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		purchaseOrderDto.setAccountDto(accountDto);
		
		/* TODO incorporate find product in any lineitem for purchaseOrder */
		/* TODO incorporate find product in all lineitems for purchaseOrder */

		Long purchaseOrderCount = purchaseOrderRepository.countSearchByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrderCount).isGreaterThan(0L);
	}

	
	@Test
	public void whenSearchByDto_thenReturnPurchaseOrders() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		purchaseOrderDto.setAccountDto(accountDto);

		/* TODO incorporate find product in any lineitem for purchaseOrder */
		/* TODO incorporate find product in all lineitems for purchaseOrder */

		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.searchByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrders).isNotEmpty();
		
		Optional<PurchaseOrder> purchaseOrder = purchaseOrders.stream()
			.filter(p -> p.getAccount() != null)
			.filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty()))
			.findFirst();
		
		assertThat(purchaseOrder.orElse(null)).isNotNull();
		assertThat(purchaseOrder.get().getOrderIdentifier().contains(purchaseOrderDto.getOrderIdentifier())).isTrue();
		assertThat(purchaseOrder.get().getAccount().getShippingAddress().contains(purchaseOrderDto.getAccountDto().getShippingAddress())).isTrue();
	}

	@Test
	public void whenSearchPageByDto_thenReturnPurchaseOrders() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30");
		purchaseOrderDto.setStart(0);
		purchaseOrderDto.setLimit(2);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		purchaseOrderDto.setAccountDto(accountDto);

		/* TODO incorporate find product in any lineitem for purchaseOrder */
		/* TODO incorporate find product in all lineitems for purchaseOrder */

		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.searchPageByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrders.size()).isLessThanOrEqualTo(2);
		
		Optional<PurchaseOrder> purchaseOrder = purchaseOrders.stream()
			.filter(p -> p.getAccount() != null)
			.filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty()))
			.findFirst();
		
		assertThat(purchaseOrder.orElse(null)).isNotNull();
		assertThat(purchaseOrder.get().getOrderIdentifier().contains(purchaseOrderDto.getOrderIdentifier())).isTrue();
		assertThat(purchaseOrder.get().getAccount().getShippingAddress().contains(purchaseOrderDto.getAccountDto().getShippingAddress())).isTrue();
	}

	private PurchaseOrder getAnotherPurchaseOrder() {
		PurchaseOrder anotherPurchaseOrder = new PurchaseOrder();
	
		anotherPurchaseOrder.setOrderIdentifier("EOM-92");
		anotherPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		anotherPurchaseOrder.setInvoiced(true);
		anotherPurchaseOrder.setStatus("SHIPPED");
		
		return anotherPurchaseOrder;
	}

	private PurchaseOrder getExtraPurchaseOrder() {
		PurchaseOrder anotherPurchaseOrder = new PurchaseOrder();
		
		anotherPurchaseOrder.setOrderIdentifier("NET30-512");
		anotherPurchaseOrder.setPurchaseDttm(LocalDateTime.now());
		anotherPurchaseOrder.setInvoiced(false);
		anotherPurchaseOrder.setStatus("SHIPPED");
		
		return anotherPurchaseOrder;
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

	private List<LineItem> getSomeLineItems() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		
		LineItem anotherLineItem = new LineItem();
		anotherLineItem.setQuantity(15L);
		// TODO: setProduct
		
		LineItem extraLineItem = new LineItem();
		extraLineItem.setQuantity(16L);
		// TODO: setProduct
		
		lineItems.add(anotherLineItem);
		lineItems.add(extraLineItem);
		
		return lineItems;
	}

	private List<LineItem> getMoreLineItems() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		
		LineItem anotherLineItem = new LineItem();
		anotherLineItem.setQuantity(17L);
		// TODO: setProduct
		
		LineItem extraLineItem = new LineItem();
		extraLineItem.setQuantity(18L);
		// TODO: setProduct
		
		lineItems.add(anotherLineItem);
		lineItems.add(extraLineItem);
		
		return lineItems;
	}

}
