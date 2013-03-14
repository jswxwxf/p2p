/**
 * 
 */
package com.hp.subscription.transfer;

/**
 * @author wangxif
 *
 */
public class ItemTO {
	
	private int itemId;
	
	private String itemName;
	
	/**
	 * 
	 */
	public ItemTO() { }

	/**
	 * @param itemId
	 * @param itemName
	 */
	public ItemTO(int itemId, String itemName) {
		super();
		this.itemId = itemId;
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
	
}
