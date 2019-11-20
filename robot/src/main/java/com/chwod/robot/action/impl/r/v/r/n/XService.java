package com.chwod.robot.action.impl.r.v.r.n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 你叫什么名字？
 */

@Component("SS:R-V-R-N-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Value("${com.chwod.robot.name:程序}")
	private String name;

	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		eventContext.setCentralLanguage(sentence.getPartList().get(3).getWord());
		eventContext.setType(EventContext.SENTENCE_TYPE_DECLARATIVE);
		
		sentence.setResponseWord(this.name);
		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		logger.debug("Learning class : [{}], sentence : [{}]", this.getClass().getName(),
				eventContext.getCurrentEvent());		
	}

}
