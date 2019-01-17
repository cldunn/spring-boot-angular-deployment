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
import com.cldbiz.userportal.unit.repository.data.ContactData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/customerData.xml", "/productData.xml", "/purchaseOrderData.xml", "/lineItemData.xml"})
public class PurchaseOrderRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderRepositoryTest.class);
	
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
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		PurchaseOrder purchaseOrder = purchaseOrders.get(0);
		
		Boolean exists = purchaseOrderRepository.existsById(purchaseOrder.getId());
		purchaseOrderRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		Long purchaseOrderCount =  purchaseOrderRepository.countAll();
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrderCount).isEqualTo(TOTAL_ROWS.intValue());
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
	public void whenFindByIds_thenReturnPurchaseOrders() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		List<Long> purchaseOrderIds = purchaseOrders.stream().map(PurchaseOrder::getId).collect(Collectors.toList());
		
		purchaseOrders = purchaseOrderRepository.findByIds(purchaseOrderIds);
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrders.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(purchaseOrders.get(0).getAccount()).isNotNull();
		
		Optional<PurchaseOrder> purchaseOrder = purchaseOrders.stream().
			    filter(p -> Boolean.FALSE.equals(p.getLineItems().isEmpty())).
			    findFirst();
		
		assertThat(purchaseOrder.orElse(null)).isNotNull();
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
	public void whenDeleteByIds_thenRemovePurchaseOrders() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		
		List<Long> purchaseOrderIds = purchaseOrders.stream().map(PurchaseOrder::getId).collect(Collectors.toList());
		
		purchaseOrderRepository.deleteByIds(purchaseOrderIds);
		purchaseOrderRepository.flush();
		
		long purchaseOrderCnt = purchaseOrderRepository.countAll();

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
	public void whenDeleteByEntity_thenRemovePurchaseOrder() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		PurchaseOrder purchaseOrder = purchaseOrders.stream().filter(a -> a.getId().equals(3L)).findFirst().get();
		
		purchaseOrderRepository.deleteByEntity(purchaseOrder);
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
	public void whenDeleteByEntities_thenRemovePurchaseOrders() {
		List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
		
		purchaseOrderRepository.deleteByEntities(purchaseOrders);
		purchaseOrderRepository.flush();
		
		long purchaseOrderCnt = purchaseOrderRepository.countAll();

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
	public void whenSaveEntity_thenReturnSavedPurchaseOrder() {
		PurchaseOrder anotherPurchaseOrder = getAnotherPurchaseOrder();
		
		Account anotherAccount = getAnotherAccount();
		
		Contact anotherContact = ContactData.getAnotherContact();
		anotherAccount.setContact(anotherContact);
		
		Customer anotherCustomer = getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);

		anotherPurchaseOrder.setAccount(anotherAccount);
		anotherAccount.getPurchaseOrders().add(anotherPurchaseOrder);
		
		List<LineItem>someLineItems = getSomeLineItems();
		
		Product anotherProduct = getAnotherProduct();
		someLineItems.forEach(l -> l.setProduct(anotherProduct));
		
		anotherPurchaseOrder.setLineItems(someLineItems);
		
		PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.saveEntity(anotherPurchaseOrder);
		purchaseOrderRepository.flush();
		
		assertThat(savedPurchaseOrder.equals(anotherPurchaseOrder)).isTrue();
		
		long purchaseOrderCnt = purchaseOrderRepository.countAll();
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
	public void whenSaveEntities_thenReturnSavedPurchaseOrders() {
		PurchaseOrder anotherPurchaseOrder = getAnotherPurchaseOrder();
		
		// Manually resolve bi-directional references
		Account anotherAccount = getAnotherAccount();
		
		Contact anotherContact = ContactData.getAnotherContact();
		anotherAccount.setContact(anotherContact);
		
		Customer anotherCustomer = getAnotherCustomer();
		anotherCustomer.setAccount(anotherAccount);
		anotherAccount.setCustomer(anotherCustomer);

		anotherPurchaseOrder.setAccount(anotherAccount);
		anotherAccount.getPurchaseOrders().add(anotherPurchaseOrder);
		
		List<LineItem> someLineItems = getSomeLineItems();
		
		Product anotherProduct = getAnotherProduct();
		someLineItems.forEach(l -> l.setProduct(anotherProduct));
		
		anotherPurchaseOrder.setLineItems(someLineItems);
		
		PurchaseOrder extraPurchaseOrder = getExtraPurchaseOrder();
		
		// Manually resolve bi-directional references
		Account extraAccount = getExtraAccount();

		Contact extraContact = ContactData.getExtraContact();
		extraAccount.setContact(extraContact);
		
		Customer extraCustomer = getExtraCustomer();
		extraCustomer.setAccount(extraAccount);
		extraAccount.setCustomer(extraCustomer);

		extraPurchaseOrder.setAccount(extraAccount);
		extraAccount.getPurchaseOrders().add(extraPurchaseOrder);
		
		List<LineItem> moreLineItems = getMoreLineItems();
		
		Product extraProduct = getExtraProduct();
		moreLineItems.forEach(l -> l.setProduct(extraProduct));
		
		extraPurchaseOrder.setLineItems(moreLineItems);
		
		List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
		purchaseOrders.add(anotherPurchaseOrder);
		purchaseOrders.add(extraPurchaseOrder);
		
		List<PurchaseOrder> savedPurchaseOrders = purchaseOrderRepository.saveEntities(purchaseOrders);
		purchaseOrderRepository.flush();
		
		assertThat(savedPurchaseOrders.size() == 2).isTrue();
		
		assertThat(purchaseOrders.stream().allMatch(t -> savedPurchaseOrders.contains(t))).isTrue();
		assertThat(savedPurchaseOrders.stream().allMatch(t -> purchaseOrders.contains(t))).isTrue();
		
		long purchaseOrderCnt = purchaseOrderRepository.countAll();
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
	public void whenExistsByDto_thenReturnTrue() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30-418");
		
		Boolean exists = purchaseOrderRepository.existsByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setInvoiced(true);
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		
		purchaseOrderDto.setAccountDto(accountDto);
		
		long purchaseOrderCnt =  purchaseOrderRepository.countByDto(purchaseOrderDto);
		purchaseOrderRepository.flush();
		
		assertThat(purchaseOrderCnt).isGreaterThanOrEqualTo(2L);
	}
	

	@Test
	public void whenFindByDto_thenReturnPurchaseOrders() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30-418");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setAccountName("Target");
		
		purchaseOrderDto.setAccountDto(accountDto);
		
		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Papermate Pen");
		
		purchaseOrderDto.setLineItemDto(lineItemDto);
		
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

		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Papermate Pen");
		
		purchaseOrderDto.setLineItemDto(lineItemDto);

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
	public void whenSearchByDto_thenReturnPurchaseOrders() {
		PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
		purchaseOrderDto.setOrderIdentifier("NET30");
		
		AccountDto accountDto = new AccountDto();
		accountDto.setShippingAddress("750");
		purchaseOrderDto.setAccountDto(accountDto);

		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Pen");

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

		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setProductDto(new ProductDto());
		lineItemDto.getProductDto().setName("Pen");

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
	
	private List<LineItem> getSomeLineItems() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		
		LineItem anotherLineItem = new LineItem();
		anotherLineItem.setQuantity(15L);
		
		LineItem extraLineItem = new LineItem();
		extraLineItem.setQuantity(16L);
		
		lineItems.add(anotherLineItem);
		lineItems.add(extraLineItem);
		
		return lineItems;
	}

	private List<LineItem> getMoreLineItems() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		
		LineItem anotherLineItem = new LineItem();
		anotherLineItem.setQuantity(17L);
		
		LineItem extraLineItem = new LineItem();
		extraLineItem.setQuantity(18L);
		
		lineItems.add(anotherLineItem);
		lineItems.add(extraLineItem);
		
		return lineItems;
	}

	private Product getAnotherProduct() {
		Optional<Product> product = productRepository.findById(1L);

		return product.orElse(null);
	}

	private Product getExtraProduct() {
		Optional<Product> product = productRepository.findById(2L);

		return product.orElse(null);
	}
}
