package com.yuqincar.dao.order;

import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.order.Address;

public interface AddressDao extends BaseDao<Address> {

	Address getByDescription(String description);

	Address getByDetail(String detail);
    
    public Address getEqualAddress(Address address);

}
