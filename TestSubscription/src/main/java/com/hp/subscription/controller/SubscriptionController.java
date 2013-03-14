/**
 * 
 */
package com.hp.subscription.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.hp.subscription.transfer.ItemTO;
import com.hp.subscription.transfer.SubscriptionTO;
import com.hp.subscription.transfer.SubscriptionTOArray;

/**
 * @author wangxif
 *
 */
@Controller
@RequestMapping("/subscription")
public class SubscriptionController {
	
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/subscriptions/{subsId}", method = RequestMethod.GET)
	public @ResponseBody SubscriptionTO getItemById(@PathVariable int subsId) {
		restTemplate.getForObject("http://localhost:8080/TestInApp/inapp/items/{id}", ItemTO.class, 1);
		return new SubscriptionTO(1, "Subscription1");
	}
	
	@RequestMapping(value = "/subscriptions", method = RequestMethod.GET)
	public @ResponseBody SubscriptionTOArray findSubscriptionsByItemName(
			@RequestParam(value = "itemName", required = true) String itemName) {
		restTemplate.getForObject("http://localhost:8080/TestInApp/inapp/items?name={itemName}", Object.class, itemName);
		return new SubscriptionTOArray(new SubscriptionTO(1, "Subscription1"));
	}
	
	@RequestMapping(value = "/subscriptions", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody SubscriptionTO subscript(@RequestBody SubscriptionTO subscription) {
		ItemTO inAppItem = restTemplate.postForObject("http://localhost:8080/TestInApp/inapp/items", new ItemTO(1, "testItem"), ItemTO.class);
		SubscriptionTO ret = new SubscriptionTO(2, subscription.getItemName());
		ret.setItemId(inAppItem.getItemId());
		return ret;
	}
	
	@RequestMapping(value = "/subscriptions/{subsId}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody void updateSubscription(@PathVariable int subsId, @RequestBody SubscriptionTO subscription) {
		restTemplate.put("http://localhost:8080/TestInApp/inapp/items/7", new ItemTO(7, "name from subscription"));
		SubscriptionTO ret = new SubscriptionTO(5, subscription.getItemName());
		ret.setItemId(7);
	}
	
	@RequestMapping(value = "/subscriptions/{subsId}", method = RequestMethod.DELETE)
	public @ResponseBody void unsubscript(@PathVariable int subsId) {
		restTemplate.delete("http://localhost:8080/TestInApp/inapp/items/7");
	}
	
}
