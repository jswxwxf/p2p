/**
 * 
 */
package com.hp.inapp.transfer;

import java.util.Arrays;
import java.util.List;


/**
 * @author wangxif
 *
 */
public class ItemTOArray {

	private List<ItemTO> items;
	
	/**
	 * 
	 */
	public ItemTOArray(List<ItemTO> items) {
		this.items = items;
	}
	
	/**
	 * 
	 */
	public ItemTOArray(ItemTO... items) {
		this.items = Arrays.asList(items);
	}
	
	/**
	 * @return the array
	 */
	public List<ItemTO> getItems() {
		return items;
	}
	
	/**
	 * @param array the array to set
	 */
	public void setItems(List<ItemTO> array) {
		this.items = array;
	}
	
}
