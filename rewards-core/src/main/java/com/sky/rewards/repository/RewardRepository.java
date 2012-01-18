package com.sky.rewards.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sky.rewards.api.dto.ChannelSubscription;
import com.sky.rewards.api.dto.Reward;

/**
 * Storage for Rewards.
 */
public class RewardRepository {

	private Map<ChannelSubscription, Reward> store;
	
	public RewardRepository() {
		store = new HashMap<ChannelSubscription,Reward>();
	}
	
	/**
	 * Find all rewards associated with given subscriptions
	 */
	public List<Reward> findBySubscriptions(Collection<ChannelSubscription> subscriptions) {
		List<Reward> rewards = new ArrayList<Reward>();
		for (ChannelSubscription subscription : subscriptions) {
			if (store.containsKey(subscription)) {
				rewards.add(store.get(subscription));
			}
		}
		return rewards;
	}

	
	/** 
	 * Adds a new reward to the given ChannelSubscription.
	 * If one exists from before it'll be overwritten.
	 */
	public void add(ChannelSubscription subscription, Reward reward) {
		store.put(subscription, reward);
	}

}
