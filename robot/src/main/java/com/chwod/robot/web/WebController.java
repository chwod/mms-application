package com.chwod.robot.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.service.TalkService;
import com.chwod.robot.utils.Constants;

@Controller
public class WebController {

	private static final Logger logger = LoggerFactory.getLogger(WebController.class);

	@Autowired
	private TalkService talkService;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return Constants.index;
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> talk(HttpServletRequest request, HttpSession session, String q) {

		Map<String, Object> result = new HashMap<>();
		result.put(Constants.status, true);

		if (StringUtils.isEmpty(q)) {
			result.put(Constants.status, false);
			result.put(Constants.message, Constants.MESSAGE_EMPTY);
			return result;
		}

		if (q.length() > 50) {
			result.put(Constants.status, false);
			result.put(Constants.message, Constants.MESSAGE_MORE);
			return result;
		}

		request.setAttribute(Constants.requestWord, q);

		Sentence sentence = new Sentence();
		sentence.setRequestWord(q);

		EventContext eventContext = session.getAttribute(Constants.EVENT_CONTEXT) == null ? new EventContext(session.getId()) : (EventContext) session.getAttribute(Constants.EVENT_CONTEXT);
		sentence = this.talkService.talk(sentence, eventContext);
		eventContext.setTargetResponse(eventContext.getCurrentResponse());
		eventContext.setCurrentResponse(sentence.getResponseWord());
		eventContext.setTargetEvent(eventContext.getCurrentEvent());
		eventContext.setCurrentEvent(q);
		
		session.setAttribute(Constants.EVENT_CONTEXT, eventContext);

		logger.info("sentence:[{}], parse:[{}], reposne:[{}]", sentence.getWord(), sentence.getParseWord(),
				sentence.getResponseWord());

		request.setAttribute(Constants.parseWord, sentence.getParseWord());
		request.setAttribute(Constants.responseWord, sentence.getResponseWord());
		result.put(Constants.result, sentence.getResponseWord());
		result.put(Constants.time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));

		return result;
	}
}
