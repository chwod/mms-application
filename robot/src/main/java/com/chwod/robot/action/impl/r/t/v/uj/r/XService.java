package com.chwod.robot.action.impl.r.t.v.uj.r;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 你刚才说的什么？
 */

@Component("SS:R-T-V-UJ-R-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		String currentEvent = eventContext.getCurrentEvent();
		
		eventContext.setType(currentEvent == null ? EventContext.SENTENCE_TYPE_DECLARATIVE_NEGATIVE :
			EventContext.SENTENCE_TYPE_DECLARATIVE);
		
		sentence.setResponseWord(currentEvent == null ? "我刚才什么也没有说啊！" : currentEvent);
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub
		
	}

}
