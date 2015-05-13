package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.VersionDao;
import com.zhanglong.sg.entity.Version;

@JsonRpcService("/version")
public class VersionService extends BaseService {

	@Resource
	private VersionDao versionDao;

	/**
	 * 更新列表
	 * @param code
	 * @param imei
	 * @return
	 * @throws Throwable
	 */
    public Object list(int code, String imei) throws Throwable {

    	boolean find = inWriteList(imei);

		List<Version> list = this.versionDao.findAll(code);

		Version apk = null;
		List<Version> update_list = new ArrayList<Version>();
		for (Version versionTable : list) {
			
			if (versionTable.getVersionEnable() != Version.ENABLE && !find) {
				continue;
			}

			if (versionTable.getVersionType() == Version.TYPE_APK) {
				apk = versionTable;
			} else if (versionTable.getVersionType() == Version.TYPE_ZIP) {
				update_list.add(versionTable);
			}
		}

		HashMap<String, Object> result = new HashMap<String, Object>();

		if (apk != null) {
			result.put("apk", apk);
		} else {
			result.put("update_list", update_list);
		}

		return this.success(result);
    }

    /**
     * 
     * @param imei
     * @return
     */
    public static boolean inWriteList(String imei) {

    	String[] imeiList = new String[]{
    			"356206050684946",
    			"357073058570454",
    			"863472022149113",
    			"863583025865487",
				"99000554405824",
				"865479028190072",
				"356405057482516",
				"866231025427919",
				"862751026935010",
				"862307022168203",
				"865312020409119",
				"863629028698241",
				"860310025461791",
				"004999010640000",
				"863472022149113",
				"864895028524674"};

		boolean find = false;
		for (String string : imeiList) {
			if (string.equals(imei)) {
				find = true;
			}
		}
		return find;
    }
}
