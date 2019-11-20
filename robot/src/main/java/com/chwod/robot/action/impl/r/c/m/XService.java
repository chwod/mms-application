package com.chwod.robot.action.impl.r.c.m;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 你再说一遍？
 */

@Component("SS:R-C-M-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(),
				sentence.getRequestWord(), sentence.getProcessDeep());
		String currentResponse = eventContext.getCurrentResponse();

		eventContext.setType(currentResponse == null ? EventContext.SENTENCE_TYPE_DECLARATIVE_NEGATIVE
				: EventContext.SENTENCE_TYPE_DECLARATIVE);

		sentence.setResponseWord(currentResponse == null ? "我刚才什么也没有说啊！" : currentResponse);
		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		logger.debug("Learning class : [{}], sentence : [{}]", this.getClass().getName(),
				eventContext.getCurrentEvent());
	}

}
