package com.models;


import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import com.models.PlaceModel;
import com.models.UserModel;

public class NewTest {
/////////////////////////////////////////////////////////////////////////////////////////// SAVE PLACE
	@DataProvider(name = "dp")
	public Object[][] dp() {
		return new Object[][] { { "samar", "kfc" }, { "menna", "costa" } };
	}

	@Test(dataProvider = "dp")
	public void savePlace(String x1, String x2) {
		Assert.assertEquals(true, PlaceModel.savePlace(x1, x2));
	}
/////////////////////////////////////////////////////////////////////////////////////////// ADD PLACE
	@DataProvider(name = "dp1")
	public Object[][] dp1() {
		return new Object[][] { { "spain", "honey moon" } };
	}

	@Test(dataProvider = "dp1")
	public void addPlace(String x1, String x2) {
		Assert.assertEquals(true, PlaceModel.addPlace(x1, x2));
	}
}
