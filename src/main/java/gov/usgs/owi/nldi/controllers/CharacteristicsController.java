package gov.usgs.owi.nldi.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import gov.usgs.owi.nldi.dao.BaseDao;
import gov.usgs.owi.nldi.dao.LookupDao;
import gov.usgs.owi.nldi.dao.StreamingDao;
import gov.usgs.owi.nldi.services.LogService;
import gov.usgs.owi.nldi.services.Navigation;
import gov.usgs.owi.nldi.services.Parameters;
import gov.usgs.owi.nldi.transform.CharacteristicTransformer;

@Controller
@RequestMapping
public class CharacteristicsController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(CharacteristicsController.class);
	
	@Autowired
	public CharacteristicsController(LookupDao inLookupDao, StreamingDao inStreamingDao, Navigation inNavigation, Parameters inParameters, @Qualifier("rootUrl") String inRootUrl, LogService inLogService) {
		super(inLookupDao, inStreamingDao, inNavigation, inParameters, inRootUrl, inLogService);
	}

	@GetMapping(value="{characteristicType}/characteristics")
	public void getCharacteristics(HttpServletRequest request, HttpServletResponse response, @PathVariable(Parameters.CHARACTERISTIC_TYPE) String characteristicType) throws IOException {
		BigInteger logId = logService.logRequest(request);
		try (CharacteristicTransformer transformer = new CharacteristicTransformer(response)) {
			Map<String, Object> parameterMap = new HashMap<> ();
			parameterMap.put(Parameters.CHARACTERISTIC_TYPE, characteristicType.toLowerCase());
			addContentHeader(response);
			streamResults(transformer, BaseDao.CHARACTERISTICS, parameterMap);
		} catch (Throwable e) {
			LOG.error(e.getLocalizedMessage());
			response.sendError(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage());
		}
		logService.logRequestComplete(logId, response.getStatus());
	}
}