package com.cg.homeloan;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cg.homeloan.entities.Admin;
import com.cg.homeloan.entities.FinanceVerificationOfficer;
import com.cg.homeloan.entities.Status;
import com.cg.homeloan.repositories.IFinanceVerificationRepository;
import com.cg.homeloan.services.IFinanceVerificationService;

@SpringBootTest
class FinanceVerificationServiceTest {

	public static FinanceVerificationOfficer financeVerificationOfficer;

	@BeforeAll
	public static void setUp() {
		financeVerificationOfficer = new FinanceVerificationOfficer();
	}

	@Autowired
	IFinanceVerificationService finaceVerificationService;

	@MockBean
	IFinanceVerificationRepository financeVerificationRepository;


	
	@Test
	@DisplayName("Positive Test case for Validate FinanceVerificationOfficer")
	public void testValidAdminPositive() {
		when(financeVerificationRepository.findByUsernameAndPassword("aman@123", "12345")).thenReturn(financeVerificationOfficer);
		boolean val = finaceVerificationService.isValidFinanceOfficer("aman@123", "12345");
		assertEquals(val, true);
	}

	@Test
	@DisplayName("Negative Test case for Validate FinanceVerificationOfficers")
	public void testValidAdminNegative() {
		when(financeVerificationRepository.findByUsernameAndPassword("aman@123", "12345")).thenReturn(financeVerificationOfficer);
		boolean val = finaceVerificationService.isValidFinanceOfficer("aman@123", "12345");
		assertNotEquals(val, false);
	}
}
