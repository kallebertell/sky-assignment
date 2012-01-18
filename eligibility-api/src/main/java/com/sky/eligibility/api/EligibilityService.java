package com.sky.eligibility.api;

import com.sky.eligibility.api.dto.Eligibility;

/**
 * Exposes available Eligiblity Services
 */
public interface EligibilityService {

	/**
	 * Get the eligibility of a customer given an account number
	 * @param customerAccountNumber
	 * @return Eligibility of customer
	 * @throws com.sky.eligibility.api.exception.InvalidAccountNumberException
	 * @throws com.sky.eligibility.api.exception.TechnicalFailureException
	 */
	Eligibility getEligibility(String customerAccountNumber);
	
}
