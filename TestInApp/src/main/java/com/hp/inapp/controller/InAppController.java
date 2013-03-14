/**
 * 
 */
package com.hp.inapp.controller;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hp.inapp.transfer.ItemTO;
import com.hp.inapp.transfer.ItemTOArray;

/**
 * @author wangxif
 * 
 */
@Controller
@RequestMapping("/inapp")
public class InAppController {
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InAppController.class);

	@RequestMapping(value = "/items/{itemId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ItemTO getItemById(@PathVariable int itemId) {
		return new ItemTO(1, "TestItem");
	}
	
	@RequestMapping(value = "/items", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ItemTOArray findItemsByName(@RequestParam(value = "name", required = true) String name) {
		return new ItemTOArray(new ItemTO(1, "TestItem"));
	}

	@RequestMapping(value = "/items", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public @ResponseBody ItemTO createItem(@RequestBody ItemTO item) {
		return new ItemTO(2, item.getItemName());
	}
	
	@RequestMapping(value = "/items/{itemId}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody void updateItem(@PathVariable int itemId, @RequestBody ItemTO item) {
		new ItemTO(itemId, item.getItemName());
	}
	
	@RequestMapping(value = "/items/{itemId}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteItem(@PathVariable int itemId) {
		logger.info("deleteItem");
	}

}
