package com.chwod.robot.action.impl.d.v.ns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.chwod.robot.action.ActionService;
import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants.LEARNING;

/**
 * @implSpec 当然是北京了
 * @author xliu
 *
 */
@Component("SS:D-V-NS-UL")
public class ULService implements ActionService {

	private static final Logger logger = LoggerFactory.getLogger(ULService.class);

	@Override
	public Sentence process(Sentence sentence, EventContext eventContext) {

		logger.debug("Process class : [{}], sentence : [{}], deep : [{}]", this.getClass().getName(),
				sentence.getRequestWord(), sentence.getProcessDeep());

		return sentence;
	}

	@Override
	public void learning(EventContext eventContext, LEARNING flag) {
		// TODO Auto-generated method stub

	}

}
