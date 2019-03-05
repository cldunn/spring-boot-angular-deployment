package com.cldbiz.userportal.unit.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cldbiz.userportal.dao.misc.MiscDao;
import com.cldbiz.userportal.domain.Commission;
import com.cldbiz.userportal.domain.Product;
import com.cldbiz.userportal.domain.PurchaseOrder;
import com.cldbiz.userportal.domain.User;
import com.cldbiz.userportal.dto.ProductDto;
import com.cldbiz.userportal.dto.UserDto;
import com.cldbiz.userportal.dto.misc.AccountForCommissionedUserDto;
import com.cldbiz.userportal.dto.misc.PurchaseOrderAccountDto;
import com.cldbiz.userportal.repository.product.ProductRepository;
import com.cldbiz.userportal.repository.user.UserRepository;
import com.cldbiz.userportal.unit.BaseRepositoryTest;

public class MiscDaoTest extends BaseRepositoryTest {
	@Autowired
	MiscDao miscDao;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;
	
	@Test
	public void whenFindPurchaseOrdersForProduct_thenReturnPurchaseOrderAccountDtos() {
		Optional<Product> product = productRepository.findById(105L);
		assertThat(product.get()).isNotNull();
		
		ProductDto productDto = new ProductDto(product.get());
		
		List<PurchaseOrderAccountDto> purchaseOrderAccountDtos = miscDao.findPurchaseOrdersForProduct(productDto);
		productRepository.flush();
		
		assertThat(purchaseOrderAccountDtos).isNotEmpty();
		
		purchaseOrderAccountDtos.forEach(purchaseOrderAccountDto -> {
			assertThat(purchaseOrderAccountDto.getAccountName()).isNotNull();
			assertThat(purchaseOrderAccountDto.getOrderIdentifier()).isNotNull();
		});
	}

	@Test
	public void whenFindPurchaseOrderAccountsForProduct_thenReturnPurchaseOrderAccountDtos() {
		Optional<Product> product = productRepository.findById(105L);
		assertThat(product.get()).isNotNull();
		
		ProductDto productDto = new ProductDto(product.get());
		
		List<PurchaseOrderAccountDto> purchaseOrderAccountDtos = miscDao.findPurchaseOrderAccountsForProduct(productDto);
		productRepository.flush();
		
		assertThat(purchaseOrderAccountDtos).isNotEmpty();
		
		purchaseOrderAccountDtos.forEach(purchaseOrderAccountDto -> {
			assertThat(purchaseOrderAccountDto.getAccountName()).isNotNull();
			assertThat(purchaseOrderAccountDto.getOrderIdentifier()).isNotNull();
			assertThat(purchaseOrderAccountDto.getLineItems().stream().anyMatch(lineItem -> lineItem.getProduct().getId().equals(product.get().getId())));
		});
	}

	@Test
	public void whenFindPurchaseOrdersForUserForProduct_thenReturnPurchaseOrders() {
		Optional<User> user = userRepository.findById(2L);
		assertThat(user.get()).isNotNull();
		
		Optional<Product> product = productRepository.findById(105L);
		assertThat(product.get()).isNotNull();
		
		UserDto userDto = new UserDto(user.get());
		ProductDto productDto = new ProductDto(product.get());
		
		List<PurchaseOrder> purchaseOrders = miscDao.findPurchaseOrdersForUserForProduct(userDto, productDto);
		productRepository.flush();
		
		assertThat(purchaseOrders).isNotEmpty();
		
		purchaseOrders.forEach(purchaseOrder -> {
			assertThat(purchaseOrder.getLineItems().stream().anyMatch(lineItem -> lineItem.getProduct().getId().equals(product.get().getId())));
		});
	}

	@Test
	public void whenFindAccountCommissionedByUser_thenReturnAccountForCommissionedUserDtos() {
		Optional<User> user = userRepository.findById(2L);
		assertThat(user.get()).isNotNull();
		
		UserDto userDto = new UserDto(user.get());
		
		List<AccountForCommissionedUserDto> accountForCommissionedUserDtos = miscDao.findAccountCommissionedByUser(userDto);
		productRepository.flush();
		
		assertThat(accountForCommissionedUserDtos).isNotEmpty();
		
		accountForCommissionedUserDtos.forEach(accountForCommissionedUserDto -> {
			assertThat(accountForCommissionedUserDto.getFirstName().equals(userDto.getFirstName()));
			assertThat(accountForCommissionedUserDto.getLastName().equals(userDto.getLastName()));
			assertThat(accountForCommissionedUserDto.getEmail().equals(userDto.getEmail()));
			assertThat(accountForCommissionedUserDto.getPurchaseOrders()).isNotNull();
		});
		
	}

}
