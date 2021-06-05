package com.cg.homeloan.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.homeloan.entities.Customer;
import com.cg.homeloan.entities.EMI;
import com.cg.homeloan.entities.LoanAgreement;
import com.cg.homeloan.entities.LoanApplication;
import com.cg.homeloan.entities.Status;
import com.cg.homeloan.exceptions.AdminApprovalException;
import com.cg.homeloan.exceptions.CustomerNotFoundException;
import com.cg.homeloan.exceptions.FinanceVerificationException;
import com.cg.homeloan.exceptions.LandVerificationException;
import com.cg.homeloan.exceptions.LoanAgreementNotFoundException;
import com.cg.homeloan.exceptions.LoanApplicationNotFoundException;
import com.cg.homeloan.repositories.ICustomerRepository;
import com.cg.homeloan.repositories.ILoanApplicationRepository;

@Service
public class LoanApplicationService implements ILoanApplicationService {

	@Autowired
	ILoanApplicationRepository loanApplicationRepository;
	
	@Autowired
	ICustomerRepository customerRepository;
	
	@Autowired
	IEmiService emiService;
	
	@Autowired
	ILoanAgreementService loanAgreementService;

	@Override
	public LoanApplication addLoanApplication(int userId, double loanAppliedAmount,int loanTenureYears) throws CustomerNotFoundException {
		Customer customer = customerRepository.findById(userId).orElseThrow(()->new CustomerNotFoundException("Customer detail not found !!!"));
		LoanApplication loanApplication = new LoanApplication(customer,loanAppliedAmount,loanTenureYears);
				return loanApplicationRepository.save(loanApplication);
	}

	@Override
	public LoanApplication updateLoanApplication(int loanApplicationId, LoanApplication loanApplication)
			throws LoanApplicationNotFoundException {
		loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(() -> new LoanApplicationNotFoundException("Loan Application Not Found!!!"));
		return loanApplicationRepository.save(loanApplication);
	}

	@Override
	public LoanApplication deleteLoanApplication(int loanApplicationId) throws LoanApplicationNotFoundException {
		LoanApplication loanApplication =loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(() -> new LoanApplicationNotFoundException("Loan Application Not Found!!!"));
		loanApplicationRepository.deleteById(loanApplicationId);
		return loanApplication;
	}
	
	@Override
	public LoanApplication getLoanApplication(int loanApplicationId) throws LoanApplicationNotFoundException {
		return loanApplicationRepository.findById(loanApplicationId)
				.orElseThrow(() -> new LoanApplicationNotFoundException("Loan Application Not Found!!!"));
	}
	
	@Override
	public List<LoanApplication> getAllLoanApplication() {
		return loanApplicationRepository.findAll();
	}
	
	@Override
	public LoanAgreement checkStatus(int loanApplicationId) throws LoanAgreementNotFoundException {
		return loanAgreementService.getLoanAgreement(loanApplicationId);
	}
	@Override
	public LoanApplication updateLandStatus(int loanApplicationId) throws LandVerificationException, LoanApplicationNotFoundException{
		LoanApplication loanApplication = getLoanApplication(loanApplicationId);
				
		if(loanApplication.getStatus()== Status.WAITING_FOR_LAND_VERIFICATION_OFFICE_APPROVAL 
						&& !loanApplication.isLandVerificationApproval() ) 
		{
			loanApplication.setLandVerificationApproval(true);
			loanApplication.setStatus(Status.WAITING_FOR_FINANCE_APPROVAL);
			return loanApplicationRepository.save(loanApplication);	
		}
		else  
		{
		 throw new LandVerificationException("Check your Loan Application ") ;	
		}	
	}
	
	@Override
	public LoanApplication updateFinanceStatus(int loanApplicationId) throws FinanceVerificationException, LoanApplicationNotFoundException {
		LoanApplication loanApplication = getLoanApplication(loanApplicationId);
		
		if (loanApplication.getStatus() == Status.WAITING_FOR_FINANCE_APPROVAL
				&& loanApplication.isLandVerificationApproval()
				&& !loanApplication.isFinanceVerificationApproval()) {
			loanApplication.setFinanceVerificationApproval(true);
			loanApplication.setStatus(Status.PENDING);
			return loanApplicationRepository.save(loanApplication);

		} else 
			throw new FinanceVerificationException("Ask Land Verification Officer for Approval");
	}
	
	@Override
	public LoanApplication updateAdminStatus(int loanApplicationId) throws AdminApprovalException, LoanApplicationNotFoundException {
		LoanApplication loanApplication = getLoanApplication(loanApplicationId);
		
		if (loanApplication.getStatus() == Status.PENDING
				&& loanApplication.isLandVerificationApproval() 
				&& loanApplication.isFinanceVerificationApproval()) {
			loanApplication.setAdminApproval(true);
			loanApplication.setStatus(Status.APPROVED);
			
			EMI emi=emiService.addEmiDetails(loanApplication.getLoanAppliedAmount(), 10 , loanApplication.getLoanTenureYears());
			loanApplication.setLoanApprovedAmount(emi.getLoanAmount());
			
			loanAgreementService.addLoanAgreement(loanApplicationId, emi);
			
			return loanApplicationRepository.save(loanApplication);

		} else 
			throw new AdminApprovalException("Ask Finance Verification Officer For Approval");
	}

}
