package com.yuqincar.service.order;

import java.util.List;

import com.yuqincar.domain.order.Address;
import com.yuqincar.service.base.BaseService;

public interface AddressService extends BaseService {

	public Address getAddressByDescription(String description);
	public Address getAddressByDetail(String detail);
	public void saveAddresses(List<Address> addresses);
	public void saveAddress(Address address);
	public Address getById(Long id);
	public void deleteAddress(long id);
}
