package com.yuqincar.dao.order.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.order.AddressDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Address;
import com.yuqincar.utils.QueryHelper;

@Repository
public class AddressDaoImpl extends BaseDaoImpl<Address> implements AddressDao {

	public Address getByDescription(String description) {
		return (Address) getSession().createQuery(//
				"FROM Address d WHERE d.description=?")
				.setParameter(0, description).uniqueResult();
	}

	public Address getByDetail(String detail) {
		return (Address) getSession().createQuery(//
				"FROM Address d WHERE d.detail=?").setParameter(0, detail)
				.uniqueResult();
	}

	public void saveAddresses(List<Address> addresses) {
		for (Address address : addresses) {
			getSession().save(address.getLocation());
			getSession().save(address);
		}
	}

	public Address getEqualAddress(Address address) {
		return (Address)getSession()
				.createQuery(
						"from Address as ad where ad.description=? and ad.detail=? and ad.location.longitude=? and ad.location.latitude=?")
				.setParameter(0, address.getDescription())
				.setParameter(1, address.getDetail())
				.setParameter(2, address.getLocation().getLongitude())
				.setParameter(3, address.getLocation().getLatitude()).uniqueResult();
	}

}
