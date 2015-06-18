package com.zhanglong.sg.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.WhiteListDao;
import com.zhanglong.sg.dao.VersionDao;
import com.zhanglong.sg.entity.Version;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/version")
public class VersionController {

	@Resource
	private WhiteListDao imeiDao;

	@Resource
	private VersionDao versionDao;

    public Object list(int code, String imei, int channel) throws Exception {

    	boolean find = this.imeiDao.find(imei);

		List<Version> list = this.versionDao.findAll(code);

		Version apk = null;
		List<Version> update_list = new ArrayList<Version>();
		for (Version versionTable : list) {

			if (versionTable.getChannel() != channel) {
				continue;
			}
			if (versionTable.getVersionEnable() != Version.ENABLE && !find) {
				continue;
			}

			if (versionTable.getVersionType() == Version.TYPE_APK) {
				apk = versionTable;
			} else if (versionTable.getVersionType() == Version.TYPE_ZIP) {
				update_list.add(versionTable);
			}
		}

		Result result = new Result();

		if (apk != null) {
			result.setValue("apk", apk);
		} else {
			result.setValue("update_list", update_list);
		}

		return result.toMap();
    }
}
