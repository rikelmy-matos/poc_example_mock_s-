package com.devsuperior.examplemockspy.services;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.examplemockspy.dto.ProductDTO;
import com.devsuperior.examplemockspy.entities.Product;
import com.devsuperior.examplemockspy.repositories.ProductRepository;
import com.devsuperior.examplemockspy.services.exceptions.InvalidDataException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	
	private Product product;
	private ProductDTO productDTO;
	
	@BeforeEach
	void setUp()throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		
		product = new Product(1L, "Playstation", 2000.0);
		productDTO = new ProductDTO(product);
		
		Mockito.when(repository.save(any())).thenReturn(product);
		
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}
	
	@Test
	public void insertShouldReturnProductDTOWhenValidDate() {
		
		ProductService serviceSpy = Mockito.spy(service);
		Mockito.doNothing().when(serviceSpy).validateData(productDTO);
		
		ProductDTO result = serviceSpy.insert(productDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getName(), "Playstation");
		
	}
	
	@Test
	public void insertShouldReturnInvalidDataExceptionWhenProductNameIsBlank() {
		
		productDTO.setName("");
	
		ProductService serviceSpy = Mockito.spy(service);
		Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);
		
		Assertions.assertThrows(InvalidDataException.class, () -> {
			ProductDTO result = serviceSpy.insert(productDTO);
		});
	}
	
	@Test
	public void insertShouldReturnInvalidDataExceptionWhenProductPriceIsNegativeOrZero() {
		
		productDTO.setPrice(-5.0);
	
		ProductService serviceSpy = Mockito.spy(service);
		Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);
		
		Assertions.assertThrows(InvalidDataException.class, () -> {
			ProductDTO result = serviceSpy.insert(productDTO);
		});
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExistsAndValidData() {
		
		ProductService serviceSpy = Mockito.spy(service);
		Mockito.doNothing().when(serviceSpy).validateData(productDTO);
		
		ProductDTO result = serviceSpy.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingId);
	}
	
	@Test
	public void updateShouldReturnInvalidDataExceptionWhenIdExistsAndProductNameIsBlank() {
		
		productDTO.setName("");
		
		ProductService serviceSpy = Mockito.spy(service);
		Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);
		
		Assertions.assertThrows(InvalidDataException.class, () -> {
			ProductDTO result = serviceSpy.update(existingId, productDTO);
		});
		
	}
	
	@Test
	public void updateShouldReturnInvalidDataExceptionWhenIdExistsAndProductPriceIsNegativeOrZero() {
		
		productDTO.setPrice(-5.0);
		
		ProductService serviceSpy = Mockito.spy(service);
		Mockito.doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);
		
		Assertions.assertThrows(InvalidDataException.class, () -> {
			ProductDTO result = serviceSpy.update(existingId, productDTO);
		});
		
	}
	

}
