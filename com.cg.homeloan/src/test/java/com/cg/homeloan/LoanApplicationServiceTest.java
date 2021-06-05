package com.cg.homeloan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cg.homeloan.entities.Customer;
import com.cg.homeloan.entities.LoanApplication;
import com.cg.homeloan.exceptions.CustomerNotFoundException;
import com.cg.homeloan.exceptions.LoanApplicationNotFoundException;
import com.cg.homeloan.repositories.ILoanApplicationRepository;
import com.cg.homeloan.services.ILoanApplicationService;

@SpringBootTest
class LoanApplicationServiceTest {

	public static LoanApplication loanApplication, loanApplication1, loanApplication2, obj;
	public static Customer customer,customer1;

	@BeforeAll
	public static void setUp() {
		customer = new Customer();
		customer.setUserId(1);
		customer.setCustomerName("Sita");
		customer.setDateOfBirth(LocalDate.now());
		customer.setGender("female");
		customer.setEmailId("sita@gmail.com");
		customer.setMobileNumber("7898789887");
		customer.setNationality("Indian");
		customer.setPanNumber("213BP2P");
		customer.setAadharNumber("528545691236");
		customer.setUsername("Sita");
		customer.setPassword("1234");
		
		loanApplication = new LoanApplication(customer,1200000,10);
		loanApplication.setApplicationId(3);
//		loanApplication.setCustomer(customer);
//		loanApplication.setLoanAppliedAmount(120000);
		
		customer1 = new Customer();
		customer1.setUserId(2);
		customer1.setCustomerName("Sita");
		customer1.setDateOfBirth(LocalDate.now());
		customer1.setGender("female");
		customer1.setEmailId("sita@gmail.com");
		customer1.setMobileNumber("7898789887");
		customer1.setNationality("Indian");
		customer1.setPanNumber("213BP2P");
		customer1.setAadharNumber("528545691236");
		customer1.setUsername("Sita");
		customer1.setPassword("1234");
		
		loanApplication1 = new LoanApplication();
		loanApplication1.setApplicationId(4);
		loanApplication1.setCustomer(customer1);
		loanApplication1.setLoanAppliedAmount(100000);
		loanApplication1.setLoanTenureYears(10);
		

	}

	@Autowired
	ILoanApplicationService loanApplicationService;

	@MockBean
	ILoanApplicationRepository loanApplicationRepository;

	@Test
	@DisplayName("positive test case of add loanapplication")
	void testAddLoanApplicationPositive() throws Exception {
		
		when(loanApplicationRepository.save(loanApplication)).thenReturn(loanApplication);
		assertEquals(loanApplication, loanApplicationService.addLoanApplication(1,1200000,10));
	}

	@Test
	@DisplayName("negative test case of add loanapplication")
	void testAddLoanApplicationNegative() throws CustomerNotFoundException {
		when(loanApplicationRepository.save(loanApplication)).thenReturn(loanApplication);
		assertNotEquals(loanApplication, loanApplicationService.addLoanApplication(1,1200000,10));
	}

	@Test
	void testUpdateLoanApplicationPositive() throws Exception {
		obj = new LoanApplication(customer,1200000,10);
		when(loanApplicationRepository.save(obj)).thenReturn(obj);
		assertEquals(obj, loanApplicationService.updateLoanApplication(1, obj));
	}

	@Test
	void testUpdateLoanApplicationNegative() throws Exception {
		LoanApplication obj = new LoanApplication(customer1, 1000000,10);
		when(loanApplicationRepository.findById(1)).thenReturn(Optional.of(obj));
		assertNotEquals(obj, loanApplicationService.updateLoanApplication(1, obj));
	}

	@Test
	@DisplayName("test case for deleting loan application")
	void testDeleteLoanApplicationPositive() throws Exception {
		doNothing().when(loanApplicationRepository).deleteById(4);
		when(loanApplicationRepository.findById(4)).thenReturn(Optional.of(loanApplication1));
		assertEquals(loanApplication1, loanApplicationService.deleteLoanApplication(4));
	}

	@Test
	@DisplayName("a negative test case for delete loan application ")
	public void testDeleteLoanApplicationNegative() throws Exception {
		doNothing().when(loanApplicationRepository).deleteById(4);
		when(loanApplicationRepository.findById(4)).thenReturn(Optional.of(loanApplication1));
		Assertions.assertThrows(LoanApplicationNotFoundException.class,()->loanApplicationService.deleteLoanApplication(4));
	}
//
//	@Test
//	@DisplayName("wrong test case for delete customer")
//	void testDeleteCustomerNegative() throws Exception {
//		Customer obj = new Customer();
//		obj.setUserId(1);
//		doNothing().when(customerRepository).deleteById(1);
//		when(customerRepository.findById(1)).thenReturn(Optional.of(obj));
//		Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(2));
//	}

	@Test
	@DisplayName("positive retrieve all loan application")
	void testGetAllLoanApplicationPositive() throws Exception {
		List<LoanApplication> list = new ArrayList<>();
		list.add(loanApplication);
		list.add(loanApplication1);
		loanApplicationService.getAllLoanApplication();
		when(loanApplicationRepository.findAll()).thenReturn(list);
		assertEquals(list.size(), loanApplicationService.getAllLoanApplication().size());

	}

	@Test
	@DisplayName("negative retrieve all loan application")
	void testGetAllLoanApplicationNegative() throws Exception {
		List<LoanApplication> list = new ArrayList<>();
		list.add(loanApplication);
		list.add(loanApplication1);
		loanApplicationService.getAllLoanApplication();
		when(loanApplicationRepository.findAll()).thenReturn(list);
		assertNotEquals(3, loanApplicationService.getAllLoanApplication().size());

	}

	@Test
	@DisplayName("positive retrieve loan application by id")
	void testGetLoanApplicationPositive() throws Exception {
		//LoanApplication obj = new LoanApplication(1, LocalDate.now());
		when(loanApplicationRepository.findById(1)).thenReturn(Optional.of(obj));
		loanApplication = loanApplicationService.getLoanApplication(1);
		assertEquals(obj.toString(), loanApplication.toString());
	}

	@Test
	@DisplayName("negative retrieve loan application by id")
	void testGetLoanApplicationNegative() throws Exception {
		//LoanApplication obj = new LoanApplication(1, LocalDate.now());
		//LoanApplication obj1 = new LoanApplication(2, LocalDate.now());
		when(loanApplicationRepository.findById(1)).thenReturn(Optional.of(obj));
		loanApplication = loanApplicationService.getLoanApplication(2);
		assertNotEquals(loanApplication.toString(), loanApplication.toString());
	}

}
