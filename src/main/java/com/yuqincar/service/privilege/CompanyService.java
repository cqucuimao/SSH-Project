package com.yuqincar.service.privilege;

import com.yuqincar.domain.common.Company;
import com.yuqincar.service.base.BaseService;

public interface CompanyService extends BaseService {
	public Company getCompanyById(long companyId);
}
