package com.zhanglong.sg.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.VersionDao;
import com.zhanglong.sg.entity.Version;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("//version")
public class Version2Controller {

	@Resource
	private VersionDao versionDao;

    public Object list(int code, String imei, int channel) throws Exception {

    	boolean find = inWriteList(imei);

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
