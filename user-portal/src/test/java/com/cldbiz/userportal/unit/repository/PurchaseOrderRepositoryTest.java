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
import com.cldbiz.userportal.domain.Customer;
import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.domain.WishList;
import com.cldbiz.userportal.domain.Contact;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.PurchaseOrderDto;
import com.cldbiz.userportal.dto.WishListDto;
import com.cldbiz.userportal.repository.account.AccountRepository;
import com.cldbiz.userportal.repository.contact.ContactRepository;
import com.cldbiz.userportal.repository.customer.CustomerRepository;
import com.cldbiz.userportal.repository.lineItem.LineItemRepository;
import com.cldbiz.userportal.repository.product.ProductRepository;
import com.cldbiz.userportal.repository.purchaseOrder.PurchaseOrderRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.cldbiz.userportal.unit.repository.data.AccountData;
import com.cldbiz.userportal.unit.repository.data.ContactData;
import com.cldbiz.userportal.unit.repository.data.CustomerData;
import com.cldbiz.userportal.unit.repository.data.LineItemData;
import com.cldbiz.userportal.unit.repository.data.ProductData;
import com.cldbiz.userportal.unit.repository.data.PurchaseOrderData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/customerData.xml", "/productData.xml", "/purchaseOrderData.xml", "/lineItemData.xml"})
public class PurchaseOrderRepositoryTest extends BaseRepositoryTest {

	private static final Long TOTAL_ROWS = 3L;
	
	@Autowired
	ContactRepository contactRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	LineItemRepository lineItemRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;

	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");
		
		// get purchaseOrder
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		PurchaseOrder purchaseOrder = purchaseOrders.get(0);
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke existsById here
		Boolean exists = purchaseOrderRepository.existsById(purchaseOrder.getId());
		purchaseOrderRepository.flush();
		
		// test for existence
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		log.info("whenCountAll_thenReturnCount");
		
		// invoke countAll here
		Long purchaseOrderCount =  purchaseOrderRepository.countAll();
		purchaseOrderRepository.flush();
		
