
package com.cg.homeloan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
import com.cg.homeloan.entities.LoanAgreement;
import com.cg.homeloan.exceptions.CustomerNotFoundException;
import com.cg.homeloan.exceptions.LoanAgreementNotFoundException;
import com.cg.homeloan.repositories.ILoanAgreementRepository;
import com.cg.homeloan.services.ILoanAgreementService;

@SpringBootTest
class LoanAgreementServiceTest {
	public static LoanAgreement loanAgreement, loanAgreement1, loanAgreement2, loanAgreement3;

	@BeforeAll
	public static void setUp() {
		loanAgreement = new LoanAgreement();
		loanAgreement.setLoanAgreementId(1);
		loanAgreement.setLoanApplicationId(1);
		loanAgreement.setEmi(null);
		loanAgreement1 = new LoanAgreement();
		loanAgreement1.setLoanAgreementId(1);
		loanAgreement1.setLoanApplicationId(1);
		loanAgreement1.setEmi(null);

		loanAgreement2 = new LoanAgreement();

		loanAgreement3 = new LoanAgreement(1, null);

	}

	@Autowired
	ILoanAgreementService loanAgreementService;

	@MockBean
	ILoanAgreementRepository loanAgreementRepository;

	@Test
	@DisplayName("positive add loan agreement")
	void testAddLoanAgreementPositive() throws Exception {
		when(loanAgreementRepository.save(loanAgreement3)).thenReturn(loanAgreement3);
		assertEquals(loanAgreement3, loanAgreementService.addLoanAgreement(1, null));
	}

	@Test
	@DisplayName("negative add loan agreement")
	void testAddLoanAgreementNegative() throws Exception {
		when(loanAgreementRepository.save(loanAgreement)).thenReturn(loanAgreement);
		assertNotEquals(loanAgreement1, loanAgreementService.addLoanAgreement(3, null));
	}

	@Test
	@DisplayName("positive get loan agreement")
	void testgetLoanAgreementPositive() throws Exception {
		when(loanAgreementRepository.findById(1)).thenReturn(Optional.of(loanAgreement1));
		assertEquals(loanAgreement1.toString(), loanAgreement.toString());
	}

	@Test
	@DisplayName("negative get loan agreement")
	void testgetLoanAgreemenNegative() throws Exception {
		when(loanAgreementRepository.findById(2)).thenReturn(Optional.of(loanAgreement2));
		assertNotEquals(loanAgreement2.toString(), loanAgreement.toString());
	}

	@Test
	@DisplayName("positive get all loan agreements")
	void testGetAllLoanAgreementsPositive() throws Exception {
		List<LoanAgreement> list = new ArrayList<>();
		list.add(loanAgreement1);
		list.add(loanAgreement2);
		loanAgreementService.getAllLoanAgreements();
		when(loanAgreementRepository.findAll()).thenReturn(list);
		assertEquals(list.size(), loanAgreementService.getAllLoanAgreements().size());
	}

	@Test
	@DisplayName("negative get all loan agreements")
	void testGetAllLoanAgreementsNegative() throws Exception {
		List<LoanAgreement> list = new ArrayList<>();
		list.add(loanAgreement1);
		list.add(loanAgreement2);
		loanAgreementService.getAllLoanAgreements();
		when(loanAgreementRepository.findAll()).thenReturn(list);
		assertNotEquals(3, loanAgreementService.getAllLoanAgreements().size());
	}

	@Test
	@DisplayName("positive delete loan agreement")
	void testDeleteLoanAgreementPositive() throws Exception {

	}

	@Test
	@DisplayName("negative delete loan agreement")
	void testDeleteLoanAgreementNegative() throws Exception {

	}
	/*
	 * @Test
	 * 
	 * @DisplayName("correct test case for delete customer") void
	 * testDeleteCustomerPositive() throws Exception { Customer obj = new
	 * Customer(); obj.setUserId(1);
	 * doNothing().when(customerRepository).deleteById(1);
	 * when(customerRepository.findById(1)).thenReturn(Optional.of(obj));
	 * assertEquals(obj, customerService.deleteCustomer(1));
	 * 
	 * }
	 * 
	 * @Test
	 * 
	 * @DisplayName("wrong test case for delete customer") void
	 * testDeleteCustomerNegative() throws Exception { Customer obj = new
	 * Customer(); obj.setUserId(1);
	 * doNothing().when(customerRepository).deleteById(1);
	 * when(customerRepository.findById(1)).thenReturn(Optional.of(obj));
	 * Assertions.assertThrows(CustomerNotFoundException.class, () ->
	 * customerService.deleteCustomer(2)); }
	 */

	@Test
	@DisplayName("correct test case for update loan agreement")
	void testUpdateLoanAgreementPositive() throws Exception {
		LoanAgreement obj = new LoanAgreement();
		obj.setLoanAgreementId(1);

		when(loanAgreementRepository.save(obj)).thenReturn(obj);
		when(loanAgreementRepository.findById((int) obj.getLoanAgreementId())).thenReturn(Optional.of(obj));
		LoanAgreement ex = loanAgreementService.updateLoanAgreement(obj.getLoanAgreementId(), obj);
		// LoanAgreement ex =
		// loanAgreementService.updateLoanAgreement(obj.getLoanAgreementId(), obj);
		assertEquals(obj, ex);
	}

	@Test
	@DisplayName("wrong test case for update loan agreement")
	void testUpdateLoanAgreementNegative() throws Exception {
		LoanAgreement obj = new LoanAgreement();
		obj.setLoanAgreementId(1);
		when(loanAgreementRepository.save(obj)).thenReturn(obj);
		Assertions.assertThrows(LoanAgreementNotFoundException.class,
				() -> loanAgreementService.updateLoanAgreement(2, obj));
	}
}
