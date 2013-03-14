/**
 * 
 */
package com.hp.subscription.transfer;

import java.util.Arrays;
import java.util.List;


/**
 * @author wangxif
 *
 */
public class SubscriptionTOArray {
	
	private List<SubscriptionTO> subscriptions;
	
	/**
	 * 
	 */
	public SubscriptionTOArray(List<SubscriptionTO> subscriptions) {
		this.subscriptions = subscriptions;
	}
	
	/**
	 * 
	 */
	public SubscriptionTOArray(SubscriptionTO... subscriptions) {
		this.subscriptions = Arrays.asList(subscriptions);
	}
	
	/**
	 * @return the subscriptions
	 */
	public List<SubscriptionTO> getSubscriptions() {
		return subscriptions;
	}
	
	/**
	 * @param subscriptions the subscriptions to set
	 */
	public void setSubscriptions(List<SubscriptionTO> subscriptions) {
		this.subscriptions = subscriptions;
	}

}
