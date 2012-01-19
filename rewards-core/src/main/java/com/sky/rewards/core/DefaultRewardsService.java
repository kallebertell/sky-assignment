package com.sky.rewards.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sky.eligibility.api.EligibilityService;
import com.sky.eligibility.api.dto.Eligibility;
import com.sky.eligibility.api.exception.InvalidAccountNumberException;
import com.sky.eligibility.api.exception.TechnicalFailureException;
import com.sky.rewards.api.RewardsService;
import com.sky.rewards.api.dto.ChannelSubscription;
import com.sky.rewards.api.dto.Reward;
import com.sky.rewards.repository.RewardRepository;

/**
 * Default implementation of RewardsService
 */
public class DefaultRewardsService implements RewardsService {

	private final EligibilityService eligibiltyService;
	private final RewardRepository rewardRepository;

	public DefaultRewardsService(EligibilityService eligibiltyService, RewardRepository rewardRepository) {
		this.eligibiltyService = eligibiltyService;
		this.rewardRepository = rewardRepository;
	}
	
	public List<Reward> getRewards(String customerAccountNumber, Collection<ChannelSubscription> subscriptions) {
		ensureNotNull("customerAccountNumber", customerAccountNumber);
		ensureNotNull("subscriptions", subscriptions);
		
		List<Reward> rewards = rewardRepository.findBySubscriptions(subscriptions);
		
		Eligibility eligibility = null;
		
		try {
			eligibility = eligibiltyService.getEligibility(customerAccountNumber);
		} catch (TechnicalFailureException e) {
			return new ArrayList<Reward>();
		} catch (InvalidAccountNumberException e) {
			throw new com.sky.rewards.api.exception.InvalidAccountNumberException();
		}
		
		if (eligibility.equals(Eligibility.CUSTOMER_ELIGIBLE)) {
			return rewards;
		}
		
		return new ArrayList<Reward>();
	}

	private void ensureNotNull(String paramName, Object param) {
		if (param == null) {
			throw new NullPointerException(paramName);
		}
	}
}
