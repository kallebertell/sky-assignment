package com.sky.rewards.core;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sky.eligibility.api.EligibilityService;
import com.sky.eligibility.api.dto.Eligibility;
import com.sky.eligibility.api.exception.TechnicalFailureException;
import com.sky.rewards.api.dto.ChannelSubscription;
import com.sky.rewards.api.dto.Reward;
import com.sky.rewards.api.exception.InvalidAccountNumberException;
import com.sky.rewards.repository.RewardRepository;


public class DefaultRewardsServiceTest {

	static final String ELIGIBLE_ACCOUNT_NUMBER = "eligibleCustomerAccountNumber";
	static final String INELIGIBLE_ACCOUNT_NUMBER = "ineligibleCustomerAccountNumber";
	static final String INVALID_ACCOUNT_NUMBER = "invalidCustomerAccountNumber";

	static final ChannelSubscription REWARDABLE_CHANNEL_SUBSCRIPTION = ChannelSubscription.SPORTS;
	static final ChannelSubscription NOT_REWARDABLE_CHANNEL_SUBSCRIPTION = ChannelSubscription.KIDS;
	static final Reward REWARD = Reward.CHAMPIONS_LEAGUE_FINAL_TICKET;
	
	DefaultRewardsService rewardsService;
	
	RewardRepository mockRewardRepository;
	EligibilityService mockEligibilityService;
	
	@Before
	public void setup() {
		mockRewardRepository = mock(RewardRepository.class);
		mockEligibilityService = mock(EligibilityService.class);

		when(mockEligibilityService.getEligibility(ELIGIBLE_ACCOUNT_NUMBER)).thenReturn(Eligibility.CUSTOMER_ELIGIBLE);
		when(mockEligibilityService.getEligibility(INELIGIBLE_ACCOUNT_NUMBER)).thenReturn(Eligibility.CUSTOMER_INELIGIBLE);

		when(mockEligibilityService.getEligibility(INVALID_ACCOUNT_NUMBER)).thenThrow(new com.sky.eligibility.api.exception.InvalidAccountNumberException());
		when(mockRewardRepository.findBySubscriptions(asList(REWARDABLE_CHANNEL_SUBSCRIPTION))).thenReturn(asList(REWARD));

		rewardsService = new DefaultRewardsService(mockEligibilityService, mockRewardRepository);
	}
	
	@Test
	public void givenEligibleCustomerAndRewardableSubscriptionShouldReceiveReward() {
		List<Reward> rewards = rewardsService.getRewards(ELIGIBLE_ACCOUNT_NUMBER, asList(REWARDABLE_CHANNEL_SUBSCRIPTION));
		assertThat(rewards).containsExactly(REWARD);
	}
	
	@Test
	public void givenIneligibleCustomerAndRewardableSubscriptionShouldNotReceiveReward() {
		List<Reward> rewards = rewardsService.getRewards(INELIGIBLE_ACCOUNT_NUMBER, asList(REWARDABLE_CHANNEL_SUBSCRIPTION));
		assertThat(rewards).isEmpty();
	}
	
	@Test
	public void givenEligableCustomerAndNonRewardableSubscriptionShouldNotReceiveReward() {
		List<Reward> rewards = rewardsService.getRewards(ELIGIBLE_ACCOUNT_NUMBER, asList(NOT_REWARDABLE_CHANNEL_SUBSCRIPTION));
		assertThat(rewards).isEmpty();		
	}
	
	@Test
	public void givenIneligibleCustomerAndNonRewardbleSubscriptionShouldNotReceiveReward() {
		List<Reward> rewards = rewardsService.getRewards(INELIGIBLE_ACCOUNT_NUMBER, asList(NOT_REWARDABLE_CHANNEL_SUBSCRIPTION));
		assertThat(rewards).isEmpty();				
	}
	
	@Test(expected=InvalidAccountNumberException.class)
	public void givenInvalidAccountNumberShouldThrowInvalidAccountNumber() {
		rewardsService.getRewards(INVALID_ACCOUNT_NUMBER, asList(REWARDABLE_CHANNEL_SUBSCRIPTION));
	}
	
	@Test
	public void givenEligibilityServiceTechnicalFailureShouldNotReceiveReward() {
		when(mockEligibilityService.getEligibility(anyString())).thenThrow(new TechnicalFailureException());
		List<Reward> rewards = rewardsService.getRewards(ELIGIBLE_ACCOUNT_NUMBER, asList(REWARDABLE_CHANNEL_SUBSCRIPTION));
		assertThat(rewards).isEmpty();
	}
	
}
