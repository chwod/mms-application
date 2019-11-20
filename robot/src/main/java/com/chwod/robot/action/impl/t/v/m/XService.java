package com.chwod.robot.action.impl.t.v.m;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 今天是几号？
 */

@Component("SS:T-V-M-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		Calendar calendar = Calendar.getInstance();
		Integer day = calendar.get(Calendar.DAY_OF_MONTH);
		
		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		
		sentence.setResponseWord(new StringBuffer("今天是").append(day).append("号.").toString());
		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		logger.debug("Learning class : [{}], sentence : [{}]", this.getClass().getName(),
				eventContext.getCurrentEvent());		
	}

}
