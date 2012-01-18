package com.sky.rewards.api;

import java.util.Collection;
import java.util.List;

import com.sky.rewards.api.dto.ChannelSubscription;
import com.sky.rewards.api.dto.Reward;
import com.sky.rewards.api.exception.InvalidAccountNumberException;

/**
 * Defines available Rewards services
 */
public interface RewardsService {

	/**
	 * 
	 * @param customerAccountNumber 
	 * @param subscriptions 
	 * @return a list of rewards for which the customer is eligible
	 * @throws InvalidAccountNumberException
	 */
	public List<Reward> getRewards(String customerAccountNumber, Collection<ChannelSubscription> subscriptions);

}
