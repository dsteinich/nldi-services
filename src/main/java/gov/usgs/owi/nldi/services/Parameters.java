package gov.usgs.owi.nldi.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import gov.usgs.owi.nldi.NavigationMode;

@Service
public class Parameters {
	private static final Logger LOG = LoggerFactory.getLogger(Navigation.class);

	public static final String FEATURE_SOURCE = "featureSource";
	public static final String FEATURE_ID = "featureID";
	public static final String COMID = "comid";
	public static final String DISTANCE = "distance";
	public static final String NAVIGATION_MODE = "navigationMode";
	public static final String STOP_COMID = "stopComid";
	public static final String LEGACY = "legacy";

	public Map<String, Object> processParameters(final String comid, final String navigationMode,
			final String distance, final String stopComid) {
		Map<String, Object> parameterMap = new HashMap<> ();

		Integer comidInt = validateComid(comid, false);
		if (null != comidInt) {
			parameterMap.put(COMID, comidInt);
		}

		if (isValidNavigationMode(navigationMode)) {
			LOG.debug("navigationMode:" + navigationMode);
			parameterMap.put(NAVIGATION_MODE, navigationMode);
		}

		if (StringUtils.hasText(distance)) {
			LOG.debug("distance:" + distance);
			parameterMap.put(DISTANCE, NumberUtils.parseNumber(distance, BigDecimal.class));
		}

		Integer stopComidInt = validateComid(stopComid, true);
		if (null != stopComidInt) {
			parameterMap.put(STOP_COMID, stopComidInt);
		}

		LOG.debug("Request Parameters:" + parameterMap.toString());

		return parameterMap;
	}

	public Integer validateComid(final String comid, boolean optional) {
		try {
			LOG.debug("comid:" + comid);
			return NumberUtils.parseNumber(comid, Integer.class);
		} catch (Exception e) {
			if (!optional) {
				LOG.info("Bad comid given:" + comid, e);
			}
			return null;
		}
	}

	public boolean isValidNavigationMode(final String navigationMode) {
		
		try {
			LOG.debug("navigationMode:" + navigationMode);
			return StringUtils.hasText(navigationMode) && null != NavigationMode.valueOf(navigationMode);
		} catch (Exception e) {
			LOG.info("Bad navigationMode given:" + navigationMode, e);
			return false;
		}
	}

}
