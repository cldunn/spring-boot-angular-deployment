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
import com.cldbiz.userportal.unit.repository.data.LineItemData;
import com.cldbiz.userportal.unit.repository.data.ProductData;
import com.cldbiz.userportal.unit.repository.data.PurchaseOrderData;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DatabaseSetup(value= {"/contactData.xml", "/accountData.xml", "/customerData.xml", "/purchaseOrderData.xml", "/productData.xml", "/lineItemData.xml"})
public class LineItemRepositoryTest extends BaseRepositoryTest {

	private static final Long TOTAL_ROWS = 4L;
	
	@Autowired
	LineItemRepository lineItemRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Test
	public void whenExistsById_thenReturnTrue() {
		log.info("whenExistsById_thenReturnTrue");
		
		List<LineItem> lineItems = lineItemRepository.findAll();
		LineItem lineItem = lineItems.get(0);
		
		// clear cache to test performance
		lineItemRepository.flush();

		// invoke existsById here
		Boolean exists = lineItemRepository.existsById(lineItem.getId());
		lineItemRepository.flush();
		
		// test for existence
		assertThat(exists).isTrue();
	}

	@Test
	public void whenCountAll_thenReturnLong() {
		log.info("whenCountAll_thenReturnCount");
		
		// invoke countAll here
		long lineItemCnt = lineItemRepository.countAll();
		lineItemRepository.flush();
		
		assertThat(lineItemCnt).isEqualTo(TOTAL_ROWS);
	}

	@Test
	public void whenFindById_thenReturnLineItem() {
		log.info("whenFindById_thenReturnLineItem");
		
		// invoke findById here
		Optional<LineItem> sameLineItem = lineItemRepository.findById(3L);
		lineItemRepository.flush();
		
		// check for line item and related entities 
		assertThat(sameLineItem.orElse(null)).isNotNull();
		assertThat(sameLineItem.get().getProduct()).isNotNull();
	}

