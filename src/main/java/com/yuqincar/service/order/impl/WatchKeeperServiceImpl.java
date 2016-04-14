package com.yuqincar.service.order.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.WatchKeeperDao;
import com.yuqincar.domain.order.WatchKeeper;
import com.yuqincar.service.order.WatchKeeperService;

@Service
public class WatchKeeperServiceImpl implements WatchKeeperService {
	@Autowired
	private WatchKeeperDao watchKeeperDao;

	@Transactional
	public void saveWatchKeeper(WatchKeeper watchKeeper) {
		watchKeeperDao.save(watchKeeper);
	}

	@Transactional
	public void updateWatchKeeper(WatchKeeper watchKeeper) {
		watchKeeperDao.update(watchKeeper);
	}

	public WatchKeeper getWatchKeeper() {
		return watchKeeperDao.getById(1L);
	}

}