		// check count
		assertThat(purchaseOrderCount).isEqualTo(TOTAL_ROWS.intValue());
	}

	@Test
	public void whenFindById_thenReturnPurchaseOrder() {
		log.info("whenFindById_thenReturnPurchaseOrder");
		
		// invoke findById here
		Optional<PurchaseOrder> samePurchaseOrder = purchaseOrderRepository.findById(3L);
		purchaseOrderRepository.flush();
		
		// check for account and related entities 
		assertThat(samePurchaseOrder.orElse(null)).isNotNull();
		assertThat(samePurchaseOrder.get().getAccount()).isNotNull();
		assertThat(samePurchaseOrder.get().getLineItems().isEmpty()).isFalse();
	}

	@Test
	public void whenFindByIds_thenReturnPurchaseOrders() {
		log.info("whenFindByIds_thenReturnPurchaseOrders");
		
		// get all purchaseOrder ids
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		List<Long> purchaseOrderIds = purchaseOrders.stream().map(PurchaseOrder::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		accountRepository.flush();
		
		// invoke findByIds here
		purchaseOrders = purchaseOrderRepository.findByIds(purchaseOrderIds);
		purchaseOrderRepository.flush();
		
		// check all purchase orders were found
		assertThat(purchaseOrders.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check purchase order belongs to account
		assertThat(purchaseOrders.get(0).getAccount()).isNotNull();
		
		// check some order has accessible line items
		Optional<PurchaseOrder> purchaseOrderWithLineItems = purchaseOrders.stream().
			    filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty()))
			    .findAny();
			    
		assertThat(purchaseOrderWithLineItems.orElse(null)).isNotNull();
	}

	@Test
	public void whenFindAll_thenReturnAllPurchaseOrders() {
		log.info("whenFindAll_thenReturnAllPurchaseOrders");
		
		// invoke findAll here
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		purchaseOrderRepository.flush();
		
		// check all purchase orders were found
		assertThat(purchaseOrders.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check all purchase orders have an account
		List<PurchaseOrder> purchaseOrdersSansAccount =
				purchaseOrders.stream().filter(po -> po.getAccount() == null)
				.collect(Collectors.toList());
		
		assertThat(purchaseOrdersSansAccount.isEmpty());
		
		// check some order has accessible line items
		Optional<PurchaseOrder> purchaseOrderWithLineItems = purchaseOrders.stream().
			    filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty()))
			    .findAny();
			    
		assertThat(purchaseOrderWithLineItems.orElse(null)).isNotNull();
	}
	
	@Test
	public void whenDeleteById_thenRemovePurchaseOrder() {
		log.info("whenDeleteById_thenRemovePurchaseOrder");
		
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(3L).get();
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteById here
		purchaseOrderRepository.deleteById(purchaseOrder.getId());
		purchaseOrderRepository.flush();
		
		// check purchase order deleted
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		assertThat(purchaseOrders.contains(purchaseOrder)).isFalse();
		
		// check related account is not deleted
		Optional<Account> account = accountRepository.findById(purchaseOrder.getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();

		// check all related line items are deleted
		Optional<LineItem> deletedLineItem = purchaseOrder.getLineItems().stream().filter(li -> {
			Optional<LineItem> lineItem = lineItemRepository.findById(li.getId());
			return lineItem.orElse(null) != null;
		}).findFirst();
		assertThat(deletedLineItem.orElse(null)).isNull();
	}

	@Test
	public void whenDeleteByIds_thenRemovePurchaseOrders() {
		log.info("whenDeleteByIds_thenRemovePurchaseOrders");

		// get all purchase order ids
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		List<Long> purchaseOrderIds = purchaseOrders.stream().map(PurchaseOrder::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		purchaseOrderRepository.flush();

		// invoke deleteByIds here
		purchaseOrderRepository.deleteByIds(purchaseOrderIds);
		purchaseOrderRepository.flush();
		
		// check all purchase orders deleted
		long purchaseOrderCnt = purchaseOrderRepository.countAll();
		assertThat(purchaseOrderCnt).isZero();

		purchaseOrders.forEach(purchaseOrder -> {
			// check for each purchase order, related account not deleted
			Optional<Account> account = accountRepository.findById(purchaseOrder.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();

			// check for each purchase order, related line items are deleted
			Optional<LineItem> deletedLineItem = purchaseOrder.getLineItems().stream().filter(li -> {
				Optional<LineItem> lineItem = lineItemRepository.findById(li.getId());
				return lineItem.orElse(null) != null;
			}).findFirst();
			assertThat(deletedLineItem.orElse(null)).isNull();
		});
	}

	@Test
	public void whenDeleteByEntity_thenRemovePurchaseOrder() {
		log.info("whenDeleteByEntity_thenRemovePurchaseOrder");
		
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(3L).get();
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteByEntity here
		purchaseOrderRepository.deleteByEntity(purchaseOrder);
		purchaseOrderRepository.flush();
		
		// check purchase order deleted
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		assertThat(purchaseOrders.contains(purchaseOrder)).isFalse();

		// check related account is not deleted
		Optional<Account> account = accountRepository.findById(purchaseOrder.getAccount().getId());
		assertThat(account.orElse(null)).isNotNull();

		// check all related line items are deleted
		Optional<LineItem> deletedLineItem = purchaseOrder.getLineItems().stream().filter(li -> {
			Optional<LineItem> lineItem = lineItemRepository.findById(li.getId());
			return lineItem.orElse(null) != null;
		}).findFirst();
		assertThat(deletedLineItem.orElse(null)).isNull();
	}

	@Test
	public void whenDeleteByEntities_thenRemovePurchaseOrders() {
		log.info("whenDeleteByEntities_thenRemovePurchaseOrders");
		
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		
		// clear cache to test performance
		accountRepository.flush();

		// invoke deleteByEntities here
		purchaseOrderRepository.deleteByEntities(purchaseOrders);
		purchaseOrderRepository.flush();
		
		// check all purchase orders deleted
		long purchaseOrderCnt = purchaseOrderRepository.countAll();
		assertThat(purchaseOrderCnt).isZero();
		
		purchaseOrders.forEach(purchaseOrder -> {
			// check for each purchase order, related account not deleted
			Optional<Account> account = accountRepository.findById(purchaseOrder.getAccount().getId());
			assertThat(account.orElse(null)).isNotNull();

			// check for each purchase order, related line items are deleted
			Optional<LineItem> deletedLineItem = purchaseOrder.getLineItems().stream().filter(li -> {
				Optional<LineItem> lineItem = lineItemRepository.findById(li.getId());
				return lineItem.orElse(null) != null;
			}).findFirst();
			assertThat(deletedLineItem.orElse(null)).isNull();
		});
	}

	@Test
	public void whenSaveEntity_thenReturnSavedPurchaseOrder() {
		log.info("whenSaveEntity_thenReturnSavedPurchaseOrder");
		
		// create new purchase order
		PurchaseOrder anotherPurchaseOrder = PurchaseOrderData.getAnotherPurchaseOrder();
		
		// create new account and related entities
		Account anotherAccount = AccountData.getAnotherAccount();
		
		Contact anotherContact = ContactData.getAnotherContact();
		anotherAccount.setContact(anotherContact);
		
		Customer anotherCustomer = CustomerData.getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);

		// add new account to new purchase order
		anotherPurchaseOrder.setAccount(anotherAccount);
		anotherAccount.getPurchaseOrders().add(anotherPurchaseOrder);
		
		// create new line items with product for each
		List<LineItem>someLineItems = LineItemData.getSomeLineItems();
		
		Product anotherProduct = ProductData.getAnotherExistingProduct();
		someLineItems.forEach(l -> l.setProduct(anotherProduct));
		
		// add line items to purchase order
		anotherPurchaseOrder.setLineItems(someLineItems);
		
		// invoke saveEntity here
		PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.saveEntity(anotherPurchaseOrder);
		purchaseOrderRepository.flush();
		
		// check purchase order entity returned
		assertThat(savedPurchaseOrder.equals(anotherPurchaseOrder)).isTrue();
		
		// check purchase order entity persisted
		long purchaseOrderCnt = purchaseOrderRepository.countAll();
		assertThat(purchaseOrderCnt).isEqualTo(TOTAL_ROWS + 1);
		
		// check returned and saved purchase order are the same and new account saved
		Optional<PurchaseOrder> rtrvPurchaseOrder = purchaseOrderRepository.findById(savedPurchaseOrder.getId());
		assertThat(rtrvPurchaseOrder.orElse(null)).isNotNull();
		assertThat(rtrvPurchaseOrder.get().equals(anotherPurchaseOrder)).isTrue();
		assertThat(rtrvPurchaseOrder.get().equals(savedPurchaseOrder)).isTrue();
		assertThat(rtrvPurchaseOrder.get().getAccount().equals(anotherAccount)).isTrue();

		// checksaved purchase order saved line items too
		assertThat(Boolean.FALSE.equals(rtrvPurchaseOrder.get().getLineItems().isEmpty()));
		assertThat(rtrvPurchaseOrder.get().getLineItems().stream().allMatch(t -> someLineItems.contains(t))).isTrue();
		assertThat(someLineItems.stream().allMatch(t -> rtrvPurchaseOrder.get().getLineItems().contains(t))).isTrue();
	}

	@Test
	public void whenSaveEntities_thenReturnSavedPurchaseOrders() {
		log.info("whenSaveEntities_thenReturnSavedPurchaseOrders");
		
		// create 1st purchase order 
		PurchaseOrder anotherPurchaseOrder = PurchaseOrderData.getAnotherPurchaseOrder();
		
		// create 1st account and related entities
		Account anotherAccount = AccountData.getAnotherAccount();
		
		Contact anotherContact = ContactData.getAnotherContact();
		anotherAccount.setContact(anotherContact);
		
		Customer anotherCustomer = CustomerData.getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);

		// add new account to 1st purchase order
		anotherPurchaseOrder.setAccount(anotherAccount);
		anotherAccount.getPurchaseOrders().add(anotherPurchaseOrder);
		
		// create line items and assign product
		List<LineItem> someLineItems = LineItemData.getSomeLineItems();
		
		Product anotherProduct = ProductData.getAnotherExistingProduct();
		someLineItems.forEach(l -> l.setProduct(anotherProduct));
		
		// assign line items to 1st purchase order
		anotherPurchaseOrder.setLineItems(someLineItems);
		
		// create 2nd purchase order
		PurchaseOrder extraPurchaseOrder = PurchaseOrderData.getExtraPurchaseOrder();
		
		// create 2nd account and related entities
		Account extraAccount = AccountData.getExtraAccount();

		Contact extraContact = ContactData.getExtraContact();
		extraAccount.setContact(extraContact);
		
		Customer extraCustomer = CustomerData.getExtraCustomer();
		extraCustomer.setAccount(extraAccount);
		extraAccount.setCustomer(extraCustomer);

		// add 2nd new account to 2nd purchase order
		extraPurchaseOrder.setAccount(extraAccount);
		extraAccount.getPurchaseOrders().add(extraPurchaseOrder);
		
		// make a second list of line items with products
		List<LineItem> moreLineItems = LineItemData.getMoreLineItems();
		
		Product extraProduct = ProductData.getExtraExistingProduct();
		moreLineItems.forEach(l -> l.setProduct(extraProduct));
		
		// add second list of line items to 2ns purchase order
		extraPurchaseOrder.setLineItems(moreLineItems);
		
		// create array of 1st and 2nd purchase orders
		List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
		purchaseOrders.add(anotherPurchaseOrder);
		purchaseOrders.add(extraPurchaseOrder);
		
		// invoke saveEntities here
		List<PurchaseOrder> savedPurchaseOrders = purchaseOrderRepository.saveEntities(purchaseOrders);
		purchaseOrderRepository.flush();
		
		// check saved entities returned
		assertThat(savedPurchaseOrders.size() == 2).isTrue();
		
		// check returned entities match saved entities
		assertThat(purchaseOrders.stream().allMatch(t -> savedPurchaseOrders.contains(t))).isTrue();
		assertThat(savedPurchaseOrders.stream().allMatch(t -> purchaseOrders.contains(t))).isTrue();
		
		// // check two entities saved
		long purchaseOrderCnt = purchaseOrderRepository.countAll();
		assertThat(purchaseOrderCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// retrieve 1st purchase order, check persisted and has 1st account
		Optional<PurchaseOrder> rtrvAnotherPurchaseOrder = purchaseOrderRepository.findById(anotherPurchaseOrder.getId());
		assertThat(rtrvAnotherPurchaseOrder.orElse(null)).isNotNull();
		assertThat(rtrvAnotherPurchaseOrder.get().equals(anotherPurchaseOrder)).isTrue();
		assertThat(rtrvAnotherPurchaseOrder.get().getAccount().equals(anotherAccount)).isTrue();
		
		// check 1st purchase order has 1st list of line items
		assertThat(Boolean.FALSE.equals(rtrvAnotherPurchaseOrder.get().getLineItems().isEmpty()));
		assertThat(rtrvAnotherPurchaseOrder.get().getLineItems().stream().allMatch(t -> someLineItems.contains(t))).isTrue();
		assertThat(someLineItems.stream().allMatch(t -> rtrvAnotherPurchaseOrder.get().getLineItems().contains(t))).isTrue();

		// retrieve 2nd purchase order, check persisted and has 2nd account
		Optional<PurchaseOrder> rtrvExtraPurchaseOrder = purchaseOrderRepository.findById(extraPurchaseOrder.getId());
		assertThat(rtrvExtraPurchaseOrder.orElse(null)).isNotNull();
		assertThat(rtrvExtraPurchaseOrder.get().equals(extraPurchaseOrder)).isTrue();
		assertThat(rtrvExtraPurchaseOrder.get().getAccount().equals(extraAccount)).isTrue();
		
		// check 2nd purchase order has 2nd list of line items
		assertThat(Boolean.FALSE.equals(rtrvExtraPurchaseOrder.get().getLineItems().isEmpty()));
		assertThat(rtrvExtraPurchaseOrder.get().getLineItems().stream().allMatch(t -> moreLineItems.contains(t))).isTrue();
		assertThat(moreLineItems.stream().allMatch(t -> rtrvExtraPurchaseOrder.get().getLineItems().contains(t))).isTrue();
	}
	

	@Test
	public void whenModified_thenPurchaseOrderUpdated() {
		log.info("whenModified_thenPurchaseOrderUpdated");
		
		// retrieve purchase order
		Optional<PurchaseOrder> originalPurchaseOrder = purchaseOrderRepository.findById(3L);
		
		// update purchase order and related entities
		originalPurchaseOrder.get().setOrderIdentifier("UPDATED - " + originalPurchaseOrder.get().getOrderIdentifier());
		originalPurchaseOrder.get().getAccount().setAccountName("UPDATED - " + originalPurchaseOrder.get().getAccount().getAccountName());
		originalPurchaseOrder.get().getLineItems().get(0).setQuantity(originalPurchaseOrder.get().getLineItems().get(0).getQuantity() + 1);
		
		// retrieve purchase order again 
		Optional<PurchaseOrder> rtrvdPurchaseOrder = purchaseOrderRepository.findById(3L);
		
		// check purchase order and related entities updated without save
		assertThat(originalPurchaseOrder.get().getOrderIdentifier().equals((rtrvdPurchaseOrder.get().getOrderIdentifier())));
		assertThat(originalPurchaseOrder.get().getAccount().getAccountName().equals((rtrvdPurchaseOrder.get().getAccount().getAccountName())));
		assertThat(originalPurchaseOrder.get().getLineItems().get(0).getQuantity().equals((rtrvdPurchaseOrder.get().getLineItems().get(0).getQuantity())));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info("whenExistsByDto_thenReturnTrue");
		
		// create purchaseOrder dto with qualifiers, including related entities
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30-418");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		
		purchaseOrderDto.setAccountDto(accountDto);
		
		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Papermate Pen");
		
		// invoke existsByDto here
		Boolean exists = purchaseOrderRepository.existsByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		// test for existence
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		log.info("whenCountByDto_thenReturnCount");
		
		// create purchaseOrder dto with qualifiers, including related entities
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setInvoiced(true);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setBillingAddress("Dallas");
		
		purchaseOrderDto.setAccountDto(accountDto);
		
		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Papermate Pen");
		
		// invoke countByDto here
		long purchaseOrderCnt =  purchaseOrderRepository.countByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		// check count 
		assertThat(purchaseOrderCnt).isGreaterThanOrEqualTo(2L);
	}
	

	@Test
	public void whenFindByDto_thenReturnPurchaseOrders() {
		log.info("whenFindByDto_thenReturnPurchaseOrders");
		
		// create purchaseOrder dto with qualifiers, including related entities
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30-418");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		
		purchaseOrderDto.setAccountDto(accountDto);
		
		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Papermate Pen");
		
		purchaseOrderDto.asParam.setLineItemDto(lineItemDto);
		
		// invoke findByDto here
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		// check some purchase orders found
		assertThat(purchaseOrders).isNotEmpty();
		
		purchaseOrders.stream().forEach(purchaseOrder -> {
			// check each purchase order meets qualifiers
			assertThat(purchaseOrder.getOrderIdentifier().equals(purchaseOrderDto.getOrderIdentifier()));
			assertThat(purchaseOrder.getAccount().getAccountName().equals(purchaseOrderDto.getAccountDto().getAccountName()));
			assertThat(purchaseOrder.getLineItems().stream().anyMatch(li -> li.getProduct().getName().equals(lineItemDto.getProductDto().getName())));
		});
	}

	@Test
	public void whenFindPageByDto_thenReturnPurchaseOrders() {
		log.info("whenFindPageByDto_thenReturnPurchaseOrders");
		
		// create purchaseOrder dto with qualifiers, including related entities
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setStatus("SHIPPED");
		
		// limit to first two purchase orders
		purchaseOrderDto.setStart(0);
		purchaseOrderDto.setLimit(2);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		purchaseOrderDto.setAccountDto(accountDto);

		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Papermate Pen");
		
		purchaseOrderDto.asParam.setLineItemDto(lineItemDto);

		// invoke findPageByDto here 
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findPageByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		// check some purchase orders found but at most two returned
		assertThat(purchaseOrders.isEmpty()).isFalse();
		assertThat(purchaseOrders.size()).isLessThanOrEqualTo(2);
		
		purchaseOrders.stream().forEach(purchaseOrder -> {
			// check each purchase order meets qualifiers
			assertThat(purchaseOrder.getStatus().equals(purchaseOrderDto.getStatus()));
			assertThat(purchaseOrder.getAccount().getAccountName().equals(purchaseOrderDto.getAccountDto().getAccountName()));
			assertThat(purchaseOrder.getLineItems().stream().anyMatch(li -> li.getProduct().getName().equals(lineItemDto.getProductDto().getName())));
		});
	}

	@Test
	public void whenSearchByDto_thenReturnPurchaseOrders() {
		log.info("whenSearchByDto_thenReturnPurchaseOrders");
		
		// create purchaseOrder dto with qualifiers, including related entities
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		purchaseOrderDto.setAccountDto(accountDto);

		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Pen");

		// invoke searchByDto here
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.searchByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		// check some purchase orders found
		assertThat(purchaseOrders).isNotEmpty();
		
		purchaseOrders.stream().forEach(purchaseOrder -> {
			// check each purchase order meets qualifiers
			assertThat(purchaseOrder.getOrderIdentifier().contains(purchaseOrderDto.getOrderIdentifier()));
			assertThat(purchaseOrder.getAccount().getShippingAddress().contains(purchaseOrderDto.getAccountDto().getShippingAddress()));
			assertThat(purchaseOrder.getLineItems().stream().anyMatch(li -> li.getProduct().getName().contains(lineItemDto.getProductDto().getName())));
		});
	}

	@Test
	public void whenSearchPageByDto_thenReturnPurchaseOrders() {
		log.info("whenSearchPageByDto_thenReturnPurchaseOrders");
		
		// create purchaseOrder dto with qualifiers, including related entities
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30");
		
		// limit to first two purchase orders
		purchaseOrderDto.setStart(0);
		purchaseOrderDto.setLimit(2);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		purchaseOrderDto.setAccountDto(accountDto);

		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Pen");

		// invoke searchPageByDto here
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.searchPageByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		// check some purchase orders found but at most two returned
		assertThat(purchaseOrders.isEmpty()).isFalse();
		assertThat(purchaseOrders.size()).isLessThanOrEqualTo(2);
		
		purchaseOrders.stream().forEach(purchaseOrder -> {
			// check each purchase order meets qualifiers
			assertThat(purchaseOrder.getOrderIdentifier().contains(purchaseOrderDto.getOrderIdentifier()));
			assertThat(purchaseOrder.getAccount().getShippingAddress().contains(purchaseOrderDto.getAccountDto().getShippingAddress()));
			assertThat(purchaseOrder.getLineItems().stream().anyMatch(li -> li.getProduct().getName().contains(lineItemDto.getProductDto().getName())));
		});
	}
}
