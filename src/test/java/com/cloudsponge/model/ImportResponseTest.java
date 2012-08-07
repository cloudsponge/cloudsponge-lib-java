package com.cloudsponge.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cloudsponge.model.ImportResponse.ImportStatus;

public class ImportResponseTest {

	@Test
	public void successfulImport() {
		final ImportResponse importResponse = new ImportResponse();
		importResponse.setStatus(ImportStatus.SUCCESS);
		
		assertTrue(importResponse.isSucessful());
	}

	@Test
	public void unsuccessfulImport() {
		final ImportResponse importResponse = new ImportResponse();
		importResponse.setStatus(ImportStatus.ERROR);

		assertFalse(importResponse.isSucessful());
	}
}
