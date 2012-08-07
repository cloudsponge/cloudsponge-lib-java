package com.cloudsponge;

import com.cloudsponge.model.ImportResponse;

public interface CloudSpongeApiFactory {

	<T extends ImportResponse> CloudSpongeApi<T> create(ContactsService<T> contactsService);
}
