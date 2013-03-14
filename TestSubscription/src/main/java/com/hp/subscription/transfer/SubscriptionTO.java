/**
 * 
 */
package com.hp.subscription.transfer;

/**
 * @author wangxif
 *
 */
public class SubscriptionTO {

	private int id;
	
	private String itemName;
	
	private int itemId;
	
	/**
	 * 
	 */
	public SubscriptionTO() { }
	
	/**
	 * @param itemId
	 * @param itemName
	 */
	public SubscriptionTO(int itemId, String itemName) {
		super();
		this.id = itemId;
		this.itemName = itemName;
	}

	/**
	 * @return the itemId
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setId(int itemId) {
		this.id = itemId;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}
	
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
}
