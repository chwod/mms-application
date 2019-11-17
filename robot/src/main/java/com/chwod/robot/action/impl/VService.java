package com.chwod.robot.action.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.service.TalkService;
import com.chwod.robot.utils.Constants;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec	是
 * @author liuxun
 *
 */
@Component("SS:V")
public class VService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Autowired
	private TalkService talkService;
	
	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {
		
		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		this.talkService.learning(eventContext, Constants.LEARNING.OK);
		
		sentence.setResponseWord("好的，我知道了.");
		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		
		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		// TODO Auto-generated method stub

	}

}
