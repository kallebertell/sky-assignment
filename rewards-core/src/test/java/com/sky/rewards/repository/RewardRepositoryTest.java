package com.sky.rewards.repository;


import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sky.rewards.api.dto.ChannelSubscription;
import com.sky.rewards.api.dto.Reward;
import com.sky.rewards.repository.RewardRepository;

public class RewardRepositoryTest {

	RewardRepository rewardRepository;
	
	@Before
	public void setup() {
		rewardRepository = new RewardRepository();
		rewardRepository.add(ChannelSubscription.SPORTS, Reward.CHAMPIONS_LEAGUE_FINAL_TICKET);
		rewardRepository.add(ChannelSubscription.MUSIC, Reward.KARAOKE_PRO_MICROPHONE);
	}
	
	@Test
	public void shouldFindTicket() {
		List<Reward> rewards = rewardRepository.findBySubscriptions(asList(ChannelSubscription.SPORTS));
		assertThat(rewards).containsOnly(Reward.CHAMPIONS_LEAGUE_FINAL_TICKET);
	}
	
	@Test
	public void shouldFindBothAwards() {
		List<Reward> rewards = rewardRepository.findBySubscriptions(asList(ChannelSubscription.SPORTS, ChannelSubscription.MUSIC));
		assertThat(rewards).containsOnly(Reward.CHAMPIONS_LEAGUE_FINAL_TICKET, Reward.KARAOKE_PRO_MICROPHONE);
	}
	
	@Test
	public void shouldNotFindAnything() {
		List<Reward> rewards = rewardRepository.findBySubscriptions(asList(ChannelSubscription.KIDS));
		assertThat(rewards).isEmpty();
	}
}
