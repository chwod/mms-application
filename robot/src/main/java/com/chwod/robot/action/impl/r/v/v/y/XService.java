package com.chwod.robot.action.impl.r.v.v.y;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 你需要吃饭么？
 */

@Component("SS:R-V-V-Y-X")
public class XService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ActionService.class);

	@Override
	public Sentence process(Sentence sentence) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(), sentence.getRequestWord(),
				sentence.getProcessDeep());
		
		sentence.setResponseWord("不需要");
		return sentence;
	}

	@Override
	public void learning(Sentence sentence, LEARNING flag) {
		// TODO Auto-generated method stub
		
	}

}
