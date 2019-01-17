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

import com.cldbiz.userportal.domain.LineItem;
import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.dto.AccountDto;
import com.cldbiz.userportal.dto.LineItemDto;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.PurchaseOrderDto;
import com.cldbiz.userportal.repository.lineItem.LineItemRepository;
import com.cldbiz.userportal.repository.product.ProductRepository;
import com.cldbiz.userportal.repository.purchaseOrder.PurchaseOrderRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/customerData.xml", "/purchaseOrderData.xml", "/productData.xml", "/lineItemData.xml"})
public class LineItemRepositoryTest extends BaseRepositoryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductRepositoryTest.class);
	
	private static final Long TOTAL_ROWS = 4L;
	
	@Autowired
	LineItemRepository lineItemRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		List<LineItem> lineItems = lineItemRepository.findAll();
		LineItem lineItem = lineItems.get(0);
		
		Boolean exists = lineItemRepository.existsById(lineItem.getId());
		lineItemRepository.flush();
		
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		long lineItemCnt = lineItemRepository.countAll();
		lineItemRepository.flush();
		
		assertThat(lineItemCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnLineItem() {
		Optional<LineItem> sameLineItem = lineItemRepository.findById(3L);
		lineItemRepository.flush();
		
		assertThat(sameLineItem.orElse(null)).isNotNull();
		assertThat(sameLineItem.get().getProduct()).isNotNull();
	}

	@Test
	public void whenFindByIds_thenReturnLineItems() {
		List<LineItem> lineItems = lineItemRepository.findAll();
		List<Long> lineItemIds = lineItems.stream().map(LineItem::getId).collect(Collectors.toList());
		
		lineItems = lineItemRepository.findByIds(lineItemIds);
		lineItemRepository.flush();
		
		assertThat(lineItems.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(lineItems.get(0).getProduct()).isNotNull();
	}

	@Test
	public void whenFindAll_thenReturnAllLineItems() {
		List<LineItem> lineItems = lineItemRepository.findAll();
		lineItemRepository.flush();
		
		assertThat(lineItems.size()).isEqualTo(TOTAL_ROWS.intValue());
		assertThat(lineItems.get(0).getProduct()).isNotNull();
		
	}

	@Test
	public void whenDeleteById_thenRemoveLineItem() {
		List<LineItem> lineItems = lineItemRepository.findAll();
		LineItem lineItem = lineItems.get(0);
		
		lineItemRepository.deleteById(lineItem.getId());
		lineItemRepository.flush();
		
		lineItems = lineItemRepository.findAll();

		assertThat(lineItems.contains(lineItem)).isFalse();
		
		Optional<Product> product = productRepository.findById(lineItem.getProduct().getId());
		assertThat(product.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteByIds_thenRemoveLineItems() {
		List<LineItem> lineItems = lineItemRepository.findAll();

		List<Long> lineItemIds = lineItems.stream().map(LineItem::getId).collect(Collectors.toList());
		
		lineItemRepository.deleteByIds(lineItemIds);
		lineItemRepository.flush();
		
		long lineItemCnt = lineItemRepository.countAll();

		assertThat(lineItemCnt).isZero();

		lineItems.forEach(lineItem -> {
			Optional<Product> product = productRepository.findById(lineItem.getProduct().getId());
			assertThat(product.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenDeleteByEntity_thenRemoveLineItem() {
		List<LineItem> lineItems = lineItemRepository.findAll();
		LineItem lineItem = lineItems.stream().filter(a -> a.getId().equals(3L)).findFirst().get();
		
		lineItemRepository.deleteByEntity(lineItem);
		lineItemRepository.flush();
		
		lineItems = lineItemRepository.findAll();
		
		assertThat(lineItems.contains(lineItem)).isFalse();

		Optional<Product> product = productRepository.findById(lineItem.getProduct().getId());
		assertThat(product.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteByEntities_thenRemoveLineItems() {
		List<LineItem> lineItems = lineItemRepository.findAll();
		
		lineItemRepository.deleteByEntities(lineItems);
		lineItemRepository.flush();
		
		long lineItemCnt = lineItemRepository.countAll();

		assertThat(lineItemCnt).isZero();
		
		lineItems.forEach(lineItem -> {
			Optional<Product> product = productRepository.findById(lineItem.getProduct().getId());
			assertThat(product.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenSaveEntity_thenReturnSavedLineItem() {
		LineItem anotherLineItem = getAnotherLineItem();
		
		Product anotherProduct = getAnotherProduct(); 
		anotherLineItem.setProduct(anotherProduct);
		
		// Since purchaseOrder.lineItems is uni-directional, assigning lineitem to purchaseOrder is how to insert
		PurchaseOrder anotherPurchaseOrder = getAnotherPurchaseOrder(); 
		anotherPurchaseOrder.getLineItems().add(anotherLineItem);

		// Since purchaseOrder.lineItems is uni-directional, save(lineItem) is for update
		LineItem savedLineItem = lineItemRepository.saveEntity(anotherLineItem);
		lineItemRepository.flush();
		
		assertThat(savedLineItem.equals(anotherLineItem)).isTrue();
		
		long lineItemCnt = lineItemRepository.countAll();
		assertThat(lineItemCnt).isEqualTo(TOTAL_ROWS + 1);
		
		Optional<LineItem> rtrvLineItem = lineItemRepository.findById(savedLineItem.getId());
		assertThat(rtrvLineItem.orElse(null)).isNotNull();
		assertThat(rtrvLineItem.get().equals(anotherLineItem)).isTrue();
		assertThat(rtrvLineItem.get().equals(savedLineItem)).isTrue();
		
		assertThat(savedLineItem.getProduct()).isNotNull();
	}

	@Test
	public void whenSaveEntities_thenReturnSavedLineItems() {
		LineItem anotherLineItem = getAnotherLineItem();

		Product anotherProduct = getAnotherProduct(); 
		anotherLineItem.setProduct(anotherProduct);
		
		// Since purchaseOrder.lineItems is uni-directional, assigning lineitem to purchaseOrder is how to insert
		PurchaseOrder anotherPurchaseOrder = getAnotherPurchaseOrder(); 
		anotherPurchaseOrder.getLineItems().add(anotherLineItem);

		LineItem extraLineItem = getExtraLineItem();
		
		Product extraProduct = getExtraProduct(); 
		extraLineItem.setProduct(extraProduct);

		// Since purchaseOrder.lineItems is uni-directional, assigning lineitem to purchaseOrder is how to insert
		PurchaseOrder extraPurchaseOrder = getExtraPurchaseOrder(); 
		extraPurchaseOrder.getLineItems().add(extraLineItem);

		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(anotherLineItem);
		lineItems.add(extraLineItem);
		
		// Since purchaseOrder.lineItems is uni-directional, save(lineItem) is for update
		List<LineItem> savedLineItems = lineItemRepository.saveEntities(lineItems);
		lineItemRepository.flush();
		
		assertThat(savedLineItems.size() == 2).isTrue();
		
		assertThat(lineItems.stream().allMatch(t -> savedLineItems.contains(t))).isTrue();
		assertThat(savedLineItems.stream().allMatch(t -> lineItems.contains(t))).isTrue();
		
		long lineItemCnt = lineItemRepository.countAll();
		assertThat(lineItemCnt).isEqualTo(TOTAL_ROWS + 2);
		
		Optional<LineItem> rtrvAnotherLineItem = lineItemRepository.findById(anotherLineItem.getId());
		assertThat(rtrvAnotherLineItem.orElse(null)).isNotNull();
		assertThat(rtrvAnotherLineItem.get().equals(anotherLineItem)).isTrue();
		assertThat(rtrvAnotherLineItem.get().getProduct().equals(anotherProduct)).isTrue();
		
		Optional<LineItem> rtrvExtaLineItem = lineItemRepository.findById(extraLineItem.getId());
		assertThat(rtrvExtaLineItem.orElse(null)).isNotNull();
		assertThat(rtrvExtaLineItem.get().equals(extraLineItem)).isTrue();
		assertThat(rtrvExtaLineItem.get().getProduct().equals(extraProduct)).isTrue();
	}

	@Test
	public void whenModified_thenLineItemUpdated() {
		Optional<LineItem> originalLineItem = lineItemRepository.findById(3L);
		originalLineItem.get().setQuantity(originalLineItem.get().getQuantity() + 1L);
		originalLineItem.get().getProduct().setName("UPDATED - " + originalLineItem.get().getProduct().getName());
		
		Optional<LineItem> rtrvdLineItem = lineItemRepository.findById(3L);
		assertThat(originalLineItem.get().getQuantity().equals((rtrvdLineItem.get().getQuantity())));
		assertThat(originalLineItem.get().getProduct().getName().equals((rtrvdLineItem.get().getProduct().getName())));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		LineItemDto lineItemDto = new LineItemDto();

		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		lineItemDto.setProductDto(productDto);
		
		Boolean exists = lineItemRepository.existsByDto(lineItemDto);
		lineItemRepository.flush();
		
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		LineItemDto lineItemDto = new LineItemDto();

		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		lineItemDto.setProductDto(productDto);
		
		long lineItemCnt =  lineItemRepository.countByDto(lineItemDto);
		lineItemRepository.flush();
		
		assertThat(lineItemCnt).isGreaterThanOrEqualTo(1L);
	}
	
	@Test
	public void whenFindByDto_thenReturnLineItems() {
		LineItemDto lineItemDto = new LineItemDto();
		
		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		lineItemDto.setProductDto(productDto);
		
		List<LineItem> lineItems = lineItemRepository.findByDto(lineItemDto);
		lineItemRepository.flush();
		
		assertThat(lineItems).isNotEmpty();
		
		Optional<LineItem> lineItem = lineItems.stream().findFirst();
		
		assertThat(lineItem.orElse(null)).isNotNull();
		assertThat(lineItem.get().getProduct()).isNotNull();
	}

	@Test
	public void whenFindPageByDto_thenReturnLineItems() {
		LineItemDto lineItemDto = new LineItemDto();
		
		Product product = getAnotherProduct();
		ProductDto productDto = new ProductDto(product);
		lineItemDto.setProductDto(productDto);
		
		lineItemDto.setStart(0);
		lineItemDto.setLimit(2);
		
		List<LineItem> lineItems = lineItemRepository.findPageByDto(lineItemDto);
		lineItemRepository.flush();
		
		assertThat(lineItems).isNotEmpty();
		assertThat(lineItems.size()).isLessThanOrEqualTo(2);
		assertThat(lineItems.get(0).getProduct()).isNotNull();
	}

	@Test
	public void whenSearchByDto_thenReturnLineItems() {
		LineItemDto lineItemDto = new LineItemDto();
		
		ProductDto productDto = new ProductDto();
		productDto.setName("printer");
		lineItemDto.setProductDto(productDto);
		
		List<LineItem> lineItems = lineItemRepository.searchByDto(lineItemDto);
		lineItemRepository.flush();
		
		assertThat(lineItems).isNotEmpty();
		assertThat(lineItems.get(0).getProduct()).isNotNull();
	}

	@Test
	public void whenSearchPageByDto_thenReturnLineItems() {
		LineItemDto lineItemDto = new LineItemDto();
		lineItemDto.setStart(0);
		lineItemDto.setLimit(2);
		
		ProductDto productDto = new ProductDto();
		productDto.setName("printer");
		lineItemDto.setProductDto(productDto);

		List<LineItem> lineItems = lineItemRepository.searchPageByDto(lineItemDto);
		lineItemRepository.flush();
		
		assertThat(lineItems).isNotEmpty();
		assertThat(lineItems.size()).isLessThanOrEqualTo(2);
		assertThat(lineItems.get(0).getProduct()).isNotNull();
	}

	private LineItem getAnotherLineItem() {
		LineItem lineItem = new LineItem();
		
		lineItem.setQuantity(18L);
		
		return lineItem;
	}
	
	private LineItem getExtraLineItem() {
		LineItem lineItem = new LineItem();
		
		lineItem.setQuantity(23L);
		
		return lineItem;

	}

	private Product getAnotherProduct() {
		Optional<Product> product = productRepository.findById(1L);
		return product.orElse(null);
	}

	private Product getExtraProduct() {
		Optional<Product> product = productRepository.findById(2L);
		return product.orElse(null);
	}

	private PurchaseOrder getAnotherPurchaseOrder() {
		Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(1L);

		return purchaseOrder.orElse(null);
	}

	private PurchaseOrder getExtraPurchaseOrder() {
		Optional<PurchaseOrder> purchaseOrder = purchaseOrderRepository.findById(2L);

		return purchaseOrder.orElse(null);
	}
}
