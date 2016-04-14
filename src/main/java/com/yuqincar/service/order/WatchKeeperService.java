package com.yuqincar.service.order;

import com.yuqincar.domain.order.WatchKeeper;

public interface WatchKeeperService {
	public void saveWatchKeeper(WatchKeeper watchKeeper); // 保存

	public void updateWatchKeeper(WatchKeeper watchKeeper); // 修改

	public WatchKeeper getWatchKeeper(); // 获取。
}
