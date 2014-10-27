package com.ts.utils;


import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.W32Errors;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg.HKEY;
import com.sun.jna.platform.win32.WinReg.HKEYByReference;

public class RegistryUtils {
	private static final Logger LOG = LoggerFactory.getLogger(RegistryUtils.class);
	
	private RegistryUtils() {
	}

	public static String getStringValue(HKEY inHive, int inWow64or32value,
			String inKeyName, String inPropertyName) {
		if(LOG.isDebugEnabled()) {
			LOG.debug(">> getStringValue(inHive, inWow64or32value, inKeyName, inPropertyName)");
		}
		
		String retValue;
		
		if (!SystemUtils.IS_OS_WINDOWS)
			throw new java.lang.UnsupportedOperationException(
					"This method is only for Windows OS.");

		HKEYByReference phkKey = new HKEYByReference();
		int rc = Advapi32.INSTANCE.RegOpenKeyEx(inHive, inKeyName, 0,
				WinNT.KEY_QUERY_VALUE | inWow64or32value, phkKey);
		if (rc != W32Errors.ERROR_SUCCESS)
			throw new Win32Exception(rc);
		try {
			retValue = Advapi32Util.registryGetStringValue(phkKey.getValue(),
					inPropertyName);
		} finally {
			rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
			if (rc != W32Errors.ERROR_SUCCESS)
				throw new Win32Exception(rc);
		}
		
		if(LOG.isDebugEnabled())
			LOG.debug("<< getStringValue()");
		
		return retValue;
	}

	public static boolean isPropertyPresent(HKEY inHive, int inWow64or32value,
			String inKeyName, String inPropertyName) {

		if (!SystemUtils.IS_OS_WINDOWS)
			throw new java.lang.UnsupportedOperationException(
					"This method is only for Windows OS.");

		HKEYByReference phkKey = new HKEYByReference();
		int rc = Advapi32.INSTANCE.RegOpenKeyEx(inHive, inKeyName, 0,
				WinNT.KEY_QUERY_VALUE | inWow64or32value, phkKey);
		if (rc != W32Errors.ERROR_SUCCESS)
			throw new Win32Exception(rc);
		try {
			return Advapi32Util.registryKeyExists(phkKey.getValue(),
					inPropertyName);
		} finally {
			rc = Advapi32.INSTANCE.RegCloseKey(phkKey.getValue());
			if (rc != W32Errors.ERROR_SUCCESS)
				throw new Win32Exception(rc);
		}
	}
}
