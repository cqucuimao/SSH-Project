package com.yuqincar.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.monitor.LocationDao;
import com.yuqincar.dao.order.AddressDao;
import com.yuqincar.domain.order.Address;
import com.yuqincar.service.order.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressDao addressDao;
	@Autowired
	private LocationDao locationDao;

	public Address getAddressByDescription(String description) {
		return addressDao.getByDescription(description);
	}

	public Address getAddressByDetail(String detail) {
		return addressDao.getByDetail(detail);
	}

	public Address getById(Long id) {
		return addressDao.getById(id);
	}

	@Transactional
	public void saveAddresses(List<Address> addresses) {
		for (Address address : addresses) {
			locationDao.save(address.getLocation());
			addressDao.save(address);
		}
	}

	@Transactional
	public void saveAddress(Address address) {
		addressDao.save(address);
	}

	@Transactional
	public void deleteAddress(long id) {
		addressDao.delete(id);
	}
}
