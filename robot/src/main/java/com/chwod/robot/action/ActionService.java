package com.chwod.robot.action;

import com.chwod.robot.bean.EventContext;
import com.chwod.robot.bean.Sentence;
import com.chwod.robot.utils.Constants;

public interface ActionService {

	/**
	 * send the sentence to the support service. and get the response word.
	 * @param sentence
	 * @param eventContext
	 * @return sentence object include response word.
	 */
	public Sentence process(Sentence sentence, EventContext eventContext);
	
	/**
	 * robot learning with learning flag.
	 * @param sentence
	 * @param flag
	 */
	public void learning(Sentence sentence, Constants.LEARNING flag);
}
