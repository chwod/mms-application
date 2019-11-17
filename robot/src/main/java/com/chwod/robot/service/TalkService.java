package com.chwod.robot.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Part;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants;

@Service
public class TalkService {

	private static final String SERVICE_NAME_PREFIX = "SS:";

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ConfigurationService configurationService;

	@Value("${com.chwod.robot.sentence.parseService.host:http://localhost:5000}")
	private String parseServiceHost;

	// think process deep
	private static Integer PROCESSDEEP = null;

	/**
	 * talk.
	 * 
	 * @param sentence
	 * @return
	 */
	public Sentence talk(Sentence sentence, EventContext eventContext) {
		if (StringUtils.isEmpty(sentence.getRequestWord())) {
			return sentence;
		}

		if (StringUtils.isEmpty(sentence.getParseWord())) {
			sentence.setPartList(this.parse(sentence.getRequestWord()));
		}

		return this.talk(sentence, eventContext,this.makeServiceName(sentence.getParseWord()));
	}

	/**
	 * // think process call.
	 * 
	 * @param sentence
	 * @param eventContext
	 * @param serviceName
	 * @return
	 */
	public Sentence talk(Sentence sentence, EventContext eventContext, String serviceName) {
		if (StringUtils.isEmpty(sentence.getRequestWord())) {
			return sentence;
		}

		if (StringUtils.isEmpty(sentence.getParseWord())) {
			sentence.setPartList(this.parse(sentence.getRequestWord()));
		}

		if (StringUtils.isEmpty(serviceName)) {
			serviceName = this.makeServiceName(sentence.getParseWord());
		}

		sentence.deepPlus();
		if (PROCESSDEEP == null) {
			PROCESSDEEP = this.configurationService.getProcessDeep();
		}
		// think too much.
		if (sentence.getProcessDeep() > PROCESSDEEP) {
			sentence.setResponseWord("好复杂，想的我头疼。");
			return sentence;
		}

		// think process call.
		if (this.applicationContext.containsBeanDefinition(serviceName)) {
			ActionService service = (ActionService) this.applicationContext.getBean(serviceName);
			sentence = service.process(sentence, eventContext);
			return sentence;
		}

		// no bean definition, it's unsupported.
		sentence.setResponseWord("理解不了这句话，你能解释一下吗？");

		// context setup
		eventContext.setType(EventContext.SENTENCE_TYPE_QUESTION);

		return sentence;
	}

	/**
	 * robot learning process call.
	 * 
	 * @param sentence
	 * @param flag
	 */
	public void learning(EventContext eventContext, Constants.LEARNING flag) {
		if ((eventContext != null) && (StringUtils.isNotBlank(eventContext.getCurrentEvent()))) {
			Sentence sentence = new Sentence(eventContext.getCurrentEvent());
			sentence.setPartList(this.parse(sentence.getRequestWord()));
			String serviceName = this.makeServiceName(sentence.getParseWord());

			// think process call.
			if (this.applicationContext.containsBeanDefinition(serviceName)) {
				ActionService service = (ActionService) this.applicationContext.getBean(serviceName);
				service.learning(eventContext, flag);
			}
		}
	}

	/**
	 * make a service name for bean invoking.
	 * 
	 * @param parseWord
	 * @return
	 */
	private String makeServiceName(String parseWord) {
		return new StringBuffer(SERVICE_NAME_PREFIX).append(parseWord).toString().toUpperCase();
	}

	/**
	 * parse sentence
	 * 
	 * @param queryString
	 * @return
	 */
	public List<Part> parse(String queryString) {
		ResponseEntity<List<Part>> response = restTemplate.exchange(
				new StringBuffer(parseServiceHost).append(queryString).append("/").toString(), HttpMethod.POST, null,
				new ParameterizedTypeReference<List<Part>>() {
				});
		List<Part> partList = response.getBody();
		return partList;
	}
}
