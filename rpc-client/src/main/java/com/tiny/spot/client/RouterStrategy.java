package com.tiny.spot.client;


import java.util.List;

import com.tiny.spot.common.RemoteAddress;

public interface RouterStrategy {

	RemoteAddress route(List<RemoteAddress> remoteAddress);
	
}