	@Test
	public void whenFindByIds_thenReturnLineItems() {
		log.info("whenFindByIds_thenReturnLineItems");
		
		// get all line item ids
		List<LineItem> lineItems = lineItemRepository.findAll();
		List<Long> lineItemIds = lineItems.stream().map(LineItem::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		lineItemRepository.flush();
		
		// invoke findByIds here
		lineItems = lineItemRepository.findByIds(lineItemIds);
		lineItemRepository.flush();
		
		// check all line items retrieved
		assertThat(lineItems.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check all line items have product
		Optional<LineItem> lineItem = lineItems.stream().filter(li -> li.getProduct() == null).findAny();
		assertThat(lineItem.orElse(null)).isNull();
	}

	@Test
	public void whenFindAll_thenReturnAllLineItems() {
		log.info("whenFindAll_thenReturnAllLineItems");
		
		// invoke findAll here
		List<LineItem> lineItems = lineItemRepository.findAll();
		lineItemRepository.flush();
		
		// check all line items retrieved
		assertThat(lineItems.size()).isEqualTo(TOTAL_ROWS.intValue());
		
		// check all line items have product
		Optional<LineItem> lineItem = lineItems.stream().filter(li -> li.getProduct() == null).findAny();
		assertThat(lineItem.orElse(null)).isNull();
		
	}

	@Test
	public void whenDeleteById_thenRemoveLineItem() {
		log.info("whenDeleteById_thenRemoveLineItem");
		
		LineItem lineItem = lineItemRepository.findById(1L).get();
		
		// clear cache to test performance
		lineItemRepository.flush();

		// invoke deleteById here
		lineItemRepository.deleteById(lineItem.getId());
		lineItemRepository.flush();
		
		List<LineItem> lineItems = lineItemRepository.findAll();

		// check line item deleted
		assertThat(lineItems.contains(lineItem)).isFalse();
		
		// check product on deleted line item is not deleted
		Optional<Product> product = productRepository.findById(lineItem.getProduct().getId());
		assertThat(product.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteByIds_thenRemoveLineItems() {
		log.info("whenDeleteByIds_thenRemoveLineItems");
		
		// get all line item ids
		List<LineItem> lineItems = lineItemRepository.findAll();
		List<Long> lineItemIds = lineItems.stream().map(LineItem::getId).collect(Collectors.toList());
		
		// clear cache to test performance
		lineItemRepository.flush();

		// invoke deleteByIds here
		lineItemRepository.deleteByIds(lineItemIds);
		lineItemRepository.flush();
		
		// check all line items deleted
		long lineItemCnt = lineItemRepository.countAll();
		assertThat(lineItemCnt).isZero();

		
		lineItems.forEach(lineItem -> {
			// check that each line item's product still exist
			Optional<Product> product = productRepository.findById(lineItem.getProduct().getId());
			assertThat(product.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenDeleteByEntity_thenRemoveLineItem() {
		log.info("whenDeleteByEntity_thenRemoveLineItem");
		
		// get line item
		LineItem lineItem = lineItemRepository.findById(3L).get();
		
		// clear cache to test performance
		lineItemRepository.flush();

		// invoke deleteByEntity here
		lineItemRepository.deleteByEntity(lineItem);
		lineItemRepository.flush();
		
		// check line item deleted
		List<LineItem> lineItems = lineItemRepository.findAll();
		assertThat(lineItems.contains(lineItem)).isFalse();

		// check line item's product not deleted
		Optional<Product> product = productRepository.findById(lineItem.getProduct().getId());
		assertThat(product.orElse(null)).isNotNull();
	}

	@Test
	public void whenDeleteByEntities_thenRemoveLineItems() {
		log.info("whenDeleteByEntities_thenRemoveLineItems");
		
		// get all line items
		List<LineItem> lineItems = lineItemRepository.findAll();
		
		// clear cache to test performance
		lineItemRepository.flush();

		// invoke deleteByEntities here
		lineItemRepository.deleteByEntities(lineItems);
		lineItemRepository.flush();
		
		// check all line items deleted
		long lineItemCnt = lineItemRepository.countAll();
		assertThat(lineItemCnt).isZero();
		
		lineItems.forEach(lineItem -> {
			/* check that no line item's product deleted */
			Optional<Product> product = productRepository.findById(lineItem.getProduct().getId());
			assertThat(product.orElse(null)).isNotNull();
		});
	}

	@Test
	public void whenSaveEntity_thenReturnSavedLineItem() {
		log.info("whenSaveEntity_thenReturnSavedLineItem");
		
		// create line item
		LineItem anotherLineItem = LineItemData.getAnotherLineItem();
		
		// assign product to line item
		Product anotherProduct = ProductData.getAnotherExistingProduct(); 
		anotherLineItem.setProduct(anotherProduct);
		
		// Since purchaseOrder.lineItems has orphanRemoval, must save to existing purchaseOrder
		PurchaseOrder anotherPurchaseOrder = PurchaseOrderData.getAnotherExistingPurchaseOrder(); 
		anotherPurchaseOrder.getLineItems().add(anotherLineItem);

		// save(lineItem) is redundant but updates id of anotherLineItem, flush alone would also work
		LineItem savedLineItem = lineItemRepository.saveEntity(anotherLineItem);
		lineItemRepository.flush();
		
		assertThat(savedLineItem.equals(anotherLineItem)).isTrue();
		
		long lineItemCnt = lineItemRepository.countAll();
		assertThat(lineItemCnt).isEqualTo(TOTAL_ROWS + 1);
		
		// check line item persisted
		Optional<LineItem> rtrvLineItem = lineItemRepository.findById(savedLineItem.getId());
		assertThat(rtrvLineItem.orElse(null)).isNotNull();
		assertThat(rtrvLineItem.get().equals(anotherLineItem)).isTrue();
		assertThat(rtrvLineItem.get().equals(savedLineItem)).isTrue();
		
		// check line item has anotherProduct
		assertThat(savedLineItem.getProduct()).isNotNull();
		assertThat(savedLineItem.getProduct().equals(anotherProduct));
	}

	@Test
	public void whenSaveEntities_thenReturnSavedLineItems() {
		log.info("whenSaveEntities_thenReturnSavedLineItems");
		
		// create line item
		LineItem anotherLineItem = LineItemData.getAnotherLineItem();

		// assign product to line item
		Product anotherProduct = ProductData.getAnotherExistingProduct(); 
		anotherLineItem.setProduct(anotherProduct);
		
		// Since purchaseOrder.lineItems has orphanRemoval, must save to existing purchaseOrder
		PurchaseOrder anotherPurchaseOrder = PurchaseOrderData.getAnotherExistingPurchaseOrder(); 
		anotherPurchaseOrder.getLineItems().add(anotherLineItem);

		// create line item
		LineItem extraLineItem = LineItemData.getExtraLineItem();
		
		// assign product to line item
		Product extraProduct = ProductData.getExtraExistingProduct(); 
		extraLineItem.setProduct(extraProduct);

		// Since purchaseOrder.lineItems has orphanRemoval, must save to existing purchaseOrder
		PurchaseOrder extraPurchaseOrder = PurchaseOrderData.getExtraExistingPurchaseOrder(); 
		extraPurchaseOrder.getLineItems().add(extraLineItem);

		// create list of line items
		List<LineItem> lineItems = new ArrayList<LineItem>();
		lineItems.add(anotherLineItem);
		lineItems.add(extraLineItem);
		
		// savedLineItems(lineItem) is redundant but updates id of new line items, flush alone would also work
		List<LineItem> savedLineItems = lineItemRepository.saveEntities(lineItems);
		lineItemRepository.flush();
		
		assertThat(savedLineItems.size() == 2).isTrue();
		
		// check list of line items returned
		assertThat(lineItems.stream().allMatch(t -> savedLineItems.contains(t))).isTrue();
		assertThat(savedLineItems.stream().allMatch(t -> lineItems.contains(t))).isTrue();
		
		// check line items persisted
		long lineItemCnt = lineItemRepository.countAll();
		assertThat(lineItemCnt).isEqualTo(TOTAL_ROWS + 2);
		
		// check persisted line item matches anotherProduct
		Optional<LineItem> rtrvAnotherLineItem = lineItemRepository.findById(anotherLineItem.getId());
		assertThat(rtrvAnotherLineItem.orElse(null)).isNotNull();
		assertThat(rtrvAnotherLineItem.get().equals(anotherLineItem)).isTrue();
		assertThat(rtrvAnotherLineItem.get().getProduct().equals(anotherProduct)).isTrue();
		
		// check persisted line item matches extraLineItem
		Optional<LineItem> rtrvExtaLineItem = lineItemRepository.findById(extraLineItem.getId());
		assertThat(rtrvExtaLineItem.orElse(null)).isNotNull();
		assertThat(rtrvExtaLineItem.get().equals(extraLineItem)).isTrue();
		assertThat(rtrvExtaLineItem.get().getProduct().equals(extraProduct)).isTrue();
	}

	@Test
	public void whenModified_thenLineItemUpdated() {
		log.info("whenModified_thenLineItemUpdated");
		
		// retrieve line item
		Optional<LineItem> originalLineItem = lineItemRepository.findById(3L);
		
		// update line item and related product
		originalLineItem.get().setQuantity(originalLineItem.get().getQuantity() + 1L);
		originalLineItem.get().getProduct().setName("UPDATED - " + originalLineItem.get().getProduct().getName());
		
		// retrieve line item again 
		Optional<LineItem> rtrvdLineItem = lineItemRepository.findById(3L);
		
		// check line item and related product updated
		assertThat(originalLineItem.get().getQuantity().equals((rtrvdLineItem.get().getQuantity())));
		assertThat(originalLineItem.get().getProduct().getName().equals((rtrvdLineItem.get().getProduct().getName())));
	}

	@Test
	public void whenExistsByDto_thenReturnTrue() {
		log.info(" whenExistsByDto_thenReturnTrue");
		
		// create line item dto
		LineItemDto lineItemDto = new LineItemDto();

		// qualify search for particular product
		Product product = ProductData.getAnotherExistingProduct();
		ProductDto productDto = new ProductDto(product);
		lineItemDto.setProductDto(productDto);
		
		// invoke existsByDto here
		Boolean exists = lineItemRepository.existsByDto(lineItemDto);
		lineItemRepository.flush();
		
		// check existence
		assertThat(Boolean.TRUE).isEqualTo(exists);
	}
	
	@Test
	public void whenCountByDto_thenReturnCount() {
		log.info("whenCountByDto_thenReturnCount");
		
		// create line item dto
		LineItemDto lineItemDto = new LineItemDto();

		// qualify search for particular product
		Product product = ProductData.getAnotherExistingProduct();
		ProductDto productDto = new ProductDto(product);
		lineItemDto.setProductDto(productDto);
		
		// invoke existsByDto here
		long lineItemCnt =  lineItemRepository.countByDto(lineItemDto);
		lineItemRepository.flush();
		
		// check count
		assertThat(lineItemCnt).isGreaterThanOrEqualTo(1L);
	}
	
	@Test
	public void whenFindByDto_thenReturnLineItems() {
		log.info("whenFindByDto_thenReturnLineItems");
		
		// create line item dto
		LineItemDto lineItemDto = new LineItemDto();
		
		// qualify search for particular product
		Product product = ProductData.getAnotherExistingProduct();
		ProductDto productDto = new ProductDto(product);
		lineItemDto.setProductDto(productDto);
		
		// invoke findByDto here
		List<LineItem> lineItems = lineItemRepository.findByDto(lineItemDto);
		lineItemRepository.flush();
		
		// check line items found
		assertThat(lineItems).isNotEmpty();
		
		lineItems.stream().forEach(li -> {
			// check each line item has product
			assertThat(li.getProduct()).isNotNull();
		});
	}

	@Test
	public void whenFindPageByDto_thenReturnLineItems() {
		log.info("whenFindPageByDto_thenReturnLineItems");
		
		// create line item dto
		LineItemDto lineItemDto = new LineItemDto();
		
		// qualify search for particular product
		Product product = ProductData.getAnotherExistingProduct();
		ProductDto productDto = new ProductDto(product);
		lineItemDto.setProductDto(productDto);
		
		// limit to first two line items
		lineItemDto.setStart(0);
		lineItemDto.setLimit(2);
		
		// invoke findPageByDto here
		List<LineItem> lineItems = lineItemRepository.findPageByDto(lineItemDto);
		lineItemRepository.flush();
		
		// check line items found
		assertThat(lineItems).isNotEmpty();
		assertThat(lineItems.size()).isLessThanOrEqualTo(2);

		lineItems.stream().forEach(li -> {
			// check each line item has product
			assertThat(li.getProduct()).isNotNull();
		});

	}

	@Test
	public void whenSearchByDto_thenReturnLineItems() {
		log.info("whenSearchByDto_thenReturnLineItems");
		
		// create line item dto
		LineItemDto lineItemDto = new LineItemDto();
		
		// qualify by related product
		ProductDto productDto = new ProductDto();
		productDto.setName("printer");
		lineItemDto.setProductDto(productDto);
		
		// invoke searchByDto here
		List<LineItem> lineItems = lineItemRepository.searchByDto(lineItemDto);
		lineItemRepository.flush();
		
		// check line items found
		assertThat(lineItems).isNotEmpty();

		lineItems.stream().forEach(li -> {
			// check each line item has product
			assertThat(li.getProduct()).isNotNull();
		});
	}

	@Test
	public void whenSearchPageByDto_thenReturnLineItems() {
		log.info("whenSearchPageByDto_thenReturnLineItems");
		
		// create line item dto
		LineItemDto lineItemDto = new LineItemDto();
		
		// limit to first two line items
		lineItemDto.setStart(0);
		lineItemDto.setLimit(2);
		
		// qualify by related product
		ProductDto productDto = new ProductDto();
		productDto.setName("printer");
		lineItemDto.setProductDto(productDto);

		// invoke searchPageByDto here
		List<LineItem> lineItems = lineItemRepository.searchPageByDto(lineItemDto);
		lineItemRepository.flush();
		
		// check line items found
		assertThat(lineItems).isNotEmpty();
		assertThat(lineItems.size()).isLessThanOrEqualTo(2);

		lineItems.stream().forEach(li -> {
			// check each line item has product
			assertThat(li.getProduct()).isNotNull();
		});
	}

}
